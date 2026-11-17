package com.emergencyblood.database;

import com.emergencyblood.dao.BloodRequestDAO;
import com.emergencyblood.dao.DonorDAO;
import com.emergencyblood.model.BloodRequest;
import com.emergencyblood.model.Donor;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.List;

/**
 * Automated end-to-end integration test for JDBC, MySQL, DonorDAO, and BloodRequestDAO.
 * Includes complete lifecycle resource cleanup to eliminate thread leaks and warnings.
 */
public class DatabaseIntegrationTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" Emergency Blood Matching - JDBC Integration Tests");
        System.out.println("==================================================");

        try {
            testDatabaseConnection();
            testDonorLifecycle();
            testBloodRequestLifecycle();

            System.out.println("==================================================");
            System.out.println(" TEST RESULTS: " + testsPassed + " PASSED, " + testsFailed + " FAILED");
            System.out.println("==================================================");
        } catch (Exception e) {
            System.err.println("FATAL TEST EXCEPTION: " + e.getMessage());
            e.printStackTrace();
            testsFailed++;
        } finally {
            cleanupDatabaseResources();
            if (testsFailed > 0) {
                System.exit(1);
            }
        }
    }

    private static void assertTrue(String testName, boolean condition, String message) {
        if (condition) {
            System.out.println("  [PASS] " + testName);
            testsPassed++;
        } else {
            System.err.println("  [FAIL] " + testName + " -> " + message);
            testsFailed++;
        }
    }

    private static void testDatabaseConnection() throws Exception {
        System.out.println("\n--- 1. Testing DBConnection ---");
        try (Connection conn = DBConnection.getConnection()) {
            assertTrue("Database Connection Valid", conn != null && !conn.isClosed(), "Connection was null or closed.");
            assertTrue("Database Catalog is emergency_blood_db",
                    "emergency_blood_db".equalsIgnoreCase(conn.getCatalog()),
                    "Catalog was: " + conn.getCatalog());
        }
    }

    private static void testDonorLifecycle() throws Exception {
        System.out.println("\n--- 2. Testing DonorDAO CRUD Lifecycle ---");
        DonorDAO donorDAO = new DonorDAO();

        // 1. Add Donor
        String testName = "Test Donor " + System.currentTimeMillis();
        Donor newDonor = new Donor(0, testName, "O+", 28, "9988776655", "Hyderabad", "Available");
        boolean added = donorDAO.addDonor(newDonor);
        assertTrue("DonorDAO.addDonor() returns true", added, "addDonor failed");
        assertTrue("Donor ID is auto-generated > 0", newDonor.getId() > 0, "ID was: " + newDonor.getId());
        int testDonorId = newDonor.getId();

        // 2. Retrieve All Donors
        List<Donor> allDonors = donorDAO.getAllDonors();
        boolean foundInAll = allDonors.stream().anyMatch(d -> d.getId() == testDonorId && d.getName().equals(testName));
        assertTrue("DonorDAO.getAllDonors() contains new donor", foundInAll, "New donor not found in list");

        // 3. Retrieve Available Donors
        List<Donor> availDonors = donorDAO.getAvailableDonors();
        boolean foundInAvail = availDonors.stream().anyMatch(d -> d.getId() == testDonorId);
        assertTrue("DonorDAO.getAvailableDonors() contains new available donor", foundInAvail, "New donor not found in available list");

        // 4. Retrieve by ID
        Donor retrieved = donorDAO.getDonorById(testDonorId);
        assertTrue("DonorDAO.getDonorById() returns correct donor", retrieved != null && retrieved.getName().equals(testName), "Donor mismatch");

        // 5. Update Donor (change name, age, blood group, location, and set to Unavailable)
        newDonor.setName(testName + " Updated");
        newDonor.setAge(30);
        newDonor.setBloodGroup("A+");
        newDonor.setLocation("Secunderabad");
        newDonor.setAvailability("Unavailable");
        boolean updated = donorDAO.updateDonor(newDonor);
        assertTrue("DonorDAO.updateDonor() returns true", updated, "updateDonor failed");

        Donor updatedRetrieved = donorDAO.getDonorById(testDonorId);
        assertTrue("Donor details successfully updated in MySQL",
                updatedRetrieved != null &&
                (testName + " Updated").equals(updatedRetrieved.getName()) &&
                updatedRetrieved.getAge() == 30 &&
                "A+".equals(updatedRetrieved.getBloodGroup()) &&
                "Unavailable".equalsIgnoreCase(updatedRetrieved.getAvailability()),
                "Updated values did not match in DB");

        // Verify no longer in available list
        List<Donor> availAfterUpdate = donorDAO.getAvailableDonors();
        boolean foundAfterUnavailable = availAfterUpdate.stream().anyMatch(d -> d.getId() == testDonorId);
        assertTrue("Donor correctly excluded from getAvailableDonors() when Unavailable", !foundAfterUnavailable, "Donor still in available list");

        // 6. Delete Donor
        boolean deleted = donorDAO.deleteDonor(testDonorId);
        assertTrue("DonorDAO.deleteDonor() returns true", deleted, "deleteDonor failed");

        Donor afterDelete = donorDAO.getDonorById(testDonorId);
        assertTrue("Donor successfully removed from MySQL after deletion", afterDelete == null, "Donor still exists in DB");
    }

    private static void testBloodRequestLifecycle() throws Exception {
        System.out.println("\n--- 3. Testing BloodRequestDAO Persistence Lifecycle ---");
        BloodRequestDAO requestDAO = new BloodRequestDAO();

        // 1. Add Blood Request
        BloodRequest req = new BloodRequest(0, "B+", 3, "Apollo Hospital Vijayawada", "CRITICAL", "Pending", new Timestamp(System.currentTimeMillis()));
        boolean added = requestDAO.addRequest(req);
        assertTrue("BloodRequestDAO.addRequest() returns true", added, "addRequest failed");
        assertTrue("BloodRequest ID auto-generated > 0", req.getId() > 0, "ID was: " + req.getId());
        int testRequestId = req.getId();

        // 2. Retrieve All Requests
        List<BloodRequest> allRequests = requestDAO.getAllRequests();
        boolean foundInList = allRequests.stream().anyMatch(r -> r.getId() == testRequestId && "B+".equals(r.getRequiredBloodGroup()));
        assertTrue("BloodRequestDAO.getAllRequests() contains new request", foundInList, "Request not found in list");

        // 3. Retrieve Latest Request
        BloodRequest latest = requestDAO.getLatestRequest();
        assertTrue("BloodRequestDAO.getLatestRequest() returns most recent request",
                latest != null && latest.getId() == testRequestId,
                "Latest request ID was: " + (latest != null ? latest.getId() : "null"));

        // 4. Update Status to Matched
        boolean statusUpdated1 = requestDAO.updateRequestStatus(testRequestId, "Matched");
        assertTrue("BloodRequestDAO.updateRequestStatus('Matched') returns true", statusUpdated1, "update status to Matched failed");

        BloodRequest matchedReq = requestDAO.getRequestById(testRequestId);
        assertTrue("Blood request status in MySQL updated to 'Matched'",
                matchedReq != null && "Matched".equalsIgnoreCase(matchedReq.getStatus()),
                "Status was: " + (matchedReq != null ? matchedReq.getStatus() : "null"));

        // 5. Update Status to Fulfilled
        boolean statusUpdated2 = requestDAO.updateRequestStatus(testRequestId, "Fulfilled");
        assertTrue("BloodRequestDAO.updateRequestStatus('Fulfilled') returns true", statusUpdated2, "update status to Fulfilled failed");

        BloodRequest fulfilledReq = requestDAO.getRequestById(testRequestId);
        assertTrue("Blood request status in MySQL updated to 'Fulfilled'",
                fulfilledReq != null && "Fulfilled".equalsIgnoreCase(fulfilledReq.getStatus()),
                "Status was: " + (fulfilledReq != null ? fulfilledReq.getStatus() : "null"));

        // Cleanup test blood request
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM blood_requests WHERE request_id = ?")) {
            pstmt.setInt(1, testRequestId);
            pstmt.executeUpdate();
            System.out.println("  (Cleaned up test blood request #" + testRequestId + ")");
        }
    }

    /**
     * Cleans up MySQL Connector/J background cleanup thread and deregisters JDBC drivers
     * to eliminate thread leaks and warning in Maven / standalone execution environments.
     */
    private static void cleanupDatabaseResources() {
        System.out.println("\n--- 4. Cleaning up JDBC and Driver Resources ---");
        try {
            // Shut down MySQL Connector/J abandoned connection cleanup thread
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
            System.out.println("  [OK] MySQL AbandonedConnectionCleanupThread shutdown complete.");
        } catch (Throwable t) {
            try {
                com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.uncheckedShutdown();
                System.out.println("  [OK] MySQL AbandonedConnectionCleanupThread unchecked shutdown complete.");
            } catch (Throwable ignored) {
            }
        }

        try {
            // Deregister JDBC drivers
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                try {
                    DriverManager.deregisterDriver(driver);
                    System.out.println("  [OK] Deregistered JDBC driver: " + driver.getClass().getName());
                } catch (SQLException e) {
                    System.err.println("  [WARN] Failed to deregister driver: " + e.getMessage());
                }
            }
        } catch (Throwable t) {
            System.err.println("  [WARN] Error deregistering drivers: " + t.getMessage());
        }
    }
}
