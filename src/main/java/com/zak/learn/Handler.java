package com.zak.learn;

import java.util.Collection;
import java.util.List;

import com.zak.learn.models.Asset;
import com.zak.learn.models.Portfolio;
import com.zak.learn.models.User;
import com.zak.learn.services.AssetService;
import com.zak.learn.services.PortfolioService;
import com.zak.learn.services.RankingService;
import com.zak.learn.services.TradingService;
import com.zak.learn.services.UserService;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class Handler {

    private final Router router;
    private final AssetService assetService = new AssetService();
    private final UserService userService = new UserService();
    private final PortfolioService portfolioService = new PortfolioService();
    private final TradingService tradingService = new TradingService(userService, portfolioService);
    private final RankingService rankingService = new RankingService(userService);

    public Handler(Router router) {
        this.router = router;

        this.router.post("/api/users").handler(this::createUser);
        this.router.post("/api/portfolios").handler(this::createPortfolio);
        this.router.post("/api/trading/buy").handler(this::buyAsset);
        this.router.post("/api/trading/sell").handler(this::sellAsset);
        this.router.get("/api/ranking").handler(this::getLeaderboard);
        this.router.get("/api/portfolios/:userId/value").handler(this::getPortfolioValue);
        this.router.get("/api/simulated-price").handler(this::getSimulatedPrice);
        this.router.get("/api/assets").handler(this::getAssets);
        this.router.post("/api/assets").handler(this::createAsset);
    }

    // --- Handlers ---

    private void createAsset(RoutingContext ctx) {
        try {
            ctx.request().bodyHandler(b -> {
                JsonObject body = b.toJsonObject();
                System.out.println(body);
                String name = body.getString("name");
                double initialPrice = body.getDouble("initialPrice");

                Asset asset = assetService.createAsset(name, initialPrice);
                ctx.response()
                        .putHeader("content-type", "application/json")
                        .end(Json.encode(asset));
            });
        } catch (Exception e) {
            ctx.response().setStatusCode(400).end(e.getMessage());
        }

    }

    private void getAssets(RoutingContext ctx) {
        Collection<Asset> assets = assetService.getAllAssets();
        System.out.println(assets);
        ctx.response()
                .putHeader("content-type", "application/json")
                .end(Json.encode(assets));
    }

    private void createUser(RoutingContext ctx) {
        try {
            ctx.request().bodyHandler(b -> {
                JsonObject body = b.toJsonObject();
                String username = body.getString("username");

                User user = userService.createUser(username);
                ctx.response()
                        .putHeader("content-type", "application/json")
                        .end(Json.encode(user));
            });
        } catch (Exception e) {
            ctx.response().setStatusCode(400).end(e.getMessage());
        }

    }

    private void createPortfolio(RoutingContext ctx) {
        try {
            ctx.request().bodyHandler(b -> {
                JsonObject body = b.toJsonObject();
                String userId = body.getString("userId");
                Portfolio portfolio = portfolioService.createPortfolio(userId);
                ctx.response()
                        .putHeader("content-type", "application/json")
                        .end(Json.encode(portfolio));
            });
        } catch (Exception e) {
            ctx.response().setStatusCode(400).end(e.getMessage());
        }
    }

    private void buyAsset(RoutingContext ctx) {
        try {
            ctx.request().bodyHandler(b -> {
                JsonObject body = b.toJsonObject();
                String userId = body.getString("userId");
                String assetId = body.getString("assetId");
                String assetName = body.getString("assetName");
                int quantity = body.getInteger("quantity");
                double price = body.getDouble("price");

                boolean success = tradingService.buyAsset(userId, assetId, assetName, quantity, price);
                if (success) {
                    rankingService.updateLeaderboard();
                    ctx.response().end("Buy operation successful");
                } else {
                    ctx.response().setStatusCode(400).end("Buy operation failed");
                }
            });
        } catch (Exception e) {
            ctx.response().setStatusCode(400).end(e.getMessage());
        }
    }

    private void sellAsset(RoutingContext ctx) {
        try {
            ctx.request().bodyHandler(b -> {
                JsonObject body = b.toJsonObject();
                String userId = body.getString("userId");
                String assetId = body.getString("assetId");
                int quantity = body.getInteger("quantity");

                boolean success = tradingService.sellAsset(userId, assetId, quantity);
                if (success) {
                    rankingService.updateLeaderboard();
                    ctx.response().end("Sell operation successful");
                } else {
                    ctx.response().setStatusCode(400).end("Sell operation failed");
                }
            });
        } catch (Exception e) {
            ctx.response().setStatusCode(400).end(e.getMessage());
        }

    }

    private void getLeaderboard(RoutingContext ctx) {
        int topN = Integer.parseInt(ctx.request().getParam("topN"));
        List<User> leaderboard = rankingService.getTopUsers(topN);
        ctx.response()
                .putHeader("content-type", "application/json")
                .end(Json.encode(leaderboard));
    }

    private void getPortfolioValue(RoutingContext ctx) {
        String userId = ctx.pathParam("userId");
        Portfolio portfolio = portfolioService.getPortfolio(userId);
        if (portfolio != null) {
            double value = portfolio.calculatePortfolioValue();
            ctx.response()
                    .putHeader("content-type", "application/json")
                    .end(new JsonObject().put("value", value).encode());
        } else {
            ctx.response().setStatusCode(404).end("Portfolio not found");
        }
    }

    private void getSimulatedPrice(RoutingContext ctx) {
        List<JsonObject> prices = assetService.getAllAssetPrices();
        ctx.response()
                .putHeader("content-type", "application/json")
                .end(Json.encode(prices));
    }

}
