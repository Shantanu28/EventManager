package com.cultur.eventmanager.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by shantanu on 10/5/17.
 */
public class EventUtil {

    public static Predicate<List> isListEmpty = list -> list == null || list.size() == 0;

    public static Predicate<String> isStringEmpty = StringUtils::isEmpty;

    public static Predicate<String> isStringNonEmpty = isStringEmpty.negate();
}
