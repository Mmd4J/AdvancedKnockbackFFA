package me.gameisntover.knockbackffa.entity;

import lombok.SneakyThrows;
import me.gameisntover.knockbackffa.nms.NMSUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class NPCVillager{
    public Object npcVillager;
    public static NPCVillager villager;
    @SneakyThrows
    public NPCVillager(Location loc) {
        npcVillager = NMSUtil.getKMSClass("mobs.NPCVillager").getConstructors()[0].newInstance(loc,"Luke",UUID.randomUUID());
    villager = this;
    }
    @SneakyThrows
    public Entity getBukkitEntity(){
        return (Entity) npcVillager.getClass().getMethod("getBukkitEntity").invoke(npcVillager);
    }

    @SneakyThrows
    public void kill(){
        npcVillager.getClass().getMethod("setInvisible", Boolean.class).invoke(npcVillager,true);
        npcVillager.getClass().getMethod("die").invoke(npcVillager);
        villager = null;
    }

}
