package me.gameisntover.knockbackffa.configurations;

import me.gameisntover.knockbackffa.kit.KnockbackFFALegacy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ScoreboardConfiguration
{
    private static File file;

    private static FileConfiguration messages;

    public static void setup() {
        file = new File(KnockbackFFALegacy.getInstance().getDataFolder(), "scoreboard.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                Files.copy(KnockbackFFALegacy.getInstance().getResource("scoreboard.yml"), file.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {

            }
        }
        messages = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return messages;
    }

    public static void save() {
        try {
            messages.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

    public static void reload() {
        messages = YamlConfiguration.loadConfiguration(file);
    }
}
