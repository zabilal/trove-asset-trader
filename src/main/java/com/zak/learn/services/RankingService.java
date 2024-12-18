package com.zak.learn.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.zak.learn.models.User;

/**
 * RankingService manages the leaderboard and user rankings.
 */
public class RankingService {
    private final UserService userService;

    public RankingService(UserService userService) {
        this.userService = userService;
    }

    public void updateLeaderboard() {
        List<User> users = new ArrayList<>(userService.getAllUsers());
        users.sort(Comparator.comparingInt(User::getGemCount).reversed());

        int rank = 1;
        for (int i = 0; i < users.size(); i++) {
            if (i > 0 && users.get(i).getGemCount() == users.get(i - 1).getGemCount()) {
                users.get(i).setRank(users.get(i - 1).getRank());
            } else {
                users.get(i).setRank(rank);
            }
            rank++;
        }
    }

    public List<User> getTopUsers(int topN) {
        return userService.getAllUsers().stream()
                .sorted(Comparator.comparingInt(User::getGemCount).reversed())
                .limit(topN)
                .collect(Collectors.toList());
    }
}
