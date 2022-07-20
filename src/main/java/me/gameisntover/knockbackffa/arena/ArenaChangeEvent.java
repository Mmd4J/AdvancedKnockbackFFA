package me.gameisntover.knockbackffa.arena;

import org.bukkit.event.HandlerList;

public class ArenaChangeEvent extends ArenaEvent {
    private final Arena arena;
    private final Arena previousArena;
    private boolean cancelled;
    public ArenaChangeEvent(Arena arena,Arena previousArena){
        this.arena = arena;
        this.previousArena = previousArena;
        cancelled = false;
    }
    @Override
    public Arena getArena() {
        return arena;
    }

    public Arena getFrom(){
        return previousArena;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
