package me.gameisntover.knockbackffa.cosmetics;

import me.gameisntover.knockbackffa.gui.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum CosmeticType {
    SOUND(ItemBuilder.builder().material(Material.NOTE_BLOCK).name("&eSounds").buttonMeta().build()),
    TRAIL(ItemBuilder.builder().material(Material.HARD_CLAY).name("&bTrails").build()),
    NULL(ItemBuilder.builder().build());

    public ItemStack item;
    CosmeticType(ItemStack item){
    this.item = item;
    }
}
