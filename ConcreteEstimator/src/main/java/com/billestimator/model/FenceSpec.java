package com.billestimator.model;

public class FenceSpec {
    public enum PostType { GALVANIZED, ALUMINUM, SCHEDULE40 }
    public enum MeshGauge { GAUGE_9, GAUGE_11, GAUGE_11_5 }
    public enum TopTreatment { NONE, BARBED_1, BARBED_2, BARBED_3, RAZOR_WIRE, PRIVACY_SLATS }

    private double perimeterFt;
    private int heightFt;
    private double customHeightFt;
    private boolean useCustomHeight;
    private MeshGauge meshGauge;
    private PostType postType;
    private double postSpacingFt;
    private int singleWalkGates;
    private int doubleGates;
    private int slidingGates;
    private boolean slidingGateMotor;
    private TopTreatment topTreatment;
    private double materialOveragePercent;
    private double fenceLaborHours;
    private double fenceLaborRate;
    private double discountPercent;
    private double discountFixed;

    // These are the editable material prices from the CSV
    private double fabricPricePerLf;
    private double postUnitPrice;
    private double walkGatePrice;
    private double doubleGatePrice;
    private double slidingGatePrice;
    private double slidingGateMotorPrice;
    private double topTreatmentPricePerLf;
    private double postConcreteBagPrice;

    public FenceSpec() {
        this.postSpacingFt = 10.0;
        this.materialOveragePercent = 5.0;
        this.meshGauge = MeshGauge.GAUGE_11;
        this.postType = PostType.GALVANIZED;
        this.topTreatment = TopTreatment.NONE;
    }

    public double getPerimeterFt() { return perimeterFt; }
    public void setPerimeterFt(double v) { this.perimeterFt = v; }

    public int getHeightFt() { return heightFt; }
    public void setHeightFt(int v) { this.heightFt = v; }

    public double getCustomHeightFt() { return customHeightFt; }
    public void setCustomHeightFt(double v) { this.customHeightFt = v; }

    public boolean isUseCustomHeight() { return useCustomHeight; }
    public void setUseCustomHeight(boolean v) { this.useCustomHeight = v; }

    public double getEffectiveHeightFt() {
        return useCustomHeight ? customHeightFt : heightFt;
    }

    public MeshGauge getMeshGauge() { return meshGauge; }
    public void setMeshGauge(MeshGauge v) { this.meshGauge = v; }

    public PostType getPostType() { return postType; }
    public void setPostType(PostType v) { this.postType = v; }

    public double getPostSpacingFt() { return postSpacingFt; }
    public void setPostSpacingFt(double v) { this.postSpacingFt = v; }

    public int getSingleWalkGates() { return singleWalkGates; }
    public void setSingleWalkGates(int v) { this.singleWalkGates = v; }

    public int getDoubleGates() { return doubleGates; }
    public void setDoubleGates(int v) { this.doubleGates = v; }

    public int getSlidingGates() { return slidingGates; }
    public void setSlidingGates(int v) { this.slidingGates = v; }

    public boolean isSlidingGateMotor() { return slidingGateMotor; }
    public void setSlidingGateMotor(boolean v) { this.slidingGateMotor = v; }

    public TopTreatment getTopTreatment() { return topTreatment; }
    public void setTopTreatment(TopTreatment v) { this.topTreatment = v; }

    public double getMaterialOveragePercent() { return materialOveragePercent; }
    public void setMaterialOveragePercent(double v) { this.materialOveragePercent = v; }

    public double getFenceLaborHours() { return fenceLaborHours; }
    public void setFenceLaborHours(double v) { this.fenceLaborHours = v; }

    public double getFenceLaborRate() { return fenceLaborRate; }
    public void setFenceLaborRate(double v) { this.fenceLaborRate = v; }

    public double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(double v) { this.discountPercent = v; }

    public double getDiscountFixed() { return discountFixed; }
    public void setDiscountFixed(double v) { this.discountFixed = v; }

    public double getFabricPricePerLf() { return fabricPricePerLf; }
    public void setFabricPricePerLf(double v) { this.fabricPricePerLf = v; }

    public double getPostUnitPrice() { return postUnitPrice; }
    public void setPostUnitPrice(double v) { this.postUnitPrice = v; }

    public double getWalkGatePrice() { return walkGatePrice; }
    public void setWalkGatePrice(double v) { this.walkGatePrice = v; }

    public double getDoubleGatePrice() { return doubleGatePrice; }
    public void setDoubleGatePrice(double v) { this.doubleGatePrice = v; }

    public double getSlidingGatePrice() { return slidingGatePrice; }
    public void setSlidingGatePrice(double v) { this.slidingGatePrice = v; }

    public double getSlidingGateMotorPrice() { return slidingGateMotorPrice; }
    public void setSlidingGateMotorPrice(double v) { this.slidingGateMotorPrice = v; }

    public double getTopTreatmentPricePerLf() { return topTreatmentPricePerLf; }
    public void setTopTreatmentPricePerLf(double v) { this.topTreatmentPricePerLf = v; }

    public double getPostConcreteBagPrice() { return postConcreteBagPrice; }
    public void setPostConcreteBagPrice(double v) { this.postConcreteBagPrice = v; }
}
