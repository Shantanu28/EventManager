package com.cultur.eventmanager.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shantanu on 21/5/17.
 */
public class PriorityConstant {

    public static final Map<String, Integer> priorityMap;

    static {
        priorityMap = new HashMap<>();
        priorityMap.put("seatgeek", 1);
        priorityMap.put("ticketfly", 2);
        priorityMap.put("ticketmaster", 3);
        priorityMap.put("goldstar", 4);
        priorityMap.put("flavorus", 5);
        priorityMap.put("eventbrite_api", 6);
        priorityMap.put("wantickets", 7);
        priorityMap.put("insomniac", 8);
        priorityMap.put("meethawaii", 9);
        priorityMap.put("gohawaii", 10);
        priorityMap.put("eventful", 11);
        priorityMap.put("bandsintown", 12);
        priorityMap.put("stubhub", 13);
        priorityMap.put("java_facebook_events", 14);
        priorityMap.put("songkick", 15);
        priorityMap.put("nearify", 16);
        priorityMap.put("islandetickets", 17);
        priorityMap.put("yelp", 18);
        priorityMap.put("hnlnow", 19);
        priorityMap.put("honolulumagazine", 20);
        priorityMap.put("honolulugov", 21);
        priorityMap.put("honolulufamily", 22);
        priorityMap.put("808shows", 23);
        priorityMap.put("hawaiieventsonline", 24);
        priorityMap.put("hawaiipublicradio", 25);
        priorityMap.put("firstfridayhawaii", 26);
        priorityMap.put("abudhabievents", 27);
        priorityMap.put("apeconcerts", 28);
        priorityMap.put("bowerypresents", 29);
        priorityMap.put("c3presents", 30);
        priorityMap.put("evvnt", 31);
        priorityMap.put("hawaiiactivities", 32);
        priorityMap.put("honoluluworldweb", 33);
        priorityMap.put("metrowize", 34);
        priorityMap.put("sfgate", 35);
        priorityMap.put("sanjose", 36);
        priorityMap.put("sfweekly", 37);
        priorityMap.put("sfFunCheap", 38);
        priorityMap.put("mindbodyonline", 39);
        priorityMap.put("facebook_login_events_friends", 40);
        priorityMap.put("facebook_login_events_status", 41);
        priorityMap.put("allevents", 42);
        priorityMap.put("worldtimeout", 43);
    }
}
