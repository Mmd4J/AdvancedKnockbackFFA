package me.gameisntover.knockbackffa.listener;

import me.gameisntover.knockbackffa.KnockbackFFA;
import me.gameisntover.knockbackffa.arena.ArenaConfiguration;
import me.gameisntover.knockbackffa.arena.Cuboid;
import me.gameisntover.knockbackffa.configurations.Messages;
import me.gameisntover.knockbackffa.util.KBFFAKit;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class GameRulesListener implements Listener {
    @EventHandler
    public void onPlayerItemPickup(PlayerPickupItemEvent e) {
        Knocker knocker = Knocker.getKnocker(e.getPlayer().getUniqueId());
        if (knocker.isInGame()) e.setCancelled(true);

    }

    @EventHandler
    public void playerChatFormat(AsyncPlayerChatEvent e) {
        Knocker knocker = Knocker.getKnocker(e.getPlayer().getUniqueId());
        if (knocker.isInGame())
            e.setFormat(Messages.CHAT_FORMAT.toString().replace("%player%", knocker.getPlayer().getName()).replace("%message%", e.getMessage()));

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        List<String> voids = ArenaConfiguration.get().getStringList("registered-voids");
        for (String vd : voids) {
            if (ArenaConfiguration.get().getString("voids." + vd + ".pos1.world") != null) {
                double x1 = ArenaConfiguration.get().getDouble("voids." + vd + ".pos1.x");
                double y1 = ArenaConfiguration.get().getDouble("voids." + vd + ".pos1.y");
                double z1 = ArenaConfiguration.get().getDouble("voids." + vd + ".pos1.z");
                double x2 = ArenaConfiguration.get().getDouble("voids." + vd + ".pos2.x");
                double y2 = ArenaConfiguration.get().getDouble("voids." + vd + ".pos2.y");
                double z2 = ArenaConfiguration.get().getDouble("voids." + vd + ".pos2.z");
                World world = Bukkit.getWorld(ArenaConfiguration.get().getString("voids." + vd + ".pos1.world"));
                Location pos1 = new Location(world, x1, y1, z1);
                Location pos2 = new Location(world, x2, y2, z2);
                Cuboid bb = new Cuboid(pos1, pos2);
                if (bb.contains(player.getLocation())) {
                    Integer damage = ArenaConfiguration.get().getInt("voids." + vd + ".damage");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (player.isDead() || !bb.contains(player.getLocation()) || player.getWorld() != pos1.getWorld())
                                cancel();
                            else {
                                player.damage(damage);
                                player.setLastDamageCause(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.VOID, damage));
                            }
                        }
                    }.runTaskTimer(KnockbackFFA.getInstance(), 0, 20);

                }
            }
        }
    }

    @EventHandler
    public void onBowUse(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Knocker knocker = Knocker.getKnocker(e.getEntity().getUniqueId());
            Player player = (Player) e.getEntity().getShooter();
            if (!knocker.isInGame())  return;
                if (player.getInventory().getItemInHand().getType().equals(Material.BOW)) return;
                    player.sendMessage(Messages.BOW_USE.toString());
                    new BukkitRunnable() {
                        int timer = 10;
                        @Override
                        public void run() {
                            timer--;
                            if (timer == 10 || timer == 9 || timer == 8 || timer == 7 || timer == 6 || timer == 5 || timer == 4 || timer == 3 || timer == 2 || timer == 1) {
                                if (player.getInventory().contains(Material.ARROW) || !player.getInventory().contains(Material.BOW)) {
                                    cancel();
                                    timer = 10;
                                }
                            }
                            if (timer == 0) {
                                if (!player.getInventory().contains(Material.ARROW) && player.getInventory().contains(Material.BOW)) {
                                    KBFFAKit kitManager = new KBFFAKit();
                                    player.getInventory().addItem(kitManager.kbbowArrow());
                                    player.sendMessage(Messages.ARROW_GET.toString());
                                }
                                cancel();
                                timer = 10;
                            }
                        }
                    }.runTaskTimer(KnockbackFFA.getInstance(), 0, 20);
            }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (Knocker.getKnocker(e.getEntity().getUniqueId()).isInGame()) {
            if (e.getFoodLevel() != 20) e.setFoodLevel(20);
            e.setCancelled(true);
        }
    }
}
