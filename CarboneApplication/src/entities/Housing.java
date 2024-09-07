package entities;

import java.time.LocalDate;
public class Housing extends Consumption {
    private double energyConsumption;
    private String energyType;

    public Housing(LocalDate startDate, LocalDate endDate, double amount, double energyConsumption, String energyType) {
        super(startDate, endDate, amount, ConsumptionType.HOUSING);
        this.energyConsumption = energyConsumption;
        this.energyType = energyType;
    }

    @Override
    public double calculateImpact() {
        if (energyType.equalsIgnoreCase("electricity")) {
            impact = energyConsumption * 1.5;
        } else if (energyType.equalsIgnoreCase("gas")) {
            impact = energyConsumption * 2.0;
        }
        return impact;
    }
}
