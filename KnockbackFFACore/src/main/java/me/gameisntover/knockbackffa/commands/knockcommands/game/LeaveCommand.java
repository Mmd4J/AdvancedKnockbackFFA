package me.gameisntover.knockbackffa.commands.knockcommands.game;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.knockbackffa.KnockbackFFA;
import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.configurations.Messages;
import me.gameisntover.knockbackffa.player.Knocker;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

@KFCommand(name = "kbffaleave",syntax = "/kbffaleave",description = "leaves the game",permissionDefault = PermissionDefault.TRUE)
public class LeaveCommand extends KnockCommand {
    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        return null;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        if (!KnockbackFFA.BungeeMode() && knocker.isInGame()) {
            String leaveText = Messages.LEAVE_ARENA.toString();
            leaveText = PlaceholderAPI.setPlaceholders(knocker.getPlayer(), leaveText);
            knocker.sendMessage(leaveText);
            knocker.leaveCurrentArena();
            knocker.getPlayer().getInventory().clear();
            knocker.toggleScoreBoard(false);
            knocker.setInGame(false);
        } else knocker.sendMessage(Messages.CANNOTUSELEAVE.toString());
    }
}
