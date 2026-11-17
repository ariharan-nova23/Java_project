package com.emergencyblood.dao;

import com.emergencyblood.database.DBConnection;
import com.emergencyblood.model.Donor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Donor entities.
 * Handles all JDBC interactions with the 'donors' table in MySQL.
 */
public class DonorDAO {

    /**
     * Inserts a new donor record into the database.
     * Uses PreparedStatement with auto-generated keys to populate the donor's ID.
     *
     * @param donor The donor to insert.
     * @return true if insertion was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean addDonor(Donor donor) throws SQLException {
        if (donor == null) {
            throw new IllegalArgumentException("Donor cannot be null.");
        }

        String sql = "INSERT INTO donors (name, blood_group, age, phone, location, available) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, donor.getName());
            pstmt.setString(2, donor.getBloodGroup());
            pstmt.setInt(3, donor.getAge());
            pstmt.setString(4, donor.getPhoneNumber());
            pstmt.setString(5, donor.getLocation());
            boolean isAvailable = "Available".equalsIgnoreCase(donor.getAvailability());
            pstmt.setBoolean(6, isAvailable);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        donor.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("DonorDAO.addDonor failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves all donor records from the database ordered by ID.
     *
     * @return List of all registered donors.
     * @throws SQLException if a database access error occurs.
     */
    public List<Donor> getAllDonors() throws SQLException {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT donor_id, name, blood_group, age, phone, location, available FROM donors ORDER BY donor_id ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                donors.add(mapResultSetToDonor(rs));
            }
        } catch (SQLException e) {
            System.err.println("DonorDAO.getAllDonors failed: " + e.getMessage());
            throw e;
        }
        return donors;
    }

    /**
     * Retrieves only donors currently marked as available.
     *
     * @return List of available donors.
     * @throws SQLException if a database access error occurs.
     */
    public List<Donor> getAvailableDonors() throws SQLException {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT donor_id, name, blood_group, age, phone, location, available FROM donors WHERE available = 1 ORDER BY donor_id ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                donors.add(mapResultSetToDonor(rs));
            }
        } catch (SQLException e) {
            System.err.println("DonorDAO.getAvailableDonors failed: " + e.getMessage());
            throw e;
        }
        return donors;
    }

    /**
     * Updates an existing donor's details in the database.
     *
     * @param donor The donor with updated details.
     * @return true if the record was updated, false if not found.
     * @throws SQLException if a database access error occurs.
     */
    public boolean updateDonor(Donor donor) throws SQLException {
        if (donor == null) {
            throw new IllegalArgumentException("Donor cannot be null.");
        }

        String sql = "UPDATE donors SET name = ?, blood_group = ?, age = ?, phone = ?, location = ?, available = ? WHERE donor_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, donor.getName());
            pstmt.setString(2, donor.getBloodGroup());
            pstmt.setInt(3, donor.getAge());
            pstmt.setString(4, donor.getPhoneNumber());
            pstmt.setString(5, donor.getLocation());
            boolean isAvailable = "Available".equalsIgnoreCase(donor.getAvailability());
            pstmt.setBoolean(6, isAvailable);
            pstmt.setInt(7, donor.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("DonorDAO.updateDonor failed for ID " + donor.getId() + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * Deletes a donor record by ID.
     *
     * @param donorId The ID of the donor to delete.
     * @return true if the record was deleted, false if not found.
     * @throws SQLException if a database access error occurs.
     */
    public boolean deleteDonor(int donorId) throws SQLException {
        String sql = "DELETE FROM donors WHERE donor_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, donorId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("DonorDAO.deleteDonor failed for ID " + donorId + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * Deletes a donor record.
     *
     * @param donor The donor to delete.
     * @return true if deleted.
     * @throws SQLException if a database access error occurs.
     */
    public boolean deleteDonor(Donor donor) throws SQLException {
        if (donor == null) return false;
        return deleteDonor(donor.getId());
    }

    /**
     * Retrieves a single donor by ID.
     *
     * @param donorId The donor ID.
     * @return The Donor object, or null if not found.
     * @throws SQLException if a database access error occurs.
     */
    public Donor getDonorById(int donorId) throws SQLException {
        String sql = "SELECT donor_id, name, blood_group, age, phone, location, available FROM donors WHERE donor_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, donorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDonor(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("DonorDAO.getDonorById failed: " + e.getMessage());
            throw e;
        }
        return null;
    }

    /**
     * Helper method to map a ResultSet row to a Donor model instance.
     */
    private Donor mapResultSetToDonor(ResultSet rs) throws SQLException {
        int id = rs.getInt("donor_id");
        String name = rs.getString("name");
        String bloodGroup = rs.getString("blood_group");
        int age = rs.getInt("age");
        String phone = rs.getString("phone");
        String location = rs.getString("location");
        boolean available = rs.getBoolean("available");
        String availability = available ? "Available" : "Unavailable";

        return new Donor(id, name, bloodGroup, age, phone, location, availability);
    }
}
