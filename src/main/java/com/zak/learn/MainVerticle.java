package com.zak.learn;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

import com.zak.learn.services.AssetService;


/**
 * MainVerticle initializes the Vert.x application and sets up routes.
 */
public class MainVerticle extends AbstractVerticle {

    private final AssetService assetService = new AssetService();


    @Override
    public void start(Promise<Void> startPromise) {
        Router router = Router.router(vertx);
        new Handler(router);



        // Start WebSocket for real-time simulated price updates
        vertx.createHttpServer(new HttpServerOptions().setPort(8090))
                .webSocketHandler(webSocket -> assetService.handleWebSocket(vertx, webSocket))
                .listen(res -> {
                    if (res.succeeded()) {
                        System.out.println("WebSocket server started on port 8090");
                    } else {
                        res.cause().printStackTrace();
                    }
                });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080, http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        System.out.println("HTTP server started on port 8080");
                    } else {
                        startPromise.fail(http.cause());
                    }
                });
    }

    // --- Handlers ---

    // private void createAsset(RoutingContext ctx) {
    //     JsonObject body = ctx.getBodyAsJson();
    //     String name = body.getString("name");
    //     double initialPrice = body.getDouble("initialPrice");

    //     Asset asset = assetService.createAsset(name, initialPrice);
    //     ctx.response()
    //             .putHeader("content-type", "application/json")
    //             .end(Json.encode(asset));
    // }

    // private void getAssets(RoutingContext ctx) {
    //     Collection<Asset> assets = assetService.getAllAssets();
    //     ctx.response()
    //             .putHeader("content-type", "application/json")
    //             .end(Json.encode(assets));
    // }

    // private void createUser(RoutingContext ctx) {
    //     JsonObject body = ctx.getBodyAsJson();
    //     String username = body.getString("username");

    //     User user = userService.createUser(username);
    //     ctx.response()
    //             .putHeader("content-type", "application/json")
    //             .end(Json.encode(user));
    // }

    // private void createPortfolio(RoutingContext ctx) {
    //     JsonObject body = ctx.getBodyAsJson();
    //     String userId = body.getString("userId");

    //     Portfolio portfolio = portfolioService.createPortfolio(userId);
    //     ctx.response()
    //             .putHeader("content-type", "application/json")
    //             .end(Json.encode(portfolio));
    // }

    // private void buyAsset(RoutingContext ctx) {
    //     JsonObject body = ctx.getBodyAsJson();
    //     String userId = body.getString("userId");
    //     String assetId = body.getString("assetId");
    //     String assetName = body.getString("assetName");
    //     int quantity = body.getInteger("quantity");
    //     double price = body.getDouble("price");

    //     boolean success = tradingService.buyAsset(userId, assetId, assetName, quantity, price);
    //     if (success) {
    //         rankingService.updateLeaderboard();
    //         ctx.response().end("Buy operation successful");
    //     } else {
    //         ctx.response().setStatusCode(400).end("Buy operation failed");
    //     }
    // }

    // private void sellAsset(RoutingContext ctx) {
    //     JsonObject body = ctx.getBodyAsJson();
    //     String userId = body.getString("userId");
    //     String assetId = body.getString("assetId");
    //     int quantity = body.getInteger("quantity");

    //     boolean success = tradingService.sellAsset(userId, assetId, quantity);
    //     if (success) {
    //         rankingService.updateLeaderboard();
    //         ctx.response().end("Sell operation successful");
    //     } else {
    //         ctx.response().setStatusCode(400).end("Sell operation failed");
    //     }
    // }

    // private void getLeaderboard(RoutingContext ctx) {
    //     int topN = Integer.parseInt(ctx.request().getParam("topN"));
    //     List<User> leaderboard = rankingService.getTopUsers(topN);
    //     ctx.response()
    //             .putHeader("content-type", "application/json")
    //             .end(Json.encode(leaderboard));
    // }

    // private void getPortfolioValue(RoutingContext ctx) {
    //     String userId = ctx.pathParam("userId");
    //     Portfolio portfolio = portfolioService.getPortfolio(userId);
    //     if (portfolio != null) {
    //         double value = portfolio.calculatePortfolioValue();
    //         ctx.response()
    //                 .putHeader("content-type", "application/json")
    //                 .end(new JsonObject().put("value", value).encode());
    //     } else {
    //         ctx.response().setStatusCode(404).end("Portfolio not found");
    //     }
    // }

    // private void getSimulatedPrice(RoutingContext ctx) {
    //     List<JsonObject> prices = simulatedPriceService.getAllAssetPrices();
    //     ctx.response()
    //             .putHeader("content-type", "application/json")
    //             .end(Json.encode(prices));
    // }

}