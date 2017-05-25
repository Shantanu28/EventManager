package com.cultur.eventmanager.services;

import com.cultur.eventmanager.constants.PriorityConstant;
import com.cultur.eventmanager.dtos.request.EventPublishRequest;
import com.cultur.eventmanager.entities.Cultur;
import com.cultur.eventmanager.entities.Event;
import com.cultur.eventmanager.entities.ImportEvent;
import com.cultur.eventmanager.entities.Venue;
import com.cultur.eventmanager.repositories.EventsRepository;
import com.cultur.eventmanager.repositories.ImportEventRepository;
import com.cultur.eventmanager.utils.EventUtil;
import org.apache.log4j.Logger;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by shantanu on 10/5/17.
 */
@Service
public class EventManagerService {

    @Autowired
    private EventSourceManagerService eventSourceManagerService;

    @Autowired
    private EventVenueManagerService eventVenueManagerService;

    @Autowired
    private ImportEventRepository importEventRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private EventCategoryService eventCategoryService;

    private Logger logger = Logger.getLogger(EventManagerService.class);

    private final DateTimeFormatter DATE_TIME_FORMATTER =
            new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").toFormatter();

    private static Predicate<Event> isLatLongEqual(EventPublishRequest request, Integer newScale) {
        return event -> {
            Double dbLatitude = EventUtil.roundOff.apply(event.getLatitude().toString(), newScale);
            Double dbLongitude = EventUtil.roundOff.apply(event.getLongitude().toString(), newScale);
            Double reqEventLat = EventUtil.roundOff.apply(request.getLatitude(), newScale);
            Double reqEventLong = EventUtil.roundOff.apply(request.getLongitude(), newScale);

            return dbLatitude.compareTo(reqEventLat) == 0 && dbLongitude.compareTo(reqEventLong) == 0;
        };
    }

    private static Predicate<Event> isEventDateEqual(EventPublishRequest request) {
        return event -> {
            String dbEventStartDate = timestampToDate.apply(event.getStart());
            String requestedEventStartDate = EventUtil.timeZoneCoverter(request.getEventStartTime(),
                    "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd",
                    "UTC", "UTC");

            return dbEventStartDate.equals(requestedEventStartDate);
        };
    }

    private static Predicate<Event> isValidDuplicate(EventPublishRequest request, Integer newScale) {
        return event -> isEventDateEqual(request).and(isLatLongEqual(request, newScale)).test(event);
    }

    private static Function<Timestamp, String> timestampToDate = timestamp -> {
        if (timestamp == null)
            return "";

        Date date = Date.from(timestamp.toInstant());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        return simpleDateFormat.format(date);
    };

