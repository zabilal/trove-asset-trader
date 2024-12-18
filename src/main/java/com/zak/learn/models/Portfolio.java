package com.zak.learn.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Portfolio model represents a user's portfolio containing assets.
 */
public class Portfolio implements Serializable {
    private final String userId;
    private final List<Asset> assets;

    public Portfolio(String userId) {
        this.userId = userId;
        this.assets = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public List<Asset> getAssets() {
        return assets;
    }   

    public void addAsset(String assetId, String name, int quantity, double price) {
        Asset existingAsset = assets.stream().filter(a -> a.getAssetId().equals(assetId)).findFirst().orElse(null);
        if (existingAsset != null) {
            existingAsset.setQuantity(existingAsset.getQuantity() + quantity);
        } else {
            assets.add(new Asset(assetId, name, quantity, price));
        }
    }

    public boolean removeAsset(String assetId, int quantity) {
        Asset existingAsset = assets.stream().filter(a -> a.getAssetId().equals(assetId)).findFirst().orElse(null);
        if (existingAsset != null && existingAsset.getQuantity() >= quantity) {
            existingAsset.setQuantity(existingAsset.getQuantity() - quantity);
            if (existingAsset.getQuantity() == 0) {
                assets.remove(existingAsset);
            }
            return true;
        }
        return false;
    }

    public double calculatePortfolioValue() {
        return assets.stream()
                .mapToDouble(asset -> asset.getPrice() * asset.getQuantity())
                .sum();
    }
}

