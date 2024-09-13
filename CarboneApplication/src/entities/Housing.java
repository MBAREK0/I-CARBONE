package entities;

import java.time.LocalDate;

public class Housing extends Consumption {

    private double energyConsumption;
    private String energyType;

    public Housing(LocalDate startDate, LocalDate endDate, double amount, ConsumptionType type, int userId, double energyConsumption, String energyType) {
        super(startDate, endDate, amount, type, userId);
        this.energyConsumption = energyConsumption;
        this.energyType = energyType;
    }

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public String getEnergyType() {
        return energyType;
    }

    @Override
    public double calculateImpact() {
        double impactPerEnergy = (energyType.equals("ELECTRICITY") ? 1.5 : 2.0);
        return impactPerEnergy * energyConsumption * getAmount();
    }
}
