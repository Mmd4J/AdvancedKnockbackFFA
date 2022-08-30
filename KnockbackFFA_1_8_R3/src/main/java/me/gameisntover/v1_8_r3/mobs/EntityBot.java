package me.gameisntover.v1_8_r3.mobs;

import net.minecraft.server.v1_8_R3.EntityZombie;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class EntityBot extends EntityZombie {

    public EntityBot(Location loc,String name) {
        super(((CraftWorld)loc.getWorld()).getHandle());
        setCustomName(name);
        setCustomNameVisible(true);
    }

}
