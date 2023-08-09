package com.github.kaiquy.candy.commands;

import com.github.kaiquy.candy.data.insulin.Insulin;
import com.github.kaiquy.candy.misc.ItemBuilder;
import com.github.kaiquy.candy.provider.SpigotProvider;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InsulinCommand extends SpigotProvider {


    @Command(
            name = "insulin",
            usage = "insulin [jogador] [insulina]"
    )

    public void execute(Context<CommandSender>  context, Player target, String name) {
        final Insulin insulin = insulinCache.getById(name);

        if (target == null) {
            context.getSender().sendMessage("§cEste jogador não está online!");
            return;
        }

        if (insulin == null) {
            target.sendMessage("§cEsta insulina não existe");
            return;
        }

        ItemStack insulinItem = new ItemBuilder(Material.SKULL_ITEM)
                .name(insulin.getName())
                .texture(insulin.getTexture())
                .lore(
                        "",
                        " §7" + insulin.getMl() + "ml",
                        ""
                ).build();

        final NBTItem nbtItem = new NBTItem(insulinItem);
        nbtItem.setString("insulin_id", UUID.randomUUID().toString());
        nbtItem.setString("insulin_name", insulin.getName());
        nbtItem.setInteger("insulin_ml", insulin.getMl());

        insulinItem = nbtItem.getItem();

        target.getInventory().addItem(insulinItem);
    }
}
