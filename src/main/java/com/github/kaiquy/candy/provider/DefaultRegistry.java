package com.github.kaiquy.candy.provider;

import com.github.kaiquy.candy.CandyPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.View;
import me.saiintbrisson.minecraft.ViewFrame;
import me.saiintbrisson.minecraft.command.message.MessageHolder;
import me.saiintbrisson.minecraft.command.message.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

@RequiredArgsConstructor
public class DefaultRegistry {

    @Getter
    private ViewFrame viewFrame;
    private final CandyPlugin plugin;

    public void registerListener(Listener... listeners) {
        final PluginManager pluginManager = Bukkit.getPluginManager();

        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, plugin);
        }
    }

    public void registerCommands(Object... objects) {
        final BukkitFrame frame = new BukkitFrame(plugin);
        final MessageHolder messageHolder = frame.getMessageHolder();

        messageHolder.setMessage(MessageType.NO_PERMISSION,"§c§lERRO! §cVocê não possuí permissão para utilizar este comando.");
        messageHolder.setMessage(MessageType.INCORRECT_USAGE, "§c§lERRO! §cUtilize /{usage}.");

        frame.registerCommands(objects);
    }

    public void registerViews(View... views) {
        this.viewFrame = new ViewFrame(plugin);

        viewFrame.register(views);
    }
}
