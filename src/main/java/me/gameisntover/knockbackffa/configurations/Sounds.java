package me.gameisntover.knockbackffa.configurations;

import com.cryptomorin.xseries.XSound;
import me.gameisntover.knockbackffa.KnockbackFFA;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public enum Sounds {
    ARENACHANGE("arenachange", XSound.BLOCK_NOTE_BLOCK_PLING.parseSound()),
    ITEM_REMOVED("itemremoved",XSound.BLOCK_NOTE_BLOCK_BASS.parseSound()),
    JUMP_PLATE("jumpplate",XSound.ENTITY_BAT_TAKEOFF.parseSound()),
    PLAYER_JOIN("join",XSound.ENTITY_PLAYER_LEVELUP.parseSound()),
    GUI_CLOSE("guiclose",XSound.BLOCK_CHEST_CLOSE.parseSound()),
    GUI_OPEN("guiopen",XSound.BLOCK_CHEST_OPEN.parseSound());
    private String path;
    private Sound sound;
    private FileConfiguration config;
    private File file;
    Sounds(String path, Sound defaultsound){
    this.sound = defaultsound;
    this.path = path;
    file = new File(KnockbackFFA.getInstance().getDataFolder(), "sound.yml");
    if (!file.exists()) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    config = YamlConfiguration.loadConfiguration(file);
    }
    public Sound getSound(){
    if (config.isSet(path)) return Sound.valueOf(config.getString(path));
    else return sound;
    }
}
