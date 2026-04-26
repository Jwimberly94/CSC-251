package com.billestimator.service;

import java.io.*;
import java.util.*;

/**
 * This class reads and writes a simple key,value CSV for prices and labor rates.
 * The file is one entry per line.
 * If a line starts with #, I just ignore it.
 */
public class CsvPricingService {

    // This is the path to the pricing CSV file
    private final String filePath;
    // I keep the pricing values in this map
    private final Map<String, Double> prices = new LinkedHashMap<>();

    // When this starts up, it saves the path and loads the data
    public CsvPricingService(String filePath) {
        this.filePath = filePath;
        System.out.println("Loading pricing service from: " + filePath); // just here so I can check the file path
        load();
    }

    // This reads all the pricing values from the CSV file
    public void load() {
        // Clear old values first
        prices.clear();
        System.out.println("Loading prices from file..."); // just here so I can see when reload happens
        // Make a File object for the CSV path
        File f = new File(filePath);
        // If the file is missing, there is nothing to load
        if (!f.exists()) {
            System.out.println("File does not exist: " + filePath);
            return;
        }
        // Read the file line by line
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            // Go through each line in the file
            while ((line = br.readLine()) != null) {
                // Clean up extra spaces
                line = line.trim();
                // Skip blank lines and comment lines
                if (line.isEmpty() || line.startsWith("#")) continue;
                // Break the line into key and value
                String[] parts = line.split(",", 2);
                // Only use the line if it has both pieces
                if (parts.length == 2) {
                    try {
                        // Turn the value into a number and save it
                        prices.put(parts[0].trim(), Double.parseDouble(parts[1].trim()));
                    } catch (NumberFormatException ignored) {
                        // If a value is bad, I just skip that line
                    }
                }
            }
            System.out.println("Loaded " + prices.size() + " pricing entries"); // just here so I can see how much loaded
        } catch (IOException e) {
            System.err.println("Warning: could not read pricing file: " + filePath);
            e.printStackTrace(); // leaving this so I can see the full error
        }
    }

    public void save() {
        File f = new File(filePath);
        f.getParentFile().mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            for (Map.Entry<String, Double> e : prices.entrySet()) {
                pw.println(e.getKey() + "," + e.getValue());
            }
        } catch (IOException ex) {
            System.err.println("Error saving pricing file: " + filePath);
        }
    }

    public double get(String key, double defaultVal) {
        return prices.getOrDefault(key, defaultVal);
    }

    public void set(String key, double value) {
        prices.put(key, value);
    }

    public Map<String, Double> getAll() {
        return Collections.unmodifiableMap(prices);
    }
}
