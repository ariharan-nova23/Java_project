package com.emergencyblood.dao;

import com.emergencyblood.database.DBConnection;
import com.emergencyblood.model.BloodRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for BloodRequest entities.
 * Handles all JDBC operations for the 'blood_requests' table in MySQL.
 */
public class BloodRequestDAO {

    /**
     * Saves an emergency blood request to the database.
     * Uses PreparedStatement with RETURN_GENERATED_KEYS.
     *
     * @param request The BloodRequest to save.
     * @return true if insertion succeeded, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean addRequest(BloodRequest request) throws SQLException {
        if (request == null) {
            throw new IllegalArgumentException("BloodRequest cannot be null.");
        }

        String sql = "INSERT INTO blood_requests (blood_group, units_required, urgency, location, request_date, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, request.getRequiredBloodGroup());
            pstmt.setInt(2, request.getUnitsRequired());
            pstmt.setString(3, request.getUrgency());
            pstmt.setString(4, request.getLocation());

            Timestamp ts = request.getRequestDate();
            if (ts == null) {
                ts = new Timestamp(System.currentTimeMillis());
                request.setRequestDate(ts);
            }
            pstmt.setTimestamp(5, ts);

            String status = request.getStatus();
            if (status == null || status.trim().isEmpty()) {
                status = "Pending";
                request.setStatus(status);
            }
            pstmt.setString(6, status);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        request.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("BloodRequestDAO.addRequest failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves all emergency blood requests ordered by date descending.
     *
     * @return List of all blood requests.
     * @throws SQLException if a database access error occurs.
     */
    public List<BloodRequest> getAllRequests() throws SQLException {
        List<BloodRequest> list = new ArrayList<>();
        String sql = "SELECT request_id, blood_group, units_required, urgency, location, request_date, status FROM blood_requests ORDER BY request_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("BloodRequestDAO.getAllRequests failed: " + e.getMessage());
            throw e;
        }
        return list;
    }

    /**
     * Retrieves the most recent blood request, or null if none exist.
     *
     * @return The latest BloodRequest or null.
     * @throws SQLException if a database access error occurs.
     */
    public BloodRequest getLatestRequest() throws SQLException {
        String sql = "SELECT request_id, blood_group, units_required, urgency, location, request_date, status FROM blood_requests ORDER BY request_id DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return mapResultSetToRequest(rs);
            }
        } catch (SQLException e) {
            System.err.println("BloodRequestDAO.getLatestRequest failed: " + e.getMessage());
            throw e;
        }
        return null;
    }

    /**
     * Updates the status of an emergency blood request.
     *
     * @param requestId The ID of the blood request.
     * @param status    The new status (e.g., "Pending", "Matched", "Fulfilled", "Cancelled").
     * @return true if updated, false if not found.
     * @throws SQLException if a database access error occurs.
     */
    public boolean updateRequestStatus(int requestId, String status) throws SQLException {
        String sql = "UPDATE blood_requests SET status = ? WHERE request_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, requestId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("BloodRequestDAO.updateRequestStatus failed for ID " + requestId + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves a blood request by its ID.
     *
     * @param requestId The request ID.
     * @return The BloodRequest object, or null if not found.
     * @throws SQLException if a database access error occurs.
     */
    public BloodRequest getRequestById(int requestId) throws SQLException {
        String sql = "SELECT request_id, blood_group, units_required, urgency, location, request_date, status FROM blood_requests WHERE request_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, requestId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRequest(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("BloodRequestDAO.getRequestById failed: " + e.getMessage());
            throw e;
        }
        return null;
    }

    private BloodRequest mapResultSetToRequest(ResultSet rs) throws SQLException {
        int id = rs.getInt("request_id");
        String bloodGroup = rs.getString("blood_group");
        int unitsRequired = rs.getInt("units_required");
        String urgency = rs.getString("urgency");
        String location = rs.getString("location");
        Timestamp requestDate = rs.getTimestamp("request_date");
        String status = rs.getString("status");

        return new BloodRequest(id, bloodGroup, unitsRequired, location, urgency, status, requestDate);
    }
}
