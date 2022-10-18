package me.gameisntover.knockbackffa.entity;

import lombok.SneakyThrows;
import me.gameisntover.knockbackffa.arena.Arena;
import me.gameisntover.knockbackffa.bukkitevents.ArenaChangeEvent;
import me.gameisntover.knockbackffa.nms.NMSUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class NPCVillager implements Listener {
    public Object npcVillager;

    @SneakyThrows
    public NPCVillager(Location loc) {
    npcVillager = NMSUtil.getKMSClass("mobs.NPCVillager").getConstructors()[0].newInstance(loc,"luke",UUID.randomUUID());

    }
    public Entity getBukkitEntity(){
        return (Entity) NMSUtil.getKMSMethod(npcVillager,"getBukkitEntity");
    }

    public void kill(){
        NMSUtil.getKMSMethod(npcVillager,"setInvisible",true);
        NMSUtil.getKMSMethod(npcVillager,"die");
    }
    @EventHandler
    public void onArenaChange(ArenaChangeEvent e){
        kill();
        Arena arena = e.getArena();
        NPCVillager villager = new NPCVillager(arena.getGUIVillagerSpawnPoint());

    }
}
