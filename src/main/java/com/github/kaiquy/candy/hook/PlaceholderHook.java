package com.github.kaiquy.candy.hook;

import com.github.kaiquy.candy.CandyPlugin;
import com.github.kaiquy.candy.data.user.User;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class PlaceholderHook extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "candy";
    }

    @Override
    public String getAuthor() {
        return "Kaiquy";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("diabetes")) {
            final User user = CandyPlugin.getInstance().getUserCache().getByUser(player.getName());

            return String.valueOf(user.getDiabetes());
        }

        return "Invalid";
    }
}
