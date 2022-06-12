package me.gameisntover.knockbackffa.multipleversion;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public enum KnockMaterial {
    WOOL("WOOL", "WHITE_WOOL");
    private final String legacy;
    private final String nonLegacy;

    KnockMaterial(String legacy, String nonLegacy) {
        this.legacy = legacy;
        this.nonLegacy = nonLegacy;
    }

    public Material toMaterial() {
        if (Bukkit.getServer().getVersion().contains("1.8")) return Material.getMaterial(legacy);
        else if (Bukkit.getServer().getVersion().contains("1.9")) return Material.getMaterial(legacy);
        else if (Bukkit.getServer().getVersion().contains("1.10")) return Material.getMaterial(legacy);
        else if (Bukkit.getServer().getVersion().contains("1.11")) return Material.getMaterial(legacy);
        else if (Bukkit.getServer().getVersion().contains("1.12")) return Material.getMaterial(legacy);
        else if (Bukkit.getServer().getVersion().contains("1.13")) return Material.getMaterial(nonLegacy);
        else if (Bukkit.getServer().getVersion().contains("1.14")) return Material.getMaterial(nonLegacy);
        else if (Bukkit.getServer().getVersion().contains("1.15")) return Material.getMaterial(nonLegacy);
        else if (Bukkit.getServer().getVersion().contains("1.16")) return Material.getMaterial(nonLegacy);
        else if (Bukkit.getServer().getVersion().contains("1.17")) return Material.getMaterial(nonLegacy);
        else if (Bukkit.getServer().getVersion().contains("1.18")) return Material.getMaterial(nonLegacy);
        else if (Bukkit.getServer().getVersion().contains("1.19")) return Material.getMaterial(nonLegacy);
        else return Material.getMaterial(nonLegacy);
    }
}
