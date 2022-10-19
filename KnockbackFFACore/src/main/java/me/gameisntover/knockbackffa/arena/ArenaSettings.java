package me.gameisntover.knockbackffa.arena;

import me.gameisntover.knockbackffa.kit.KnockKit;
import me.gameisntover.knockbackffa.player.Knocker;
import me.gameisntover.knockbackffa.util.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
        if (knocker.isInGame()) {
            if (ArenaManager.getEnabledArena() != null) {
                Arena arena = ArenaManager.getEnabledArena();
                knocker.setInArena(true);
                if (!arena.contains(e.getTo())) return;
                if (knocker.getSelectedKit() == null) {
                    List<KnockKit> ownedKits = knocker.getOwnedKits();
                    if (!ownedKits.contains(KnockKit.getFromString("default"))) {
                        ownedKits.add(KnockKit.getFromString("default"));
                        knocker.setOwnedKits(ownedKits);
                    }
                    knocker.setSelectedKit(KnockKit.getFromString("default"));
                }
                KnockKit kit = knocker.getSelectedKit();
                if (!knocker.getInventory().contains(Items.KB_STICK.item)) knocker.giveKit(kit);
            } else knocker.setInArena(false);
        }
    }
}