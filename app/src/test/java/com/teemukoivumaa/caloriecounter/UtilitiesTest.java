package com.teemukoivumaa.caloriecounter;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UtilitiesTest {

    private final SimpleDateFormat shortDateFormat = new SimpleDateFormat("d.M EEE", Locale.ENGLISH);
    private final SimpleDateFormat longDateFormat = new SimpleDateFormat("y-MM-d", Locale.ENGLISH);

    private final Utilities utilities = new Utilities();

    @ParameterizedTest(name = "{index} => 2022-04-18 -/+ {1} == {0} ")
    @DisplayName("Should get the correct dates")
    @MethodSource("dateTestProvider")
    void getDate(String expectedDate, int numOfDays) {
        String startDate = "2022-04-18";

        assertEquals(expectedDate, utilities.getDate(startDate, numOfDays));
    }

    private static Stream<Arguments> dateTestProvider() {
        return Stream.of(
                Arguments.of("2022-04-12", -6),
                Arguments.of("2022-04-24", 6),
                Arguments.of("2022-04-18", 0)
        );
    }


    @Test
    @DisplayName("Should convert long date to short date")
    void convertFromLongToShort() {
        String longDate = "2022-04-18";
        String shortDate = "18.4 Mon";

        assertEquals(shortDate, utilities.convertFromLongToShort(longDate));
    }

    @Test
    @DisplayName("Should convert long string to date")
    void convertLongStringToDate() throws ParseException {
        String stringDate = "2022-04-18";
        Date expectedDate = longDateFormat.parse(stringDate);

        assertEquals(expectedDate, utilities.convertStringToDate(stringDate, longDateFormat));
    }

    @Test
    @DisplayName("Should convert short string to date")
    void convertShortStringToDate() throws ParseException {
        String stringDate = "18.4 Mon";
        Date expectedDate = shortDateFormat.parse(stringDate);

        assertEquals(expectedDate, utilities.convertStringToDate(stringDate, shortDateFormat));
    }

    @Test
    @DisplayName("Should get the current date as long")
    void getCurrentDateLong() {
        Date currentTime = Calendar.getInstance().getTime();
        String stringDate = longDateFormat.format(currentTime);

        assertEquals(stringDate, utilities.getCurrentDate(longDateFormat));
    }

    @Test
    @DisplayName("Should get the current date as short")
    void getCurrentDateShort() {
        Date currentTime = Calendar.getInstance().getTime();
        String stringDate = shortDateFormat.format(currentTime);

        assertEquals(stringDate, utilities.getCurrentDate(shortDateFormat));
    }

    @ParameterizedTest(name = "{index} => MAX: {1}")
    @DisplayName("Should get the max value of list")
    @MethodSource("maxTestProvider")
    void getMax(List<Integer> integers, int expectedMax) {
        assertEquals(expectedMax, utilities.getMax(integers));
    }

    private static Stream<Arguments> maxTestProvider() {
        return Stream.of(
                Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6), 6),
                Arguments.of(Arrays.asList(1, 2, 3, 4, 5), 5),
                Arguments.of(Arrays.asList(Integer.MIN_VALUE, Integer.MAX_VALUE), Integer.MAX_VALUE),
                Arguments.of(Arrays.asList(102, 4, 51, 22, -10), 102),
                Arguments.of(Arrays.asList(20, 0), 20)
        );
    }
}