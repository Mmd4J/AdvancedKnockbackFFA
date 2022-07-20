package me.gameisntover.knockbackffa.arena;

import me.gameisntover.knockbackffa.kit.KnockKit;
import me.gameisntover.knockbackffa.multipleversion.KnockMaterial;
import me.gameisntover.knockbackffa.util.Items;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class ArenaSettings implements Listener {
    @EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent e) {
                e.setCancelled(!e.getPlayer().isOp());
        }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        e.setCancelled(!e.getPlayer().isOp());
    }

    @EventHandler
    public void onPlayerGoesToArena(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = Knocker.getKnocker(e.getPlayer().getUniqueId());
        if (!knocker.isInGame()) return;
        if (ArenaManager.getEnabledArena() != null) {
            Arena arena = ArenaManager.getEnabledArena();
            knocker.setInGame(true);
            if (!arena.contains(player.getLocation())) return;
            if (knocker.getSelectedKit() == null) {
                List<KnockKit> ownedKits = knocker.getOwnedKits();
                if (!ownedKits.contains(KnockKit.getFromString("default"))) {
                    ownedKits.add(KnockKit.getFromString("default"));
                    knocker.setOwnedKits(ownedKits);
                }
                knocker.setSelectedKit(KnockKit.getFromString("default"));
            }
            KnockKit kit = knocker.getSelectedKit();
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.getType() != KnockMaterial.AIR.toMaterial() && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null) {
                    if (Items.COSMETICS_MENU.getItem().equals(item) || Items.SHOP_MENU.getItem().equals(item) || Items.KITS_MENU.getItem().equals(item)) {
                        player.getInventory().clear();
                        knocker.giveKit(kit);
                        break;
                    }
                }
            }
        } else knocker.setInArena(false);
    }
}
