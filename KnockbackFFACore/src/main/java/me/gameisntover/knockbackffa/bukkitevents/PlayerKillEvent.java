package me.gameisntover.knockbackffa.bukkitevents;

import me.gameisntover.knockbackffa.arena.Arena;
import me.gameisntover.knockbackffa.arena.ArenaManager;
import me.gameisntover.knockbackffa.player.Knocker;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerKillEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    public Arena arena;
    public Knocker killer;
    public Knocker knocker;

    public PlayerKillEvent(Knocker killer,Knocker killed) {
        super(killer.getPlayer());
        this.killer = killer;
        knocker = killed;
        arena = ArenaManager.getEnabledArena();
    }

    public Knocker getKnocker() {
        return knocker;
    }

    public Knocker getKiller() {
        return killer;
    }

    public Arena getArena() {
        return arena;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
