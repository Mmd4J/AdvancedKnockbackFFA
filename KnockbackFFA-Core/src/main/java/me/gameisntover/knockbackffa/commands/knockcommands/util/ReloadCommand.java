package me.gameisntover.knockbackffa.commands.knockcommands.util;

import me.gameisntover.knockbackffa.KnockbackFFA;
import me.gameisntover.knockbackffa.arena.ArenaConfiguration;
import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.configurations.ItemConfiguration;
import me.gameisntover.knockbackffa.configurations.ScoreboardConfiguration;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

@KFCommand(name = "kbffareload", syntax = "/kbffareload", permissionDefault = PermissionDefault.OP, description = "reloads the configs")
public class ReloadCommand extends KnockCommand {

    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        return null;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        KnockbackFFA.getInstance().reloadConfig();
        ArenaConfiguration.reload();
        ScoreboardConfiguration.reload();
        ItemConfiguration.reload();
        knocker.sendMessageWithPrefix("Configs are reloaded!");
    }
}
