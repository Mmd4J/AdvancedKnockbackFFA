package me.gameisntover.knockbackffa.listener;

import me.gameisntover.knockbackffa.util.Items;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandListener implements Listener {
    @EventHandler
    public void wandSelection1(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = Knocker.getKnocker(e.getPlayer().getUniqueId());
        if (!player.getInventory().getItemInHand().equals(Items.WAND.getItem()))
            return;
        switch (e.getAction()) {
            case LEFT_CLICK_BLOCK:
                player.sendMessage("§a Position 1 has been set!");
                knocker.setPositionA(e.getClickedBlock().getLocation());
                e.setCancelled(true);
                break;
            case RIGHT_CLICK_BLOCK:
                player.sendMessage("§a Position 2 has been set!");
                knocker.setPositionB(e.getClickedBlock().getLocation());
                break;
        }
    }
}