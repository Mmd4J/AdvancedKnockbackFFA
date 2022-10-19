package me.gameisntover.knockbackffa.bukkitevents;

import me.gameisntover.knockbackffa.entity.NPCVillager;
import me.gameisntover.knockbackffa.player.Knocker;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerInteractAtVillagerEvent extends Event implements Cancellable {
    private boolean cancelled;
    private HandlerList handlerList = new HandlerList();
    private Knocker knocker;

    private NPCVillager villager;

    public PlayerInteractAtVillagerEvent(Knocker knocker, NPCVillager villager) {
        this.knocker = knocker;
        this.villager = villager;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public Knocker getKnocker() {
        return knocker;
    }

    public NPCVillager getVillager() {
        return villager;
    }
}
