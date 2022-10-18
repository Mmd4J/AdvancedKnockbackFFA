package me.gameisntover.knockbackffa_v1_8_R3.mobs;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.util.UUID;

public class EntityHuman extends EntityPlayer {
    public EntityHuman(Location loc, UUID uuid,String name, PlayerInteractManager playerinteractmanager) {
        super(MinecraftServer.getServer(),((CraftWorld)loc.getWorld()).getHandle() ,new GameProfile(uuid,name), new PlayerInteractManager(((CraftWorld) loc.getWorld()).getHandle()));

    }
}
