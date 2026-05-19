package com.learninglog.dao;

import com.learninglog.entity.User;
import com.learninglog.model.VendorApprovalResult;
import com.learninglog.model.VendorRequestRow;
import com.learninglog.util.DatabaseConnection;
import com.learninglog.util.PasswordUtil;
import com.learninglog.util.TempPasswordUtil;
import com.learninglog.util.VendorEmailUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

public class VendorRequestDaoImpl implements VendorRequestDao {

    private static final DateTimeFormatter SUBMITTED_FMT =
            DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US);

    private static final CopyOnWriteArrayList<VendorRequestRow> MEMORY_FALLBACK = new CopyOnWriteArrayList<>(Arrays.asList(
            new VendorRequestRow(1, "olivia.martin@email.com", "+1 (555) 218-3940", "May 1, 2026", "pending", "Olivia Martin", "Martin Family Farm"),
            new VendorRequestRow(2, "james.wilson@farm.co", "+1 (555) 201-8842", "May 2, 2026", "contacted", "James Wilson", "Wilson Growers")
    ));

    private final UserDao userDao = new UserDaoImp();

    @Override
    public List<String> fetchWeeklyLabels() {
        return Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
    }

    @Override
    public List<Integer> fetchWeeklyRequestCounts() {
        return Arrays.asList(3, 5, 4, 6, 7, 5, 4);
    }

    @Override
    public List<VendorRequestRow> listVendorRequests() {
        List<VendorRequestRow> fromDb = listFromDatabase();
        if (!fromDb.isEmpty() || tableExists()) {
            return fromDb;
        }
        return new ArrayList<>(MEMORY_FALLBACK);
    }

    @Override
    public void markVendorRequestContacted(int id) {
        if (updateStatus(id, "contacted")) {
            return;
        }
        for (VendorRequestRow row : MEMORY_FALLBACK) {
            if (row.getId() == id) {
                row.setStatus("contacted");
                return;
            }
        }
    }

    @Override
    public void submitFarmerApplication(String applicantName, String farmName, String email, String phone,
                                        String category, String about) {
        if (insertApplication(applicantName, farmName, email, phone, category, about)) {
            return;
        }
        int nextId = MEMORY_FALLBACK.stream().mapToInt(VendorRequestRow::getId).max().orElse(0) + 1;
        String submitted = LocalDate.now().format(SUBMITTED_FMT);
        MEMORY_FALLBACK.add(0, new VendorRequestRow(nextId, email, phone, submitted, "pending", applicantName, farmName));
    }

    @Override
    public VendorApprovalResult approveFarmerApplication(int requestId) {
        VendorRequestRow row = findById(requestId);
        if (row == null) {
            return VendorApprovalResult.failure("Application not found.");
        }
        if (row.isApproved()) {
            return VendorApprovalResult.failure("This application is already approved.");
        }
        if (!row.canApprove()) {
            return VendorApprovalResult.failure("This application cannot be approved.");
        }

        String vendorEmail = VendorEmailUtil.deriveVendorLoginEmail(row.getEmail(), userDao);
        if (userDao.findByEmail(vendorEmail) != null) {
            return VendorApprovalResult.failure("A vendor account already exists for " + vendorEmail);
        }

        String tempPassword = TempPasswordUtil.generate(10);
        String hashed = PasswordUtil.getHashpassword(tempPassword);

        User vendor = new User(row.getApplicantName(), vendorEmail, hashed, row.getPhone());
        vendor.setRole("vendor");
        vendor.setactive(true);

        if (!userDao.insertUser(vendor)) {
            return VendorApprovalResult.failure("Could not create vendor user. Email or username may already exist.");
        }

        User saved = userDao.findByEmail(vendorEmail);
        if (saved == null) {
            return VendorApprovalResult.failure("Vendor user was created but could not be loaded.");
        }

        if (!insertVendorProfile(saved.getId(), row.getFarmName())) {
            return VendorApprovalResult.failure("Vendor user created but farm profile failed. Check vendor_profiles table.");
        }

        if (!markApproved(requestId, saved.getId(), vendorEmail)) {
            updateMemoryApproved(requestId, vendorEmail);
        }

        return VendorApprovalResult.ok(vendorEmail, tempPassword);
    }

    private boolean tableExists() {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (ResultSet rs = conn.getMetaData().getTables(null, null, "vendor_requests", null)) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private List<VendorRequestRow> listFromDatabase() {
        List<VendorRequestRow> rows = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    SELECT id, applicant_name, farm_name, contact_email, phone, status,
                           vendor_login_email, created_at
                    FROM vendor_requests
                    ORDER BY created_at DESC
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(mapRow(rs));
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
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = """
                    SELECT id, applicant_name, farm_name, contact_email, phone, status,
                           vendor_login_email, created_at
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
        for (VendorRequestRow row : MEMORY_FALLBACK) {
            if (row.getId() == requestId) {
                row.setStatus("approved");
                return;
            }
        }
    }

    private static VendorRequestRow mapRow(ResultSet rs) throws SQLException {
        Timestamp created = rs.getTimestamp("created_at");
        String submitted = created != null
                ? created.toLocalDateTime().toLocalDate().format(SUBMITTED_FMT)
                : LocalDate.now().format(SUBMITTED_FMT);
        return new VendorRequestRow(
                rs.getInt("id"),
                rs.getString("contact_email"),
                rs.getString("phone"),
                submitted,
                rs.getString("status"),
                rs.getString("applicant_name"),
                rs.getString("farm_name"),
                rs.getString("vendor_login_email")
        );
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
