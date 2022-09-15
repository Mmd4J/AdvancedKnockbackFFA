package me.gameisntover.knockbackffa.commands.knockcommands.arena;

import me.gameisntover.knockbackffa.arena.*;
import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.gui.LightGUI;
import me.gameisntover.knockbackffa.gui.guis.arena.EditArenaGUI;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.permissions.PermissionDefault;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@KFCommand(name = "kbffaarena",permissionDefault = PermissionDefault.OP,description = "arena commands",syntax = "/kbffaarena <edit,create,delete> <name> | /kbffaarena <pos1,pos2>")
public class ArenaCommands extends KnockCommand {
    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        if (args.length == 0)
        return Arrays.asList("create","edit","delete","pos1","pos2");
        else if (ArenaManager.folder.exists()&& ArenaManager.folder.list().length != 0)
            return Arrays.stream(ArenaManager.folder.list()).map(s -> s = s.replace(".yml","")).collect(Collectors.toList());
        else return Collections.singletonList("no arena");
    }

    @Override
    public void run(String[] args, Knocker knocker) {
    switch (args.length){
        case 0:
            knocker.sendMessageWithPrefix("You must use some more arguements "+getUsage());
            break;
        case 1:
            if (args[0].equalsIgnoreCase("pos1")){
                knocker.sendMessage("&a Position 1 has been set!");
                knocker.setPositionA(knocker.getLocation());
            }
            else if (args[0].equalsIgnoreCase("pos2")){
                knocker.sendMessage("&a Position 2 has been set!");
                knocker.setPositionB(knocker.getLocation());
        }
            break;
        case 2:
            switch (args[0]){
                case "create":
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
                        if (ArenaManager.folder.list().length == 1) {
                            ArenaManager.setEnabledArena(args[0]);
                            ArenaConfiguration.save();
                        }
                        ArenaCreateEvent event = new ArenaCreateEvent(knocker, arena);
                        Bukkit.getPluginManager().callEvent(event);
                        knocker.sendMessageWithPrefix("Arena " + args[0] + " has been created!");
                    } else knocker.sendMessageWithPrefix("Before using this command please consider selecting two positions with wand! /knockbackffa:kbffawand");
                    break;
                case "delete":
                    ArenaManager.ARENA_MAP.remove(args[1]);
                    File file = new File(ArenaManager.folder,args[1] + ".yml");
                    file.delete();
                    if (ArenaManager.getEnabledArena().equals(ArenaManager.ARENA_MAP.get(args[1]))){
                        ArenaManager.setEnabledArena(ArenaManager.randomArena());
                    }
                    break;
                case "edit":
                    List<String> arenaList = Arrays.asList(ArenaManager.folder.list());
                    if (!arenaList.contains(args[0] + ".yml")) knocker.sendMessage("&cThat arena name does not exist!");
                    else {
                        knocker.sendMessage("&aYou are now editing " + args[0]);
                        LightGUI gui = new EditArenaGUI(args[0]);
                        knocker.openGUI(gui);
                    }
                    break;
                default:
                    knocker.sendMessageWithPrefix("Your command arg " +args[0]+ " is invalid");
                    break;
            }
            break;
    }
    }
}
