package name.mihnayan.dateparser.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;

import name.mihnayan.dateparser.DateTimeParser;

import org.junit.Test;

public class DateParserTest {
    
    private static String[] invalidDates = {
        "29.02.2015",
        "30.02.2016",
        "32.01.2016",
        "31.04.2016",
        "01.01.201",
        "01.13.2016"
    };
    private static String[] invalidTimes = {
        "00:00:60",
        "25:00:00",
        "25:00",
        "09:60:59",
        "10:60"
    };
    private static HashMap<LocalDateTime, String[]> validDateTimes = new HashMap<>();
    
    static {
        validDateTimes.put(LocalDateTime.of(2016, 1, 1, 0, 0, 0), new String[] {
            "01.01.2016",
            "1.1.2016",
            "01.01.16",
            "1.1.16",
            "01/01/2016",
            "1/1/2016",
            "01/01/16",
            "1/1/16"});
        validDateTimes.put(LocalDateTime.of(2016, 2, 29, 0, 0, 0), new String[] {
            "29.02.2016 00:00:00",
            "29.2.2016",
            "29.2.16 00:00:00",
            "29.02.16",
            "29/02/2016",
            "29/2/2016 00:00",
            "29/2/16"
        });
        validDateTimes.put(LocalDateTime.of(2016, 1, 1, 23, 59, 59), new String[] {
            "1/1/16 23:59:59",
            "01/01/2016 23:59:59",
            "01.01.16 23:59:59"
        });
    }
    
    @Test
    public void nullTest() {
        assertNull(DateTimeParser.parseDateTimeString(null, null));
        assertNull(DateTimeParser.parseDateString(null));
        assertNull(DateTimeParser.parseTimeString(null));
    }
    
    @Test
    public void validDateTimesTest() {
        validDateTimes.forEach((validDate, datesInString) -> {
            Arrays.stream(datesInString).forEach(dateInString -> {
                assertEquals(validDate, DateTimeParser.parseDateTimeString(dateInString, null));
            });
        });
    }
    
    @Test
    public void validDateTimesTestWithDefaultTime() {
        assertEquals(LocalDateTime.of(2016, 1, 1, 23, 59, 59),
                DateTimeParser.parseDateTimeString("1/1/16", LocalTime.of(23, 59, 59)));
    }
    
    @Test
    public void invalidDateTest() {
        Arrays.stream(invalidDates).forEach(invalidDate -> {
            assertNull(DateTimeParser.parseDateString(invalidDate));
        });
    }
    
    @Test
    public void invalidTimeTest() {
        Arrays.stream(invalidTimes).forEach(invalidTime -> {
            assertNull(DateTimeParser.parseTimeString(invalidTime));
        });
    }
}
