package name.mihnayan.dateparser;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Utility class that parses date string, time string or date and time string and returns
 * LocalDate, LocalTime or LocalDateTime object correspondingly.<br /><br />
 *
 * The date is specified as a string containing the day, month and year.
 * The numbers of day and month may contain or not contain a leading zero.<br />
 * The year may consist of two or four digits. If the year consists of two digits,
 * is added at the beginning of "20" (the current era).<br />
 * The separator between the day, month and year can be "." or "/".<br /><br />
 *
 * The time can be set in hours, minutes and seconds or hours and minutes.
 * The time separator is a colon (":")
 *
 * @author Mikhail Mangushev (Mihnayan)
 *
 */
public final class DateTimeParser {

    private static final int DAY_INDEX = 0;
    private static final int MONTH_INDEX = 1;
    private static final int YEAR_INDEX = 2;
    private static final String DEFAULT_ERA = "20";
    private static final int NUMBER_OF_DATE_PARTS = 3;
    private static final int NUMBER_OF_CHARS_IN_SHORT_YEAR = 2;
    private static final int NUMBER_OF_CHARS_IN_LONG_YEAR = 4;

    private static final int INTEGER_RADIX = 10;

    private static final int HOUR_INDEX = 0;
    private static final int MINUTE_INDEX = 1;
    private static final int SECOND_INDEX = 2;
    private static final int MIN_TIME_PARTS = 2;
    private static final int MAX_TIME_PARTS = 3;


    private static final String DATE_PARTS_SPLITTER = "[.//]";

    private DateTimeParser() {
        throw new AssertionError();
    }

    /**
     * Parses date string.
     * @param dateInString Date string.
     * @return LocalDate object representing the parsed string. Null if parsing was not successful.
     */
    public static LocalDate parseDateString(String dateInString) {

        if (dateInString == null || dateInString.trim().isEmpty()) {
            return null;
        }

        String[] dateStringParts = dateInString.split(DATE_PARTS_SPLITTER);

        if (dateStringParts.length < NUMBER_OF_DATE_PARTS) {
            return null;
        }
        if (dateStringParts[YEAR_INDEX].length() == NUMBER_OF_CHARS_IN_SHORT_YEAR) {
            dateStringParts[YEAR_INDEX] = DEFAULT_ERA + dateStringParts[YEAR_INDEX];
        } else if (dateStringParts[YEAR_INDEX].length() != NUMBER_OF_CHARS_IN_LONG_YEAR) {
            return null;
        }

        try {
            return LocalDate.of(
                    Integer.parseInt(dateStringParts[YEAR_INDEX], INTEGER_RADIX),
                    Integer.parseInt(dateStringParts[MONTH_INDEX], INTEGER_RADIX),
                    Integer.parseInt(dateStringParts[DAY_INDEX], INTEGER_RADIX));
        } catch (NumberFormatException | DateTimeException e) {
            return null;
        }
    }

    /**
     * Parses time string.
     * @param timeInString Time string. String must contain hours and minutes. Seconds are
     *        optional, if not specified, then 0 is substituted.
     * @return LocalTime object representing the parsed string. Null if parsing was not successful.
     */
    public static LocalTime parseTimeString(String timeInString) {

        if (timeInString == null || timeInString.trim().isEmpty()) {
            return null;
        }

        String[] timeStringParts = timeInString.split(":");

        int partsCount = timeStringParts.length;
        if (partsCount > MAX_TIME_PARTS || partsCount < MIN_TIME_PARTS) {
            return null;
        }

        try {
            if (partsCount == MIN_TIME_PARTS) {
                return LocalTime.of(
                        Integer.parseInt(timeStringParts[HOUR_INDEX], INTEGER_RADIX),
                        Integer.parseInt(timeStringParts[MINUTE_INDEX]));
            } else {
                return LocalTime.of(
                        Integer.parseInt(timeStringParts[HOUR_INDEX], INTEGER_RADIX),
                        Integer.parseInt(timeStringParts[MINUTE_INDEX]),
                        Integer.parseInt(timeStringParts[SECOND_INDEX]));
            }
        } catch (NumberFormatException | DateTimeException e) {
            return null;
        }

    }

    /**
     * Parses a date and time string or only date string with the specified time
     *     as a separate LocalTime object.
     * @param dateTimeString Date and time string.<br />
     *        Time string must contain hours and minutes. Seconds are optional, if not specified,
     *        then 0 is substituted. The time is separated from date by a space.<br />
     *        If time string is not specified then <strong>defaultTme</strong> is used.
     * @param defaultTime Time that is used if string is not contains time part.
     * @return LocalDateTime object representing the parsed string.
     *         Null if parsing was not successful.
     */
    public static LocalDateTime parseDateTimeString(String dateTimeString, LocalTime defaultTime) {

        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }

        String[] dateTimeStringParts = dateTimeString.trim().split(" ");

        LocalDate localDate = parseDateString(dateTimeStringParts[0]);
        if (localDate == null) {
            return null;
        }

        LocalTime localTime = null;
        if (dateTimeStringParts.length > 1) {
            localTime = parseTimeString(dateTimeStringParts[1]);
        }

        if (localTime == null) {
            localTime = (defaultTime != null) ? defaultTime : LocalTime.of(0, 0, 0);
        }

        return LocalDateTime.of(localDate, localTime);
    }
}
