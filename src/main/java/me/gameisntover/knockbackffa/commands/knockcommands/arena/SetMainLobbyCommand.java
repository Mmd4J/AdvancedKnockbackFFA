package me.gameisntover.knockbackffa.commands.knockcommands.arena;

import me.gameisntover.knockbackffa.arena.ArenaConfiguration;
import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.Location;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

@KFCommand(name = "setmainlobby", description = "sets the mainlobby", syntax = "/setmainlobby", permissionDefault = PermissionDefault.OP)
public class SetMainLobbyCommand extends KnockCommand {
    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        return null;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        knocker.sendMessage("&aMain lobby spawn has been set");
        Location loc = knocker.getLocation();
        ArenaConfiguration.get().set("mainlobby.x", loc.getX());
        ArenaConfiguration.get().set("mainlobby.y", loc.getY());
        ArenaConfiguration.get().set("mainlobby.z", loc.getZ());
        String world = loc.getWorld().getName();
        ArenaConfiguration.get().set("mainlobby.world", world);
        ArenaConfiguration.save();
    }
}
