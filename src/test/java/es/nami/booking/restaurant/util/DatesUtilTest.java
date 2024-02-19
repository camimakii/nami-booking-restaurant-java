package es.nami.booking.restaurant.util;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatesUtilTest {

    private final static LocalDateTime RANGE1_START = LocalDateTime.of(2000, 1, 1, 0, 0);
    private final static LocalDateTime RANGE1_END = RANGE1_START.plusDays(1);

    // IS_OVERLAP
    @Test
    void isOverlap_overlaps2HoursAtStart_returnTrue() {
        LocalDateTime range2Start = RANGE1_START.minusHours(2);
        LocalDateTime range2End = RANGE1_START.plusHours(2);

        boolean result = DatesUtil.isOverlap(RANGE1_START, RANGE1_END, range2Start, range2End);
        assertTrue(result);
    }

    @Test
    void isOverlap_overlaps2HoursAtEnd_returnTrue() {
        LocalDateTime range2Start = RANGE1_END.minusHours(2);
        LocalDateTime range2End = RANGE1_END.plusHours(2);

        boolean result = DatesUtil.isOverlap(RANGE1_START, RANGE1_END, range2Start, range2End);
        assertTrue(result);
    }

    @Test
    void isOverlap_containsEntirely_returnTrue() {
        LocalDateTime range2Start = RANGE1_START.plusHours(2);
        LocalDateTime range2End = RANGE1_END.minusHours(2);

        boolean result = DatesUtil.isOverlap(RANGE1_START, RANGE1_END, range2Start, range2End);
        assertTrue(result);
    }

    @Test
    void isOverlap_notOverlapsAtAllAtEnd_returnTrue() {
        LocalDateTime range2Start = RANGE1_END.plusHours(2);
        LocalDateTime range2End = range2Start.plusHours(2);

        boolean result = DatesUtil.isOverlap(RANGE1_START, RANGE1_END, range2Start, range2End);
        assertFalse(result);
    }

    @Test
    void isOverlap_notOverlapsAtAllAtStart_returnTrue() {
        LocalDateTime range2End = RANGE1_START.minusHours(2);
        LocalDateTime range2Start = range2End.minusHours(2);

        boolean result = DatesUtil.isOverlap(RANGE1_START, RANGE1_END, range2Start, range2End);
        assertFalse(result);
    }

    // GENERATE DATE_TIMES WITH INTERVAL
    @Test
    void generateWithInterval_lastDateTimePlusIntervalNotAfterEnd_isTrue() {
        Duration duration = Duration.ofHours(2).plusMinutes(30);

        List<LocalDateTime> generatedDateTimesWithInterval = DatesUtil.generateStartDateTimesWithInterval(RANGE1_START, RANGE1_END, duration);
        LocalDateTime lastDateTime = generatedDateTimesWithInterval.get(generatedDateTimesWithInterval.size() - 1);
        assertTrue(lastDateTime.plus(duration).isBefore(RANGE1_END));
    }

    @Test
    void generateWithInterval_firstDateTimeIsStart_isTrue() {
        Duration duration = Duration.ofHours(2).plusMinutes(30);

        List<LocalDateTime> generatedDateTimesWithInterval = DatesUtil.generateStartDateTimesWithInterval(RANGE1_START, RANGE1_END, duration);
        LocalDateTime firstDateTime = generatedDateTimesWithInterval.get(0);
        assertTrue(firstDateTime.isEqual(RANGE1_START));
    }

}