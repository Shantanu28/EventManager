package com.cultur.eventmanager.schedular;

import com.cultur.eventmanager.repositories.EventsRepository;
import com.cultur.eventmanager.services.HttpRequestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by shantanu on 12/5/17.
 */
@Component
public class ImageProcessSchedular {

    private Logger logger = Logger.getLogger(ImageProcessSchedular.class);

    @Autowired
    private HttpRequestService httpRequestService;

    @Autowired
    private EventsRepository eventsRepository;

    @Value("${image.processor.url}")
    private String imageProcessorUrl;

    //every 5 min
    @Scheduled(fixedDelay = 300000)
    public void processImage() {
        logger.info("Starting image processing");
        try {
            List<Integer> eventList = eventsRepository.findByStatusId(10);
            eventList.forEach(event -> {
                try {
                    httpRequestService.sequentialPostService(imageProcessorUrl, "application/json;charset=UTF-8", "id=" + event);
                } catch (Exception ex) {
                    logger.error("Exception occurred while executing the image processor job scheduler : ", ex);
                }
            });
        } catch (Exception ex) {
            logger.error("Exception occurred while executing the image processor job scheduler : ", ex);
        }

        logger.info("Ending image processing");
    }

}
