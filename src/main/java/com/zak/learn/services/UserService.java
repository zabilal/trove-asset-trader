package com.zak.learn.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.zak.learn.models.User;

/**
 * UserService handles user-related operations.
 */
public class UserService {
    private final Map<String, User> users = new HashMap<>();

    public User createUser(String username) {
        User user = new User(username);
        users.put(user.getUserId(), user);
        return user;
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public void updateGemCount(String userId, int gems) {
        User user = users.get(userId);
        if (user != null) {
            user.setGemCount(user.getGemCount() + gems);
        }
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }
}