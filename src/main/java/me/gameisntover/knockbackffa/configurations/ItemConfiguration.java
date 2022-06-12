package me.gameisntover.knockbackffa.configurations;

import me.gameisntover.knockbackffa.kit.KnockbackFFALegacy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ItemConfiguration {
    private static File file;

    private static FileConfiguration soundConfig;

    public static void setup() {
        file = new File(KnockbackFFALegacy.getInstance().getDataFolder(), "items.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
                Files.copy(KnockbackFFALegacy.getInstance().getResource("Items.yml"), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
            }
        }
        soundConfig = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return soundConfig;
    }

    public static void save() {
        try {
            soundConfig.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

    public static void reload() {
        soundConfig = YamlConfiguration.loadConfiguration(file);
    }
}
