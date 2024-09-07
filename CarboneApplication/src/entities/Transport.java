package entities;

import java.time.LocalDate;


public class Transport extends Consumption {
    private double distanceTraveled;
    private String vehicleType;

    public Transport(LocalDate startDate, LocalDate endDate, double amount, double distanceTraveled, String vehicleType) {
        super(startDate, endDate, amount, ConsumptionType.TRANSPORT);
        this.distanceTraveled = distanceTraveled;
        this.vehicleType = vehicleType;
    }

    @Override
    public double calculateImpact() {
        if (vehicleType.equalsIgnoreCase("car")) {
            impact = distanceTraveled * 0.5;
        } else if (vehicleType.equalsIgnoreCase("train")) {
            impact = distanceTraveled * 0.1;
        }
        return impact;
    }
}
