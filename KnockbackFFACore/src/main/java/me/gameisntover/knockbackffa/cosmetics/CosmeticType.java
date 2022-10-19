package me.gameisntover.knockbackffa.cosmetics;

import com.cryptomorin.xseries.XMaterial;
import me.gameisntover.knockbackffa.gui.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public enum CosmeticType {
    SOUND(ItemBuilder.builder().material(XMaterial.NOTE_BLOCK.parseMaterial()).name("&eSounds").buttonMeta().build()),
    TRAIL(ItemBuilder.builder().material(XMaterial.WHITE_TERRACOTTA.parseMaterial()).name("&bTrails").build());

    public ItemStack item;
    CosmeticType(ItemStack item){
    this.item = item;
    }
}
