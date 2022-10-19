package me.gameisntover.knockbackffa_v1_8_R3.mobs;


import net.minecraft.server.v1_8_R3.EntityVillager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;
import java.util.UUID;

public class NPCVillager extends EntityVillager {
    public NPCVillager(Location loc, String name, UUID uuid) {
        super(((CraftWorld) loc.getWorld()).getHandle());
        uniqueID = uuid;
        setPosition(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ());
        setCustomName(ChatColor.translateAlternateColorCodes('&',name));
        setCustomNameVisible(true);

        b(true);
        k(true);
        setInvisible(true);

        try {
            Field invulnerableField = net.minecraft.server.v1_8_R3.Entity.class.getDeclaredField("invulnerable");
            invulnerableField.setAccessible(true);
            invulnerableField.setBoolean(this, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);

    }

    public Location getLocation(){
        return getBukkitEntity().getLocation();
    }


}
