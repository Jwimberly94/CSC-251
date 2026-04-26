package com.billestimator.model;

public class ProjectInfo {
    private String projectName;
    private String clientName;
    private String location;
    private String estimateDate;
    private String estimatorName;
    private String notes;

    public ProjectInfo() {}

    public String getProjectName() { return projectName; }
    public void setProjectName(String v) { this.projectName = v; }

    public String getClientName() { return clientName; }
    public void setClientName(String v) { this.clientName = v; }

    public String getLocation() { return location; }
    public void setLocation(String v) { this.location = v; }

    public String getEstimateDate() { return estimateDate; }
    public void setEstimateDate(String v) { this.estimateDate = v; }

    public String getEstimatorName() { return estimatorName; }
    public void setEstimatorName(String v) { this.estimatorName = v; }

    public String getNotes() { return notes; }
    public void setNotes(String v) { this.notes = v; }
}
