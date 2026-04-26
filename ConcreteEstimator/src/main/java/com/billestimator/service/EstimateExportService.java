package com.billestimator.service;

import com.billestimator.model.ProjectInfo;
import com.billestimator.model.ConcreteSpec;
import com.billestimator.model.ConcreteResult;
import com.billestimator.model.FenceSpec;
import com.billestimator.model.FenceResult;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This builds the estimate report text and saves it to a file.
 */
public class EstimateExportService {

    private final String savePath;

    public EstimateExportService(String savePath) {
        this.savePath = savePath;
        new File(savePath).mkdirs();
    }

    public String buildReport(ProjectInfo info, ConcreteSpec cSpec, ConcreteResult cRes,
                               boolean fenceIncluded, FenceSpec fSpec, FenceResult fRes) {
        StringBuilder sb = new StringBuilder();
        String sep = "=".repeat(60);
        String thin = "-".repeat(60);

        sb.append(sep).append("\n");
        sb.append("       CONCRETE PAD & CHAIN-LINK FENCE ESTIMATE\n");
        sb.append(sep).append("\n\n");

        // Start with the project info section
        sb.append("PROJECT INFORMATION\n").append(thin).append("\n");
        sb.append(String.format("  Project Name   : %s%n", info.getProjectName()));
        sb.append(String.format("  Client         : %s%n", info.getClientName()));
        sb.append(String.format("  Location       : %s%n", info.getLocation()));
        sb.append(String.format("  Estimate Date  : %s%n", info.getEstimateDate()));
        sb.append(String.format("  Estimator      : %s%n", info.getEstimatorName()));
        if (info.getNotes() != null && !info.getNotes().isEmpty()) {
            sb.append(String.format("  Notes          : %s%n", info.getNotes()));
        }
        sb.append("\n");

        // Then show the basic concrete pad summary
        sb.append("CONCRETE PAD SUMMARY\n").append(thin).append("\n");
        sb.append(String.format("  Dimensions     : %.1f ft x %.1f ft%n", cSpec.getLengthFt(), cSpec.getWidthFt()));
        sb.append(String.format("  Thickness      : %.1f inches%n", cSpec.getThicknessInches()));
        sb.append(String.format("  Area           : %.2f sq ft%n", cRes.areaSqFt));
        sb.append(String.format("  Raw Volume     : %.2f CY%n", cRes.rawCubicYards));
        sb.append(String.format("  Waste %%        : %.1f%%%n", cSpec.getWastePercent()));
        sb.append(String.format("  Adj. Volume    : %.2f CY%n", cRes.adjustedCubicYards));
        sb.append(String.format("  Price / CY     : $%.2f%n", cSpec.getPricePerCubicYard()));
        sb.append("\n");

        // This part shows the concrete costs in more detail
        sb.append("CONCRETE COST BREAKDOWN\n").append(thin).append("\n");
        sb.append(String.format("  Material Cost  : $%.2f%n", cRes.materialCost));
        sb.append(String.format("  Labor Hours    : %.2f hrs (%.0f emp x %.1f hrs)%n",
                cRes.totalLaborHours, (double) cSpec.getEmployees(), cSpec.getHoursPerEmployee()));
        sb.append(String.format("  Labor Rate     : $%.2f / hr%n", cSpec.getLaborRatePerHour()));
        sb.append(String.format("  Labor Cost     : $%.2f%n", cRes.laborCost));
        if (cSpec.isIncludeRebar()) {
            sb.append(String.format("  Rebar/Mesh     : $%.2f%n", cRes.rebarCost));
        }
        if (cSpec.isIncludeEquipmentRental()) {
            sb.append(String.format("  Equipment      : $%.2f%n", cRes.equipmentCost));
        }
        sb.append(String.format("  Subtotal       : $%.2f%n", cRes.subtotal));
        if (cRes.discountAmount > 0) {
            sb.append(String.format("  Discount       : -$%.2f%n", cRes.discountAmount));
        }
        if (cRes.contingencyAmount > 0) {
            sb.append(String.format("  Contingency    : +$%.2f%n", cRes.contingencyAmount));
        }
        sb.append(String.format("  PAD TOTAL      : $%.2f%n", cRes.grandTotal));
        sb.append("\n");

        if (fenceIncluded && fRes != null) {
            sb.append("CHAIN-LINK FENCE SUMMARY\n").append(thin).append("\n");
            sb.append(String.format("  Perimeter      : %.1f LF%n", fSpec.getPerimeterFt()));
            sb.append(String.format("  Height         : %.0f ft%n", fSpec.getEffectiveHeightFt()));
            sb.append(String.format("  Mesh Gauge     : %s%n", fSpec.getMeshGauge().toString().replace("GAUGE_", "").replace("_", ".")));
            sb.append(String.format("  Post Type      : %s%n", fSpec.getPostType()));
            sb.append(String.format("  Post Spacing   : %.0f ft o.c.%n", fSpec.getPostSpacingFt()));
            sb.append(String.format("  Fabric LF      : %.2f LF%n", fRes.fabricLinearFt));
            sb.append(String.format("  Line Posts     : %d%n", fRes.linePostCount));
            sb.append(String.format("  Terminal Posts : %d%n", fRes.terminalPostCount));
            sb.append(String.format("  Post Concrete  : %d bags%n", fRes.postConcreteBags));
            sb.append("\n");

            sb.append("FENCE COST BREAKDOWN\n").append(thin).append("\n");
            sb.append(String.format("  Fabric Cost    : $%.2f%n", fRes.fabricCost));
            sb.append(String.format("  Post Cost      : $%.2f%n", fRes.postCost));
            sb.append(String.format("  Gate Cost      : $%.2f%n", fRes.gateCost));
            sb.append(String.format("  Post Concrete  : $%.2f%n", fRes.postConcreteCost));
            sb.append(String.format("  Top Treatment  : $%.2f%n", fRes.topTreatmentCost));
            sb.append(String.format("  Hardware       : $%.2f%n", fRes.hardwareCost));
            sb.append(String.format("  Material Sub   : $%.2f%n", fRes.materialSubtotal));
            sb.append(String.format("  Labor Cost     : $%.2f%n", fRes.laborCost));
            sb.append(String.format("  Subtotal       : $%.2f%n", fRes.subtotal));
            if (fRes.discountAmount > 0) {
                sb.append(String.format("  Discount       : -$%.2f%n", fRes.discountAmount));
            }
            sb.append(String.format("  FENCE TOTAL    : $%.2f%n", fRes.grandTotal));
            sb.append("\n");
        }

        // End with the full project total
        sb.append(sep).append("\n");
        double projectTotal = cRes.grandTotal + (fenceIncluded && fRes != null ? fRes.grandTotal : 0);
        sb.append(String.format("  PROJECT GRAND TOTAL : $%.2f%n", projectTotal));
        sb.append(sep).append("\n");

        return sb.toString();
    }

    public void saveEstimate(String reportText, String projectName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String safeName = projectName.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        String fileName = savePath + File.separator + safeName + "_" + timestamp + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            pw.print(reportText);
            System.out.println("Estimate saved to: " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving estimate: " + e.getMessage());
        }
    }
}
