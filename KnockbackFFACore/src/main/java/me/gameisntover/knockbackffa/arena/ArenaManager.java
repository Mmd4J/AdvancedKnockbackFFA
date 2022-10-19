package me.gameisntover.knockbackffa.arena;

import me.gameisntover.knockbackffa.KnockbackFFA;
import me.gameisntover.knockbackffa.bukkitevents.ArenaChangeEvent;
import me.gameisntover.knockbackffa.configurations.Messages;
import me.gameisntover.knockbackffa.configurations.Sounds;
import me.gameisntover.knockbackffa.entity.NPCVillager;
import me.gameisntover.knockbackffa.player.Knocker;
import me.gameisntover.knockbackffa.util.Knocktils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class ArenaManager {

    public static final Map<String, Arena> ARENA_MAP = new HashMap<>();

    public static final File folder = new File(KnockbackFFA.getInstance().getDataFolder(), "arenas" + File.separator);

    /**
     * Changes the arena to another arena
     *
     * @param arena the arena
     */
    public static void changeArena(Arena arena) {
        ArenaChangeEvent changeEvent = new ArenaChangeEvent(arena, getEnabledArena());
        Bukkit.getPluginManager().callEvent(changeEvent);
        if (changeEvent.isCancelled()) return;
        String arenaName = arena.getName();
        setEnabledArena(arenaName);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            Knocker knocker = Knocker.getKnocker(p.getUniqueId());
            // null safety
            if(knocker == null) continue;
            if (knocker.isInGame()) {
                p.closeInventory();
                p.getInventory().clear();
                knocker.teleportPlayerToArena();
                p.playSound(p.getLocation(), Sounds.ARENACHANGE.getSound(), 1, 1);
                p.sendMessage(Knocktils.translateColors(Messages.ARENA_CHANGE.toString().replace("%arena%", arenaName)));
            }
        }
        if (NPCVillager.villager != null) NPCVillager.villager.kill();
        new NPCVillager(arena.getGUIVillagerSpawnPoint());

    }

    public static List<Arena> getArenaList() {
        List<Arena> arenas = new ArrayList<>();
        String[] folderList = folder.list();
        if(folderList == null) {
            return arenas;
        }
        for (String arena : folderList) {
            arenas.add(load(arena.replace(".yml", "")));
        }
        return arenas;
    }

    public static Arena enabledArena = null;


    public static Arena create(String arenaName, Location position1, Location position2, Location spawnPoint) {
        File cfile = new File(KnockbackFFA.getInstance().getDataFolder(), "arenas" + File.separator + arenaName + ".yml");
        if (!KnockbackFFA.getInstance().getDataFolder().exists())
            KnockbackFFA.getInstance().getDataFolder().mkdir();
        if (!cfile.exists()) {
            try {
                cfile.createNewFile();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error creating " + cfile.getName() + "!");
            }
        }
        Arena arena = load(arenaName);
        arena.getConfig().set("world-border", false);
        arena.getConfig().set("auto-reset", false);
        arena.getConfig().set("arena.pos1.x", position1.getX());
        arena.getConfig().set("arena.pos2.x", position2.getX());
        arena.getConfig().set("arena.pos1.y", position1.getY());
        arena.getConfig().set("arena.pos2.y", position2.getY());
        arena.getConfig().set("arena.pos1.z", position1.getZ());
        arena.getConfig().set("arena.pos2.z", position2.getZ());
        arena.getConfig().set("arena.pos1.world", position1.getWorld().getName());
        arena.getConfig().set("arena.pos2.world", position2.getWorld().getName());
        arena.getConfig().set("arena.spawn.x", spawnPoint.getX());
        arena.getConfig().set("arena.spawn.y", spawnPoint.getY());
        arena.getConfig().set("arena.spawn.z", spawnPoint.getZ());
        arena.getConfig().set("arena.spawn.world", spawnPoint.getWorld().getName());
        arena.save();
        return arena;
    }

    public static Arena load(String arenaName) {
        if (!ARENA_MAP.containsKey(arenaName)) ARENA_MAP.put(arenaName, new Arena(arenaName));
        return ARENA_MAP.get(arenaName);
    }

    /**
     * @return the enabledArena
     */
    public static Arena getEnabledArena() {
        return enabledArena;
    }

    /**
     * sets the arena enabled
     *
     * @param @arena
     */
    public static void setEnabledArena(String arenaName) {
        enabledArena = load(arenaName);
    }

    public static void setEnabledArena(Arena arena) {
        enabledArena = arena;
    }

    /**
     * Returns a random arena name
     *
     * @return String
     */
    public static String randomArena() {
        String[] arenas = folder.list();
        int random = new Random().nextInt(arenas.length);
        return arenas[random];
    }
}
