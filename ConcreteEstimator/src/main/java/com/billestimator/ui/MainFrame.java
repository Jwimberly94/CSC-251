package com.billestimator.ui;

import com.billestimator.model.*;
import com.billestimator.service.*;
import com.billestimator.util.InputValidator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class MainFrame extends JFrame {

    // Stuff this window needs in order to work
    private final CsvPricingService pricingService;
    private final EstimateExportService exportService;

    // Main tabs for the app
    private final JTabbedPane tabbedPane = new JTabbedPane();

    // Tab 1 stuff: project info
    // I keep the basic project details in these fields
    private JTextField tfProjectName, tfClientName, tfLocation, tfDate, tfEstimator;
    private JTextArea taNotes;

    // Tab 2 stuff: concrete pad inputs
    // These are all the concrete form controls
    private JComboBox<PadPreset> cbPadPreset;
    private JTextField tfPadLength, tfPadWidth;
    private JComboBox<String> cbThickness;
    private JTextField tfCustomThickness;
    private JTextField tfWaste, tfEmployees, tfHoursPerEmp, tfLaborRate, tfConcretePrice;
    private JCheckBox chkRebar, chkEquipment;
    private JTextField tfRebarCost, tfEquipmentCost;
    private JTextField tfPadDiscPct, tfPadDiscFixed, tfContingency;

    // Tab 3 stuff: fence inputs
    private JCheckBox chkIncludeFence;
    private JPanel fencePanel;
    private JComboBox<FencePreset> cbFencePreset;
    private JTextField tfFencePerimeter;
    private JComboBox<String> cbFenceHeight;
    private JTextField tfCustomFenceHeight;
    private JComboBox<FenceSpec.MeshGauge> cbMeshGauge;
    private JComboBox<FenceSpec.PostType> cbPostType;
    private JTextField tfPostSpacing;
    private JSpinner spWalkGates, spDoubleGates, spSlidingGates;
    private JCheckBox chkMotor;
    private JComboBox<FenceSpec.TopTreatment> cbTopTreatment;
    private JTextField tfFenceOverage, tfFenceLaborHrs, tfFenceLaborRate;
    private JTextField tfFabricPrice, tfPostPrice, tfWalkGatePrice, tfDoubleGatePrice;
    private JTextField tfSlidingGatePrice, tfMotorPrice, tfTopTreatPrice, tfPostConcretePrice;
    private JTextField tfFenceDiscPct, tfFenceDiscFixed;

    // Tab 4 stuff: results output
    private JTextArea taResults;

    // I pass the pricing and export services into the frame here
    public MainFrame(CsvPricingService pricingService, EstimateExportService exportService) {
        super("Concrete Pad & Chain-Link Fence Estimator");
        this.pricingService = pricingService;
        this.exportService = exportService;
        System.out.println("Creating MainFrame..."); // just here so I can trace startup
        initUI();
    }

    private void initUI() {
        // Close the whole app when this window closes
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Give the window a starting size
        setSize(900, 720);
        // Keep it from getting way too small
        setMinimumSize(new Dimension(800, 600));
        // Start it in the middle of the screen
        setLocationRelativeTo(null);
        System.out.println("Initializing UI components..."); // just here so I can trace startup

        // Put all the main tabs into the tabbed pane
        tabbedPane.addTab("1. Project Info", buildProjectInfoTab());
        tabbedPane.addTab("2. Concrete Pad", buildConcreteTab());
        tabbedPane.addTab("3. Chain-Link Fence", buildFenceTab());
        tabbedPane.addTab("4. Estimate Results", buildResultsTab());

        // Put the tabs in the middle of the window
        add(tabbedPane, BorderLayout.CENTER);

        // This bottom row holds the main buttons
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        // Main calculate button
        JButton btnCalc = new JButton("Calculate Estimate");
        // Save button
        JButton btnSave = new JButton("Save Estimate");
        // Pricing editor button
        JButton btnPricing = new JButton("Edit Pricing...");
        stylePrimaryButton(btnCalc);
        // Hook each button up to what it should do
        btnCalc.addActionListener(e -> runCalculation());
        btnSave.addActionListener(e -> saveEstimate());
        btnPricing.addActionListener(e -> openPricingEditor());
        // Add the buttons to the bottom row
        bottomBar.add(btnPricing);
        bottomBar.add(btnSave);
        bottomBar.add(btnCalc);
        // Put that row at the bottom of the window
        add(bottomBar, BorderLayout.SOUTH);
        System.out.println("UI initialized successfully!"); // just here so I can trace startup
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(new Color(34, 139, 34));
        button.setForeground(Color.WHITE);
        button.setFont(button.getFont().deriveFont(Font.BOLD));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setMargin(new Insets(10, 18, 10, 18));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(24, 97, 24)),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)));
    }

    // ─────────────────────────────────────────────────────────────
    //  Tab 1: project info section
    // ─────────────────────────────────────────────────────────────
    private JScrollPane buildProjectInfoTab() {
        System.out.println("Building Project Info tab..."); // just here so I can trace startup
        // I use GridBagLayout here so the form lines up nicely
        JPanel p = new JPanel(new GridBagLayout());
        // Add some padding so it does not feel cramped
        p.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        // These settings control how the form pieces line up
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        tfProjectName = new JTextField(30);
        tfClientName  = new JTextField(30);
        tfLocation    = new JTextField(30);
        tfDate        = new JTextField(LocalDate.now().toString(), 30);
        tfEstimator   = new JTextField(30);
        taNotes       = new JTextArea(4, 30);
        taNotes.setLineWrap(true);
        taNotes.setWrapStyleWord(true);

        String[] labels = {"Project Name:", "Client Name:", "Location / Address:",
                           "Estimate Date:", "Estimator Name:"};
        JTextField[] fields = {tfProjectName, tfClientName, tfLocation, tfDate, tfEstimator};

        for (int i = 0; i < labels.length; i++) {
            c.gridx = 0; c.gridy = i; c.fill = GridBagConstraints.NONE;
            p.add(new JLabel(labels[i]), c);
            c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
            p.add(fields[i], c);
            c.weightx = 0;
        }
        c.gridx = 0; c.gridy = labels.length; c.fill = GridBagConstraints.NONE;
        p.add(new JLabel("Notes:"), c);
        c.gridx = 1; c.fill = GridBagConstraints.BOTH; c.weighty = 1; c.weightx = 1;
        p.add(new JScrollPane(taNotes), c);

        JScrollPane sp = new JScrollPane(p);
        sp.setBorder(null);
        return sp;
    }

    // ─────────────────────────────────────────────────────────────
    //  Tab 2: concrete pad section
    // ─────────────────────────────────────────────────────────────
    private JScrollPane buildConcreteTab() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        // Quick preset section
        JPanel presetPanel = titledPanel("Pad Configuration");
        List<PadPreset> presets = PresetRegistry.getPadPresets();
        cbPadPreset = new JComboBox<>(presets.toArray(new PadPreset[0]));
        cbPadPreset.setMaximumRowCount(12);
        tfPadLength = new JTextField("0", 8);
        tfPadWidth  = new JTextField("0", 8);

        cbPadPreset.addActionListener(e -> {
            PadPreset sel = (PadPreset) cbPadPreset.getSelectedItem();
            if (sel != null && sel.getLengthFt() > 0) {
                tfPadLength.setText(String.valueOf((int) sel.getLengthFt()));
                tfPadWidth.setText(String.valueOf((int) sel.getWidthFt()));
            }
        });

        addRow(presetPanel, "Preset:", cbPadPreset);
        addRow(presetPanel, "Length (ft):", tfPadLength);
        addRow(presetPanel, "Width (ft):", tfPadWidth);
        root.add(presetPanel);

        // Thickness choices
        JPanel thkPanel = titledPanel("Slab Thickness");
        cbThickness = new JComboBox<>(new String[]{
                "4\" – Residential / light commercial",
                "5\" – Standard commercial",
                "6\" – Heavy-duty / forklift",
                "8\" – Industrial / heavy equipment",
                "Custom"});
        tfCustomThickness = new JTextField("4", 6);
        tfCustomThickness.setEnabled(false);
        cbThickness.addActionListener(e -> {
            boolean custom = cbThickness.getSelectedIndex() == 4;
            tfCustomThickness.setEnabled(custom);
        });
        addRow(thkPanel, "Thickness:", cbThickness);
        addRow(thkPanel, "Custom (inches):", tfCustomThickness);
        root.add(thkPanel);

        // Concrete price inputs
        JPanel pricePanel = titledPanel("Concrete Pricing & Waste");
        tfConcretePrice = new JTextField(String.valueOf(pricingService.get("concrete_price_per_cy", 150.0)), 8);
        tfWaste         = new JTextField(String.valueOf(pricingService.get("concrete_waste_pct", 8.0)), 8);
        addRow(pricePanel, "Price / Cubic Yard ($):", tfConcretePrice);
        addRow(pricePanel, "Waste % (5–15):", tfWaste);
        root.add(pricePanel);

        // Labor inputs
        JPanel laborPanel = titledPanel("Labor");
        tfEmployees   = new JTextField("4", 8);
        tfHoursPerEmp = new JTextField("8", 8);
        tfLaborRate   = new JTextField(String.valueOf(pricingService.get("concrete_labor_rate", 35.0)), 8);
        addRow(laborPanel, "# Employees:", tfEmployees);
        addRow(laborPanel, "Hours / Employee:", tfHoursPerEmp);
        addRow(laborPanel, "Labor Rate ($/hr):", tfLaborRate);
        root.add(laborPanel);

        // Optional extras
        JPanel addonPanel = titledPanel("Optional Add-ons");
        chkRebar      = new JCheckBox("Rebar / Wire Mesh");
        tfRebarCost   = new JTextField(String.valueOf(pricingService.get("rebar_cost_per_sqft", 0.65)), 8);
        tfRebarCost.setEnabled(false);
        chkRebar.addActionListener(e -> tfRebarCost.setEnabled(chkRebar.isSelected()));

        chkEquipment    = new JCheckBox("Equipment Rental");
        tfEquipmentCost = new JTextField("500", 8);
        tfEquipmentCost.setEnabled(false);
        chkEquipment.addActionListener(e -> tfEquipmentCost.setEnabled(chkEquipment.isSelected()));

        addonPanel.add(chkRebar);
        addonPanel.add(new JLabel("Cost / sq ft ($):"));
        addonPanel.add(tfRebarCost);
        addonPanel.add(Box.createHorizontalStrut(20));
        addonPanel.add(chkEquipment);
        addonPanel.add(new JLabel("Total rental ($):"));
        addonPanel.add(tfEquipmentCost);
        root.add(addonPanel);

        // Discount and buffer inputs
        JPanel discPanel = titledPanel("Discounts & Contingency");
        tfPadDiscPct   = new JTextField("0", 6);
        tfPadDiscFixed = new JTextField("0", 8);
        tfContingency  = new JTextField("0", 6);
        addRow(discPanel, "Discount %:", tfPadDiscPct);
        addRow(discPanel, "Fixed Discount ($):", tfPadDiscFixed);
        addRow(discPanel, "Contingency %:", tfContingency);
        root.add(discPanel);

        JScrollPane sp = new JScrollPane(root);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(14);
        return sp;
    }

    // ─────────────────────────────────────────────────────────────
    //  Tab 3: chain-link fence section
    // ─────────────────────────────────────────────────────────────
    private JScrollPane buildFenceTab() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        chkIncludeFence = new JCheckBox("Include Chain-Link Fencing in this estimate");
        chkIncludeFence.setFont(chkIncludeFence.getFont().deriveFont(Font.BOLD));
        chkIncludeFence.addActionListener(e -> fencePanel.setVisible(chkIncludeFence.isSelected()));
        root.add(chkIncludeFence);
        root.add(Box.createVerticalStrut(8));

        fencePanel = new JPanel();
        fencePanel.setLayout(new BoxLayout(fencePanel, BoxLayout.Y_AXIS));
        fencePanel.setVisible(false);

        // Preset choices
        JPanel presetPanel = titledPanel("Fence Configuration");
        List<FencePreset> fPresets = PresetRegistry.getFencePresets();
        cbFencePreset = new JComboBox<>(fPresets.toArray(new FencePreset[0]));
        cbFencePreset.setMaximumRowCount(12);
        tfFencePerimeter = new JTextField("0", 8);
        cbFenceHeight = new JComboBox<>(new String[]{"4", "5", "6", "8", "10", "Custom"});
        tfCustomFenceHeight = new JTextField("6", 6);
        tfCustomFenceHeight.setEnabled(false);

        cbFencePreset.addActionListener(e -> {
            FencePreset sel = (FencePreset) cbFencePreset.getSelectedItem();
            if (sel != null && sel.getPerimeterFt() > 0) {
                tfFencePerimeter.setText(String.valueOf((int) sel.getPerimeterFt()));
                cbFenceHeight.setSelectedItem(String.valueOf(sel.getHeightFt()));
            }
        });
        cbFenceHeight.addActionListener(e -> {
            tfCustomFenceHeight.setEnabled("Custom".equals(cbFenceHeight.getSelectedItem()));
        });

        addRow(presetPanel, "Preset:", cbFencePreset);
        addRow(presetPanel, "Perimeter (LF):", tfFencePerimeter);
        addRow(presetPanel, "Height (ft):", cbFenceHeight);
        addRow(presetPanel, "Custom Height (ft):", tfCustomFenceHeight);
        fencePanel.add(presetPanel);

        // Material settings
        JPanel matPanel = titledPanel("Fence Materials");
        cbMeshGauge = new JComboBox<>(FenceSpec.MeshGauge.values());
        cbMeshGauge.setSelectedItem(FenceSpec.MeshGauge.GAUGE_11);
        cbPostType  = new JComboBox<>(FenceSpec.PostType.values());
        tfPostSpacing = new JTextField("10", 6);
        addRow(matPanel, "Mesh Gauge:", cbMeshGauge);
        addRow(matPanel, "Post Type:", cbPostType);
        addRow(matPanel, "Post Spacing (ft):", tfPostSpacing);
        fencePanel.add(matPanel);

        // Gate options
        JPanel gatePanel = titledPanel("Gate Options");
        spWalkGates   = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        spDoubleGates = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        spSlidingGates= new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        chkMotor      = new JCheckBox("Motor for sliding gate(s)");
        addRow(gatePanel, "Single Walk Gates (3–4 ft):", spWalkGates);
        addRow(gatePanel, "Double Drive Gates (12–16 ft):", spDoubleGates);
        addRow(gatePanel, "Sliding Gates (12–40 ft):", spSlidingGates);
        gatePanel.add(chkMotor);
        fencePanel.add(gatePanel);

        // Top add-on choice
        JPanel topPanel = titledPanel("Top Treatment");
        cbTopTreatment = new JComboBox<>(FenceSpec.TopTreatment.values());
        addRow(topPanel, "Top Treatment:", cbTopTreatment);
        fencePanel.add(topPanel);

        // Unit price inputs
        JPanel fpPanel = titledPanel("Fence Unit Prices");
        tfFabricPrice       = new JTextField(String.valueOf(pricingService.get("fence_fabric_per_lf", 4.50)), 8);
        tfPostPrice         = new JTextField(String.valueOf(pricingService.get("fence_post_unit", 18.00)), 8);
        tfWalkGatePrice     = new JTextField(String.valueOf(pricingService.get("fence_walk_gate", 150.00)), 8);
        tfDoubleGatePrice   = new JTextField(String.valueOf(pricingService.get("fence_double_gate", 450.00)), 8);
        tfSlidingGatePrice  = new JTextField(String.valueOf(pricingService.get("fence_sliding_gate", 800.00)), 8);
        tfMotorPrice        = new JTextField(String.valueOf(pricingService.get("fence_gate_motor", 600.00)), 8);
        tfTopTreatPrice     = new JTextField(String.valueOf(pricingService.get("fence_top_treatment_per_lf", 1.20)), 8);
        tfPostConcretePrice = new JTextField(String.valueOf(pricingService.get("fence_post_concrete_bag", 7.00)), 8);
        addRow(fpPanel, "Fabric Price ($/LF):", tfFabricPrice);
        addRow(fpPanel, "Post Unit Price ($):", tfPostPrice);
        addRow(fpPanel, "Walk Gate Price ($):", tfWalkGatePrice);
        addRow(fpPanel, "Double Gate Price ($):", tfDoubleGatePrice);
        addRow(fpPanel, "Sliding Gate Price ($):", tfSlidingGatePrice);
        addRow(fpPanel, "Gate Motor Price ($):", tfMotorPrice);
        addRow(fpPanel, "Top Treatment ($/LF):", tfTopTreatPrice);
        addRow(fpPanel, "Post Concrete Bag ($):", tfPostConcretePrice);
        fencePanel.add(fpPanel);

        // Labor and extra material allowance
        JPanel flPanel = titledPanel("Fence Labor & Overage");
        tfFenceLaborHrs  = new JTextField("16", 8);
        tfFenceLaborRate = new JTextField(String.valueOf(pricingService.get("fence_labor_rate", 30.0)), 8);
        tfFenceOverage   = new JTextField("5", 6);
        addRow(flPanel, "Total Labor Hours:", tfFenceLaborHrs);
        addRow(flPanel, "Labor Rate ($/hr):", tfFenceLaborRate);
        addRow(flPanel, "Material Overage %:", tfFenceOverage);
        fencePanel.add(flPanel);

        // Fence discount inputs
        JPanel fdPanel = titledPanel("Fence Discounts");
        tfFenceDiscPct   = new JTextField("0", 6);
        tfFenceDiscFixed = new JTextField("0", 8);
        addRow(fdPanel, "Discount %:", tfFenceDiscPct);
        addRow(fdPanel, "Fixed Discount ($):", tfFenceDiscFixed);
        fencePanel.add(fdPanel);

        root.add(fencePanel);
        JScrollPane sp = new JScrollPane(root);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(14);
        return sp;
    }

    // ─────────────────────────────────────────────────────────────
    //  Tab 4: results section
    // ─────────────────────────────────────────────────────────────
    private JScrollPane buildResultsTab() {
        taResults = new JTextArea();
        taResults.setEditable(false);
        taResults.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        taResults.setText("Press 'Calculate Estimate' to generate a report.");
        JScrollPane sp = new JScrollPane(taResults);
        return sp;
    }

    // ─────────────────────────────────────────────────────────────
    //  Main calculate logic
    // ─────────────────────────────────────────────────────────────
    private void runCalculation() {
        System.out.println("Starting calculation..."); // just here so I can see the flow
        // Put the project info from the form into one object
        ProjectInfo info = new ProjectInfo();
        // Copy the form values into that object
        info.setProjectName(tfProjectName.getText().trim());
        info.setClientName(tfClientName.getText().trim());
        info.setLocation(tfLocation.getText().trim());
        info.setEstimateDate(tfDate.getText().trim());
        info.setEstimatorName(tfEstimator.getText().trim());
        info.setNotes(taNotes.getText().trim());

        // I need a project name before making the estimate
        if (info.getProjectName().isEmpty()) {
            // Let the user know what is missing
            JOptionPane.showMessageDialog(this, "Please enter a project name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            // Send them back to the first tab
            tabbedPane.setSelectedIndex(0);
            return;
        }

        // Build the concrete settings object
        ConcreteSpec cSpec = new ConcreteSpec();

        double len = InputValidator.parsePositiveDouble(tfPadLength.getText(), "Pad Length", this);
        double wid = InputValidator.parsePositiveDouble(tfPadWidth.getText(), "Pad Width", this);
        if (!InputValidator.isValid(len) || !InputValidator.isValid(wid)) { tabbedPane.setSelectedIndex(1); return; }
        cSpec.setLengthFt(len);
        cSpec.setWidthFt(wid);

        double thk;
        int thkIdx = cbThickness.getSelectedIndex();
        if (thkIdx == 0) thk = 4;
        else if (thkIdx == 1) thk = 5;
        else if (thkIdx == 2) thk = 6;
        else if (thkIdx == 3) thk = 8;
        else {
            thk = InputValidator.parsePositiveDouble(tfCustomThickness.getText(), "Custom Thickness", this);
            if (!InputValidator.isValid(thk)) { tabbedPane.setSelectedIndex(1); return; }
        }
        cSpec.setThicknessInches(thk);

        double waste = InputValidator.parsePositiveDouble(tfWaste.getText(), "Waste %", this);
        if (!InputValidator.isValid(waste)) { tabbedPane.setSelectedIndex(1); return; }
        cSpec.setWastePercent(waste);

        double concretePrice = InputValidator.parsePositiveDouble(tfConcretePrice.getText(), "Concrete Price/CY", this);
        if (!InputValidator.isValid(concretePrice)) { tabbedPane.setSelectedIndex(1); return; }
        cSpec.setPricePerCubicYard(concretePrice);

        int emp = InputValidator.parsePositiveInt(tfEmployees.getText(), "Employees", this);
        if (emp < 0) { tabbedPane.setSelectedIndex(1); return; }
        cSpec.setEmployees(emp);

        double hrs = InputValidator.parsePositiveDouble(tfHoursPerEmp.getText(), "Hours/Employee", this);
        if (!InputValidator.isValid(hrs)) { tabbedPane.setSelectedIndex(1); return; }
        cSpec.setHoursPerEmployee(hrs);

        double laborRate = InputValidator.parsePositiveDouble(tfLaborRate.getText(), "Labor Rate", this);
        if (!InputValidator.isValid(laborRate)) { tabbedPane.setSelectedIndex(1); return; }
        cSpec.setLaborRatePerHour(laborRate);

        cSpec.setIncludeRebar(chkRebar.isSelected());
        if (chkRebar.isSelected()) {
            double rebarCost = InputValidator.parsePositiveDouble(tfRebarCost.getText(), "Rebar Cost/sqft", this);
            if (!InputValidator.isValid(rebarCost)) { tabbedPane.setSelectedIndex(1); return; }
            cSpec.setRebarCostPerSqFt(rebarCost);
        }

        cSpec.setIncludeEquipmentRental(chkEquipment.isSelected());
        if (chkEquipment.isSelected()) {
            double eqCost = InputValidator.parsePositiveDouble(tfEquipmentCost.getText(), "Equipment Rental", this);
            if (!InputValidator.isValid(eqCost)) { tabbedPane.setSelectedIndex(1); return; }
            cSpec.setEquipmentRentalCost(eqCost);
        }

        double padDiscPct = InputValidator.parsePositiveDouble(tfPadDiscPct.getText(), "Pad Discount %", this);
        double padDiscFix = InputValidator.parsePositiveDouble(tfPadDiscFixed.getText(), "Pad Fixed Discount", this);
        double contingency = InputValidator.parsePositiveDouble(tfContingency.getText(), "Contingency %", this);
        if (!InputValidator.isValid(padDiscPct) || !InputValidator.isValid(padDiscFix) || !InputValidator.isValid(contingency)) {
            tabbedPane.setSelectedIndex(1); return;
        }
        cSpec.setDiscountPercent(padDiscPct);
        cSpec.setDiscountFixed(padDiscFix);
        cSpec.setContingencyPercent(contingency);

        ConcreteResult cRes = ConcreteCalculator.calculate(cSpec);

        // Only build the fence part if that option is checked
        boolean fenceIncluded = chkIncludeFence.isSelected();
        FenceSpec fSpec = null;
        FenceResult fRes = null;

        if (fenceIncluded) {
            fSpec = new FenceSpec();

            double peri = InputValidator.parsePositiveDouble(tfFencePerimeter.getText(), "Fence Perimeter", this);
            if (!InputValidator.isValid(peri)) { tabbedPane.setSelectedIndex(2); return; }
            fSpec.setPerimeterFt(peri);

            String hStr = (String) cbFenceHeight.getSelectedItem();
            if ("Custom".equals(hStr)) {
                double ch = InputValidator.parsePositiveDouble(tfCustomFenceHeight.getText(), "Custom Fence Height", this);
                if (!InputValidator.isValid(ch)) { tabbedPane.setSelectedIndex(2); return; }
                fSpec.setUseCustomHeight(true);
                fSpec.setCustomHeightFt(ch);
            } else {
                fSpec.setHeightFt(Integer.parseInt(hStr));
            }

            fSpec.setMeshGauge((FenceSpec.MeshGauge) cbMeshGauge.getSelectedItem());
            fSpec.setPostType((FenceSpec.PostType) cbPostType.getSelectedItem());

            double postSpacing = InputValidator.parsePositiveDouble(tfPostSpacing.getText(), "Post Spacing", this);
            if (!InputValidator.isValid(postSpacing)) { tabbedPane.setSelectedIndex(2); return; }
            fSpec.setPostSpacingFt(postSpacing);

            fSpec.setSingleWalkGates((Integer) spWalkGates.getValue());
            fSpec.setDoubleGates((Integer) spDoubleGates.getValue());
            fSpec.setSlidingGates((Integer) spSlidingGates.getValue());
            fSpec.setSlidingGateMotor(chkMotor.isSelected());
            fSpec.setTopTreatment((FenceSpec.TopTreatment) cbTopTreatment.getSelectedItem());

            double fabP = InputValidator.parsePositiveDouble(tfFabricPrice.getText(), "Fabric Price/LF", this);
            double posP = InputValidator.parsePositiveDouble(tfPostPrice.getText(), "Post Price", this);
            double wgP  = InputValidator.parsePositiveDouble(tfWalkGatePrice.getText(), "Walk Gate Price", this);
            double dgP  = InputValidator.parsePositiveDouble(tfDoubleGatePrice.getText(), "Double Gate Price", this);
            double sgP  = InputValidator.parsePositiveDouble(tfSlidingGatePrice.getText(), "Sliding Gate Price", this);
            double mgP  = InputValidator.parsePositiveDouble(tfMotorPrice.getText(), "Motor Price", this);
            double ttP  = InputValidator.parsePositiveDouble(tfTopTreatPrice.getText(), "Top Treatment Price/LF", this);
            double pcP  = InputValidator.parsePositiveDouble(tfPostConcretePrice.getText(), "Post Concrete Bag Price", this);
            if (!InputValidator.isValid(fabP) || !InputValidator.isValid(posP)
                    || !InputValidator.isValid(wgP) || !InputValidator.isValid(dgP)
                    || !InputValidator.isValid(sgP) || !InputValidator.isValid(mgP)
                    || !InputValidator.isValid(ttP) || !InputValidator.isValid(pcP)) {
                tabbedPane.setSelectedIndex(2); return;
            }
            fSpec.setFabricPricePerLf(fabP);
            fSpec.setPostUnitPrice(posP);
            fSpec.setWalkGatePrice(wgP);
            fSpec.setDoubleGatePrice(dgP);
            fSpec.setSlidingGatePrice(sgP);
            fSpec.setSlidingGateMotorPrice(mgP);
            fSpec.setTopTreatmentPricePerLf(ttP);
            fSpec.setPostConcreteBagPrice(pcP);

            double flh = InputValidator.parsePositiveDouble(tfFenceLaborHrs.getText(), "Fence Labor Hours", this);
            double flr = InputValidator.parsePositiveDouble(tfFenceLaborRate.getText(), "Fence Labor Rate", this);
            double fov = InputValidator.parsePositiveDouble(tfFenceOverage.getText(), "Fence Overage %", this);
            double fdp = InputValidator.parsePositiveDouble(tfFenceDiscPct.getText(), "Fence Discount %", this);
            double fdf = InputValidator.parsePositiveDouble(tfFenceDiscFixed.getText(), "Fence Fixed Discount", this);
            if (!InputValidator.isValid(flh) || !InputValidator.isValid(flr)
                    || !InputValidator.isValid(fov) || !InputValidator.isValid(fdp) || !InputValidator.isValid(fdf)) {
                tabbedPane.setSelectedIndex(2); return;
            }
            fSpec.setFenceLaborHours(flh);
            fSpec.setFenceLaborRate(flr);
            fSpec.setMaterialOveragePercent(fov);
            fSpec.setDiscountPercent(fdp);
            fSpec.setDiscountFixed(fdf);

            fRes = FenceCalculator.calculate(fSpec);
        }

        String report = exportService.buildReport(info, cSpec, cRes, fenceIncluded, fSpec, fRes);
        taResults.setText(report);
        taResults.setCaretPosition(0);
        tabbedPane.setSelectedIndex(3);
    }

    private void saveEstimate() {
        System.out.println("Saving estimate..."); // just here so I can see the flow
        // Grab the generated report text
        String text = taResults.getText();
        // If nothing real was calculated yet, stop here
        if (text == null || text.startsWith("Press ")) {
            JOptionPane.showMessageDialog(this, "Please calculate an estimate first.", "No Estimate", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Use the project name for the saved file name
        String projectName = tfProjectName.getText().trim();
        if (projectName.isEmpty()) projectName = "Estimate";
        // Save it with the export service
        exportService.saveEstimate(text, projectName);
        System.out.println("Estimate saved!"); // just here so I can see the flow
        // Show a quick success message
        JOptionPane.showMessageDialog(this, "Estimate saved to the estimates folder.", "Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openPricingEditor() {
        System.out.println("Opening pricing editor..."); // just here so I can see the flow
        // Open the pricing editor window
        new PricingEditorDialog(this, pricingService).setVisible(true);
        // Refresh the fields after editing so the new prices show up
        tfConcretePrice.setText(String.valueOf(pricingService.get("concrete_price_per_cy", 150.0)));
        tfLaborRate.setText(String.valueOf(pricingService.get("concrete_labor_rate", 35.0)));
        tfRebarCost.setText(String.valueOf(pricingService.get("rebar_cost_per_sqft", 0.65)));
        tfFabricPrice.setText(String.valueOf(pricingService.get("fence_fabric_per_lf", 4.50)));
        tfPostPrice.setText(String.valueOf(pricingService.get("fence_post_unit", 18.00)));
        tfWalkGatePrice.setText(String.valueOf(pricingService.get("fence_walk_gate", 150.00)));
        tfDoubleGatePrice.setText(String.valueOf(pricingService.get("fence_double_gate", 450.00)));
        tfSlidingGatePrice.setText(String.valueOf(pricingService.get("fence_sliding_gate", 800.00)));
        tfMotorPrice.setText(String.valueOf(pricingService.get("fence_gate_motor", 600.00)));
        tfTopTreatPrice.setText(String.valueOf(pricingService.get("fence_top_treatment_per_lf", 1.20)));
        tfPostConcretePrice.setText(String.valueOf(pricingService.get("fence_post_concrete_bag", 7.00)));
        tfFenceLaborRate.setText(String.valueOf(pricingService.get("fence_labor_rate", 30.0)));
    }

    // ─────────────────────────────────────────────────────────────
    //  Small helper methods
    // ─────────────────────────────────────────────────────────────
    private JPanel titledPanel(String title) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        p.setBorder(new TitledBorder(title));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }

    private void addRow(JPanel panel, String label, JComponent field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }
}
