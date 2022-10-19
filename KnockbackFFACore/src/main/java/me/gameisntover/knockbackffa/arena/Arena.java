package me.gameisntover.knockbackffa.arena;

import me.gameisntover.knockbackffa.util.YamlData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Arena {

    private final YamlData config;
    private final String arenaName;


    public Arena(String name) {
        this.arenaName = name;
        config = new YamlData(ArenaManager.folder,arenaName);
    }

    public YamlData getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error saving " + arenaName +".yml!");
        }
    }



    /**
     * checks if the arena is ready and its able to use
     *
     * @return true if the arena is ready
     */
    public boolean isReady() {
        List<String> arenaList = Arrays.asList(Objects.requireNonNull(ArenaManager.folder.list()));
        return arenaList.contains(arenaName);
    }

    /**
     * checks if the arena is enabled
     *
     * @return true if the arena is the enabled arena
     */
    public boolean isEnabled() {
        return ArenaManager.getEnabledArena().getName().equals(arenaName);
    }


    /**
     * Deletes the arena file and changes the enabled arena
     * if that arena is the enabled arena.
     */
    public void removeArena() {
        config.getFile().delete();
        while (ArenaManager.getEnabledArena().getName().equals(arenaName))
            ArenaManager.setEnabledArena(ArenaManager.randomArena());
        save();
    }


    /**
     * Returns a list of arena positions.
     * 0 Index is always the first position
     * and the 1 Index is always the second position
     *
     * @return List<Location>
     */
    public List<Location> getArenaPositions() {
        List<Location> positions = new ArrayList<>();
        positions.add(getPos1());
        positions.add(getPos2());
        return positions;
    }

    /**
     * Returns the arena name
     *
     * @return String
     */
    public String getName() {
        return arenaName;
    }

    /**
     * Checks if the player is in arena region
     *
     * @param @location
     * @return true if location is in the region
     */
    public boolean contains(Location location) {
        Cuboid cuboid = new Cuboid(getPos1(), getPos2());
        return cuboid.contains(location);
    }

    /**
     * Returns the arena spawn location
     *
     * @return Region
     */
    public Location getSpawnLocation() {
        return getConfig().getLocation("arena.spawn");
    }

    /**
     * Returns the arenas first pos
     *
     * @return First position of the arena
     */
    public Location getPos1() {
        double x = getConfig().getDouble("arena.pos1.x");
        double y = getConfig().getDouble("arena.pos1.y");
        double z = getConfig().getDouble("arena.pos1.z");
        World world = getSpawnLocation().getWorld();
        return new Location(world, x, y, z);
    }

    /**
     * Returns the arenas second pos
     *
     * @return Second position of the arena
     */
    public Location getPos2() {
        double x = getConfig().getDouble("arena.pos2.x");
        double y = getConfig().getDouble("arena.pos2.y");
        double z = getConfig().getDouble("arena.pos2.z");
        World world = getSpawnLocation().getWorld();
        return new Location(world, x, y, z);
    }

    /**
     * Returns the arena cuboid region
     *
     * @return Cuboid
     */
    public Cuboid getCuboid() {
        return new Cuboid(getPos1(), getPos2());
    }

    public Location getGUIVillagerSpawnPoint(){
        if (config.isSet("arena.villager-spawn-point")) return config.getLocation("arena.villager-spawn-point");
        else return getSpawnLocation();
    }

}