package me.gameisntover.knockbackffa.commands.knockcommands.arena;

import me.gameisntover.knockbackffa.arena.ArenaManager;
import me.gameisntover.knockbackffa.arena.EditArenaGUI;
import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.gui.LightGUI;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;

@KFCommand(name = "kbffaeditarena", description = "edits arena", permissionDefault = PermissionDefault.OP, syntax = "/kbffaeditarena <arenaname>")
public class EditArenaCommand extends KnockCommand {

    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        List<String> arenaList = Arrays.asList(Arrays.stream(Arrays.stream(ArenaManager.getfolder().list()).map(s -> {
            return s.replace(".yml", "");
        }).toArray()).toArray(String[]::new));
        return arenaList;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        if (args.length == 1) {
            List<String> arenaList = Arrays.asList(ArenaManager.getfolder().list());
            if (!arenaList.contains(args[0] + ".yml")) knocker.sendMessage("&cThat arena name does not exist!");
            else {
                knocker.sendMessage("&aYou are now editing " + args[0]);
                LightGUI gui = new EditArenaGUI(args[0]);
                knocker.openGUI(gui);
            }
        } else knocker.sendMessage("&cCommand Arguements missing or is invalid /editarena arenaname");

    }
}
