package com.learninglog.dao;

import com.learninglog.model.VendorOrderRow;
import com.learninglog.model.WeeklyData;

import java.util.List;
import java.util.Map;

public interface VendorDashboardDao {

    Map<String, Object> fetchVendorKpis(int vendorUserId);

    List<WeeklyData> fetchWeeklyOrders(int vendorUserId);

    List<VendorOrderRow> listRecentOrders(int vendorUserId, int limit);
}
