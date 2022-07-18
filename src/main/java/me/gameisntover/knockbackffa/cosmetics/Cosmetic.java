package me.gameisntover.knockbackffa.cosmetics;

import lombok.Getter;
import me.gameisntover.knockbackffa.KnockbackFFA;
import me.gameisntover.knockbackffa.util.Knocker;
import me.gameisntover.knockbackffa.util.YamlData;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Cosmetic {

    @Nullable
    protected YamlData data;
    protected String name = "none";
    protected String description = "no cosmetic is selected. cool!";
    protected Material icon = Material.BARRIER;
    protected float price = 0;
    protected String displayName = "&cNone!";
    @Nullable
    protected Knocker owner;
    private final static Map<String,Cosmetic> cosmeticMap = new HashMap<>();
    @Getter
    protected static final File folder = new File(KnockbackFFA.getInstance().getDataFolder(), "cosmetics");

    public Cosmetic(String name, Knocker knocker, boolean hasData) {
        if (name == null) return;
        if (hasData) {
            data = new YamlData(folder, name);
            this.price = data.getInt("price");
            this.icon = Material.valueOf(data.getString("icon"));
            this.description = data.getString("description");
            this.displayName = data.getString("display-name");
        }
        this.name = name;
        owner = knocker;
    }

    public abstract CosmeticType getType();

    public static Cosmetic fromString(String name, Knocker knocker) {
        Cosmetic cosmetic = null;
        if (name.equalsIgnoreCase("none")) cosmetic = new NullCosmetic(knocker);
        else if (name.equalsIgnoreCase("nonetrail")) cosmetic = new NullTCosmetic(knocker);
        else {
            YamlData data = new YamlData(folder, name);
            switch (CosmeticType.valueOf(data.getString("type"))) {
                case TRAIL:
                    cosmetic = new TrailCosmetic(name, knocker, data.getStringList("blocks"));
                    break;
                case SOUND:
                    cosmetic = new SoundCosmetic(name, knocker, data.getStringList("sounds"));
                    break;
                case NULL:
                    cosmetic = new NullCosmetic(knocker);
                    break;
            }
        }
        return cosmetic;
    }

    public abstract void onLoad();

    @Override
    public String toString() {
        return name;
    }

    public static class NullCosmetic extends Cosmetic {
        public NullCosmetic(Knocker knocker) {
            super("none", knocker, false);
        }

        @Override
        public CosmeticType getType() {
            return CosmeticType.NULL;
        }

        @Override
        public void onLoad() {

        }
    }

    public static void createCosmetic(CosmeticType type, String name, String description, String displayname, Material icon, float price) {
        YamlData data = new YamlData(folder, name + ".yml");
        data.set("display-name", "&e" + displayname);
        data.set("description", description);
        data.set("type", type.toString());
        data.set("icon", icon.name());
        data.set("price", price);
        switch (type){
            case SOUND:
                data.set("sounds", Arrays.asList("NOTE_PIANO:1:1","NOTE_PIANO:1:1.1","NOTE_PIANO:1:2","NOTE_PIANO:1:3"));
       break;
            case TRAIL:
                data.set("blocks",Arrays.asList(Material.GRASS.name(),Material.STONE.name()));
                break;
        }
        data.save();
    }


    public static class NullTCosmetic extends TrailCosmetic{
        public NullTCosmetic(Knocker knocker) {
            super("nonetrail", knocker, new ArrayList<>(),false);
        }

        @Override
        public CosmeticType getType() {
            return CosmeticType.NULL;
        }

        @Override
        public void onLoad() {

        }
    }
}
