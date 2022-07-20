package me.gameisntover.knockbackffa.multipleversion;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public enum KnockMaterial {
    WHITE_WOOL( "WHITE_WOOL", new MaterialData(Material.WOOL,DyeColor.WHITE.getData())),
    YELLOW_WOOL( "YELLOW_WOOL", new MaterialData(Material.WOOL,DyeColor.YELLOW.getData())),
    ORANGE_WOOL( "ORANGE_WOOL", new MaterialData(Material.WOOL,DyeColor.ORANGE.getData())),
    RED_WOOL("RED_WOOL",new MaterialData(Material.WOOL,DyeColor.RED.getData()));
    public final MaterialData legacy;
    public final String nonLegacy;

    KnockMaterial(String nonLegacy, MaterialData legacy) {
        this.legacy = legacy;
        this.nonLegacy = nonLegacy;
    }

}
