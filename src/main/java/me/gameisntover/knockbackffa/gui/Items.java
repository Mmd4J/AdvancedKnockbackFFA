package me.gameisntover.knockbackffa.gui;

import me.gameisntover.knockbackffa.configurations.ItemConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public enum Items {
    WAND(ItemBuilder.builder().material(Material.BLAZE_ROD).name(ChatColor.GOLD + "PositionSelector Wand").itemflags(Collections.singletonList(ItemFlag.HIDE_ENCHANTS)).enchants(Collections.singletonList(new KEnchant(Enchantment.DURABILITY,99))).build()),
    COSMETICS_MENU(ItemBuilder.builder().name(ItemConfiguration.get().getString("LobbyItems.cosmetic.name")).material(Material.getMaterial(ItemConfiguration.get().getString("LobbyItems.cosmetic.material"))).lores(ItemConfiguration.get().getStringList("LobbyItems.cosmetic.lore")).coolMeta().build()),
    KITS_MENU(ItemBuilder.builder().name(ItemConfiguration.get().getString("LobbyItems.kits.name")).material(Material.getMaterial(ItemConfiguration.get().getString("LobbyItems.kits.material"))).lores(ItemConfiguration.get().getStringList("LobbyItems.kits.lore")).coolMeta().build()),
    SHOP_MENU(ItemBuilder.builder().name(ItemConfiguration.get().getString("LobbyItems.shop.name")).material(Material.getMaterial(ItemConfiguration.get().getString("LobbyItems.shop.material"))).lores(ItemConfiguration.get().getStringList("LobbyItems.shop.lore")).coolMeta().build());
    public ItemStack item;
    Items(org.bukkit.inventory.ItemStack item ){
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}
