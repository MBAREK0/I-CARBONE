package entities;

import java.time.LocalDate;

public abstract class Consumption {
    private LocalDate startDate;
    private LocalDate endDate;
    private double amount;
    protected double impact;
    private ConsumptionType type;

    public Consumption(LocalDate startDate, LocalDate endDate, double amount, ConsumptionType type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.type = type;
    }

    public Consumption(){}

    public abstract double calculateImpact();


    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getAmount() { return amount; }
    public double getImpact() { return impact; }

    @Override
    public String toString() {
        return "Type: " + type + ", From: " + startDate + " To: " + endDate + ", Amount: " + amount;
    }
}
