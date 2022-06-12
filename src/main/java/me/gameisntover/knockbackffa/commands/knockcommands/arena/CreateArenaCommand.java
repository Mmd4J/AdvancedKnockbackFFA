package me.gameisntover.knockbackffa.commands.knockcommands.arena;

import me.gameisntover.knockbackffa.arena.*;
import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

@KFCommand(name = "kbffacreatearena",
        description = "a command for creating arena",
        syntax = "/kbffacreatearena <name>",
        permissionDefault = PermissionDefault.OP)
public class CreateArenaCommand extends KnockCommand {
    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        return null;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        if (args.length == 0)
            knocker.sendMessageWithPrefix("You must specify a name for the arena!");
        else if (args.length == 1) {
            if (knocker.getPositionA() != null && knocker.getPositionB() != null) {
                Location loc1 = knocker.getPositionA();
                Location loc2 = knocker.getPositionB();
                Arena arena = ArenaManager.create(args[0], loc1, loc2, knocker.getLocation());
                List<String> blocks = new ArrayList<>();
                List<String> locations = new ArrayList<>();
                Cuboid region = new Cuboid(loc1, loc2);
                for (Block block : region.getBlocks()) {
                    blocks.add(block.getType().name());
                    locations.add(block.getLocation().toString());
                }
                arena.getConfig().set("blocks", blocks);
                arena.save();
                if (ArenaManager.getfolder().list().length == 1) {
                    ArenaManager.setEnabledArena(args[0]);
                    ArenaConfiguration.save();
                }
                ArenaCreateEvent event = new ArenaCreateEvent(knocker, arena);
                Bukkit.getPluginManager().callEvent(event);
                knocker.sendMessageWithPrefix("Arena " + args[0] + " has been created!");
            } else
                knocker.sendMessageWithPrefix("Before using this command please consider selecting two positions with wand! /knockbackffa:kbffawand");

        }
    }
}
