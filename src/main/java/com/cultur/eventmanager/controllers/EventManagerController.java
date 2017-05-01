package com.cultur.eventmanager.controllers;

import com.cultur.eventmanager.dtos.request.EventPublishRequest;
import com.cultur.eventmanager.dtos.response.EventPublishResponse;
import com.cultur.eventmanager.services.EventManagerService;
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
public class EventManagerController {

    private Logger logger = Logger.getLogger(EventManagerController.class);
    private EventManagerService service;

    @Autowired
    public EventManagerController(EventManagerService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/javainuse", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE })
    public String sayHello() {
        return "Swagger Hello World";
    }

    @RequestMapping(value = "/api/event/publish", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EventPublishResponse publish(@Valid @RequestBody EventPublishRequest eventPublishRequest) {
        logger.info("Request : " + eventPublishRequest);
        EventPublishResponse response = service.process(eventPublishRequest);

        logger.info("Response : " + response);
        return response;
    }
}
