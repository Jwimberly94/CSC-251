package com.billestimator.model;

public class FencePreset {
    private String name;
    private double enclosureLengthFt;
    private double enclosureWidthFt;
    private double perimeterFt;
    private int heightFt;
    private String typicalUse;

    public FencePreset(String name, double enclosureLengthFt, double enclosureWidthFt,
                       double perimeterFt, int heightFt, String typicalUse) {
        this.name = name;
        this.enclosureLengthFt = enclosureLengthFt;
        this.enclosureWidthFt = enclosureWidthFt;
        this.perimeterFt = perimeterFt;
        this.heightFt = heightFt;
        this.typicalUse = typicalUse;
    }

    public String getName() { return name; }
    public double getEnclosureLengthFt() { return enclosureLengthFt; }
    public double getEnclosureWidthFt() { return enclosureWidthFt; }
    public double getPerimeterFt() { return perimeterFt; }
    public int getHeightFt() { return heightFt; }
    public String getTypicalUse() { return typicalUse; }

    @Override
    public String toString() {
        return name + " (" + (int)enclosureLengthFt + "' x " + (int)enclosureWidthFt
                + "', " + (int)perimeterFt + " LF, " + heightFt + " ft)";
    }
}
