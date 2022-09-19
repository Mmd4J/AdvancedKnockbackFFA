package me.gameisntover.knockbackffa.entity;

import me.gameisntover.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class GUIVillager implements Listener {
    public Villager villager;
    private ArmorStand vehicle;
    public GUIVillager(String name, Location location){
        villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        villager.setProfession(Villager.Profession.BLACKSMITH);
        Bukkit.getPluginManager().registerEvents(this, KnockbackFFA.getInstance());
        villager.setCustomName(name);
        villager.setCustomNameVisible(true);
        villager.setCanPickupItems(false);
        villager.setAdult();
        vehicle = (ArmorStand) location.getWorld().spawnEntity(location,EntityType.ARMOR_STAND);
        vehicle.setPassenger(villager);
        vehicle.setSmall(true);

    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e){
        if (e.getEntity().equals(vehicle) || e.getEntity().equals(villager))
            e.setCancelled(true);
    }
}
