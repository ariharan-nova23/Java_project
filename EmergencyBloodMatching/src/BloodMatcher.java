import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles blood group validation and compatibility checking logic.
 * Keeps business logic clean and separated from console I/O in Main.java.
 */
public class BloodMatcher {

    // List of standard valid blood groups
    private static final List<String> VALID_BLOOD_GROUPS = Arrays.asList(
        "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
    );

    /**
     * Normalizes blood group string (e.g., "o+" -> "O+", " a- " -> "A-").
     * @param input Raw input string
     * @return Normalized uppercase blood group string
     */
    public static String normalizeBloodGroup(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().toUpperCase();
    }

    /**
     * Validates whether the given blood group is supported.
     * @param bloodGroup Blood group string to check
     * @return true if valid, false otherwise
     */
    public static boolean isValidBloodGroup(String bloodGroup) {
        String normalized = normalizeBloodGroup(bloodGroup);
        return VALID_BLOOD_GROUPS.contains(normalized);
    }

    /**
     * Returns a copy of all valid blood groups supported by the system.
     */
    public static List<String> getValidBloodGroups() {
        return new ArrayList<>(VALID_BLOOD_GROUPS);
    }

    /**
     * Determines red-blood-cell compatibility between a recipient and a donor.
     * 
     * @param recipientGroup Required blood group of recipient
     * @param donorGroup Blood group of donor
     * @return true if donor is compatible with recipient, false otherwise
     */
    public static boolean isCompatible(String recipientGroup, String donorGroup) {
        String recipient = normalizeBloodGroup(recipientGroup);
        String donor = normalizeBloodGroup(donorGroup);

        if (!isValidBloodGroup(recipient) || !isValidBloodGroup(donor)) {
            return false;
        }

        switch (recipient) {
            case "A+":
                return donor.equals("A+") || donor.equals("A-") || donor.equals("O+") || donor.equals("O-");

            case "A-":
                return donor.equals("A-") || donor.equals("O-");

            case "B+":
                return donor.equals("B+") || donor.equals("B-") || donor.equals("O+") || donor.equals("O-");

            case "B-":
                return donor.equals("B-") || donor.equals("O-");

            case "AB+":
                // Universal recipient: can receive from all valid blood groups
                return true;

            case "AB-":
                return donor.equals("A-") || donor.equals("B-") || donor.equals("AB-") || donor.equals("O-");

            case "O+":
                return donor.equals("O+") || donor.equals("O-");

            case "O-":
                return donor.equals("O-");

            default:
                return false;
        }
    }

    /**
     * Filters a list of registered donors and returns only those who are available
     * and compatible with the emergency blood request.
     * 
     * @param donors List of all registered donors
     * @param request Emergency blood request details
     * @return List of compatible available donors
     */
    public static List<Donor> findCompatibleDonors(List<Donor> donors, BloodRequest request) {
        List<Donor> compatibleDonors = new ArrayList<>();
        if (donors == null || request == null) {
            return compatibleDonors;
        }

        String requiredGroup = request.getRequiredBloodGroup();

        for (Donor donor : donors) {
            if (donor.isAvailable() && isCompatible(requiredGroup, donor.getBloodGroup())) {
                compatibleDonors.add(donor);
            }
        }

        return compatibleDonors;
    }
}
