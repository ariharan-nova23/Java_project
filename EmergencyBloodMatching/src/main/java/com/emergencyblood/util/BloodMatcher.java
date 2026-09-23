package com.emergencyblood.util;

import com.emergencyblood.model.BloodRequest;
import com.emergencyblood.model.Donor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles blood group validation and medical compatibility rules for GUI.
 */
public class BloodMatcher {

    private static final List<String> VALID_BLOOD_GROUPS = Arrays.asList(
        "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
    );

    public static String normalizeBloodGroup(String input) {
        if (input == null) return "";
        return input.trim().toUpperCase();
    }

    public static boolean isValidBloodGroup(String bloodGroup) {
        return VALID_BLOOD_GROUPS.contains(normalizeBloodGroup(bloodGroup));
    }

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
                return true; // Universal recipient
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

    public static List<Donor> findCompatibleDonors(List<Donor> donors, BloodRequest request) {
        List<Donor> result = new ArrayList<>();
        if (donors == null || request == null) return result;

        String reqGroup = request.getRequiredBloodGroup();
        for (Donor donor : donors) {
            if ("Available".equalsIgnoreCase(dGetAvailability(donor)) && isCompatible(reqGroup, donor.getBloodGroup())) {
                result.add(donor);
            }
        }
        return result;
    }

    private static String dGetAvailability(Donor donor) {
        return donor.getAvailability();
    }
}
