package me.gameisntover.knockbackffa.listener;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.knockbackffa.arena.ArenaManager;
import me.gameisntover.knockbackffa.configurations.Messages;
import me.gameisntover.knockbackffa.configurations.ScoreboardConfiguration;
import me.gameisntover.knockbackffa.configurations.Sounds;
import me.gameisntover.knockbackffa.kit.KnockbackFFALegacy;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListeners implements Listener
{
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = Knocker.getKnocker(player.getUniqueId());
            if (knocker.isInGame())
                knocker.toggleScoreBoard(ScoreboardConfiguration.get().getBoolean("enabled"));


        if (KnockbackFFALegacy.getInstance().getConfig().getBoolean("joinmessage"))
            e.setJoinMessage(PlaceholderAPI.setPlaceholders(e.getPlayer(), Messages.JOIN_MESSAGE.toString()));

        if (KnockbackFFALegacy.getInstance().getConfig().getBoolean("joinsound"))
            Knocker.getKnocker(player.getUniqueId()).playSound(Sounds.PLAYER_JOIN.getSound(),1,1);

        if (ArenaManager.getEnabledArena()==null) knocker.setInGame(false);
         else {
            if (KnockbackFFALegacy.BungeeMode()) {
                knocker.teleportPlayerToArena();
                player.getInventory().clear();
                knocker.giveLobbyItems();

            } else if (!knocker.isInGame()) knocker.leaveCurrentArena();
            knocker.setInGame(KnockbackFFALegacy.BungeeMode());
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        Knocker knocker = Knocker.getKnocker(e.getPlayer().getUniqueId());
        if (KnockbackFFALegacy.getInstance().getConfig().getBoolean("leavemessage")) e.setQuitMessage(PlaceholderAPI.setPlaceholders(e.getPlayer(), Messages.LEAVE_MESSAGE.toString()));
        knocker.toggleScoreBoard(false);
        knocker.setInGame(false);

    }
}
