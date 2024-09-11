package utils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DateChecker {

    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString);  // You might want to handle parsing exceptions here
    }

    public static boolean isDateValid(String dateString) {
        try {
            LocalDate.parse(dateString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static List<LocalDate> getDatesBetween(LocalDate start, LocalDate end) {

        LocalDate adjustedEnd = end.plusDays(1);

        return start.datesUntil(adjustedEnd)
                .collect(Collectors.toList());
    }


    public static Boolean isValidPeriod(LocalDate startDate, LocalDate endDate) {
        return !startDate.isAfter(endDate);
    }

    public static Boolean isDateInPeriod(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public static Boolean checkDate(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    public static Boolean check(String date) {
        return isDateValid(date) && checkDate(parseDate(date));
    }
}
