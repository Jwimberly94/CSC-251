package com.billestimator;

import com.billestimator.service.CsvPricingService;
import com.billestimator.service.EstimateExportService;
import com.billestimator.ui.MainFrame;

import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.*;

public class App {

    public static void main(String[] args) {
        // Grab the user's home folder first
        String userHome = System.getProperty("user.home");
        // I keep the app data in a folder inside home
        String dataDir  = userHome + File.separator + "BillEstimator";
        // This is where the pricing CSV will live
        String pricingFile = dataDir + File.separator + "pricing.csv";
        // This folder is where saved estimates go
        String estimatesDir = dataDir + File.separator + "estimates";

        System.out.println("Data directory: " + dataDir);
        
        // If the pricing file is missing, make a starter one
        File pf = new File(pricingFile);
        if (!pf.exists()) {
            System.out.println("Pricing file does not exist, creating...");
            // Make the data folder if it is not there yet
            new File(dataDir).mkdirs();
            // Copy over the default pricing file from resources
            try (InputStream is = App.class.getResourceAsStream("/pricing.csv")) {
                if (is != null) {
                    System.out.println("Copying default pricing.csv to " + pricingFile);
                    Files.copy(is, Paths.get(pricingFile));
                }
            } catch (IOException e) {
                System.err.println("Could not copy default pricing file: " + e.getMessage());
                e.printStackTrace(); // leaving this so I can see the full error
            }
        }

        // Set up the pricing service
        CsvPricingService pricingService = new CsvPricingService(pricingFile);
        // Set up the export service
        EstimateExportService exportService = new EstimateExportService(estimatesDir);
        System.out.println("Services initialized successfully");

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            MainFrame frame = new MainFrame(pricingService, exportService);
            frame.setVisible(true);
        });
    }
}
