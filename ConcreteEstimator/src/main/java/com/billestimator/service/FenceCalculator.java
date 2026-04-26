package com.billestimator.service;

import com.billestimator.model.FenceSpec;
import com.billestimator.model.FenceResult;

public class FenceCalculator {

    public static FenceResult calculate(FenceSpec s) {
        FenceResult r = new FenceResult();

        // These are the gate widths I am using for the estimate
        double gateOpenings = (s.getSingleWalkGates() * 4.0)
                + (s.getDoubleGates() * 14.0)
                + (s.getSlidingGates() * 16.0);

        r.fabricLinearFt = round2(Math.max(0, s.getPerimeterFt() - gateOpenings));

        // I am assuming 4 corner posts, plus 2 terminal posts per gate opening
        int gateCount = s.getSingleWalkGates() + s.getDoubleGates() + s.getSlidingGates();
        r.terminalPostCount = 4 + (gateCount * 2);

        double postableLength = Math.max(0, r.fabricLinearFt - (r.terminalPostCount * s.getPostSpacingFt()));
        r.linePostCount = (int) Math.max(0, Math.floor(postableLength / s.getPostSpacingFt()));

        r.totalPostCount = r.linePostCount + r.terminalPostCount;
        r.postConcreteBags = r.totalPostCount; // I am using 1 bag of concrete per post

        r.topRailLinearFt = round2(r.fabricLinearFt);

        // Add a little extra for material overage
        double overage = 1.0 + (s.getMaterialOveragePercent() / 100.0);

        r.fabricCost = round2(r.fabricLinearFt * overage * s.getFabricPricePerLf());
        r.postCost = round2(r.totalPostCount * s.getPostUnitPrice());
        r.gateCost = round2(
                (s.getSingleWalkGates() * s.getWalkGatePrice())
                + (s.getDoubleGates() * s.getDoubleGatePrice())
                + (s.getSlidingGates() * s.getSlidingGatePrice())
                + (s.getSlidingGates() > 0 && s.isSlidingGateMotor() ? s.getSlidingGates() * s.getSlidingGateMotorPrice() : 0));
        r.postConcreteCost = round2(r.postConcreteBags * s.getPostConcreteBagPrice());

        double topTreatmentLf = (s.getTopTreatment() != FenceSpec.TopTreatment.NONE) ? r.topRailLinearFt : 0;
        r.topTreatmentCost = round2(topTreatmentLf * s.getTopTreatmentPricePerLf());

        // I am estimating hardware as 8% of the main material costs
        double priorMat = r.fabricCost + r.postCost + r.gateCost + r.postConcreteCost + r.topTreatmentCost;
        r.hardwareCost = round2(priorMat * 0.08);

        r.materialSubtotal = round2(priorMat + r.hardwareCost);
        r.laborCost = round2(s.getFenceLaborHours() * s.getFenceLaborRate());
        r.subtotal = round2(r.materialSubtotal + r.laborCost);

        double discountFromPercent = round2(r.subtotal * (s.getDiscountPercent() / 100.0));
        r.discountAmount = round2(discountFromPercent + s.getDiscountFixed());
        r.grandTotal = round2(r.subtotal - r.discountAmount);

        return r;
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
