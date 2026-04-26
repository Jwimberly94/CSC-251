package com.billestimator.model;

public class PadPreset {
    private String name;
    private double lengthFt;
    private double widthFt;
    private String typicalUse;

    public PadPreset(String name, double lengthFt, double widthFt, String typicalUse) {
        this.name = name;
        this.lengthFt = lengthFt;
        this.widthFt = widthFt;
        this.typicalUse = typicalUse;
    }

    public String getName() { return name; }
    public double getLengthFt() { return lengthFt; }
    public double getWidthFt() { return widthFt; }
    public String getTypicalUse() { return typicalUse; }

    @Override
    public String toString() { return name + " (" + (int)lengthFt + "' x " + (int)widthFt + "')"; }
}
