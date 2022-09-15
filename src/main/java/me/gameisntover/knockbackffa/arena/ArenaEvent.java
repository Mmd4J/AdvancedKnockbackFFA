package me.gameisntover.knockbackffa.arena;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class ArenaEvent extends Event implements Cancellable {

    public HandlerList handlerList = new HandlerList();

    public abstract Arena getArena();

    public HandlerList getHandlerList() {
        return handlerList;
    }
}
