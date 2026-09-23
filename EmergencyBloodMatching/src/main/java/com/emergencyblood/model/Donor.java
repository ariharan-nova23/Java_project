package com.emergencyblood.model;

import javafx.beans.property.*;

/**
 * Model class representing a Blood Donor in the Emergency Blood Matching System.
 * Uses JavaFX properties to enable seamless TableView bindings.
 */
public class Donor {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty bloodGroup;
    private final IntegerProperty age;
    private final StringProperty phoneNumber;
    private final StringProperty location;
    private final StringProperty availability;

    public Donor(int id, String name, String bloodGroup, int age, String phoneNumber, String location, String availability) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.bloodGroup = new SimpleStringProperty(bloodGroup);
        this.age = new SimpleIntegerProperty(age);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.location = new SimpleStringProperty(location);
        this.availability = new SimpleStringProperty(availability);
    }

    // ID
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    // Name
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    // Blood Group
    public String getBloodGroup() { return bloodGroup.get(); }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup.set(bloodGroup); }
    public StringProperty bloodGroupProperty() { return bloodGroup; }

    // Age
    public int getAge() { return age.get(); }
    public void setAge(int age) { this.age.set(age); }
    public IntegerProperty ageProperty() { return age; }

    // Phone Number
    public String getPhoneNumber() { return phoneNumber.get(); }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber.set(phoneNumber); }
    public StringProperty phoneNumberProperty() { return phoneNumber; }

    // Location
    public String getLocation() { return location.get(); }
    public void setLocation(String location) { this.location.set(location); }
    public StringProperty locationProperty() { return location; }

    // Availability
    public String getAvailability() { return availability.get(); }
    public void setAvailability(String availability) { this.availability.set(availability); }
    public StringProperty availabilityProperty() { return availability; }
}
