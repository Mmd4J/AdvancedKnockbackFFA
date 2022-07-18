package me.gameisntover.knockbackffa.gui.guis.cosmetic;

import me.gameisntover.knockbackffa.configurations.ItemConfiguration;
import me.gameisntover.knockbackffa.gui.ItemBuilder;
import me.gameisntover.knockbackffa.gui.LightButton;
import me.gameisntover.knockbackffa.gui.LightButtonManager;
import me.gameisntover.knockbackffa.gui.LightGUI;
import me.gameisntover.knockbackffa.gui.guis.kit.KitShopGUI;
import me.gameisntover.knockbackffa.util.Knocker;
import me.gameisntover.knockbackffa.util.Knocktils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

public class ShopMenuGUI extends LightGUI {
    public ShopMenuGUI(Knocker knocker) {
        super("Shop Menu", 5);
        String cIcon = ItemConfiguration.get().getString("ShopMenu.cosmetic.material");
        String cName = Knocktils.translateColors(ItemConfiguration.get().getString("ShopMenu.cosmetic.name"));
        LightButton cosmeticItem = LightButtonManager.createButton(ItemBuilder.builder().material(Material.getMaterial(cIcon)).name(cName).coolMeta().build(), event -> {
            LightGUI cosmeticShop = new CosmeticShopGUI(knocker);
            knocker.openGUI(cosmeticShop);
        });
        ItemMeta cosmeticMeta = cosmeticItem.getItem().getItemMeta();
        cosmeticMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.cosmetic.name")));
        cosmeticMeta.setLore(ItemConfiguration.get().getStringList("ShopMenu.cosmetic.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
        cosmeticItem.getItem().setItemMeta(cosmeticMeta);
        setButton(cosmeticItem, ItemConfiguration.get().getInt("ShopMenu.cosmetic.slot"));
        String kIcon = ItemConfiguration.get().getString("ShopMenu.kit.material");
        String kName = ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.kit.name"));
        LightButton kitItem = LightButtonManager.createButton(ItemBuilder.builder().material(Material.getMaterial(kIcon)).name(kName).coolMeta().build(), event -> {
            LightGUI kitShop = new KitShopGUI(knocker);
            knocker.openGUI(kitShop);
        });
        ItemMeta kitMeta = kitItem.getItem().getItemMeta();
        kitMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.kit.name")));
        kitMeta.setLore(ItemConfiguration.get().getStringList("ShopMenu.kit.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
        kitItem.getItem().setItemMeta(kitMeta);
        setButton(kitItem, ItemConfiguration.get().getInt("ShopMenu.kit.slot"));
    }
}
