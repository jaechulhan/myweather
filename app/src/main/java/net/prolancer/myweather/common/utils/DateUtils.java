package net.prolancer.myweather.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
    public static final String SIMPLE_DATETIME_FORMAT = "yy-MM-dd HH:mm";

    /**
     * Get Current Date String
     * @return
     */
    public static String getCurrentDateString() {
        Date date = new Date(); // This object contains the current date value
        SimpleDateFormat formatter = new SimpleDateFormat(SIMPLE_DATETIME_FORMAT);
        return formatter.format(date);
    }

    /**
     * Convert String to Date with format
     * @param dateStr
     * @return
     */
    public static Date convertStringToDate(String dateStr) {
        return convertStringToDate(dateStr, SIMPLE_DATE_FORMAT);
    }

    /**
     * Convert String to Date with format
     * @param dateStr
     * @param format
     * @return
     */
    public static Date convertStringToDate(String dateStr, String format) {
        try {
            return new SimpleDateFormat(format).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get Simple Day String (Mon, Tue, Wed, Thr, Fri, Sat, Sun)
     * @param dateStr
     * @return
     */
    public static String getSimpleDayString(String dateStr) {
        return new SimpleDateFormat("EE").format(DateUtils.convertStringToDate(dateStr));
    }

    /**
     * Get Simple Day String (Mon, Tue, Wed, Thr, Fri, Sat, Sun)
     * @param date
     * @return
     */
    public static String getSimpleDayString(Date date) {
        return new SimpleDateFormat("EE").format(date);
    }
}
