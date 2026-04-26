package com.billestimator.model;

public class ConcreteSpec {
    private double lengthFt;
    private double widthFt;
    private double thicknessInches;
    private double wastePercent;        // for example, 8.0 means 8 percent
    private int employees;
    private double hoursPerEmployee;
    private double laborRatePerHour;
    private double pricePerCubicYard;
    private boolean includeRebar;
    private double rebarCostPerSqFt;
    private boolean includeEquipmentRental;
    private double equipmentRentalCost;
    private double discountPercent;
    private double discountFixed;
    private double contingencyPercent;

    public ConcreteSpec() {
        this.wastePercent = 8.0;
        this.contingencyPercent = 0.0;
    }

    public double getLengthFt() { return lengthFt; }
    public void setLengthFt(double v) { this.lengthFt = v; }

    public double getWidthFt() { return widthFt; }
    public void setWidthFt(double v) { this.widthFt = v; }

    public double getThicknessInches() { return thicknessInches; }
    public void setThicknessInches(double v) { this.thicknessInches = v; }

    public double getWastePercent() { return wastePercent; }
    public void setWastePercent(double v) { this.wastePercent = v; }

    public int getEmployees() { return employees; }
    public void setEmployees(int v) { this.employees = v; }

    public double getHoursPerEmployee() { return hoursPerEmployee; }
    public void setHoursPerEmployee(double v) { this.hoursPerEmployee = v; }

    public double getLaborRatePerHour() { return laborRatePerHour; }
    public void setLaborRatePerHour(double v) { this.laborRatePerHour = v; }

    public double getPricePerCubicYard() { return pricePerCubicYard; }
    public void setPricePerCubicYard(double v) { this.pricePerCubicYard = v; }

    public boolean isIncludeRebar() { return includeRebar; }
    public void setIncludeRebar(boolean v) { this.includeRebar = v; }

    public double getRebarCostPerSqFt() { return rebarCostPerSqFt; }
    public void setRebarCostPerSqFt(double v) { this.rebarCostPerSqFt = v; }

    public boolean isIncludeEquipmentRental() { return includeEquipmentRental; }
    public void setIncludeEquipmentRental(boolean v) { this.includeEquipmentRental = v; }

    public double getEquipmentRentalCost() { return equipmentRentalCost; }
    public void setEquipmentRentalCost(double v) { this.equipmentRentalCost = v; }

    public double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(double v) { this.discountPercent = v; }

    public double getDiscountFixed() { return discountFixed; }
    public void setDiscountFixed(double v) { this.discountFixed = v; }

    public double getContingencyPercent() { return contingencyPercent; }
    public void setContingencyPercent(double v) { this.contingencyPercent = v; }
}
