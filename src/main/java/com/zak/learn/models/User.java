package com.zak.learn.models;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User model represents the user entity.
 */
public class User {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    private final String userId;
    private final String username;
    private int gemCount;
    private int rank;

    public User(String username) {
        this.userId = "user-" + ID_GENERATOR.incrementAndGet();
        this.username = username;
        this.gemCount = 0;
        this.rank = 0;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getGemCount() {
        return gemCount;
    }

    public void setGemCount(int gemCount) {
        this.gemCount = gemCount;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
