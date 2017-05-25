package com.cultur.eventmanager.services;

import com.cultur.eventmanager.entities.Cultur;
import com.cultur.eventmanager.repositories.CulturRepository;
import org.apache.log4j.Logger;
import org.atteo.evo.inflector.English;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by shantanu on 3/5/17.
 */
@Service
public class EventCategoryService {

    @Autowired
    private CulturRepository culturRepository;

    private Logger logger = Logger.getLogger(EventCategoryService.class);

    private static List<Cultur> culturList;

    public List<Cultur> processEventForCategory(String eventName, String eventDescription) {
        logger.info("Looking for event Category...");
        List<Cultur> culturEventList = new ArrayList<>();

        if (culturList == null)
            culturList = culturRepository.findAll();

        culturList.forEach(cultur -> {
            String keywordSigular = English.plural(cultur.getName(), 1);
            String keywordPlural = English.plural(cultur.getName(), 2);

            if (isPresent(keywordSigular, eventName) || isPresent(keywordPlural, eventName) ||
                    isPresent(keywordSigular, eventDescription) || isPresent(keywordPlural, eventDescription)) {
                culturEventList.add(cultur);
            } else {
                cultur.getCulturKeywordList().forEach(culturKeyword -> {
                    String keywordSigular1 = English.plural(culturKeyword.getKeyword(), 1);
                    String keywordPlural1 = English.plural(culturKeyword.getKeyword(), 2);

                    if (isPresent(keywordSigular1, eventName) || isPresent(keywordPlural1, eventName) ||
                            isPresent(keywordSigular1, eventDescription) || isPresent(keywordPlural1, eventDescription)) {
                        culturEventList.add(cultur);
                    }
                });
            }
        });

        List<Cultur> culturList = culturEventList.stream().map(cultur -> culturRepository.findOne(cultur.getId())).collect(Collectors.toList());

        return culturList;
    }

    private boolean isPresent(String query, String s) {
        String [] deli = s.split("[.\\s,?;]+");

        return Arrays.stream(deli).anyMatch(query::equalsIgnoreCase);
    }
}
