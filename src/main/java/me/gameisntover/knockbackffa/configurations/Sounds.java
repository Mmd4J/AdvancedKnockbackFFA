package me.gameisntover.knockbackffa.configurations;

import me.gameisntover.knockbackffa.kit.KnockbackFFALegacy;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public enum Sounds {
    ARENACHANGE("arenachange", Sound.NOTE_PLING),
    ITEM_REMOVED("itemremoved", Sound.NOTE_BASS),
    JUMP_PLATE("jumpplate", Sound.BAT_TAKEOFF),
    PLAYER_JOIN("join", Sound.LEVEL_UP),
    GUI_CLOSE("guiclose", Sound.CHEST_CLOSE),
    GUI_OPEN("guiopen", Sound.CHEST_OPEN);
    private final String path;
    private final Sound sound;
    private final FileConfiguration config;
    private final File file;

    Sounds(String path, Sound defaultsound) {
        this.sound = defaultsound;
        this.path = path;
        file = new File(KnockbackFFALegacy.getInstance().getDataFolder(), "sound.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public Sound getSound() {
        if (config.isSet(path)) return Sound.valueOf(config.getString(path));
        else return sound;
    }
}
