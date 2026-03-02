# AppGUI - Vet Clinic Management UI

## Overview
AppGUI is a Swing-based GUI that uses the VetClinicCore classes for all logic. It schedules appointments, adds services, adds veterinarians, and shows a quick summary.

## Files Used
- AppGUI.java
- VetClinicManager.java
- Appointment.java
- Service.java
- Veterinarian.java

## Run the GUI
From the VetClinicCore_Candanoza/VetClinicCore_Candanoza folder:

```bash
javac *.java
java AppGUI
```

## How It Works
- On startup, AppGUI loads data from the CSV files.
- When you add or schedule items, AppGUI saves the updated data back to CSV.

## Data Files
AppGUI reads and writes the following CSV files in the same folder:
- appointments.csv
- services.csv
- veterinarians.csv

## Notes
- Appointment time format: yyyy-MM-dd HH:mm
- Phone number format: 10 digits (formatting is flexible)
