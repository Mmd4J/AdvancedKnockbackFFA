package me.gameisntover.knockbackffa_1_8_r3.mobs;

import net.minecraft.server.v1_8_R3.EntityVillager;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.util.UUID;

public class ShopVillager extends EntityVillager {
    protected UUID uuid;
    public ShopVillager(String name,Location loc){
        super(((CraftWorld) loc.getWorld()).getHandle());

    }

    @Override
    public void b(boolean flag) {
        super.b(flag);
    }

    public UUID getUniqueID() {
        return uuid;
    }



    public Location getLocation() {
        return new Location(world.getWorld(),locX,locY,locZ);
    }


    public void teleport(Location newLoc){

    }
}
