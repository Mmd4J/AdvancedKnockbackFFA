package me.gameisntover.knockbackffa.util;

import lombok.Getter;
import me.gameisntover.knockbackffa.KnockbackFFA;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class YamlData extends YamlConfiguration {
    private final File file;
    private static final Map<String, YamlData> yamlDataMap = new HashMap<>();

    public YamlData(File parent, String name) {
        file = new File(parent, name + ".yml");
        try {
            if (!parent.exists()) parent.mkdir();
            if (!file.exists()) file.createNewFile();
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public YamlData(String name) {
        this(KnockbackFFA.getInstance().getDataFolder(), name);
    }

    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getLocation(String s) {
        float pitch;
        float yaw;
        double x;
        double y;
        double z;
        World world;
        pitch = getInt(s + ".pitch");
        yaw = getInt(s + ".yaw");
        x = getDouble(s + ".x");
        y = getDouble(s + ".y");
        z = getDouble(s + ".z");
        world = Bukkit.getWorld(getString(s + ".world"));
    return new Location(world,x,y,z,yaw,pitch);
    }
}
