package com.github.kaiquy.candy.data.user;

import com.github.kaiquy.candy.misc.Cache;

public class UserCache extends Cache<User> {

    public User getByUser(String username) {
        return getCached(user -> user.getName().equalsIgnoreCase(username));
    }
}
