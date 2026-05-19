package com.learninglog.util;

import com.learninglog.dao.VendorDashboardDao;
import com.learninglog.dao.VendorDashboardDaoImpl;
import com.learninglog.model.VendorOrderRow;
import com.learninglog.model.WeeklyData;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class VendorDashboardAttributes {

    private static final VendorDashboardDao DASHBOARD_DAO = new VendorDashboardDaoImpl();

    private VendorDashboardAttributes() {
    }

    public static void apply(HttpServletRequest req, int vendorUserId) {
        Map<String, Object> kpis = DASHBOARD_DAO.fetchVendorKpis(vendorUserId);
        req.setAttribute("statProductsListed", kpis.get("productsListed"));
        req.setAttribute("statActiveOrders", kpis.get("activeOrders"));
        req.setAttribute("statPendingDeliveries", kpis.get("pendingDeliveries"));
        req.setAttribute("statTotalEarnings", kpis.get("totalEarnings"));

        List<WeeklyData> weekly = DASHBOARD_DAO.fetchWeeklyOrders(vendorUserId);
        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        for (WeeklyData wd : weekly) {
            dates.add(wd.getDate());
            counts.add(wd.getCount());
        }
        req.setAttribute("chartDates", dates);
        req.setAttribute("chartCounts", counts);
        req.setAttribute("recentOrders", DASHBOARD_DAO.listRecentOrders(vendorUserId, 5));
    }

    public static void applyEmpty(HttpServletRequest req) {
        req.setAttribute("statProductsListed", 0);
        req.setAttribute("statActiveOrders", 0);
        req.setAttribute("statPendingDeliveries", 0);
        req.setAttribute("statTotalEarnings", 0.0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d", Locale.US);
        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int daysAgo = 6; daysAgo >= 0; daysAgo--) {
            dates.add(today.minusDays(daysAgo).format(fmt));
            counts.add(0);
        }
        req.setAttribute("chartDates", dates);
        req.setAttribute("chartCounts", counts);
        req.setAttribute("recentOrders", Collections.<VendorOrderRow>emptyList());
    }
}
