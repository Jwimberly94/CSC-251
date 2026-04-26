package com.billestimator.util;

import javax.swing.*;
import java.awt.Component;

public class InputValidator {

    /** 
     * This tries to turn text into a positive double.
     * If the value is bad, it shows a message and gives back NaN.
     */
    public static double parsePositiveDouble(String text, String fieldName, Component parent) {
        // Stop right away if the field is blank
        if (text == null || text.trim().isEmpty()) {
            // Show the validation message
            JOptionPane.showMessageDialog(parent,
                    fieldName + " cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            // NaN means the input was not usable
            return Double.NaN;
        }
        // Try turning the text into a number
        try {
            // Parse the text into a double
            double val = Double.parseDouble(text.trim());
            System.out.println("Parsed double: " + val); // just here so I can see what was entered
            // Negative values are not allowed here
            if (val < 0) {
                // Show the validation message
                JOptionPane.showMessageDialog(parent,
                        fieldName + " must be a non-negative number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                // NaN means the input was not usable
                return Double.NaN;
            }
            // If it passed everything, send the value back
            return val;
        } catch (NumberFormatException e) {
            // Show the validation message
            JOptionPane.showMessageDialog(parent,
                    fieldName + " must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            // NaN means the input was not usable
            return Double.NaN;
        }
    }

    /** 
     * This tries to turn text into a positive int.
     * If the value is bad, it shows a message and gives back -1.
     */
    public static int parsePositiveInt(String text, String fieldName, Component parent) {
        // Stop right away if the field is blank
        if (text == null || text.trim().isEmpty()) {
            // Show the validation message
            JOptionPane.showMessageDialog(parent,
                    fieldName + " cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            // -1 means the input was not usable
            return -1;
        }
        // Try turning the text into an int
        try {
            // Parse the text into an int
            int val = Integer.parseInt(text.trim());
            System.out.println("Parsed int: " + val); // just here so I can see what was entered
            // Negative values are not allowed here
            if (val < 0) {
                // Show the validation message
                JOptionPane.showMessageDialog(parent,
                        fieldName + " must be a non-negative integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                // -1 means the input was not usable
                return -1;
            }
            // If it passed everything, send the value back
            return val;
        } catch (NumberFormatException e) {
            // Show the validation message
            JOptionPane.showMessageDialog(parent,
                    fieldName + " must be a valid integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            // -1 means the input was not usable
            return -1;
        }
    }

    // Quick check so I know the value is not NaN
    public static boolean isValid(double val) {
        return !Double.isNaN(val);
    }
}
