package com.cultur.eventmanager.services;

import com.cultur.eventmanager.dtos.request.EventPublishRequest;
import com.cultur.eventmanager.dtos.response.EventPublishResponse;
import com.cultur.eventmanager.entities.EventImportSource;
import com.cultur.eventmanager.enums.ResponseStatus;
import com.cultur.eventmanager.queues.QueueProducer;
import com.cultur.eventmanager.repositories.EventImportSourceRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by shantanu on 29/4/17.
 */
@Service
public class EventManagerService {

    private Logger logger = Logger.getLogger(EventManagerService.class);

    private EventImportSourceRepository eventImportSourceRepository;

    @Autowired
    private QueueProducer<EventPublishRequest> eventDataProcessQueueProducer;

    @Autowired
    public EventManagerService(EventImportSourceRepository eventImportSourceRepository) {
        this.eventImportSourceRepository = eventImportSourceRepository;
    }

    @Transactional
    public EventPublishResponse process(EventPublishRequest eventPublishRequest) {
        if (!isValidImportSrc(eventPublishRequest.getImportSrcName())) {
            return new EventPublishResponse("Invalid import source name", ResponseStatus.INVALID_REQUEST);
        }

        if (!startBackgroundJob(eventPublishRequest)) {
            return new EventPublishResponse("Not able to push to Queue", ResponseStatus.INTERNAL_ERROR);
        }

        return new EventPublishResponse("Event pushed successfully", ResponseStatus.SUCCESS);
    }

    private boolean isValidImportSrc(String importSrcName) {
        logger.info("Valdating importSrcName:" + importSrcName);
        EventImportSource eventImportSource = eventImportSourceRepository.findByName(importSrcName.toLowerCase());

        return eventImportSource != null;

        /*List<ImportEvent> importEventList = eventImportSource.getImportEventList();

        if (importEventList != null && importEventList.size() > 0) {
            //create an entry in Import event table and save
        } else {
            return false;
        }*/
    }

    private boolean startBackgroundJob(EventPublishRequest eventPublishRequest) {
        try {
            logger.info("Pushing Event publish request message to Queue");
            eventDataProcessQueueProducer.pushMessage(eventPublishRequest);

            return true;
        } catch (Exception ex) {
            logger.error("Failed to push event publish request message to Queue", ex);
            return false;
        }
    }
}
