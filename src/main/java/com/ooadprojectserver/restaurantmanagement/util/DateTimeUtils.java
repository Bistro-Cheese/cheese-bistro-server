package com.ooadprojectserver.restaurantmanagement.util;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.isNullObject;
import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.isNullOrEmpty;


@Slf4j
public class DateTimeUtils {
    private DateTimeUtils() {
    }

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT2 = "dd/MM/yyyy";
    public static final String DATE_FORMAT3 = "dd-MM-yyyy";
    public static final String DATE_FORMAT4 = "yyyy/MM/dd";
    public static final String MONTH_FORMAT = "yyyy-MM";
    public static final String MONTH_FORMAT1 = "MM/yyyy";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT2 = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_TIME_FORMAT3 = "yyyyMMddHHmmss";
    public static final String DATE_TIME_FORMAT4 = "HH:mm:ss - dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT5 = "yyyyMMddHHmmss.SSS";
    public static final String DATE_TIME_FORMAT6 = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    public static final String DATE_TIME_FORMAT7 = "MM/dd/yyyy HH:mm:ss";
    public static final String DATE_TIME_FORMAT8 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DATE_TIME_FORMAT9 = "EEE, dd MMM yyyy HH:mm:ss";
    public final String TIME_ZONE = "Asia/Ho_Chi_Minh";
    public final String UTC = "UTC";

    public static final String DATE_PATTERN_DDMMYYYY = "ddMMYYYY";

    public static final String DATE_PATTERN_HHMMSS = "HH:mm:ss";

    public static final String DATE_PATTERN_YYYY = "YYYY";

    public static final String DATE_PATTERN_MM = "MM";


    /*
     * @description: Safe to date
     * */
    public static Date safeToDate(Object obj1) {
        if (obj1 == null) {
            return null;
        }
        return (Date) obj1;
    }

    /*
     * @description: Compare two string date by pattern
     * */
    public static int compare(String first, String second, String pattern) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat(pattern);
        Date firstDate = parser.parse(first);
        Date secondDate = parser.parse(second);

