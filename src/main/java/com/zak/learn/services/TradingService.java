package com.zak.learn.services;

import com.zak.learn.models.Portfolio;

/**
 * TradingService implements the trading logic for buying and selling assets.
 */
public class TradingService {
    private final UserService userService;
    private final PortfolioService portfolioService;

    public TradingService(UserService userService, PortfolioService portfolioService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
    }

    public boolean buyAsset(String userId, String assetId, String name, int quantity, double price) {
        Portfolio portfolio = portfolioService.getPortfolio(userId);
        if (portfolio != null) {
            portfolio.addAsset(assetId, name, quantity, price);
            userService.updateGemCount(userId, 1);
            return true;
        }
        return false;
    }

    public boolean sellAsset(String userId, String assetId, int quantity) {
        Portfolio portfolio = portfolioService.getPortfolio(userId);
        if (portfolio != null && portfolio.removeAsset(assetId, quantity)) {
            userService.updateGemCount(userId, 1);
            return true;
        }
        return false;
    }
}