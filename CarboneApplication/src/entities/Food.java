package entities;

import java.time.LocalDate;

public class Food extends Consumption {

    private String typeOfFood;
    private double weight;

    // Constructor definition
    public Food(LocalDate startDate, LocalDate endDate, double amount, ConsumptionType type, int userId, String typeOfFood, double weight) {
        super(startDate, endDate, amount, type, userId);
        this.typeOfFood = typeOfFood;
        this.weight = weight;
    }

    // Getter and setter methods


    public String getTypeOfFood() {
        return typeOfFood;
    }

    public void setTypeOfFood(String typeOfFood) {
        this.typeOfFood = typeOfFood;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public double calculateImpact() {
        double impactPerWeight = (typeOfFood.equals("MEAT") ? 5.0 : 0.5);
        return impactPerWeight * weight * super.getAmount();
    }

}
