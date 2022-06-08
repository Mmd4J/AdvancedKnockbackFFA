package me.gameisntover.knockbackffa.util;

import me.gameisntover.knockbackffa.configurations.ItemConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KBFFAKit implements Listener
{
    public ItemStack kbStick() {
        ItemStack kbstick = new ItemStack(Material.STICK,1);
        ItemMeta meta = kbstick.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("SpecialItems.KBStick.name")));
        meta.spigot().setUnbreakable(true);
        meta.addEnchant(Enchantment.KNOCKBACK, ItemConfiguration.get().getInt("SpecialItems.KBStick.kb-level"), true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        kbstick.setItemMeta(meta);
        return kbstick;
    }
    public ItemStack kbBow() {
        ItemStack kbBow = new ItemStack(Material.BOW, 1);
        ItemMeta kbBowMeta = kbBow.getItemMeta();
        kbBowMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',ItemConfiguration.get().getString("SpecialItems.KBBow.name")));
        kbBowMeta.spigot().setUnbreakable(true);
        kbBowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, ItemConfiguration.get().getInt("SpecialItems.KBBow.kb-level"), true);
        kbBowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        kbBowMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        kbBow.setItemMeta(kbBowMeta);
        return kbBow;
    }
    public ItemStack kbbowArrow() {
            ItemStack kbArrow = new ItemStack(Material.ARROW);
            ItemMeta kbArrowMeta = kbArrow.getItemMeta();
            kbArrowMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',ItemConfiguration.get().getString("SpecialItems.KBArrow.name")));
            kbArrow.setItemMeta(kbArrowMeta);
            return kbArrow;
        }

    public ItemStack BuildingBlock() {
            ItemStack buildingBlock = new ItemStack(Material.WOOL);
            ItemMeta buildingBlockMeta = buildingBlock.getItemMeta();
            buildingBlockMeta.setDisplayName(ChatColor.WHITE + "Building Block");
            buildingBlock.setItemMeta(buildingBlockMeta);
            return  buildingBlock;
        }


    public ItemStack JumpPlate() {
            ItemStack jumpPlate = new ItemStack(Material.GOLD_PLATE);
            ItemMeta jumpPlateMeta = jumpPlate.getItemMeta();
            jumpPlateMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',ItemConfiguration.get().getString("SpecialItems.JumpPlate.name")));
            jumpPlate.setItemMeta(jumpPlateMeta);
            return jumpPlate;
    }
    public ItemStack EnderPearl() {
            ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL);
            ItemMeta enderpearlmeta = enderpearl.getItemMeta();
            enderpearlmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',ItemConfiguration.get().getString("SpecialItems.EnderPearl.name")));
            enderpearl.setItemMeta(enderpearlmeta);
            return enderpearl;
    }


    @EventHandler
    public void onEndermiteSpawn(CreatureSpawnEvent e) {
        Entity endermite = e.getEntity();
        if (endermite instanceof Endermite) e.setCancelled(true);
        }
}
