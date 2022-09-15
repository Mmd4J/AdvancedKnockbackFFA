package me.gameisntover.knockbackffa.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class LightButtonManager {
    public static LightButton createButton(ItemStack item, Consumer<InventoryClickEvent> eventAction) {
        return new LightButton(item) {
            @Override
            public void onClick(InventoryClickEvent e) {
                eventAction.accept(e);
            }
        };
    }
}
