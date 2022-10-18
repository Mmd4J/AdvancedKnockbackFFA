package me.gameisntover.knockbackffa.bukkitevents;

import me.gameisntover.knockbackffa.arena.Arena;
import me.gameisntover.knockbackffa.player.Knocker;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerJoinArenaEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Arena arena;
    private final Knocker player;
    private boolean isCancelled;

    public PlayerJoinArenaEvent(Knocker player, Arena arena) {
        this.player = player;
        this.arena = arena;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Arena getArena() {
        return arena;
    }

    public Knocker getKnocker() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean arg0) {
        this.isCancelled = arg0;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
