package com.github.kaiquy.candy.misc;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ItemBuilder implements Cloneable {

    private ItemStack itemStack;
    private ItemMeta meta;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        meta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        itemStack = new ItemStack(material, amount);
        meta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material, int amount, int data) {
        itemStack = new ItemStack(material, amount, (short) data);
        meta = itemStack.getItemMeta();
    }

    public ItemBuilder(String material, int amount, int data) {
        if (Material.getMaterial(material) == null) {
            itemStack = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);

            final String url = "https://textures.minecraft.net/texture/" + material;

            final SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            final GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(url.getBytes()), null);

            final byte[] bytes = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());

            final Property property = new Property("textures", new String(bytes));
            profile.getProperties().put("textures", property);

            Field field = null;
            try {
                field = skullMeta.getClass().getDeclaredField("profile");
            } catch (NoSuchFieldException exception) {
                exception.printStackTrace();
            }

            if (field != null) {
                field.setAccessible(true);

                try {
                    field.set(skullMeta, profile);
                } catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }

            meta = skullMeta;
        } else {
            itemStack = new ItemStack(Material.valueOf(material), amount, (short) data);
            meta = itemStack.getItemMeta();
        }
    }

    public ItemBuilder material(Material material) {
        itemStack.setType(material);
        return this;
    }

    public ItemBuilder data(int data) {
        itemStack.setDurability((short) data);
        return this;
    }

    public ItemBuilder durability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder addDurability(short durability) {
        final short currentDurability = itemStack.getDurability();
        if (currentDurability == 0)
            return this;

        final short newDurability = (short) (currentDurability + durability);
        itemStack.setDurability(newDurability);
        return this;
    }

    public ItemBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name) {
        meta.setDisplayName(colorize(name));
        return this;
    }

    public ItemBuilder lore(String... lore) {
        return lore(ImmutableList.copyOf(lore));
    }

    public ItemBuilder lore(List<String> lore) {
        meta.setLore(colorize(lore));
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        return addLore(Arrays.asList(lore));
    }

    public ItemBuilder addLore(List<String> lore) {
        final List<String> newLore = meta.getLore() == null ?
                new ArrayList<>() :
                meta.getLore();

        newLore.addAll(lore);
        meta.setLore(colorize(newLore));
        return this;
    }

    public ItemBuilder addLoreIf(boolean condition, List<String> lore) {
        if (!condition) return this;
        return addLore(lore);
    }

    public ItemBuilder addLoreIf(boolean condition, String... lore) {
        if (!condition) return this;
        return addLore(Arrays.asList(lore));
    }

    public ItemBuilder removeLore(String... lore) {
        return removeLore(ImmutableList.copyOf(lore));
    }

    public ItemBuilder removeLore(List<String> lore) {
        if (lore.size() == 0)
            return this;

        final List<String> currentLore = meta.getLore();
        final List<String> newLore = currentLore == null ?
                new ArrayList<>() :
                currentLore;

        newLore.removeAll(lore);
        meta.setLore(colorize(newLore));
        return this;
    }

    public ItemBuilder removeLoreLine(int line) {
        final List<String> currentLore = meta.getLore();
        if (currentLore == null || line > currentLore.size())
            return this;

        currentLore.remove(line);
        meta.setLore(colorize(currentLore));
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        if (enchantments.size() == 0)
            return this;

        for (Entry<Enchantment, Integer> entry : enchantments.entrySet())
            meta.addEnchant(entry.getKey(), entry.getValue(), true);

        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment... enchantments) {
        return removeEnchantment(ImmutableList.copyOf(enchantments));
    }

    public ItemBuilder removeEnchantment(List<Enchantment> enchantments) {
        if (enchantments.size() == 0)
            return this;

        for (Enchantment enchantment : enchantments)
            itemStack.removeEnchantment(enchantment);

        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        meta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder glow(boolean active) {
        if (active) {
            meta.addEnchant(Enchantment.OXYGEN, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            return this;
        }

        if (!itemStack.containsEnchantment(Enchantment.OXYGEN))
            return this;

        itemStack.removeEnchantment(Enchantment.OXYGEN);
        meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder texture(String texture) {
        if (itemStack == null
                || itemStack.getType() != Material.SKULL_ITEM
                || itemStack.getDurability() != 3)
            itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        final String newTexture = "https://textures.minecraft.net/texture/" + texture;

        final SkullMeta skullMeta = (SkullMeta) meta;
        final GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(newTexture.getBytes()), null);

        final byte[] bytes = Base64.encodeBase64(
                String.format(
                        "{textures:{SKIN:{url:\"%s\"}}}",
                        newTexture
                ).getBytes()
        );

        final Property property = new Property("textures", new String(bytes));
        profile.getProperties().put("textures", property);

        Field field = null;
        try {
            field = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException exception) {
            exception.printStackTrace();
        }

        if (field != null) {
            field.setAccessible(true);

            try {
                field.set(skullMeta, profile);
            } catch (IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }

        meta = skullMeta;
        return this;
    }

    public ItemBuilder skull(String owner) {
        if (itemStack == null
                || itemStack.getType() != Material.SKULL_ITEM
                || itemStack.getDurability() != 3)
            itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        final SkullMeta skullMeta = (SkullMeta) meta;
        skullMeta.setOwner(owner);
        return this;
    }

    public ItemBuilder mob(EntityType type) {
        if (itemStack.getType() != Material.MOB_SPAWNER)
            return this;

        final BlockStateMeta blockStateMeta = (BlockStateMeta) meta;
        final BlockState state = blockStateMeta.getBlockState();

        ((CreatureSpawner) state).setSpawnedType(type);
        blockStateMeta.setBlockState(state);
        return this;
    }

    public ItemBuilder armor(Color color) {
        final LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
        armorMeta.setColor(color);
        return this;
    }

    public ItemBuilder addPotion(List<String> potions) {
        if (itemStack.getType() != Material.POTION)
            return this;

        for (String potionString : potions) {
            final String[] split = potionString.split(":");

            if (split.length < 3)
                continue;

            final String potionName = split[0];
            final int duration = Integer.parseInt(split[1]);
            final int amplifier = Integer.parseInt(split[2]);

            final PotionMeta potionMeta = (PotionMeta) meta;
            final PotionEffectType type = PotionEffectType.getByName(potionName);

            if (type == null)
                continue;

            final PotionEffect effect = type.createEffect(duration * 20, amplifier);
            potionMeta.addCustomEffect(effect, true);

            final Potion potion = Potion.fromItemStack(itemStack);
            potion.setSplash(potion.isSplash());
            potion.apply(itemStack);
        }

        return this;
    }

    public ItemBuilder addPotion(String potionName, int duration, int amplifier) {
        if (itemStack.getType() != Material.POTION)
            return this;

        final PotionMeta potionMeta = (PotionMeta) meta;
        final PotionEffectType type = PotionEffectType.getByName(potionName);

        if (type == null)
            return this;

        final PotionEffect effect = type.createEffect(duration * 20, amplifier);
        potionMeta.addCustomEffect(effect, true);

        final Potion potion = Potion.fromItemStack(itemStack);
        potion.setSplash(potion.isSplash());
        potion.apply(itemStack);
        return this;
    }

    public ItemBuilder removePotion(String potionName) {
        if (itemStack.getType() != Material.POTION)
            return this;

        final PotionMeta potionMeta = (PotionMeta) meta;
        final PotionEffectType type = PotionEffectType.getByName(potionName);

        if (type == null)
            return this;

        potionMeta.removeCustomEffect(type);

        final Potion potion = Potion.fromItemStack(itemStack);
        potion.setSplash(potion.isSplash());
        potion.apply(itemStack);
        return this;
    }

    public ItemBuilder clearPotion() {
        if (itemStack.getType() != Material.POTION)
            return this;

        final PotionMeta potionMeta = (PotionMeta) meta;
        potionMeta.clearCustomEffects();

        final Potion potion = Potion.fromItemStack(itemStack);
        potion.setSplash(potion.isSplash());
        potion.apply(itemStack);
        return this;
    }

    public ItemBuilder addFlag(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder removeFlag(ItemFlag... flags) {
        meta.removeItemFlags(flags);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public ItemBuilder clone() {
        try {
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException ignored) {
            return null;
        }
    }

    private String colorize(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    private String[] colorize(String... lore) {
        Arrays.setAll(lore, i -> colorize(lore[i]));
        return lore;
    }

    private List<String> colorize(List<String> lore) {
        return lore.stream().map(this::colorize).collect(Collectors.toList());
    }

}