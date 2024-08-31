package services;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;


public class User {
    public String id;
    public String name;
    public int age;
    public List<Consumption> consumptionList;

    public User(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.consumptionList = new LinkedList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }

    public void addConsumption(LocalDate startDate, LocalDate endDate, double amount) {
        consumptionList.add(new Consumption(startDate, endDate, amount));
    }

    public double getTotalConsumption(LocalDate startDate, LocalDate endDate) {
        return consumptionList.stream()
                .filter(c -> (c.getStartDate().isBefore(endDate) || c.getStartDate().isEqual(endDate)) &&
                        (c.getEndDate().isAfter(startDate) || c.getEndDate().isEqual(startDate)))
                .mapToDouble(c -> {
                    LocalDate overlapStart = startDate.isAfter(c.getStartDate()) ? startDate : c.getStartDate();
                    LocalDate overlapEnd = endDate.isBefore(c.getEndDate()) ? endDate : c.getEndDate();
                    long daysBetween = overlapEnd.toEpochDay() - overlapStart.toEpochDay() + 1;
                    long totalDays = c.getEndDate().toEpochDay() - c.getStartDate().toEpochDay() + 1;
                    double dailyAmount = c.getAmount() / totalDays;
                    return dailyAmount * daysBetween;
                })
                .sum();
    }

    public double getDailyConsumption(LocalDate date) {
        return getTotalConsumption(date, date);
    }

    public double getWeeklyConsumption(int year, int weekOfYear) {
        LocalDate startOfWeek = LocalDate.ofYearDay(year, 1).plusWeeks(weekOfYear - 1);
        LocalDate endOfWeek = startOfWeek.plusWeeks(1).minusDays(1);
        return getTotalConsumption(startOfWeek, endOfWeek);
    }

    public double getMonthlyConsumption(int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        return getTotalConsumption(startOfMonth, endOfMonth);
    }

    public double getYearlyConsumption(int year) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);
        return getTotalConsumption(startOfYear, endOfYear);
    }

    @Override
    public String toString() {
        return "ID: " + id + "| Name: " + name + "| Age: " + age;
    }
}
