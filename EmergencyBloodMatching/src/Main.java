import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main application entry point for the Emergency Blood Matching System (Checkpoint 1).
 * Manages console user interface, menu options, and exception-safe user input.
 */
public class Main {

    private static final List<Donor> donorList = new ArrayList<>();
    private static BloodRequest currentRequest = null;
    private static int nextDonorId = 1;
    private static int nextRequestId = 101;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        System.out.println("========================================");
        System.out.println("   EMERGENCY BLOOD MATCHING SYSTEM      ");
        System.out.println("            (Checkpoint 1)              ");
        System.out.println("========================================");

        while (!exit) {
            displayMenu();
            int choice = readInt(scanner, "Enter your choice (1-5): ", 1, 5);

            switch (choice) {
                case 1:
                    registerDonor(scanner);
                    break;
                case 2:
                    viewDonors();
                    break;
                case 3:
                    createEmergencyBloodRequest(scanner);
                    break;
                case 4:
                    findCompatibleDonors();
                    break;
                case 5:
                    exit = true;
                    System.out.println("\nThank you for using Emergency Blood Matching System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }

        scanner.close();
    }

    /**
     * Displays the main menu options.
     */
    private static void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("1. Register Donor");
        System.out.println("2. View Donors");
        System.out.println("3. Create Emergency Blood Request");
        System.out.println("4. Find Compatible Donors");
        System.out.println("5. Exit");
        System.out.println("========================================");
    }

    /**
     * Feature 1: Registers a new donor with full input validation and exception handling.
     */
    private static void registerDonor(Scanner scanner) {
        System.out.println("\n--- REGISTER DONOR ---");

        String name = readNonEmptyString(scanner, "Enter donor name: ");
        int age = readInt(scanner, "Enter age (18-65): ", 18, 65);
        String bloodGroup = readValidBloodGroup(scanner, "Enter blood group (e.g., O+, A-, AB+): ");
        String location = readNonEmptyString(scanner, "Enter location: ");
        boolean available = readBoolean(scanner, "Available for donation? (yes/no): ");

        Donor donor = new Donor(nextDonorId++, name, age, bloodGroup, location, available);
        donorList.add(donor);

        System.out.println("\n[SUCCESS] Donor registered successfully with ID: " + donor.getDonorId());
    }

    /**
     * Feature 2: Displays all registered donors in memory.
     */
    private static void viewDonors() {
        System.out.println("\n--- REGISTERED DONORS ---");

        if (donorList.isEmpty()) {
            System.out.println("No donors registered yet. Please register donors first.");
            return;
        }

        for (Donor donor : donorList) {
            donor.displayDonorDetails();
        }
    }

    /**
     * Feature 3: Creates an emergency blood request with validation.
     */
    private static void createEmergencyBloodRequest(Scanner scanner) {
        System.out.println("\n--- CREATE EMERGENCY BLOOD REQUEST ---");

        String bloodGroup = readValidBloodGroup(scanner, "Enter required blood group (e.g., O+, A-, AB+): ");
        int units = readInt(scanner, "Enter units required (1-10): ", 1, 10);
        String location = readNonEmptyString(scanner, "Enter location: ");
        String urgency = readNonEmptyString(scanner, "Enter urgency level (LOW / MEDIUM / HIGH / CRITICAL): ");

        currentRequest = new BloodRequest(nextRequestId++, bloodGroup, units, location, urgency);

        System.out.println("\n[SUCCESS] Emergency Blood Request created successfully!");
        currentRequest.displayRequestDetails();
    }

    /**
     * Feature 4: Finds and displays compatible donors for the current request.
     */
    private static void findCompatibleDonors() {
        System.out.println("\n--- FIND COMPATIBLE DONORS ---");

        if (currentRequest == null) {
            System.out.println("No active emergency blood request found.");
            System.out.println("Please create an emergency request first (Option 3).");
            return;
        }

        if (donorList.isEmpty()) {
            System.out.println("No donors registered in system.");
            return;
        }

        System.out.println("Active Emergency Request:");
        currentRequest.displayRequestDetails();
        System.out.println("----------------------------------------");
        System.out.println("Evaluating Donors for Compatibility:");

        String reqGroup = currentRequest.getRequiredBloodGroup();
        int compatibleCount = 0;

        for (Donor donor : donorList) {
            boolean isComp = BloodMatcher.isCompatible(reqGroup, donor.getBloodGroup());
            if (isComp) {
                System.out.println(" -> " + donor.getName() + " (" + donor.getBloodGroup() + ") -> Compatible");
                compatibleCount++;
            } else {
                System.out.println(" -> " + donor.getName() + " (" + donor.getBloodGroup() + ") -> Not compatible");
            }
        }

        System.out.println("----------------------------------------");
        System.out.println("Summary of Available Compatible Donors:");

        List<Donor> compatibleList = BloodMatcher.findCompatibleDonors(donorList, currentRequest);

        if (compatibleList.isEmpty()) {
            System.out.println("No available compatible donors found for " + reqGroup + ".");
        } else {
            for (Donor donor : compatibleList) {
                donor.displayDonorDetails();
            }
            System.out.println("\nTotal Compatible Donors Found: " + compatibleList.size());
        }
    }

    // =========================================================================
    // INPUT VALIDATION & EXCEPTION HANDLING HELPER METHODS
    // =========================================================================

    /**
     * Reads an integer input with range check and exception handling for non-numeric input.
     */
    private static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid age/number format. Please enter a valid number.");
            }
        }
    }

    /**
     * Reads a non-empty string input.
     */
    private static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
    }

    /**
     * Reads and validates blood group input, re-prompting until valid input is given.
     */
    private static String readValidBloodGroup(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            String normalized = BloodMatcher.normalizeBloodGroup(input);

            if (BloodMatcher.isValidBloodGroup(normalized)) {
                return normalized;
            } else {
                System.out.println("Invalid blood group.");
                System.out.println("Valid groups are: " + String.join(", ", BloodMatcher.getValidBloodGroups()));
            }
        }
    }

    /**
     * Reads and parses a boolean availability input (yes/no, y/n, true/false).
     */
    private static boolean readBoolean(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("yes") || input.equals("y") || input.equals("true")) {
                return true;
            } else if (input.equals("no") || input.equals("n") || input.equals("false")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
    }
}
