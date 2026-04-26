package com.billestimator.ui;

import com.billestimator.service.CsvPricingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

/**
 * Small dialog I use to look at and edit the pricing values from the CSV.
 */
public class PricingEditorDialog extends JDialog {

    private final CsvPricingService service;
    private DefaultTableModel tableModel;

    public PricingEditorDialog(Frame owner, CsvPricingService service) {
        super(owner, "Edit Pricing & Labor Rates", true);
        this.service = service;
        initUI();
    }

    private void initUI() {
        setSize(500, 420);
        setLocationRelativeTo(getOwner());

        tableModel = new DefaultTableModel(new String[]{"Key", "Value"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 1; }
        };

        for (Map.Entry<String, Double> e : service.getAll().entrySet()) {
            tableModel.addRow(new Object[]{e.getKey(), String.valueOf(e.getValue())});
        }

        JTable table = new JTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(280);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);

        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save Changes");
        JButton btnCancel = new JButton("Cancel");
        btnSave.addActionListener(e -> save(table));
        btnCancel.addActionListener(e -> dispose());
        buttons.add(btnCancel);
        buttons.add(btnSave);
        add(buttons, BorderLayout.SOUTH);

        JLabel hint = new JLabel("  Double-click a Value cell to edit it.");
        hint.setFont(hint.getFont().deriveFont(Font.ITALIC, 11f));
        add(hint, BorderLayout.NORTH);
    }

    private void save(JTable table) {
        // Finish any cell edit before I read the values
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String key = tableModel.getValueAt(i, 0).toString();
            String valStr = tableModel.getValueAt(i, 1).toString().trim();
            try {
                double val = Double.parseDouble(valStr);
                service.set(key, val);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid number for key '" + key + "': " + valStr,
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        service.save();
        JOptionPane.showMessageDialog(this, "Pricing saved.", "Saved", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
