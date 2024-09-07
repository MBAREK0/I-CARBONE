package entities;

import entities.Consumption;

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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void addConsumption(LocalDate startDate, LocalDate endDate, double amount) {

        if (consumptionList.isEmpty()) {
           // consumptionList.add(new Consumption(startDate, endDate, amount));
            return;
        }
        for (Consumption c : consumptionList) {

            if ((endDate.isAfter(c.getStartDate()) && endDate.isBefore(c.getEndDate()) || endDate.isEqual(c.getEndDate()) || endDate.isEqual(c.getStartDate())  ) || 
                (startDate.isAfter(c.getStartDate()) && startDate.isBefore(c.getEndDate()) || startDate.isEqual(c.getEndDate()) || startDate.isEqual(c.getStartDate()))
            ) {
                LocalDate newStartDate = startDate.isBefore(c.getStartDate()) ? startDate : c.getStartDate();
                LocalDate newEndDate = endDate.isAfter(c.getEndDate()) ? endDate : c.getEndDate();
                double newAmount = c.getAmount() + amount;
                consumptionList.remove(c);
                //consumptionList.add(new Consumption(newStartDate, newEndDate, newAmount));
                return;

            }else{
                //consumptionList.add(new Consumption(startDate, endDate, amount));
                return;
            }
        }

    }

    public double getTotalConsumption(LocalDate startDate, LocalDate endDate) {
        double totalConsumption = 0.0;

        for (Consumption c : consumptionList) {

            if ((c.getStartDate().isBefore(endDate) || c.getStartDate().isEqual(endDate)) &&
                    (c.getEndDate().isAfter(startDate) || c.getEndDate().isEqual(startDate))) {

                LocalDate StartOfPeriod = startDate.isAfter(c.getStartDate()) ? startDate : c.getStartDate();
                LocalDate EndOfPeriod = endDate.isBefore(c.getEndDate()) ? endDate : c.getEndDate();

                long daysBetween = EndOfPeriod.toEpochDay() - StartOfPeriod.toEpochDay() + 1;

                long totalDays = c.getEndDate().toEpochDay() - c.getStartDate().toEpochDay() + 1;

                double dailyAmount = c.getAmount() / totalDays;

                totalConsumption += dailyAmount * daysBetween;
            }
        }

        return totalConsumption;
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
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        return getTotalConsumption(startOfMonth, endOfMonth);
    }

    public double getYearlyConsumption(int year) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);
        return getTotalConsumption(startOfYear, endOfYear);
    }

    public String generateReport() {
        String result = "";
        
        for (Consumption c : consumptionList) {
            result += c.toString() + "\n\t\t";
        }
        return result;
    }

    @Override
    public String toString() {
        return "ID: " + id + "| Name: " + name + "| Age: " + age;
    }
}
