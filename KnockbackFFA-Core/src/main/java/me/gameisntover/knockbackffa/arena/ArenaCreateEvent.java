package me.gameisntover.knockbackffa.arena;

import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class ArenaCreateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Arena arena;
    private final Knocker knocker;

    public ArenaCreateEvent(Knocker knocker, Arena arena) {
        this.knocker = knocker;
        this.arena = arena;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Arena getArena() {
        return arena;
    }

    public Knocker getKnocker() {
        return knocker;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
