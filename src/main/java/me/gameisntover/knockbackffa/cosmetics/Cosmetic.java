package me.gameisntover.knockbackffa.cosmetics;

import lombok.Getter;
import me.gameisntover.knockbackffa.kit.KnockbackFFALegacy;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Getter
public abstract class Cosmetic {
    private static File file;
    private static FileConfiguration configuration;
    private String name;
    private String description;
    private Material icon;
    private CosmeticType type;
    private Double price;

    public Cosmetic(CosmeticType cosmeticType, String name, String description, Double price, Material icon) {

    }

    public static void setup() {
        file = new File(KnockbackFFALegacy.getInstance().getDataFolder(), "cosmetics.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
                Files.copy(KnockbackFFALegacy.getInstance().getResource("cosmetics.yml"), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
            }
        }
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return configuration;
    }

    public static void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

    public static void reload() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public static Cosmetic getFromString(String name) {
        CosmeticType cosmeticType = CosmeticType.valueOf(get().getString(name + ".type"));
        if (cosmeticType.equals(CosmeticType.SOUND))
            return new SoundCosmetic(ChatColor.translateAlternateColorCodes('&', get().getString(name + ".name")), ChatColor.translateAlternateColorCodes('&', get().getString(name + ".lore")), get().getDouble(name + "price"), Material.getMaterial(get().getString(name + ".icon")), get().getStringList(name + "sounds"));
        else if (cosmeticType.equals(CosmeticType.TRAIL))
            return new TrailCosmetic(ChatColor.translateAlternateColorCodes('&', name), ChatColor.translateAlternateColorCodes('&', get().getString(name + ".lore")), get().getDouble(name + "price"), Material.getMaterial(get().getString(name + ".icon")), get().getStringList("blocks"));
        else return null;
    }

    public abstract void onLoad(Knocker knocker);
}
