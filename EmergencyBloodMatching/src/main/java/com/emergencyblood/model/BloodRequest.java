package com.emergencyblood.model;

import javafx.beans.property.*;

import java.sql.Timestamp;

/**
 * Model class representing an Emergency Blood Request in the system.
 */
public class BloodRequest {
    private final IntegerProperty id;
    private final StringProperty requiredBloodGroup;
    private final IntegerProperty unitsRequired;
    private final StringProperty location;
    private final StringProperty urgency;
    private final StringProperty status;
    private final ObjectProperty<Timestamp> requestDate;

    public BloodRequest(int id, String requiredBloodGroup, int unitsRequired, String location, String urgency) {
        this(id, requiredBloodGroup, unitsRequired, location, urgency, "Pending", new Timestamp(System.currentTimeMillis()));
    }

    public BloodRequest(int id, String requiredBloodGroup, int unitsRequired, String location, String urgency, String status, Timestamp requestDate) {
        this.id = new SimpleIntegerProperty(id);
        this.requiredBloodGroup = new SimpleStringProperty(requiredBloodGroup);
        this.unitsRequired = new SimpleIntegerProperty(unitsRequired);
        this.location = new SimpleStringProperty(location);
        this.urgency = new SimpleStringProperty(urgency);
        this.status = new SimpleStringProperty(status != null ? status : "Pending");
        this.requestDate = new SimpleObjectProperty<>(requestDate != null ? requestDate : new Timestamp(System.currentTimeMillis()));
    }

    // ID
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    // Required Blood Group
    public String getRequiredBloodGroup() { return requiredBloodGroup.get(); }
    public void setRequiredBloodGroup(String group) { this.requiredBloodGroup.set(group); }
    public StringProperty requiredBloodGroupProperty() { return requiredBloodGroup; }

    // Units Required
    public int getUnitsRequired() { return unitsRequired.get(); }
    public void setUnitsRequired(int units) { this.unitsRequired.set(units); }
    public IntegerProperty unitsRequiredProperty() { return unitsRequired; }

    // Location
    public String getLocation() { return location.get(); }
    public void setLocation(String location) { this.location.set(location); }
    public StringProperty locationProperty() { return location; }

    // Urgency
    public String getUrgency() { return urgency.get(); }
    public void setUrgency(String urgency) { this.urgency.set(urgency); }
    public StringProperty urgencyProperty() { return urgency; }

    // Status
    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
    public StringProperty statusProperty() { return status; }

    // Request Date
    public Timestamp getRequestDate() { return requestDate.get(); }
    public void setRequestDate(Timestamp requestDate) { this.requestDate.set(requestDate); }
    public ObjectProperty<Timestamp> requestDateProperty() { return requestDate; }
}
