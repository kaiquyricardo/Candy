package com.github.kaiquy.candy.data.candy;

import com.github.kaiquy.candy.provider.SpigotProvider;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class CandyController extends SpigotProvider {

    public void load(Configuration config) {
        ConfigurationSection sections = config.getConfigurationSection("candy");

        for (String key : sections.getKeys(false)) {
            ConfigurationSection value = sections.getConfigurationSection(key);

            candyCache.addCachedElements(new Candy(
                    key,
                    value.getString("name").replace("&", "§"),
                    value.getString("texture"),
                    value.getInt("sugar"),
                    value.getInt("food")
            ));
        }
    }
}
