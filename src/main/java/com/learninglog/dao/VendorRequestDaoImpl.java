package com.learninglog.dao;

import com.learninglog.model.VendorRequestRow;

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

    private static final CopyOnWriteArrayList<VendorRequestRow> REQUESTS = new CopyOnWriteArrayList<>(Arrays.asList(
            new VendorRequestRow(1, "olivia.martin@email.com", "+1 (555) 218-3940", "May 1, 2026", "pending", "Olivia Martin", "Martin Family Farm"),
            new VendorRequestRow(2, "james.wilson@farm.co", "+1 (555) 201-8842", "May 2, 2026", "contacted", "James Wilson", "Wilson Growers"),
            new VendorRequestRow(3, "priya.sharma@greenleaf.in", "+91 98765 43210", "May 3, 2026", "pending", "Priya Sharma", "GreenLeaf Organics"),
            new VendorRequestRow(4, "marcus.lee@harvest.io", "+1 (555) 330-1199", "May 4, 2026", "contacted", "Marcus Lee", "Harvest Co-op"),
            new VendorRequestRow(5, "ana.gomez@tierra.es", "+34 612 445 778", "May 5, 2026", "pending", "Ana Gómez", "Tierra Fértil"),
            new VendorRequestRow(6, "sam.okafor@roots.ng", "+234 803 555 2211", "May 6, 2026", "contacted", "Sam Okafor", "Roots Collective")
    ));

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
        return new ArrayList<>(REQUESTS);
    }

    @Override
    public void markVendorRequestContacted(int id) {
        for (VendorRequestRow row : REQUESTS) {
            if (row.getId() == id) {
                row.setStatus("contacted");
                return;
            }
        }
    }

    @Override
    public void submitFarmerApplication(String applicantName, String farmName, String email, String phone) {
        int nextId = REQUESTS.stream().mapToInt(VendorRequestRow::getId).max().orElse(0) + 1;
        String submitted = LocalDate.now().format(SUBMITTED_FMT);
        REQUESTS.add(0, new VendorRequestRow(nextId, email, phone, submitted, "pending", applicantName, farmName));
    }
}
