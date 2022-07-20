package me.gameisntover.knockbackffa.multipleversion;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

@SuppressWarnings("deprecation")
public enum KnockMaterial{

    WHITE_WOOL( "WHITE_WOOL", new MaterialData(Material.WOOL,DyeColor.WHITE.getData())),
    YELLOW_WOOL( "YELLOW_WOOL", new MaterialData(Material.WOOL,DyeColor.YELLOW.getData())),
    ORANGE_WOOL( "ORANGE_WOOL", new MaterialData(Material.WOOL,DyeColor.ORANGE.getData())),
    RED_WOOL("RED_WOOL",new MaterialData(Material.WOOL,DyeColor.RED.getData())),
    BLAZE_ROD("BLAZE_ROD",new MaterialData(Material.BLAZE_ROD)),
    IRON_INGOT("IRON_INGOT",new MaterialData(Material.IRON_INGOT)),
    EMERALD("EMERALD",new MaterialData(Material.EMERALD)),
    DIAMOND("DIAMOND",new MaterialData(Material.DIAMOND)),
    GOLD_INGOT("GOLD_INGOT",new MaterialData(Material.GOLD_INGOT)),
    BARRIER("BARRIER",new MaterialData(Material.BARRIER)),
    CHEST("CHEST",new MaterialData(Material.CHEST)),
    ENDER_CHEST("ENDER_CHEST",new MaterialData(Material.ENDER_CHEST)),
    DIAMOND_SWORD("DIAMOND_SWORD",new MaterialData(Material.DIAMOND_SWORD)),
    GOLDEN_SWORD("GOLDEN_SWORD",new MaterialData(Material.GOLD_SWORD)),
    IRON_SWORD("IRON_SWORD",new MaterialData(Material.IRON_SWORD)),
    WOODEN_SWORD("WOODEN_SWORD",new MaterialData(Material.WOOD_SWORD)),
    STICK("STICK",new MaterialData(Material.STICK)),
    GOLD_PLATE("LIGHT_WEIGHTED_PRESSURE_PLATE",new MaterialData(Material.GOLD_PLATE)),
    STONE_PLATE("STONE_PRESSURE_PLATE",new MaterialData(Material.STONE_PLATE)),
    IRON_PLATE("HEAVY_WEIGHTED_PRESSURE_PLATE",new MaterialData(Material.IRON_PLATE)),
    DIAMOND_AXE("DIAMOND_AXE",new MaterialData(Material.DIAMOND_AXE)),
    GOLDEN_AXE("GOLDEN_AXE",new MaterialData(Material.GOLD_AXE)),
    IRON_AXE("IRON_AXE",new MaterialData(Material.IRON_AXE)),
    WOODEN_AXE("WOODEN_AXE",new MaterialData(Material.WOOD_AXE)),
    AIR("AIR",new MaterialData(Material.AIR)),
    BOW("BOW",new MaterialData(Material.BOW)),
    ARROW("ARROW",new MaterialData(Material.ARROW)),
    ENDER_PEARL("ENDER_PEARL",new MaterialData(Material.ENDER_PEARL));
    public final MaterialData legacy;
    public final String nonLegacy;

    KnockMaterial(String nonLegacy, MaterialData legacy) {
        this.legacy = legacy;
        this.nonLegacy = nonLegacy;
    }
    public Material toMaterial(){
        if (Bukkit.getServer().getVersion().contains("1.8") || Bukkit.getServer().getVersion().contains("1.9") ||
                Bukkit.getServer().getVersion().contains("1.10") || Bukkit.getServer().getVersion().contains("1.11") ||
                Bukkit.getServer().getVersion().contains("1.12")) return legacy.toItemStack().getType();
        else return Material.getMaterial(nonLegacy);
    }
}
