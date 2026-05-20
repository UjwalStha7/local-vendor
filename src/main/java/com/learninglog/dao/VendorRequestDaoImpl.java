package com.learninglog.dao;

import com.learninglog.entity.User;
import com.learninglog.model.VendorApprovalResult;
import com.learninglog.model.VendorRequestRow;
import com.learninglog.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

public class VendorRequestDaoImpl implements VendorRequestDao {

    private static final DateTimeFormatter SUBMITTED_FMT =
            DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US);

    private static final String CREATE_VENDOR_REQUESTS_TABLE = """
            CREATE TABLE IF NOT EXISTS vendor_requests (
                id INT AUTO_INCREMENT PRIMARY KEY,
                applicant_name VARCHAR(120) NOT NULL,
                farm_name VARCHAR(160) NOT NULL,
                contact_email VARCHAR(100) NOT NULL,
                phone VARCHAR(30) NOT NULL,
                category VARCHAR(50),
                about_text TEXT,
                status ENUM('pending', 'contacted', 'approved', 'rejected') NOT NULL DEFAULT 'pending',
                vendor_user_id INT NULL,
                vendor_login_email VARCHAR(100) NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                FOREIGN KEY (vendor_user_id) REFERENCES users(id) ON DELETE SET NULL
            )
            """;

    private static volatile boolean tableEnsured;

    private static final CopyOnWriteArrayList<VendorRequestRow> MEMORY_FALLBACK = new CopyOnWriteArrayList<>();

    private final UserDao userDao = new UserDaoImp();

    @Override
    public List<VendorRequestRow> listVendorRequests() {
        ensureVendorRequestsTable();
        if (tableExists()) {
            return listFromDatabase(null);
        }
        return new ArrayList<>(MEMORY_FALLBACK);
    }

    @Override
    public List<VendorRequestRow> listPendingVendorRequests() {
        ensureVendorRequestsTable();
        if (tableExists()) {
            return listFromDatabase("pending");
        }
        List<VendorRequestRow> pending = new ArrayList<>();
        for (VendorRequestRow row : MEMORY_FALLBACK) {
            if (row.isPending()) {
                pending.add(row);
            }
        }
        return pending;
    }

    @Override
    public boolean isContactEmailAlreadyUsed(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        User existing = userDao.findByEmail(normalized);
        if (existing != null && "vendor".equalsIgnoreCase(existing.getRole())) {
            return true;
        }
        ensureVendorRequestsTable();
        if (tableExists()) {
            return hasApprovedVendorRequestForContactEmail(normalized);
        }
        for (VendorRequestRow row : MEMORY_FALLBACK) {
            if (row.getEmail().equalsIgnoreCase(normalized) && row.isApproved() && row.hasActiveVendorLink()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasOpenVendorApplication(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        ensureVendorRequestsTable();
        if (tableExists()) {
            return hasOpenVendorRequestForContactEmail(normalized);
        }
        for (VendorRequestRow row : MEMORY_FALLBACK) {
            if (row.getEmail().equalsIgnoreCase(normalized) && row.isAwaitingAdminResponse()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLatestVendorApplicationRejected(String email) {
        if (email == null || email.isBlank() || hasOpenVendorApplication(email)) {
            return false;
        }
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        ensureVendorRequestsTable();
        if (tableExists()) {
            return "rejected".equalsIgnoreCase(findLatestStatusForContactEmail(normalized));
        }
        return MEMORY_FALLBACK.stream()
                .filter(row -> row.getEmail().equalsIgnoreCase(normalized))
                .max((a, b) -> Integer.compare(a.getId(), b.getId()))
                .map(VendorRequestRow::isRejected)
                .orElse(false);
    }

    @Override
    public void submitFarmerApplication(String applicantName, String farmName, String email, String phone,
                                        String category, String about) {
        if (hasOpenVendorApplication(email) || isContactEmailAlreadyUsed(email)) {
            return;
        }
        ensureVendorRequestsTable();
        if (insertApplication(applicantName, farmName, email, phone, category, about)) {
            return;
        }
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        for (VendorRequestRow row : MEMORY_FALLBACK) {
            if (row.getEmail().equalsIgnoreCase(normalized)
                    && (row.isAwaitingAdminResponse() || (row.isApproved() && row.hasActiveVendorLink()))) {
                return;
            }
        }
        int nextId = MEMORY_FALLBACK.stream().mapToInt(VendorRequestRow::getId).max().orElse(0) + 1;
        String submitted = LocalDate.now().format(SUBMITTED_FMT);
        MEMORY_FALLBACK.add(0, new VendorRequestRow(nextId, email, phone, submitted, "pending", applicantName, farmName));
        System.err.println("vendor_requests: saved application in memory only (database unavailable).");
    }

    @Override
    public VendorApprovalResult approveFarmerApplication(int requestId) {
        ensureVendorRequestsTable();
        VendorRequestRow row = findById(requestId);
        if (row == null) {
            return VendorApprovalResult.failure("Application not found.");
        }
        if (row.isApproved() && row.hasActiveVendorLink()) {
            return VendorApprovalResult.failure("This application is already approved.");
        }
        if (!row.canApprove()) {
            return VendorApprovalResult.failure("This application cannot be approved.");
        }

        String contactEmail = row.getEmail().trim().toLowerCase(Locale.ROOT);
        User existing = userDao.findByEmail(contactEmail);
        if (existing == null) {
            return VendorApprovalResult.failure(
                    "No registered account for " + contactEmail
                            + ". Ask the applicant to register as a customer first.");
        }
        if ("vendor".equalsIgnoreCase(existing.getRole())) {
            return VendorApprovalResult.failure("This account is already a vendor.");
        }
        if ("admin".equalsIgnoreCase(existing.getRole())) {
            return VendorApprovalResult.failure("Admin accounts cannot be approved as vendors.");
        }

        if (!userDao.updateRole(existing.getId(), "vendor")) {
            return VendorApprovalResult.failure("Could not update account role to vendor.");
        }

        if (!userDao.hasVendorProfile(existing.getId()) && !insertVendorProfile(existing.getId(), row.getFarmName())) {
            return VendorApprovalResult.failure("Role updated but vendor shop profile could not be created.");
        }

        if (!markApproved(requestId, existing.getId(), existing.getEmail())) {
            updateMemoryApproved(requestId, existing.getEmail());
        }

        return VendorApprovalResult.ok(existing.getEmail());
    }

    @Override
    public VendorApprovalResult rejectFarmerApplication(int requestId) {
        ensureVendorRequestsTable();
        VendorRequestRow row = findById(requestId);
        if (row == null) {
            return VendorApprovalResult.failure("Application not found.");
        }
        if (row.isApproved()) {
            return VendorApprovalResult.failure("Approved applications cannot be rejected.");
        }
        if (!row.isPending()) {
            return VendorApprovalResult.failure("This application is not pending.");
        }
        if (!updateStatus(requestId, "rejected")) {
            updateMemoryStatus(requestId, "rejected");
        }
        return VendorApprovalResult.rejected();
    }

    @Override
    public void markRequestsRejectedForDeletedVendor(int vendorUserId) {
        if (vendorUserId <= 0) {
            return;
        }
        ensureVendorRequestsTable();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    UPDATE vendor_requests
                    SET status = 'rejected', vendor_user_id = NULL, vendor_login_email = NULL
                    WHERE vendor_user_id = ?
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, vendorUserId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("vendor_requests release on delete: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        for (VendorRequestRow row : MEMORY_FALLBACK) {
            if (row.hasActiveVendorLink() && row.getVendorUserId() != null
                    && row.getVendorUserId() == vendorUserId) {
                row.setStatus("rejected");
                row.setVendorUserId(null);
            }
        }
    }

    private boolean hasOpenVendorRequestForContactEmail(String normalizedEmail) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    SELECT 1 FROM vendor_requests
                    WHERE LOWER(contact_email) = ? AND status IN ('pending', 'contacted')
                    LIMIT 1
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, normalizedEmail);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            System.err.println("vendor_requests open application check: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private String findLatestStatusForContactEmail(String normalizedEmail) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    SELECT status FROM vendor_requests
                    WHERE LOWER(contact_email) = ?
                    ORDER BY created_at DESC, id DESC
                    LIMIT 1
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, normalizedEmail);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("status");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("vendor_requests latest status: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    /** Block re-application only after a prior request for this contact email was approved. */
    private boolean hasApprovedVendorRequestForContactEmail(String normalizedEmail) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    SELECT 1 FROM vendor_requests
                    WHERE LOWER(contact_email) = ? AND status = 'approved' AND vendor_user_id IS NOT NULL
                    LIMIT 1
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, normalizedEmail);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            System.err.println("vendor_requests email check: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private void ensureVendorRequestsTable() {
        if (tableEnsured) {
            return;
        }
        synchronized (VendorRequestDaoImpl.class) {
            if (tableEnsured) {
                return;
            }
            Connection conn = null;
            try {
                conn = DatabaseConnection.getConnection();
                try (PreparedStatement ps = conn.prepareStatement(CREATE_VENDOR_REQUESTS_TABLE)) {
                    ps.execute();
                }
                tableEnsured = tableExists();
            } catch (SQLException e) {
                System.err.println("vendor_requests ensure table: " + e.getMessage());
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
    }

    private boolean tableExists() {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (ResultSet rs = conn.getMetaData().getTables(conn.getCatalog(), null, "vendor_requests", new String[]{"TABLE"})) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private List<VendorRequestRow> listFromDatabase(String statusFilter) {
        List<VendorRequestRow> rows = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    SELECT id, applicant_name, farm_name, contact_email, phone, status,
                           vendor_login_email, vendor_user_id, created_at
                    FROM vendor_requests
                    """;
            if (statusFilter != null) {
                sql += " WHERE status = ?";
            }
            sql += " ORDER BY created_at DESC";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (statusFilter != null) {
                    ps.setString(1, statusFilter);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        rows.add(mapRow(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("vendor_requests list: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return rows;
    }

    private VendorRequestRow findById(int id) {
        ensureVendorRequestsTable();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    SELECT id, applicant_name, farm_name, contact_email, phone, status,
                           vendor_login_email, vendor_user_id, created_at
                    FROM vendor_requests WHERE id = ?
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapRow(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("vendor_requests find: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return MEMORY_FALLBACK.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }

    private boolean insertApplication(String applicantName, String farmName, String email, String phone,
                                      String category, String about) {
        ensureVendorRequestsTable();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    INSERT INTO vendor_requests
                    (applicant_name, farm_name, contact_email, phone, category, about_text, status)
                    VALUES (?, ?, ?, ?, ?, ?, 'pending')
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, applicantName);
                ps.setString(2, farmName);
                ps.setString(3, email);
                ps.setString(4, phone);
                ps.setString(5, blankToNull(category));
                ps.setString(6, blankToNull(about));
                return ps.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            System.err.println("vendor_requests insert: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private boolean updateStatus(int id, String status) {
        ensureVendorRequestsTable();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE vendor_requests SET status = ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, status);
                ps.setInt(2, id);
                return ps.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private boolean markApproved(int requestId, int vendorUserId, String vendorLoginEmail) {
        ensureVendorRequestsTable();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    UPDATE vendor_requests
                    SET status = 'approved', vendor_user_id = ?, vendor_login_email = ?
                    WHERE id = ?
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, vendorUserId);
                ps.setString(2, vendorLoginEmail);
                ps.setInt(3, requestId);
                return ps.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            System.err.println("vendor_requests approve: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private boolean insertVendorProfile(int vendorUserId, String businessName) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO vendor_profiles (vendor_user_id, business_name) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, vendorUserId);
                ps.setString(2, businessName);
                return ps.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            System.err.println("vendor_profiles insert: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private void updateMemoryApproved(int requestId, String vendorLoginEmail) {
        updateMemoryStatus(requestId, "approved");
    }

    private void updateMemoryStatus(int requestId, String status) {
        for (VendorRequestRow row : MEMORY_FALLBACK) {
            if (row.getId() == requestId) {
                row.setStatus(status);
                return;
            }
        }
    }

    private static VendorRequestRow mapRow(ResultSet rs) throws SQLException {
        Timestamp created = rs.getTimestamp("created_at");
        String submitted = created != null
                ? created.toLocalDateTime().toLocalDate().format(SUBMITTED_FMT)
                : LocalDate.now().format(SUBMITTED_FMT);
        int vendorUserIdRaw = rs.getInt("vendor_user_id");
        Integer vendorUserId = rs.wasNull() ? null : vendorUserIdRaw;
        return new VendorRequestRow(
                rs.getInt("id"),
                rs.getString("contact_email"),
                rs.getString("phone"),
                submitted,
                rs.getString("status"),
                rs.getString("applicant_name"),
                rs.getString("farm_name"),
                rs.getString("vendor_login_email"),
                vendorUserId
        );
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
