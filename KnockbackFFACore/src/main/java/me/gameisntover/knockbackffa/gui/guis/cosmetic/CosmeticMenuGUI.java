package me.gameisntover.knockbackffa.gui.guis.cosmetic;

import me.gameisntover.knockbackffa.configurations.Sounds;
import me.gameisntover.knockbackffa.cosmetics.Cosmetic;
import me.gameisntover.knockbackffa.cosmetics.CosmeticType;
import me.gameisntover.knockbackffa.cosmetics.TrailCosmetic;
import me.gameisntover.knockbackffa.gui.ItemBuilder;
import me.gameisntover.knockbackffa.gui.LightButton;
import me.gameisntover.knockbackffa.gui.LightButtonManager;
import me.gameisntover.knockbackffa.gui.LightGUI;
import me.gameisntover.knockbackffa.player.Knocker;
import me.gameisntover.knockbackffa.util.Knocktils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class CosmeticMenuGUI extends LightGUI {
    List<Cosmetic> cList;
    public CosmeticMenuGUI(Knocker knocker) {
        super("Cosmetic Menu", 5);
        setOpenEvent(event -> knocker.playSound(Sounds.GUI_OPEN.getSound(), 1, 1));
        setCloseEvent(event -> knocker.playSound(Sounds.GUI_CLOSE.getSound(), 1, 1));
        cList = knocker.getOwnedCosmetics();
        if (cList.size() > 0) cList.forEach(cosmetic -> {
            if (cosmetic != null) {
                String displayName = Knocktils.translateColors(cosmetic.getName());
                assert cosmetic.getData() != null;
                List<String> lore = cosmetic.getData().getStringList("lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                LightButton cosmeticItem = LightButtonManager.createButton(ItemBuilder.builder().material(cosmetic.getIcon()).name(displayName).buttonMeta().build(), event -> {
                    String selC = cList.get(event.getSlot()).getName();
                    CosmeticType cosmeticType = CosmeticType.valueOf(cosmetic.getData().getString("type"));
                    switch (cosmeticType) {
                        case SOUND:
                            if (event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY))
                                knocker.setSelectedCosmetic(null);
                            else knocker.setSelectedCosmetic(Cosmetic.fromString(selC,knocker));
                            break;
                        case TRAIL:
                            if (event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY))
                                knocker.setSelectedTrail(null);
                            else knocker.setSelectedTrail((TrailCosmetic) Cosmetic.fromString(selC,knocker));
                            break;
                    }
                    knocker.closeGUI();
                    destroy();
                });
                ItemMeta meta = cosmeticItem.getItem().getItemMeta();
                CosmeticType cosmeticType = CosmeticType.valueOf(cosmetic.getData().getString("type"));
                switch (cosmeticType) {
                    case SOUND:
                        if (knocker.getSelectedCosmetic() == null)
                            knocker.setSelectedCosmetic(cosmetic);
                        if (knocker.getSelectedCosmetic().equals(cosmetic)) {
                            meta.setDisplayName(Knocktils.translateColors(meta.getDisplayName()) + " §8(§aSelected§8)");
                            meta.addEnchant(Enchantment.DURABILITY, 1, true);
                        } else {
                            meta.removeEnchant(Enchantment.DURABILITY);
                            meta.setDisplayName(Knocktils.translateColors(meta.getDisplayName()));
                        }
                        break;
                    case TRAIL:
                        if (knocker.getSelectedTrail() == null)
                            knocker.setSelectedTrail((TrailCosmetic) cosmetic);
                        if (knocker.getSelectedTrail().equals(cosmetic)) {
                            meta.setDisplayName(Knocktils.translateColors(meta.getDisplayName()) + " §8(§aSelected§8)");
                            meta.addEnchant(Enchantment.DURABILITY, 1, true);
                        } else {
                            meta.removeEnchant(Enchantment.DURABILITY);
                            meta.setDisplayName(Knocktils.translateColors(meta.getDisplayName()));
                        }
                        break;
                }
                meta.setLore(lore);
                cosmeticItem.getItem().setItemMeta(meta);
                setButton(cosmeticItem, cList.indexOf(cosmetic));
            } else knocker.setOwnedCosmetics(cList);
        });
    }
}
