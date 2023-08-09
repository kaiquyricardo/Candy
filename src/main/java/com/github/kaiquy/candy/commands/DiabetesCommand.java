package com.github.kaiquy.candy.commands;

import com.github.kaiquy.candy.data.user.User;
import com.github.kaiquy.candy.provider.SpigotProvider;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.github.kaiquy.candy.misc.ActionBar.send;

public class DiabetesCommand extends SpigotProvider {

    @Command(
            name = "diabete"
    )

    public void execute(Context<CommandSender> context) {
        final Player player = (Player) context.getSender();
        final User user = userCache.getByUser(player.getName());

        send(player, "§aAnalisando...");

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            send(player, "§fDiabete: §d" + user.getDiabetes());
        }, 60L);
    }
}
