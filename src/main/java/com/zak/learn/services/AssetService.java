package com.zak.learn.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.stream.Collectors;

import com.zak.learn.models.Asset;

import io.netty.util.internal.ThreadLocalRandom;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

public class AssetService {
    private final Map<String, Asset> assets = new HashMap<>();
    private final Random random = new Random();

    // Schedule price updates every 10 seconds
    // vertx.setPeriodic(10000, id -> assetService.updatePrices());

    public AssetService() {

        this.createAsset("AAPL", 150.0);
        this.createAsset("GOOG", 2800.0);
        this.createAsset("TSLA", 700.0);

        // Schedule price updates every 10 seconds
        // vertx.setPeriodic(10000, id -> assetService.updatePrices());

        // Simulate periodic price updates
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getAllAssets().forEach(asset -> {
                    double newPrice = asset.getPrice() * (0.95 + (random.nextDouble() * 0.1));
                    asset.setPrice(Math.round(newPrice * 100.0) / 100.0);
                });
            }
        }, 0, 2000);
    }

    public Asset createAsset(String name, double initialPrice) {
        String assetId = UUID.randomUUID().toString();
        Asset asset = new Asset(assetId, name, 0, initialPrice);
        this.assets.put(assetId, asset);
        return asset;
    }

    public Collection<Asset> getAllAssets() {
        return this.assets.values();
    }

    public List<JsonObject> getAllAssetPrices() {
        return getAllAssets().stream().map(asset -> new JsonObject()
                .put("assetId", asset.getAssetId())
                .put("price", asset.getPrice())).collect(Collectors.toList());
    }

    public void updatePrices() {
        this.assets.values().forEach(asset -> {
            double randomFactor = ThreadLocalRandom.current().nextDouble(0.95, 1.05);
            double newPrice = asset.getPrice() * randomFactor;
            asset.setPrice(Math.round(newPrice * 100.0) / 100.0); // Round to 2 decimal places
        });
    }

    public void handleWebSocket(Vertx vertx, io.vertx.core.http.ServerWebSocket webSocket) {
        if ("/realtime-price".equals(webSocket.path())) {
            vertx.setPeriodic(1000, id -> {
                if (!webSocket.isClosed()) {
                    List<JsonObject> prices = getAllAssetPrices();
                    webSocket.writeTextMessage(Json.encode(prices));
                } else {
                    vertx.cancelTimer(id);
                }
            });
        } else {
            webSocket.reject();
        }
    }
}
