package me.gameisntover.knockbackffa.commands.knockcommands.arena;

import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

@KFCommand(name = "gotoworld", description = "teleports player to a world that it is required", permissionDefault = PermissionDefault.OP, syntax = "/gotoworld <worldname>")
public class GotoWorldCommand extends KnockCommand {
    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        List<String> worldList = Bukkit.getWorlds().stream().map(world -> world.getName()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        return worldList;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        if (args.length > 0) {
            World world = Bukkit.getWorld(args[0]);
            if (world != null) knocker.getPlayer().teleport(world.getSpawnLocation());
            else knocker.sendMessage("&cWorld does not exist!");
        }
    }
}
