package com.github.kaiquy.candy.commands;


import com.github.kaiquy.candy.data.candy.Candy;
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

public class CandyCommand extends SpigotProvider {

    @Command(
            name = "candy",
            permission = "candy.give",
            usage = "candy [jogador] [doce]"
    )

    public void execute(Context<CommandSender>  context, Player target, String name) {
        final Candy candy = candyCache.getById(name);

        if (target == null) {
            context.getSender().sendMessage("§cEste jogador não está online!");
            return;
        }

        if (candy == null) {
            target.sendMessage("§cEste doce não existe");
            return;
        }

        ItemStack item = new ItemBuilder(Material.SKULL_ITEM)
                .name(candy.getName())
                .texture(candy.getTexture())
                .lore(
                        "",
                        " §7" + candy.getSugar(),
                        ""
                ).build();

        final NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("candy_id", UUID.randomUUID().toString());
        nbtItem.setString("candy_name", candy.getName());
        nbtItem.setInteger("candy_sugar", candy.getSugar());
        nbtItem.setInteger("candy_food", candy.getFood());
        item = nbtItem.getItem();

        target.getInventory().addItem(item);
    }
}
