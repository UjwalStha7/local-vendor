package com.learninglog.entity;

public class Product {
    private String name;
    private String category;
    private double price;
    private String unit;
    private int stock;
    private boolean organic;
    private String farmerName;
    private String photoPath;
    private String description;

    public Product(String name, String category, double price, String unit,
                   int stock, boolean organic, String farmerName,
                   String photoPath, String description) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.unit = unit;
        this.stock = stock;
        this.organic = organic;
        this.farmerName = farmerName;
        this.photoPath = photoPath;
        this.description = description;
    }

    // Getters
    public String getName()        { return name; }
    public String getCategory()    { return category; }
    public double getPrice()       { return price; }
    public String getUnit()        { return unit; }
    public int getStock()          { return stock; }
    public boolean isOrganic()     { return organic; }
    public String getFarmerName()  { return farmerName; }
    public String getPhotoPath()   { return photoPath; }
    public String getDescription() { return description; }
}