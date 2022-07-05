package me.gameisntover.knockbackffa.kit;

import me.gameisntover.knockbackffa.KnockbackFFA;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KitManager {
    public static File folder = new File(KnockbackFFA.getInstance().getDataFolder(), "Kits" + File.separator);
    public static File getfolder() {
        return folder;
    }
    public static KnockKit load(String kitsName) {
        return new KnockKit(kitsName);
    }

    public static KnockKit create(String name, List<ItemStack> kitItems,String icon) {
        KnockKit kit = new KnockKit(name);
        kit.get().set("name",name);
        kit.setItems(kitItems);
        kit.setPrice(100);
        kit.setIcon(icon);
        List<String> lore = new ArrayList<>();
        lore.add("this is a kit");
        List<String> defaultKitLore = new ArrayList<>();
        defaultKitLore.add(ChatColor.GRAY + "Another cool kit!");
        defaultKitLore.add(ChatColor.GRAY + "Must be configured in plugins/KnockbackFFA/kits/"+name+".yml !");
        kit.get().set("lore",defaultKitLore);
        kit.save();
        return kit;
    }
}
