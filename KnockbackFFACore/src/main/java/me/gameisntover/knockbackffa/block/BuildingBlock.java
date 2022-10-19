package me.gameisntover.knockbackffa.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.List;

public class BuildingBlock implements KnockBlock{
    private Block block;
    protected BuildingBlock(Block block, List<String> blockNames){
    this.block = block;
        // TODO: 7/17/2022
    }


    @Override
    public BlockState getState() {
        return block.getState();
    }

    @Override
    public Material getType() {
        return block.getType();
    }
}
