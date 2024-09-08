package entities;

import java.time.LocalDate;

public class Transport extends Consumption {

    private double distanceTraveled;
    private String vehicleType;

    public Transport(LocalDate startDate, LocalDate endDate, double amount, ConsumptionType type, int userId, double distanceTraveled, String vehicleType) {
        super(startDate, endDate, amount, type, userId);
        this.distanceTraveled = distanceTraveled;
        this.vehicleType = vehicleType;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    @Override
    public double calculateImpact() {
        double impactPerDistance = (vehicleType.equals("CAR") ? 0.5 : 0.1);
        return impactPerDistance * distanceTraveled;
    }
}
