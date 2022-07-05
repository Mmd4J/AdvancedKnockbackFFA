package me.gameisntover.knockbackffa.util;

import lombok.Getter;
import me.gameisntover.knockbackffa.KnockbackFFA;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public class Config extends YamlConfiguration {
    private File file;

    public Config(File parent, String name) {
        file = new File(parent, name + ".yml");
        if (!file.exists()) KnockbackFFA.getInstance().saveResource(name + ".yml", true);
        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Config(String name) {
        this(KnockbackFFA.getInstance().getDataFolder(), name);
    }

    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
