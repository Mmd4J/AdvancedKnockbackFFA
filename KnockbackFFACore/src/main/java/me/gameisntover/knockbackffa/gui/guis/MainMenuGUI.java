package me.gameisntover.knockbackffa.gui.guis;

import me.gameisntover.knockbackffa.gui.LightButton;
import me.gameisntover.knockbackffa.gui.LightGUI;
import me.gameisntover.knockbackffa.gui.guis.cosmetic.CosmeticMenuGUI;
import me.gameisntover.knockbackffa.gui.guis.cosmetic.ShopMenuGUI;
import me.gameisntover.knockbackffa.gui.guis.kit.KitsMenuGUI;
import me.gameisntover.knockbackffa.player.Knocker;
import me.gameisntover.knockbackffa.util.Items;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MainMenuGUI extends LightGUI {
    public MainMenuGUI() {
        super("KnockbackFFA",5);
        setButton(new LightButton(Items.COSMETICS_MENU.getItem()) {
            @Override
            public void onClick(InventoryClickEvent e) {
                Knocker knocker = Knocker.getKnocker(e.getWhoClicked().getUniqueId());
                e.setCancelled(true);
                LightGUI cosmeticMenu = new CosmeticMenuGUI(knocker);
                knocker.openGUI(cosmeticMenu);
            }
        },11);
        setButton(new LightButton(Items.SHOP_MENU.getItem()) {
            @Override
            public void onClick(InventoryClickEvent e) {
                Knocker knocker = Knocker.getKnocker(e.getWhoClicked().getUniqueId());
                e.setCancelled(true);
                LightGUI shopMenu = new ShopMenuGUI(knocker);
                knocker.openGUI(shopMenu);
            }
        },12);
        setButton(new LightButton(Items.KITS_MENU.getItem()) {
            @Override
            public void onClick(InventoryClickEvent e) {
                Knocker knocker = Knocker.getKnocker(e.getWhoClicked().getUniqueId());
                e.setCancelled(true);
                LightGUI kitsMenu = new KitsMenuGUI(knocker);
                knocker.openGUI(kitsMenu);
            }
        },13);
    }
}
