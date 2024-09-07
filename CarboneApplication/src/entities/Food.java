package entities;

import java.time.LocalDate;
public class Food extends Consumption {
    private String foodType;
    private double weight;

    public Food(LocalDate startDate, LocalDate endDate, double amount, String foodType, double weight) {
        super(startDate, endDate, amount, ConsumptionType.FOOD);
        this.foodType = foodType;
        this.weight = weight;
    }

    @Override
    public double calculateImpact() {
        if (foodType.equalsIgnoreCase("meat")) {
            impact = weight * 5.0;
        } else if (foodType.equalsIgnoreCase("vegetable")) {
            impact = weight * 0.5;
        }
        return impact;
    }
}
