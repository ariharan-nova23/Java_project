/**
 * Represents an emergency blood request in the system.
 * Encapsulates request details such as required blood group, units, location, and urgency.
 */
public class BloodRequest {
    private int requestId;
    private String requiredBloodGroup;
    private int unitsRequired;
    private String location;
    private String urgency;

    // Default Constructor
    public BloodRequest() {
    }

    // Parameterized Constructor
    public BloodRequest(int requestId, String requiredBloodGroup, int unitsRequired, String location, String urgency) {
        this.requestId = requestId;
        this.requiredBloodGroup = requiredBloodGroup;
        this.unitsRequired = unitsRequired;
        this.location = location;
        this.urgency = urgency;
    }

    // Getters and Setters
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getRequiredBloodGroup() {
        return requiredBloodGroup;
    }

    public void setRequiredBloodGroup(String requiredBloodGroup) {
        this.requiredBloodGroup = requiredBloodGroup;
    }

    public int getUnitsRequired() {
        return unitsRequired;
    }

    public void setUnitsRequired(int unitsRequired) {
        this.unitsRequired = unitsRequired;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    /**
     * Displays formatted details of the blood request.
     */
    public void displayRequestDetails() {
        System.out.println("Request ID: " + requestId 
            + " | Required Blood Group: " + requiredBloodGroup 
            + " | Units Required: " + unitsRequired 
            + " | Location: " + location 
            + " | Urgency Level: " + urgency);
    }

    @Override
    public String toString() {
        return "BloodRequest [requestId=" + requestId + ", requiredBloodGroup=" + requiredBloodGroup 
                + ", unitsRequired=" + unitsRequired + ", location=" + location 
                + ", urgency=" + urgency + "]";
    }
}
