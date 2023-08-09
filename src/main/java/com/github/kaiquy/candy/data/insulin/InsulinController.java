package com.github.kaiquy.candy.data.insulin;

import com.github.kaiquy.candy.provider.SpigotProvider;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class InsulinController extends SpigotProvider {

    public void load(Configuration config) {
        ConfigurationSection sections = config.getConfigurationSection("insulin");

        for (String key : sections.getKeys(false)) {
            ConfigurationSection value = sections.getConfigurationSection(key);

            insulinCache.addCachedElements(new Insulin(
                    key,
                    value.getString("name").replace("&", "§"),
                    value.getString("texture"),
                    value.getInt("ml")
            ));
        }
    }
}
