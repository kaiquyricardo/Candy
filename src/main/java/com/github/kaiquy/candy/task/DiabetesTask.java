package com.github.kaiquy.candy.task;

import com.github.kaiquy.candy.data.user.User;
import com.github.kaiquy.candy.provider.SpigotProvider;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class DiabetesTask extends SpigotProvider {

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(players -> {
                    final User user = userCache.getByUser(players.getName());
                    if (user.getDiabetes() >= config.getInt("user.damage")) {
                        players.damage(1);
                    }
                });
            }
        }.runTaskTimer(plugin,  0L, 20L);
    }
}
