/*
 * Source Note:
 * Taken from Mariah_Mitchell/FarmStoreInvent.java.
 * This inventory module is unique to Mariah's codebase and included in the polished merge.
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class FarmStoreInvent extends JFrame {

    private static final String INVENTORY_FILE = 
        System.getProperty("user.dir") + "/inventory.csv";


    public static class Item {
        // Source Block: From Mariah_Mitchell
        private String itemID;
        private String itemName;
        private double price;
        private int quantity;

        // Source Block: From Mariah_Mitchell
        public Item(String itemID, String itemName, double price, int quantity) {
            this.itemID = itemID;
            this.itemName = itemName;
            this.price = price;
            this.quantity = quantity;
        }

        // Source Block: From Mariah_Mitchell
        public String getItemID()   { return itemID; }
        public String getItemName() { return itemName; }
        public double getPrice()    { return price; }
        public int getQuantity()    { return quantity; }

        public void setItemName(String itemName) { this.itemName = itemName; }
        public void setPrice(double price)       { this.price = price; }
        public void setQuantity(int quantity)    { this.quantity = quantity; }

        /**
         * Converts this item to a CSV-formatted string
         * Source Block: From Mariah_Mitchell
         */
        public String toCSV() {
            return String.format("%s,%s,%.2f,%d", itemID, itemName, price, quantity);
        }

        /**
         * Creates an Item from a CSV line; throws IllegalArgumentException if the format is invalid
         * Source Block: From Mariah_Mitchell
         */
        public static Item fromCSV(String csvLine) {
            String[] parts = csvLine.split(",");

            if (parts.length != 4) {
                throw new IllegalArgumentException("Invalid CSV format: " + csvLine);
            }

            return new Item(
                parts[0].trim(),
                parts[1].trim(),
                Double.parseDouble(parts[2].trim()),
                Integer.parseInt(parts[3].trim())
            );
        }

        @Override
        // Source Block: From Mariah_Mitchell
        public String toString() {
            return String.format("ID: %s | Name: %s | Price: $%.2f | Qty: %d",
                itemID, itemName, price, quantity);
        }
    }

    private JTextField txtItemID, txtItemName, txtPrice, txtQuantity;
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<Item> itemList = new ArrayList<>();

    // Source Block: From Mariah_Mitchell
    public FarmStoreInvent() {
        System.out.println("Inventory file path: " + INVENTORY_FILE);
        setTitle("Farm Store Inventory System");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createMenu();
        createTable();
        loadFromCSV();
    }

    // Source Block: From Mariah_Mitchell
    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu(" View Menu");

        JMenuItem addItem = new JMenuItem("Add Item");
        JMenuItem saveInventory = new JMenuItem("Save to CSV");
        JMenuItem exit = new JMenuItem("Exit");

        addItem.addActionListener(e -> showAddDialog());
        saveInventory.addActionListener(e -> {
            saveToCSV();
            JOptionPane.showMessageDialog(this, "Inventory saved to CSV successfully!");
        });
        exit.addActionListener(e -> System.exit(0));

        menu.add(addItem);
        menu.add(saveInventory);
        menu.add(exit);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    // Source Block: From Mariah_Mitchell
    private void createTable() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Item ID");
        tableModel.addColumn("Item Name");
        tableModel.addColumn("Price");
        tableModel.addColumn("Quantity");

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // Source Block: From Mariah_Mitchell
    private void showAddDialog() {
        JDialog dialog = new JDialog(this, "Add Item", true);
        dialog.setSize(350, 300);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));

        dialog.add(new JLabel("Item ID:"));
        txtItemID = new JTextField();
        dialog.add(txtItemID);

        dialog.add(new JLabel("Item Name:"));
        txtItemName = new JTextField();
        dialog.add(txtItemName);

        dialog.add(new JLabel("Price:"));
        txtPrice = new JTextField();
        dialog.add(txtPrice);

        dialog.add(new JLabel("Quantity:"));
        txtQuantity = new JTextField();
        dialog.add(txtQuantity);

        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        dialog.add(btnSave);
        dialog.add(btnCancel);

        btnSave.addActionListener(e -> {
            if (addItem()) {
                JOptionPane.showMessageDialog(dialog, "Item Added Successfully!");
                clearFields();
                dialog.dispose();
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Source Block: From Mariah_Mitchell
    private boolean addItem() {
        try {
            Item item = new Item(
                    txtItemID.getText().trim(),
                    txtItemName.getText().trim(),
                    Double.parseDouble(txtPrice.getText().trim()),
                    Integer.parseInt(txtQuantity.getText().trim())
            );
            itemList.add(item);
            tableModel.addRow(new Object[]{
                    item.getItemID(),
                    item.getItemName(),
                    item.getPrice(),
                    item.getQuantity()
            });
            saveToCSV();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
            return false;
        }
    }

    // Source Block: From Mariah_Mitchell
    private void clearFields() {
        txtItemID.setText("");
        txtItemName.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
    }

    /**
     * Writes all inventory items to the CSV file
        * Source Block: From Mariah_Mitchell
     */
    private void saveToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(INVENTORY_FILE))) {
            writer.println("ItemID,ItemName,Price,Quantity");
            for (Item item : itemList) {
                writer.println(item.toCSV());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads inventory items from the CSV file into the table
        * Source Block: From Mariah_Mitchell
     */
    private void loadFromCSV() {
        File file = new File(INVENTORY_FILE);
        if (!file.exists()) return;

        itemList.clear();
        tableModel.setRowCount(0);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // skip header row
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Item item = Item.fromCSV(line);
                    itemList.add(item);
                    tableModel.addRow(new Object[]{
                            item.getItemID(),
                            item.getItemName(),
                            item.getPrice(),
                            item.getQuantity()
                    });
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid row: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Source Block: From Mariah_Mitchell
    public void printInventoryToConsole() {
        if (itemList.isEmpty()) {
            System.out.println("  No inventory items found.");
        } else {
            for (Item item : itemList) {
                System.out.println("  - " + item);
            }
        }
    }
}
