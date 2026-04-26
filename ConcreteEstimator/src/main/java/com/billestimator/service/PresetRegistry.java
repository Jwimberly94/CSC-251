package com.billestimator.service;

import com.billestimator.model.PadPreset;
import com.billestimator.model.FencePreset;

import java.util.ArrayList;
import java.util.List;

public class PresetRegistry {

    public static List<PadPreset> getPadPresets() {
        List<PadPreset> list = new ArrayList<>();
        list.add(new PadPreset("Equipment Pad (Generator/HVAC)", 10, 10, "Generator / HVAC unit"));
        list.add(new PadPreset("Small Residential Pad", 20, 20, "AC unit / shed base"));
        list.add(new PadPreset("Dumpster Pad", 12, 20, "Commercial waste enclosure"));
        list.add(new PadPreset("Garage / Workshop Pad", 20, 40, "2-car garage"));
        list.add(new PadPreset("RV / Boat Storage Slab", 14, 40, "RV / boat storage"));
        list.add(new PadPreset("Small Warehouse Slab", 50, 100, "Small warehouse / storage"));
        list.add(new PadPreset("Basketball / Sport Court", 50, 84, "Basketball / sport court"));
        list.add(new PadPreset("Commercial Loading Apron", 60, 80, "Truck / loading dock apron"));
        list.add(new PadPreset("Mid Warehouse Slab", 100, 150, "Mid-size warehouse"));
        list.add(new PadPreset("Parking Lot Section", 100, 200, "Commercial parking"));
        list.add(new PadPreset("Large Warehouse Slab", 150, 200, "Large warehouse / distribution"));
        list.add(new PadPreset("Custom (User Input)", 0, 0, "Any custom size"));
        return list;
    }

    public static List<FencePreset> getFencePresets() {
        List<FencePreset> list = new ArrayList<>();
        list.add(new FencePreset("Small Yard Enclosure", 20, 40, 120, 4, "Residential / pet"));
        list.add(new FencePreset("Dumpster / Waste Enclosure", 12, 20, 64, 6, "Commercial waste enclosure"));
        list.add(new FencePreset("Garage / Shop Perimeter", 20, 40, 120, 6, "Workshop security"));
        list.add(new FencePreset("Small Warehouse Perimeter", 50, 100, 300, 6, "Small facility"));
        list.add(new FencePreset("Equipment Yard", 100, 100, 400, 8, "Equipment yard w/ barbed wire top"));
        list.add(new FencePreset("Mid Warehouse Perimeter", 100, 150, 500, 8, "Commercial facility"));
        list.add(new FencePreset("Parking Lot Perimeter", 100, 200, 600, 4, "Commercial parking lot"));
        list.add(new FencePreset("Large Facility Perimeter", 150, 200, 700, 8, "Large warehouse"));
        list.add(new FencePreset("Security Compound", 200, 300, 1000, 10, "High-security / industrial"));
        list.add(new FencePreset("Custom (User Input)", 0, 0, 0, 6, "Any configuration"));
        return list;
    }
}
