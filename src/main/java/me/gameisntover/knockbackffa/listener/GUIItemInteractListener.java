package me.gameisntover.knockbackffa.listener;

import me.gameisntover.knockbackffa.gui.Items;
import me.gameisntover.knockbackffa.gui.LightGUI;
import me.gameisntover.knockbackffa.gui.guis.cosmetic.CosmeticMenuGUI;
import me.gameisntover.knockbackffa.gui.guis.cosmetic.ShopMenuGUI;
import me.gameisntover.knockbackffa.gui.guis.kit.KitsMenuGUI;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIItemInteractListener implements Listener {
    @EventHandler
    public void onPlayerItemInteract(PlayerInteractEvent e) {
        Knocker knocker = Knocker.getKnocker(e.getPlayer().getUniqueId());
        if (e.getItem() == null) return;
        ItemStack item = e.getItem();
        if (e.getItem().getItemMeta() == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        Player player = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                if (Items.KITS_MENU.getItem().equals(item)) {
                    e.setCancelled(true);
                    LightGUI cosmeticMenu = new CosmeticMenuGUI(knocker);
                    knocker.openGUI(cosmeticMenu);
                }
                if (Items.SHOP_MENU.getItem().equals(item)) {
                    e.setCancelled(true);
                    LightGUI shopMenu = new ShopMenuGUI(knocker);
                    knocker.openGUI(shopMenu);
                }
                if (Items.KITS_MENU.getItem().equals(item)) {
                    e.setCancelled(true);
                    LightGUI kitsMenu = new KitsMenuGUI(knocker);
                    knocker.openGUI(kitsMenu);
                }
            }
        }

    }
}
