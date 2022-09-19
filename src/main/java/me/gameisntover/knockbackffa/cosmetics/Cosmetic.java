package me.gameisntover.knockbackffa.cosmetics;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import me.gameisntover.knockbackffa.KnockbackFFA;
import me.gameisntover.knockbackffa.util.Knocker;
import me.gameisntover.knockbackffa.util.YamlData;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Cosmetic {

    @Nullable
    protected YamlData data;
    protected String name = "none";
    protected String description = "no cosmetic is selected. cool!";
    protected XMaterial icon = XMaterial.BARRIER;
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
            this.icon = XMaterial.matchXMaterial(data.getString("icon")).get();
            this.description = data.getString("description");
            this.displayName = data.getString("display-name");
        }
        this.name = name;
        owner = knocker;
    }

    public abstract CosmeticType getType();

    public static Cosmetic fromString(String name, Knocker knocker) {
        Cosmetic cosmetic = null;
        if (Arrays.asList("nonetrail","none","").contains(name)) cosmetic = null;
        else {
            YamlData data = new YamlData(folder, name);
            switch (CosmeticType.valueOf(data.getString("type"))) {
                case TRAIL:
                    cosmetic = new TrailCosmetic(name, knocker, data.getStringList("blocks"));
                    break;
                case SOUND:
                    cosmetic = new SoundCosmetic(name, knocker, data.getStringList("sounds"));
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
                data.set("blocks",Arrays.asList(XMaterial.GRASS.name(),XMaterial.STONE.name()));
                break;
        }
        data.save();
    }

}
