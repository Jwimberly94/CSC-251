package com.billestimator.service;

import com.billestimator.model.ConcreteSpec;
import com.billestimator.model.ConcreteResult;

public class ConcreteCalculator {

    // This does the concrete estimate math
    public static ConcreteResult calculate(ConcreteSpec spec) {
        // I put all the calculated values in here
        ConcreteResult result = new ConcreteResult();

        // Figure out the pad area in square feet
        result.areaSqFt = round2(spec.getLengthFt() * spec.getWidthFt());
        System.out.println("Area calculated: " + result.areaSqFt + " sq ft"); // just here so I can check the math

        // Change thickness from inches into feet
        double thicknessFt = spec.getThicknessInches() / 12.0;
        // Get the raw cubic yards before adding waste
        result.rawCubicYards = round2((spec.getLengthFt() * spec.getWidthFt() * thicknessFt) / 27.0);
        System.out.println("Raw cubic yards: " + result.rawCubicYards); // just here so I can check the math

        // Add the waste percentage
        double wasteFactor = 1.0 + (spec.getWastePercent() / 100.0);
        // This is the adjusted cubic yards after waste
        result.adjustedCubicYards = round2(result.rawCubicYards * wasteFactor);

        // Material cost is adjusted yards times price per yard
        result.materialCost = round2(result.adjustedCubicYards * spec.getPricePerCubicYard());

        // Total labor hours for everybody combined
        result.totalLaborHours = round2(spec.getEmployees() * spec.getHoursPerEmployee());
        // Labor cost is hours times rate
        result.laborCost = round2(result.totalLaborHours * spec.getLaborRatePerHour());

        // Only add rebar cost if that option was checked
        result.rebarCost = spec.isIncludeRebar() ? round2(result.areaSqFt * spec.getRebarCostPerSqFt()) : 0;
        // Same idea for equipment rental
        result.equipmentCost = spec.isIncludeEquipmentRental() ? round2(spec.getEquipmentRentalCost()) : 0;

        // Add everything up for the subtotal
        result.subtotal = round2(result.materialCost + result.laborCost + result.rebarCost + result.equipmentCost);
        System.out.println("Subtotal: " + result.subtotal); // just here so I can check the math

        // Work out the discount amount
        double discountFromPercent = round2(result.subtotal * (spec.getDiscountPercent() / 100.0));
        // Combine the percent discount and the fixed discount
        result.discountAmount = round2(discountFromPercent + spec.getDiscountFixed());
        // Take the discount off the subtotal
        double afterDiscount = round2(result.subtotal - result.discountAmount);

        // Contingency is just a little buffer for extra costs
        result.contingencyAmount = round2(afterDiscount * (spec.getContingencyPercent() / 100.0));
        // Final total after everything is added in
        result.grandTotal = round2(afterDiscount + result.contingencyAmount);
        System.out.println("Grand total: " + result.grandTotal); // just here so I can check the math

        return result;
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
