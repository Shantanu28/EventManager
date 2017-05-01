package com.cultur.eventmanager.services;

import com.cultur.eventmanager.dtos.request.EventPublishRequest;
import com.cultur.eventmanager.entities.ImportEvent;
import com.cultur.eventmanager.repositories.EventImportSourceRepository;
import com.cultur.eventmanager.repositories.ImportEventRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by shantanu on 1/5/17.
 */
@Component
public class EventProcessor {

    @Autowired
    private EventImportSourceRepository eventImportSourceRepository;

    @Autowired
    private ImportEventRepository importEventRepository;

    private Logger logger = Logger.getLogger(EventProcessor.class);

    public void processEvent(EventPublishRequest eventPublishRequest) {
        logger.info("Recieved published message");
        logger.info(eventPublishRequest.toString());

        Integer maxImportSourceId = eventImportSourceRepository.getMaxImportSourceId(eventPublishRequest.getImportSrcName());

        logger.info(maxImportSourceId);

        if (maxImportSourceId == null) {
            Timestamp timestamp = new Timestamp((new Date()).getTime());
            ImportEvent importEvent = new ImportEvent();
            importEvent.setCreatedAt(timestamp);
            importEvent.setUpdatedAt(timestamp);
            importEvent.setEventImportSource(eventImportSourceRepository.findByName(eventPublishRequest.getImportSrcName()));
            importEvent.setWorkflowState("loading");

            ImportEvent savedImportEvent = importEventRepository.save(importEvent);
            maxImportSourceId = savedImportEvent.getId();
        }

        logger.info(maxImportSourceId);
    }
}
