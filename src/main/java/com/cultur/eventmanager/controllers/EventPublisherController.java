package com.cultur.eventmanager.controllers;

import com.cultur.eventmanager.dtos.request.EventPublishRequest;
import com.cultur.eventmanager.dtos.response.EventPublishResponse;
import com.cultur.eventmanager.services.EventPublisherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by shantanu on 29/4/17.
 */
@RestController
@Api(value = "EventPublisher", description = "Event publisher API")
public class EventPublisherController {

    private Logger logger = Logger.getLogger(EventPublisherController.class);
    private EventPublisherService service;

    @Autowired
    public EventPublisherController(EventPublisherService service) {
        this.service = service;
    }

    @RequestMapping(value = "/api/event/publish", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Plublish the event")
    public EventPublishResponse publish(@Valid @RequestBody EventPublishRequest eventPublishRequest) {
        logger.info("Request : " + eventPublishRequest);
        EventPublishResponse response = service.process(eventPublishRequest);

        logger.info("Response : " + response);
        return response;
    }
}
