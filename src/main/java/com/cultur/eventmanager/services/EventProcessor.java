package com.cultur.eventmanager.services;

import com.cultur.eventmanager.dtos.request.EventPublishRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by shantanu on 1/5/17.
 */
@Component
public class EventProcessor {

    @Autowired
    private HttpRequestService httpRequestService;

    @Autowired
    private EventManagerService eventManagerService;

    @Value("${image.processor.url}")
    private String imageProcessorUrl;

    private Logger logger = Logger.getLogger(EventProcessor.class);

    public void processEvent(EventPublishRequest request) {
        logger.info("Recieved published message");

        int eventId = eventManagerService.execute(request);
        startImageProcessing(eventId);
    }

    private void startImageProcessing(int eventId) {
        if (eventId > 0) {
            httpRequestService.asyncPostService(imageProcessorUrl, "application/json;charset=UTF-8", "id=" + eventId);
        }
    }




}
