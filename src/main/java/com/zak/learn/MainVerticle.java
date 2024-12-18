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

}