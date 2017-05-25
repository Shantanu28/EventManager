package com.cultur.eventmanager.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * Created by shantanu on 10/5/17.
 */
public class EventUtil {

    public static Predicate<List> isListEmpty = list -> list == null || list.size() == 0;

    public static Predicate<String> isStringEmpty = StringUtils::isEmpty;

    public static Predicate<String> isStringNonEmpty = isStringEmpty.negate();

    public static BiFunction<String, Integer, Double> roundOff = (val, newScale) ->
            new BigDecimal(val).setScale(newScale, RoundingMode.HALF_UP).doubleValue();

    public static String timeZoneCoverter(String srcDate, String srcDateFormatString,
                                    String destDateFormatString, String srcTimeZone, String destTimeZone) {
        String destDate = "";

        try {
            DateFormat srcDateFormat = new SimpleDateFormat(srcDateFormatString);
            srcDateFormat.setTimeZone(TimeZone.getTimeZone(srcTimeZone));

            DateFormat destDateFormat = new SimpleDateFormat(destDateFormatString);
            destDateFormat.setTimeZone(TimeZone.getTimeZone(destTimeZone));

            Date date = srcDateFormat.parse(srcDate);
            destDate = destDateFormat.format(date);
        } catch (Exception e) {
        }

        return destDate;
    }
}
