package me.gameisntover.knockbackffa.entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.mojang.authlib.GameProfile;
import me.gameisntover.knockbackffa.nms.NMSUtil;
import org.bukkit.Location;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class NPC {
    public Object player;
    public Object craftWorld;
    public Object minecraftServer;
    public Object playerInteractManager;
    public NPC(Location loc, UUID uuid,String name){
        try {
            craftWorld = loc.getWorld().getClass().cast(NMSUtil.getNMSClass("CraftWorld"));
            playerInteractManager = NMSUtil.getNMSClass("PlayerInteractManager").getConstructors()[0].newInstance(NMSUtil.getNMSMethod(craftWorld,"getHandle"));
            player = NMSUtil.getNMSClass("EntityPlayer").getConstructors()[0].newInstance(minecraftServer, NMSUtil.getNMSMethod(craftWorld,"getHandle") ,new GameProfile(uuid,name), playerInteractManager);
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
