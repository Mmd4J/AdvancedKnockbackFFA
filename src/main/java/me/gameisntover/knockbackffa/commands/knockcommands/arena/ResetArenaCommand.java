package me.gameisntover.knockbackffa.commands.knockcommands.arena;

import me.gameisntover.knockbackffa.arena.ArenaManager;
import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.util.Knocker;
import me.gameisntover.knockbackffa.arena.Arena;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import org.bukkit.permissions.PermissionDefault;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
@KFCommand(name = "resetarena",permissionDefault = PermissionDefault.OP,syntax = "/resetarena <arenaname>",description = "resets the arena")
public class ResetArenaCommand extends KnockCommand {
    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        return Arrays.asList(Arrays.stream(Arrays.stream(Objects.requireNonNull(ArenaManager.getfolder().list())).map(s -> {
            return s.replace(".yml", "");
        }).toArray()).toArray(String[]::new));
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        if (args.length > 0){
            File file = new File(ArenaManager.getfolder() + File.separator + args[0] + ".yml");
            if (file.exists()) {
                Arena arena = ArenaManager.load(args[0]);
                arena.resetArena();
                knocker.sendMessageWithPrefix("Arena has been reset!");
            }else knocker.sendMessage("Arena does not exist");

        }
    }
}
