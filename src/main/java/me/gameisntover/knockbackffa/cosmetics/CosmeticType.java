package me.gameisntover.knockbackffa.cosmetics;

import me.gameisntover.knockbackffa.gui.ItemBuilder;
import me.gameisntover.knockbackffa.multipleversion.KnockMaterial;
import org.bukkit.inventory.ItemStack;

public enum CosmeticType {
    SOUND(ItemBuilder.builder().material(KnockMaterial.NOTE_BLOCK).name("&eSounds").buttonMeta().build()),
    TRAIL(ItemBuilder.builder().material(KnockMaterial.WHITE_TERRACOTTA).name("&bTrails").build());

    public ItemStack item;
    CosmeticType(ItemStack item){
    this.item = item;
    }
}
