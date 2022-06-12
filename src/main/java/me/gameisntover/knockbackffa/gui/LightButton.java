package me.gameisntover.knockbackffa.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public abstract class LightButton {
    private ItemStack item;

    public LightButton(ItemStack item) {
        this.item = item;

    }

    public abstract void onClick(InventoryClickEvent e);
}
