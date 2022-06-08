package me.gameisntover.knockbackffa.commands.knockcommands.arena;

import me.gameisntover.knockbackffa.util.Knocker;
import me.gameisntover.knockbackffa.arena.VoidChunkGenerator;
import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

@KFCommand(name = "kbffaworldcreate",syntax = "/kbffaworldcreate <name>", permissionDefault = PermissionDefault.OP, description = "creates a world for player")
public class CreateWorldCommand extends KnockCommand {
    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        return null;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        if (args.length > 0) {
            WorldCreator wc = new WorldCreator(args[0]);
            wc.generateStructures(false);
            wc.generator(new VoidChunkGenerator());
            wc.createWorld();
            World world = Bukkit.getWorld(args[0]);
            assert world != null;
            Block stone = world.getSpawnLocation().getBlock();
            if (stone.getType() == Material.AIR) {
                stone.setType(Material.STONE);
            }
            knocker.sendMessageWithPrefix("World " + args[0] + " has been loaded");
        } else knocker.sendMessageWithPrefix("You're using this command in a wrong way! " + getUsage());
    }
}
