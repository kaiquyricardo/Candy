package com.github.kaiquy.candy.provider;

import com.github.kaiquy.candy.CandyPlugin;
import com.github.kaiquy.candy.data.candy.CandyCache;
import com.github.kaiquy.candy.data.insulin.InsulinCache;
import com.github.kaiquy.candy.data.user.UserCache;
import com.github.kaiquy.candy.data.user.UserController;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

public class SpigotProvider implements Listener {


    protected CandyPlugin plugin = CandyPlugin.getPlugin(CandyPlugin.class);
    protected UserCache userCache = plugin.getUserCache();
    protected UserController userController = plugin.getUserController();
    protected CandyCache candyCache = plugin.getCandyCache();
    protected InsulinCache insulinCache = plugin.getInsulinCache();
    protected ConfigurationSection config = plugin.getConfig();
}
