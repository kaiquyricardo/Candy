package com.github.kaiquy.candy.misc;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar {

    /**
     * Sends an action bar message to the specified player.
     *
     * @param player The player to send the action bar message to.
     * @param input  The text to display in the action bar.
     */

    public static void send(Player player, String input) {
        final PacketPlayOutChat packet = new PacketPlayOutChat(
                new ChatComponentText(input),
                (byte) 2
        );

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}