package entities;

import java.time.LocalDate;

public abstract class Consumption {
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private double amount;
    private double consumptionImpact;
    private ConsumptionType type;
    private int userId;

    public Consumption(LocalDate startDate, LocalDate endDate, double amount, ConsumptionType type, int userId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.type = type;
        this.userId = userId;

    }
    public Consumption() {

    }


    public abstract double calculateImpact();

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getConsumptionImpact() { return consumptionImpact; }
    public void setConsumptionImpact(double consumptionImpact) { this.consumptionImpact = consumptionImpact; }

    public ConsumptionType getType() { return type; }
    public void setType(ConsumptionType type) { this.type = type; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "Type: " + type + ", From: " + startDate + " To: " + endDate + ", Amount: " + amount + ", Consumption Impact: " + consumptionImpact + ", UserID: " + userId;
    }
}