    @Transactional
    public Integer execute(EventPublishRequest request) {
        try {
            String importEventId = eventSourceManagerService.findOrCreateImportEventId(request.getImportSrcName());
            request.setImportEventId(importEventId);

            Venue venueDetail = eventVenueManagerService.findOrCreateVenue(request);

            List<Cultur> eventCategoryList = eventCategoryService.processEventForCategory(request.getEventName(), request.getEventDescription());

            return findOrCreateEvent(request, venueDetail, eventCategoryList);
        } catch (Exception ex) {
            logger.error("Error found " + ex);
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }

    private Integer findOrCreateEvent(EventPublishRequest request, Venue venueDetail, List<Cultur> eventCategoryList) {
        logger.info("Looking for duplicate event");
        List<Event> duplicateEvents = eventsRepository.findByNameIgnoreCase(request.getEventName());

        if (EventUtil.isListEmpty.test(duplicateEvents)) {
            return saveEvent(request, venueDetail, eventCategoryList);
        }

        logger.info("Duplicate event found, count: " + duplicateEvents.size());

        List<Event> duplicateEventList = duplicateEvents.stream().filter(isValidDuplicate(request, 2)).collect(Collectors.toList());

        if (EventUtil.isListEmpty.test(duplicateEventList))
            return saveEvent(request, venueDetail, eventCategoryList);
        else {
            Event event = getHighestPriorityEvent(duplicateEventList, request.getImportSrcName());

            if (event != null)
                return updateEvent(request, venueDetail, eventCategoryList, event);
            else
                return 0;
        }
    }

    private Event getHighestPriorityEvent(List<Event> duplicateEventList, String reqImportSrcName) {
        duplicateEventList.sort((event1, event2) -> {
            if (event1.getImportEventId() == null)
                return 1;

            if (event2.getImportEventId() == null)
                return -1;

            ImportEvent importEvent1 = importEventRepository.findOne(event1.getImportEventId());
            ImportEvent importEvent2 = importEventRepository.findOne(event2.getImportEventId());

            if (importEvent1 == null)
                return 1;

            if (importEvent2 == null)
                return -1;

            String importSrcName1 = importEvent1.getEventImportSource().getName();
            String importSrcName2 = importEvent2.getEventImportSource().getName();

            Integer importSrcIndex1 = PriorityConstant.priorityMap.get(importSrcName1);
            Integer importSrcIndex2 = PriorityConstant.priorityMap.get(importSrcName2);

            if (importSrcIndex1 == null)
                return 1;

            if (importSrcIndex2 == null)
                return -1;

            return importSrcIndex1 - importSrcIndex2;
        });

        Event highPriorityEvent = duplicateEventList.get(0);

        if (duplicateEventList.size() > 1) {
            duplicateEventList.subList(1, duplicateEventList.size()).forEach(event -> eventsRepository.delete(event.getId()));
        }

        if (highPriorityEvent.getImportEventId() == null)
            return highPriorityEvent;

        ImportEvent importEvent = importEventRepository.findOne(highPriorityEvent.getImportEventId());

        if (importEvent == null)
            return highPriorityEvent;

        String importSrcName = importEvent.getEventImportSource().getName();

        Integer importSrcIndex1 = PriorityConstant.priorityMap.get(importSrcName);
        Integer importSrcIndex2 = PriorityConstant.priorityMap.get(reqImportSrcName.toLowerCase());

        if (importSrcIndex1 == null || importSrcIndex2 == null || importSrcIndex2 <= importSrcIndex1)
            return highPriorityEvent;
        else
            return null;
    }

    private Integer saveEvent(EventPublishRequest request, Venue venueDetail, List<Cultur> eventCategoryList) {
        logger.info("Creating a new event");
        Timestamp timestamp = new Timestamp((new Date()).getTime());
        Event.EventBuilder eventBuilder = new Event.EventBuilder();
        eventBuilder.withVenue(venueDetail).withAddress(request.getEventAddress())
                .withCreatedAt(timestamp).withUpdatedAt(timestamp).withDescription(request.getEventDescription())
                .withImportEventId(Integer.valueOf(request.getImportEventId())).withLatitude(Double.valueOf(request.getLatitude()))
                .withLongitude(Double.valueOf(request.getLongitude()))
                .withName(request.getEventName()).withPublished(true).withSourceUrl(request.getOfficialWebSiteUrl())
                .withTimezone(request.getEventTimeZone()).withTicketLink(request.getEventTicketLink());

        if (EventUtil.isStringNonEmpty.test(request.getEventPhotoUrl()))
            eventBuilder.withPhotoSourceUrl(request.getEventPhotoUrl());

        if (EventUtil.isStringNonEmpty.test(request.getEventPrice()))
            eventBuilder.withPrice(new BigDecimal(request.getEventPrice()));

        Timestamp startTimeStrToTimestamp = new Timestamp((new Date()).getTime());

        if (EventUtil.isStringNonEmpty.test(request.getEventStartTime())) {
            OffsetDateTime odt = OffsetDateTime.parse(request.getEventStartTime(), DATE_TIME_FORMATTER);
            Instant instant = odt.toInstant();
            startTimeStrToTimestamp = Timestamp.from(instant);
            eventBuilder.withStart(startTimeStrToTimestamp);
        }

        if (EventUtil.isStringNonEmpty.test(request.getEventEndTime())) {
            OffsetDateTime odt = OffsetDateTime.parse(request.getEventEndTime(), DATE_TIME_FORMATTER);
            Instant instant = odt.toInstant();
            Timestamp stringToTimestamp = Timestamp.from(instant);
            eventBuilder.withEnd(stringToTimestamp);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(startTimeStrToTimestamp.getTime());
            cal.add(Calendar.HOUR, 4);
            Timestamp later = new Timestamp(cal.getTime().getTime());
            eventBuilder.withEnd(later);
        }

        if (EventUtil.isStringNonEmpty.test(request.getRecurEveryXWeeks()))
            eventBuilder.withRecurEveryXWeeks(Integer.valueOf(request.getRecurEveryXWeeks()));

        if (EventUtil.isStringNonEmpty.test(request.getRecurEveryXDays()))
            eventBuilder.withRecurEveryXDays(Integer.valueOf(request.getRecurEveryXDays()));

        eventBuilder.withRecurWeeklyOnWeekdays(request.getRecurWeeklyOnWeekdays());
        eventBuilder.withRecurType(request.getRecurType());
        eventBuilder.withCulturList(eventCategoryList);
        eventBuilder.withStatusId(10);

        Event event = new Event(eventBuilder);
        event = eventsRepository.save(event);

        logger.info("New Event created");

        return event.getId();
    }

    private Integer updateEvent(EventPublishRequest request, Venue venueDetail, List<Cultur> eventCategoryList, Event event) {
        logger.info("Updating event.....");
        Timestamp timestamp = new Timestamp((new Date()).getTime());
        event.setVenue(venueDetail);
        event.setAddress(request.getEventAddress());
        event.setCreatedAt(timestamp);
        event.setUpdatedAt(timestamp);
        event.setDescription(request.getEventDescription());
        event.setImportEventId(Integer.valueOf(request.getImportEventId()));
        event.setLatitude(Double.valueOf(request.getLatitude()));
        event.setLongitude(Double.valueOf(request.getLongitude()));
        event.setName(request.getEventName());
        event.setPublished(true);
        event.setSourceUrl(request.getOfficialWebSiteUrl());
        event.setTimezone(request.getEventTimeZone());
        event.setTicketLink(request.getEventTicketLink());

        if (EventUtil.isStringNonEmpty.test(request.getEventPhotoUrl()))
            event.setPhotoSourceUrl(request.getEventPhotoUrl());

        if (EventUtil.isStringNonEmpty.test(request.getEventPrice()))
            event.setPrice(new BigDecimal(request.getEventPrice()));

        Timestamp startTimeStrToTimestamp = new Timestamp((new Date()).getTime());

        if (EventUtil.isStringNonEmpty.test(request.getEventStartTime())) {
            OffsetDateTime odt = OffsetDateTime.parse(request.getEventStartTime(), DATE_TIME_FORMATTER);
            Instant instant = odt.toInstant();
            startTimeStrToTimestamp = Timestamp.from(instant);
            event.setStart(startTimeStrToTimestamp);
        }

        if (EventUtil.isStringNonEmpty.test(request.getEventEndTime())) {
            OffsetDateTime odt = OffsetDateTime.parse(request.getEventEndTime(), DATE_TIME_FORMATTER);
            Instant instant = odt.toInstant();
            Timestamp stringToTimestamp = Timestamp.from(instant);
            event.setEnd(stringToTimestamp);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(startTimeStrToTimestamp.getTime());
            cal.add(Calendar.HOUR, 4);
            Timestamp later = new Timestamp(cal.getTime().getTime());
            event.setEnd(later);
        }

        if (EventUtil.isStringNonEmpty.test(request.getRecurEveryXWeeks()))
            event.setRecurEveryXWeeks(Integer.valueOf(request.getRecurEveryXWeeks()));

        if (EventUtil.isStringNonEmpty.test(request.getRecurEveryXDays()))
            event.setRecurEveryXDays(Integer.valueOf(request.getRecurEveryXDays()));

        event.setRecurWeeklyOnWeekdays(request.getRecurWeeklyOnWeekdays());
        event.setRecurType(request.getRecurType());
        event.setCulturList(eventCategoryList);
        event.setStatusId(10);

        event = eventsRepository.save(event);

        logger.info("Event Updated...");

        return event.getId();
    }
}
