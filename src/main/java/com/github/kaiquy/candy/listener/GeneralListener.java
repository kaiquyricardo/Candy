package com.github.kaiquy.candy.listener;

import com.github.kaiquy.candy.data.user.User;
import com.github.kaiquy.candy.misc.ItemBuilder;
import com.github.kaiquy.candy.provider.SpigotProvider;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class GeneralListener extends SpigotProvider {

    @EventHandler
    public void on(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        userController.create(player);
    }


    @EventHandler
    public void on(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        userController.remove(player);
    }


    @EventHandler
    public void on(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack hand = event.getItem();
        final User user = userCache.getByUser(player.getName());

        if (hand == null || hand.getType() == Material.AIR) return;

        final NBTItem nbtItem = new NBTItem(hand);


        if (nbtItem.hasKey("insulin_id")) {
            if (!(user.getDiabetes() == config.getInt("user.init"))) {
                final Action action = event.getAction();

                if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
                    event.setCancelled(true);

                    ItemStack item = new ItemBuilder(hand.clone()).build();

                    final NBTItem itemNBT = new NBTItem(item);

                    item = itemNBT.getItem();

                    player.getInventory().removeItem(item);
                    player.sendMessage("§bVocê acaba de aplicar " +
                            nbtItem.getString("insulin_name") +
                            " §bcom §f" + nbtItem.getInteger("insulin_ml") +
                            "ml§b!"
                    );

                    user.remove(nbtItem.getInteger("insulin_ml"));
                }
                return;
            }
            player.sendMessage("§cVocê não está precisando utilizar a insulina");
            return;
        }
        if (player.getFoodLevel() < 20) {
            if (nbtItem.hasKey("candy_id")) {
                final Action action = event.getAction();

                if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
                    event.setCancelled(true);

                    ItemStack item = new ItemBuilder(hand.clone()).build();

                    final NBTItem itemNBT = new NBTItem(item);

                    item = itemNBT.getItem();

                    player.sendMessage("§aVocê acaba de ingerir " +
                            nbtItem.getString("candy_name") +
                            " §acom §f" + nbtItem.getInteger("candy_sugar") +
                            "% §ade açucar!"
                    );

                    user.add(nbtItem.getInteger("candy_sugar"));

                    player.getInventory().removeItem(item);
                    player.setFoodLevel(player.getFoodLevel() + nbtItem.getInteger("candy_food"));
                    return;
                }
            }
        }
        player.sendMessage("§cVocê não está com fome!");
    }


    @EventHandler
    public void on(BlockPlaceEvent event) {
        final ItemStack hand = event.getItemInHand();

        if (hand == null || hand.getType() == Material.AIR) return;

        final NBTItem nbtItem = new NBTItem(hand);


        if (nbtItem.hasKey("insulin_id") || nbtItem.hasKey("insulin_id")) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void on(PlayerDeathEvent event) {
        final Player player = event.getEntity().getPlayer();
        final User user = userCache.getByUser(player.getName());

        if (user.getDiabetes() >= 300) {
            user.setDiabetes(config.getInt("user.init"));
        }
    }
}
