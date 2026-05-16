package com.learninglog.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Query parameters for browsing / searching products. */
public class ProductBrowseParams {

    private String search = "";
    /** one of: all, fruits, vegetables */
    private String category = "all";
    /** When null, no minimum price filter is applied. */
    private BigDecimal minPrice;
    /** When null, no maximum price filter is applied. */
    private BigDecimal maxPrice;
    private List<Integer> vendorUserIds = Collections.emptyList();
    /** all | in | limited */
    private String availability = "all";
    /** featured | price-asc | price-desc | name-asc */
    private String sort = "featured";
    private int page = 1;
    private int pageSize = 9;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search != null ? search.trim() : "";
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category != null ? category.trim().toLowerCase() : "all";
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<Integer> getVendorUserIds() {
        return vendorUserIds;
    }

    public boolean isVendorSelected(int vendorUserId) {
        return vendorUserIds != null && vendorUserIds.contains(vendorUserId);
    }

    public void setVendorUserIds(List<Integer> vendorUserIds) {
        this.vendorUserIds = vendorUserIds != null ? vendorUserIds : new ArrayList<>();
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability != null ? availability.trim().toLowerCase() : "all";
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort != null ? sort.trim().toLowerCase() : "featured";
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = Math.max(1, page);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize > 0 ? pageSize : 9;
    }

    public int getOffset() {
        return (page - 1) * pageSize;
    }
}
