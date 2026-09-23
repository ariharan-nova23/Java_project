/**
 * Represents a blood donor in the Emergency Blood Matching System.
 * Encapsulates all donor attributes using private fields and public getters/setters.
 */
public class Donor {
    private int donorId;
    private String name;
    private int age;
    private String bloodGroup;
    private String location;
    private boolean available;

    // Default Constructor
    public Donor() {
    }

    // Parameterized Constructor
    public Donor(int donorId, String name, int age, String bloodGroup, String location, boolean available) {
        this.donorId = donorId;
        this.name = name;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.location = location;
        this.available = available;
    }

    // Getters and Setters
    public int getDonorId() {
        return donorId;
    }

    public void setDonorId(int donorId) {
        this.donorId = donorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Displays formatted details of the donor.
     */
    public void displayDonorDetails() {
        System.out.println("ID: " + donorId 
            + " | Name: " + name 
            + " | Age: " + age 
            + " | Blood Group: " + bloodGroup 
            + " | Location: " + location 
            + " | Status: " + (available ? "Available" : "Not Available"));
    }

    @Override
    public String toString() {
        return "Donor [donorId=" + donorId + ", name=" + name + ", age=" + age 
                + ", bloodGroup=" + bloodGroup + ", location=" + location 
                + ", available=" + available + "]";
    }
}
