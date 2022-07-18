package me.gameisntover.knockbackffa.block;

import org.bukkit.Material;
import org.bukkit.block.BlockState;

public interface KnockBlock {
    BlockState getState();
    Material getType();
}
