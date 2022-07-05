package me.gameisntover.knockbackffa.arena;

import me.gameisntover.knockbackffa.KnockbackFFA;
import me.gameisntover.knockbackffa.util.KBFFAKit;
import me.gameisntover.knockbackffa.configurations.Messages;
import me.gameisntover.knockbackffa.configurations.Sounds;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class ArenaManager {
    public static Map<String, Arena> arenaMap = new HashMap<>();

    /**
     * Changes the arena to another arena
     *
     * @param @arena
     */
    public static void changeArena(Arena arena) {
        String arenaName = arena.getName();
        setEnabledArena(arenaName);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            Knocker knocker = Knocker.getKnocker(p.getUniqueId());
            if (knocker.isInGame()) {
                p.closeInventory();
                p.getInventory().clear();
                KBFFAKit kitManager = new KBFFAKit();
                knocker.giveLobbyItems();
                knocker.teleportPlayerToArena();
                p.playSound(p.getLocation(), Sounds.ARENACHANGE.getSound(), 1, 1);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.ARENA_CHANGE.toString().replace("%arena%", arenaName)));
            }
            if (arena.getConfig().getBoolean("auto-reset")) arena.resetArena();
        }
    }

    public static List<Arena> getArenaList() {
        List<Arena> arenas = new ArrayList<>();
        for (String arena : getfolder().list()) {
            arenas.add(load(arena.replace(".yml", "")));
        }
        return arenas;
    }

    public static Arena enabledArena = null;


    public static Arena create(String arenaName, Location position1, Location position2, Location spawnPoint) {
        File cfile = new File(KnockbackFFA.getInstance().getDataFolder(), "ArenaData" + File.separator + arenaName + ".yml");
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
        arena.getConfig().set("block-break", false);
        arena.getConfig().set("item-drop", true);
        arena.getConfig().set("world-border", false);
        arena.getConfig().set("block-break", false);
        arena.getConfig().set("item-drop", false);
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
        if (!arenaMap.containsKey(arenaName)) arenaMap.put(arenaName, new Arena(arenaName));
        return arenaMap.get(arenaName);
    }

    public static File getfolder() {
        return new File(KnockbackFFA.getInstance().getDataFolder(), "ArenaData" + File.separator);
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
        String[] arenas = getfolder().list();
        int random = new Random().nextInt(arenas.length);
        return arenas[random];
    }
}
