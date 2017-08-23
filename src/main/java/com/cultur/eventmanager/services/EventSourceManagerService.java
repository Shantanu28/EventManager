package com.cultur.eventmanager.services;

import com.cultur.eventmanager.entities.EventImportSource;
import com.cultur.eventmanager.entities.ImportEvent;
import com.cultur.eventmanager.repositories.EventImportSourceRepository;
import com.cultur.eventmanager.repositories.ImportEventRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * Created by shantanu on 10/5/17.
 */
@Service
public class EventSourceManagerService {
    private Logger logger = Logger.getLogger(EventSourceManagerService.class);

    @Autowired
    private EventImportSourceRepository eventImportSourceRepository;

    @Autowired
    private ImportEventRepository importEventRepository;

    public String findOrCreateImportEventId(String eventSourceName) {
        logger.info("Looking for importEventId");
        Integer maxImportSourceId = eventImportSourceRepository.getMaxImportSourceId(eventSourceName);
        Timestamp timestamp = Timestamp.from(Instant.now());

        if (maxImportSourceId == null) {
            logger.info("importEventId not found");
            logger.info("looking for event source existence");
            EventImportSource eventImportSource = eventImportSourceRepository.findByName(eventSourceName);
            ImportEvent.ImportEventBuilder importEventBuilder = new ImportEvent.ImportEventBuilder();
            importEventBuilder.withCreatedAt(timestamp).withUpdatedAt(timestamp)
                    .withEventImportSource(eventImportSource).withWorkflowState("loading");

            ImportEvent importEvent = new ImportEvent(importEventBuilder);

            logger.info("Saving importEvent id");
            ImportEvent savedImportEvent = importEventRepository.save(importEvent);
            maxImportSourceId = savedImportEvent.getId();
            logger.info("Saved importEvent id: " + maxImportSourceId);
        }/* else {
            try {
                logger.info("importEvent id found updating the timestamp");
                ImportEvent importEvent = importEventRepository.findOne(maxImportSourceId);
                importEvent.setWorkflowState("loading");
                importEvent.setCreatedAt(timestamp);
                importEvent.setUpdatedAt(timestamp);

                importEventRepository.save(importEvent);
            } catch (Exception ex) {
                logger.error("Exception occurs while updatation");
            }
        }*/

        return String.valueOf(maxImportSourceId);
    }
}