        if (firstDate.after(secondDate)) {
            return 1;
        } else if (firstDate.equals(secondDate)) {
            return 0;
        } else {
            return -1;
        }
    }

    /*
     * @description: Compare two date
     * */
    public static int compare(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new NullPointerException();
        }

        if (date1.after(date2)) {
            return 1;
        } else if (date1.equals(date2)) {
            return 0;
        } else {
            return -1;
        }
    }

    /*
     * @description: Convert String to Date by pattern
     * */
    public static Date stringToDate(String value, String pattern) {
        if (!isNullOrEmpty(value)) {
            SimpleDateFormat dateTime = new SimpleDateFormat(pattern);
            dateTime.setLenient(false);
            try {
                return dateTime.parse(value);
            } catch (ParseException ex) {
                log.error(ex.getMessage(), ex);
                return null;
            }
        }
        return null;
    }

    /*
     * @description: Convert date to string
     * */
    public static String dateToString(Date date, String pattern) {
        if (date == null || isNullOrEmpty(pattern)) {
            return null;
        }

        DateFormat df = new SimpleDateFormat(pattern, new Locale("vi"));
        return df.format(date);
    }

    /*
     * @description: Get last date of month
     * */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int lastDate = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DATE, lastDate);

        return calendar.getTime();
    }

    /*
     * @description: Get fist date of month
     * */
    public static Date getFirstDayOfMonth(Date date) {
        LocalDate today = new LocalDate(date);
        return today.dayOfMonth().withMinimumValue().toDate();
    }

    /*
     * @description: Get year
     * */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /*
     * @description: Get month
     * */
    public static int getMonth(Date nowDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /*
     * @description: Get date
     * */
    public static int getDate(Date nowDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        return calendar.get(Calendar.DATE);
    }

    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    public static int getMillisecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MILLISECOND);
    }

    public static Long getTimeMillisecond(Date date) {
        if (isNullObject(date)) return null;

        return date.getTime();
    }

    public static int getDayOfYear(Date date) {
        Calendar ca1 = Calendar.getInstance();
        ca1.setTime(date);
        return ca1.get(Calendar.DAY_OF_YEAR);
    }

    public static Date getStartOfDay(Date date) {
        return setTime(date, 0, 0, 0, 0);
    }

    public static Date getEndOfDay(Date date) {
        return setTime(date, 23, 59, 59, 999);
    }

    /*
     * @description: Add second
     * */
    public static Date addSecond(Date date, int period) {
        if (isNullObject(date)) return null;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, period);
        return cal.getTime();
    }

    /*
     * @description: Add minutes
     * */
    public static Date addMinute(Date date, int period) {
        if (isNullObject(date)) return null;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, period);
        return cal.getTime();
    }

    /*
     * @description: Add hour
     * */
    public static Date addHour(Date date, int period) {
        if (isNullObject(date)) return null;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, period);
        return cal.getTime();
    }

    /*
     * @description: Add date
     * */
    public static Date addDate(Date date, int period) {
        if (isNullObject(date)) return null;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, period);
        return cal.getTime();
    }

    /*
     * @description: Add month
     * */
    public static Date addMonth(Date date, int period) {
        if (isNullObject(date)) return null;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, period);
        return cal.getTime();
    }

    /*
     * @description: Add year
     * */
    public static Date addYear(Date date, int period) {
        if (isNullObject(date)) return null;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, period);
        return cal.getTime();
    }

    public static Date setTime(Date date, int hour, int minute, int second, int millisecond) {
        if (isNullObject(date)) return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);

        return calendar.getTime();
    }

    /*
     * @description: Check string is date by pattern
     * */
    public static boolean isDate(String str, String datePattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        try {
            sdf.setLenient(false);
            Date date = sdf.parse(str);
            if (date == null) {
                return false;
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    /*
     * @description: Difference date
     * */
    public static Long diffDate(Date firstDate, Date secondDate, TimeUnit timeUnit) {
        if (isNullObject(firstDate) || isNullObject(secondDate)) throw new NullPointerException();

        Duration duration = Duration.between(firstDate.toInstant(), secondDate.toInstant());

        switch (timeUnit) {
            case SECONDS:
                return duration.getSeconds() % 60;
            case MINUTES:
                return duration.toMinutes() % 60;
            case HOURS:
                return duration.toHours() % 24;
            case DAYS:
                return duration.toDays();
            default:
                throw new IllegalArgumentException("Unsupported time unit");
        }
    }

    /*
     * @description: Get quantity day of month
     * */
    public static Integer getQuantityDayOfMonth(String dateStr, String format) {
        Date date = stringToDate(dateStr, format);
        Date lastDay = getLastDayOfMonth(date);
        return getDate(lastDay);
    }


    public static Timestamp resultTimestamp() {
        Instant instant = Instant.now();
        return Timestamp.from(instant);
    }

    public static Timestamp getTimestampAfter(int amount, ChronoUnit chronoUnit) {
        Instant instant = Instant.now();
        instant = instant.plus(amount, chronoUnit);
        return Timestamp.from(instant);
    }

    public static Timestamp resultTimestampAfter24h() {
        Instant instant = Instant.now();
        instant = instant.plus(24, ChronoUnit.HOURS);
        return Timestamp.from(instant);
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCurrentTimeWithFormat(String pattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            dateFormat.applyPattern(pattern);
            return dateFormat.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDateTimeWithFormatBefore(String pattern, int day) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            dateFormat.applyPattern(pattern);
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE, -day);
            return dateFormat.format(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getCurrentDateTimeWithFormat(String pattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            dateFormat.applyPattern(pattern);
            Calendar date = Calendar.getInstance();
            return dateFormat.format(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static Timestamp resultTimeStamp(Date d) {
        return new Timestamp(d.getTime());
    }

    public static String resultDateConvertToString(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static String resultTimestampConvertString(Timestamp timestamp, String pattern) {
        Date date = new Date();
        date.setTime(timestamp.getTime());
        String formattedDate = new SimpleDateFormat(pattern).format(date);
        return formattedDate;
    }

}
