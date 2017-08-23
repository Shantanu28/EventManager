package com.cultur.eventmanager.services;

import com.cultur.eventmanager.dtos.request.EventPublishRequest;
import com.cultur.eventmanager.entities.Venue;
import com.cultur.eventmanager.repositories.VenueRepository;
import com.cultur.eventmanager.utils.EventUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by shantanu on 10/5/17.
 */
@Service
public class EventVenueManagerService {
    private Logger logger = Logger.getLogger(EventVenueManagerService.class);

    @Autowired
    private VenueRepository venueRepository;

    public Venue findOrCreateVenue(EventPublishRequest request) {
        logger.info("Looking for Venue");
        Venue venueDetail;
        List<Venue> venueList = venueRepository.findByLatitudeAndLongitude(Double.valueOf(request.getLatitude()), Double.valueOf(request.getLongitude()));

        if (EventUtil.isListEmpty.test(venueList)) {
            logger.info("No Venue found");
            Timestamp timestamp = new Timestamp((new Date()).getTime());
            Venue.VenueBuilder venueBuilder = new Venue.VenueBuilder();
            venueBuilder.withLatitude(Double.valueOf(request.getLatitude())).withLongitude(Double.valueOf(request.getLongitude()))
                    .withName(request.getEventVenueName()).withAddress(request.getEventAddress())
                    .withStreet(request.getStreet()).withCity(request.getCity())
                    .withState(request.getState()).withCountry(request.getCountry())
                    .withZip(request.getZip()).withTimezone(request.getEventTimeZone())
                    .withCreatedAt(timestamp).withUpdatedAt(timestamp);

            Venue venue = new Venue(venueBuilder);
            venueDetail = venueRepository.save(venue);
            logger.info("Created new venue entry, venueId: " + venueDetail.getId());
        } else {
            venueDetail = venueList.get(0);
            logger.info("Venue found, venueId: " + venueDetail.getId());
        }

        return venueDetail;
    }
}
