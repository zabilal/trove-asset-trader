package com.zak.learn.services;

import java.util.HashMap;
import java.util.Map;

import com.zak.learn.models.Portfolio;

/**
 * PortfolioService manages portfolios and their associated assets.
 */
public class PortfolioService {
    private final Map<String, Portfolio> portfolios = new HashMap<>();

    public Portfolio createPortfolio(String userId) {
        Portfolio portfolio = new Portfolio(userId);
        this.portfolios.put(userId, portfolio);
        return portfolio;
    }

    public Portfolio getPortfolio(String userId) {
        return this.portfolios.get(userId);
    }
}