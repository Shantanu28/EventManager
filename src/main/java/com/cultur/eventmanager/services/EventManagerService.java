package com.cultur.eventmanager.services;

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
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

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

    private static final Map<String, Integer> priorityMap;

    private final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").toFormatter();

    static {
        priorityMap = new HashMap<>();
        priorityMap.put("seatgeek", 1);
        priorityMap.put("ticketfly", 2);
        priorityMap.put("ticketmaster", 3);
        priorityMap.put("goldstar", 4);
        priorityMap.put("flavorus", 5);
        priorityMap.put("eventbrite_api", 6);
        priorityMap.put("wantickets", 7);
        priorityMap.put("java_facebook_events", 8);
    }

    @Transactional
    public int execute(EventPublishRequest request) {
        try {
            String importEventId = eventSourceManagerService.findOrCreateImportEventId(request.getImportSrcName());
            request.setImportEventId(importEventId);

            Venue venueDetail = eventVenueManagerService.findOrCreateVenue(request);

            List<Cultur> eventCategoryList = eventCategoryService.processEventForCategory(request.getEventName(), request.getEventDescription());

            int eventId = findOrCreateEvent(request, venueDetail, eventCategoryList);

            return eventId;
        } catch (Exception ex) {
            logger.error("Error found " + ex);
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }

    private Integer findOrCreateEvent(EventPublishRequest request, Venue venueDetail, List<Cultur> eventCategoryList) {
        logger.info("Looking for duplicate event");
        List<Event> findDuplicate = eventsRepository.findByNameIgnoreCaseAndLatitudeAndLongitude(request.getEventName(),
                Double.valueOf(request.getLatitude()), Double.valueOf(request.getLongitude()));

        if (EventUtil.isListEmpty.negate().test(findDuplicate)) {
            logger.info("Duplicate event found, count: " + findDuplicate.size());

            if (priorityMap.get(request.getImportSrcName().toLowerCase()) == null) {
                logger.info("Import source has less priority, so skipping");
                return 0;
            }

            boolean flag = false;
            Event event = findDuplicate.get(0);
            logger.info("Trying to find import source");
            ImportEvent importEvent = importEventRepository.findOne(event.getImportEventId());

            if (importEvent == null) {
                logger.info("Import source not found");
                return 0;
            }

            String name = importEvent.getEventImportSource().getName();
            logger.info("Import source found, src: " + name);

            if (priorityMap.get(name) == null) {
                flag = true;
            } else {
                if (priorityMap.get(name) >= priorityMap.get(request.getImportSrcName().toLowerCase())) {
                    flag = true;
                }
            }

            if (flag) {
                return updateEvent(request, venueDetail, eventCategoryList, event);
            }

            return 0;
        } else {
            return saveEvent(request, venueDetail, eventCategoryList);
        }
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
        event = eventsRepository.saveAndFlush(event);

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
        event = eventsRepository.saveAndFlush(event);

        logger.info("Event Updated...");

        return event.getId();
    }


}
