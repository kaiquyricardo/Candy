package com.github.kaiquy.candy.data.user;

import com.github.kaiquy.candy.CandyPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class UserController {

    private final CandyPlugin plugin;

    public void create(Player player) {
        final UserStorage userStorage = plugin.getUserStorage();;
        final UserCache userCache = plugin.getUserCache();

        final User find = userStorage.find(player.getName());
        if (find != null) {
            userCache.addCachedElement(find);
            return;
        }

        User user = new User(
                player.getName(),
                plugin.getConfig().getInt("user.init")
        );

        userStorage.insert(user);
        userCache.addCachedElement(user);
    }

    public void remove(Player player) {
        final UserStorage userStorage = plugin.getUserStorage();;
        final UserCache userCache = plugin.getUserCache();

        final User find = userStorage.find(player.getName());
        if (find == null) return;

        userStorage.update(find);
        userCache.removeCachedElement(find);
    }
}