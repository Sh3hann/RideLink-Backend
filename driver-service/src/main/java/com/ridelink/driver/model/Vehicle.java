package com.ridelink.driver.model;

public class Vehicle {
    private String make;
    private String model;
    private String licensePlate;
    private String color;
    private VehicleClass vehicleClass;

    public Vehicle() {}

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public VehicleClass getVehicleClass() { return vehicleClass; }
    public void setVehicleClass(VehicleClass vehicleClass) { this.vehicleClass = vehicleClass; }
}
