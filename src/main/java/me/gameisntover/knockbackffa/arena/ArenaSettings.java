package me.gameisntover.knockbackffa.arena;

import me.gameisntover.knockbackffa.KnockKit;
import me.gameisntover.knockbackffa.gui.Items;
import me.gameisntover.knockbackffa.kit.KitManager;
import me.gameisntover.knockbackffa.util.KBFFAKit;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.Material;
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
        Player player = e.getPlayer();
        Knocker knocker = Knocker.getKnocker(player.getUniqueId());
        if (knocker.isInArena()) {
            String[] arenaList = ArenaManager.getfolder().list();
                for (String arenaName : arenaList) {
                    Arena arena = ArenaManager.load(arenaName.replace(".yml", ""));
                    e.setCancelled(!arena.getConfig().getBoolean("block-break"));
            }
        } else if (knocker.isInMainLobby()) e.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = Knocker.getKnocker(player.getUniqueId());
        if (knocker.isInGame()) {
            String[] arenaList = ArenaManager.getfolder().list();
            assert arenaList != null;
            for (String arenaName : arenaList) {
                Arena arena = ArenaManager.load(arenaName.replace(".yml", ""));
                e.setCancelled(knocker.isInArena() && !arena.getConfig().getBoolean("item-drop"));
            }
        }
        }
    @EventHandler
    public void onPlayerGoesToArena(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = Knocker.getKnocker(e.getPlayer().getUniqueId());
        if (!knocker.isInGame()) return;
            if (ArenaManager.getEnabledArena()!=null) {
                Arena arena = ArenaManager.getEnabledArena();
                knocker.setInGame(true);
                if (!arena.contains(player.getLocation())) return;
                    if (knocker.get().getString("selected-kit") == null) {
                        List<String> ownedKits = knocker.get().getStringList("owned-kits");
                        if (ownedKits.contains("Default")) {
                            ownedKits.add("Default");
                            knocker.get().set("owned-kits", ownedKits);
                        }
                        knocker.get().set("selected-kit", "Default");
                    }
                    KnockKit kit = KitManager.load(knocker.get().getString("selected-kit"));
                    KBFFAKit kits = new KBFFAKit();
                    for (ItemStack item : player.getInventory().getContents()) {
                        if (item != null && item.getType() != Material.AIR && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null) {
                            if (Items.COSMETICS_MENU.getItem().equals(item) || Items.SHOP_MENU.getItem().equals(item) || Items.KITS_MENU.getItem().equals(item)) {
                                player.getInventory().clear();
                                knocker.giveKit(kit);
                                break;
                            }
                        }
                    }
            }else knocker.setInArena(false);
                }
        }
