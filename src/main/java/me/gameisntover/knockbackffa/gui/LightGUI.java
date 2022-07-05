package me.gameisntover.knockbackffa.gui;

import lombok.Getter;
import lombok.Setter;
import me.gameisntover.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
public class LightGUI implements Listener {
    private Map<Integer, LightButton> guiButtonMap = new HashMap<>();
    private Inventory inventory;
    private Consumer<InventoryOpenEvent> openEvent;
    private Consumer<InventoryCloseEvent> closeEvent;
    private boolean destroyOnClose = false;
    public LightGUI(String name, Integer slot) {
        inventory = Bukkit.createInventory(null, slot * 9, ChatColor.translateAlternateColorCodes('&', name));
        Bukkit.getPluginManager().registerEvents(this, KnockbackFFA.getInstance());
    }
    public void setOnDestroyClose(Boolean toggle){
    destroyOnClose = toggle;
    }
    public void addButton(LightButton button) {
        for (int i = 0; i < inventory.getMaxStackSize(); i++) {
            if (!guiButtonMap.containsKey(i)) {
                setButton(button,i);
                break;
            }
        }
    }

    public void setButton(LightButton button, int i) {
        guiButtonMap.put(i, button);
        inventory.setItem(i, button.getItem());
    }
    public void setButton(LightButton button, int x, int y) {
        setButton(button,x + y * 9 - 1);
    }
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (e.getInventory() != inventory) return;
        LightButton button = guiButtonMap.get(e.getSlot());
        button.onClick(e);
    }
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e){
        if (e.getInventory() == inventory && openEvent != null) openEvent.accept(e);
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        if (e.getInventory() == inventory ){
            if (closeEvent != null)
            closeEvent.accept(e);
            if (destroyOnClose) HandlerList.unregisterAll(this);
        }
    }
}
