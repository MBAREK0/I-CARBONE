package services;

import java.time.LocalDate;

public class Consumption {
    private LocalDate startDate;
    private LocalDate endDate;
    private double amount;

    public Consumption(LocalDate startDate, LocalDate endDate, double amount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return "From: " + startDate + " To: " + endDate + ", Amount: " + amount;
    }
}
