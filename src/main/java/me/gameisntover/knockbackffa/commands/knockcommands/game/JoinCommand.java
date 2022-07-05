package me.gameisntover.knockbackffa.commands.knockcommands.game;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.knockbackffa.arena.ArenaManager;
import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.configurations.Messages;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

@KFCommand(name = "kbffajoin", syntax = "/kbffajoin", description = "Joins the game", permissionDefault = PermissionDefault.TRUE)
public class JoinCommand extends KnockCommand {

    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        return null;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        Player p = knocker.getPlayer();
        if (knocker.isInGame())
            p.sendMessage(Messages.ALREADY_IN_GAME.toString());
        else {
            if (ArenaManager.getEnabledArena() == null)
                knocker.getPlayer().sendMessage(Messages.NO_READY_ARENA.toString());
            else {
                String joinText = Messages.ARENA_JOIN.toString();
                joinText = PlaceholderAPI.setPlaceholders(p, joinText);
                p.sendMessage(joinText);
                knocker.getPlayer().openInventory(p.getInventory());
                p.getInventory().clear();
                p.setFoodLevel(20);
                knocker.giveLobbyItems();
                knocker.toggleScoreBoard(true);
                knocker.setInGame(true);
            }
            knocker.teleportPlayerToArena();
        }
    }
}
