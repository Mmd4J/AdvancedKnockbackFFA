package me.gameisntover.knockbackffa.listener;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.knockbackffa.KnockbackFFA;
import me.gameisntover.knockbackffa.bukkitevents.PlayerKillEvent;
import me.gameisntover.knockbackffa.configurations.Messages;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeathListener implements Listener {
    Map<Entity, Integer> killStreak = new HashMap<>();
    Map<Entity, Entity> killer = new HashMap<>();

    @EventHandler
    public void playerDamageCheck(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        Knocker knocker = Knocker.getKnocker(player.getUniqueId());
        if (knocker.isInMainLobby()) e.setCancelled(true);
        if (!knocker.isInGame()) return;
        if (!knocker.isInArena()) e.setCancelled(true);
        else {
            if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) e.setDamage(6);
            else if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) e.setCancelled(true);
            else e.setDamage(0);
        }
    }

    @EventHandler
    public void checkDamagerFinalDamage(EntityDamageByEntityEvent e) {
        Entity player = e.getEntity();
        Entity damager = e.getDamager();
        Knocker knocker = Knocker.getKnocker(player.getUniqueId());
        if (player.getType().equals(EntityType.PLAYER)) {
            if (knocker.isInGame()) {
                if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                    if (damager instanceof Arrow) {
                        Arrow arrow = (Arrow) damager;
                        if (arrow.getShooter() instanceof Player) {
                            Player shooter = (Player) arrow.getShooter();
                            killer.put(player, shooter);
                        }
                    } else if (damager instanceof Player) killer.put(player, damager);

                }
            }
        }
    }

    @EventHandler
    public void playerDeathByVoid(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Entity damager = killer.get(player);
        Knocker knocker = Knocker.getKnocker(player.getUniqueId());
        killer.remove(player);
        knocker.setInArena(false);
        PlayerKillEvent ev = new PlayerKillEvent(Knocker.getKnocker(damager.getUniqueId()),knocker);
        Bukkit.getPluginManager().callEvent(ev);
        if (knocker.isInGame()) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                player.spigot().respawn();
                knocker.giveLobbyItems();
                knocker.teleportPlayerToArena();
                cancel();
            }
        }.runTaskTimer(KnockbackFFA.getInstance(), 0, 1);
        World world = player.getWorld();
        List<Entity> entList = world.getEntities();
        for (Entity current : entList)
            if (current instanceof Item) current.remove();
        killStreak.put(player, 0);
        knocker.setDeaths(knocker.getDeaths()+1);
        if (damager != null && damager != player) {
            Knocker damageKnocker = Knocker.getKnocker(damager.getUniqueId());
            knocker.loadCosmetic(damageKnocker.getSelectedCosmetic());
            float prize = KnockbackFFA.getInstance().getConfig().getInt("killprize");
            damager.sendMessage(Messages.PRIZE.toString().replace("%prize%", prize + ""));
            damageKnocker.addBalance(prize);
            damageKnocker.setKills(damageKnocker.getKills() + 1);
            killStreak.merge(damager, 1, Integer::sum);
            if (killStreak.get(damager) > damageKnocker.getMaxks()) {
                damageKnocker.sendMessage(Messages.KILLSTREAK_RECORD.toString().replace("%killstreak%", damageKnocker.getMaxks() + ""));
                damageKnocker.setMaxks(killStreak.get(damager));
            }
            e.setDeathMessage(PlaceholderAPI.setPlaceholders(e.getEntity(), Messages.DEATH_MESSAGE.toString().replace("%killer%", damager.getName())));
        } else {
            player.sendMessage(Messages.SUICIDE.toString());
            e.setDeathMessage(Messages.FELL_VOID_MESSAGE.toString().replace("%player_name%", player.getName()));
        }
    }
}
