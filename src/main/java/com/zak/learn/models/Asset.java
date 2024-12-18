package com.zak.learn.models;

/**
 * Asset model represents a single asset in a portfolio.
 */
public class Asset {
    private final String assetId;
    private final String name;
    private int quantity;
    private double price;

    public Asset(String assetId, String name, int quantity, double price) {
        this.assetId = assetId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }
}

