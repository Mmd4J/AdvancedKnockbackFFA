package me.gameisntover.knockbackffa.gui.guis.kit;

import me.gameisntover.knockbackffa.configurations.Sounds;
import me.gameisntover.knockbackffa.gui.ItemBuilder;
import me.gameisntover.knockbackffa.gui.LightButton;
import me.gameisntover.knockbackffa.gui.LightButtonManager;
import me.gameisntover.knockbackffa.gui.LightGUI;
import me.gameisntover.knockbackffa.kit.KitManager;
import me.gameisntover.knockbackffa.kit.KnockKit;
import me.gameisntover.knockbackffa.util.Knocker;
import me.gameisntover.knockbackffa.util.Knocktils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KitsMenuGUI extends LightGUI {
    public KitsMenuGUI(Knocker knocker) {
        super("Kits Menu", 5);
        setOpenEvent(event -> knocker.playSound(Sounds.GUI_OPEN.getSound(), 1, 1));
        setCloseEvent(event -> knocker.playSound(Sounds.GUI_CLOSE.getSound(), 1, 1));
        if (knocker.getOwnedKits() != null && knocker.getOwnedKits().size() > 0) {
            if (knocker.getOwnedKits() == null)
                knocker.setOwnedKits(new ArrayList<>());
            List<String> kitsList = knocker.getOwnedKits().stream().map(Object::toString).collect(Collectors.toList());
            for (String kit : kitsList) {
                if (kit != null) {
                    KnockKit kitItems = KitManager.load(kit);
                    if (kitItems.get().getString("icon") != null || !Objects.equals(kitItems.get().getString("icon"), "AIR")) {
                        String icon = kitItems.get().getString("icon");
                        String name = Knocktils.translateColors(kitItems.get().getString("name"));
                        LightButton kitItem = LightButtonManager.createButton(ItemBuilder.builder().material(Material.getMaterial(icon)).name(name).buttonMeta().build(), event -> {
                            String selC = kitsList.get(event.getSlot());
                            if (event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY))
                                knocker.setSelectedKit(null);
                            else knocker.setSelectedKit(KnockKit.getFromString(selC));
                            knocker.closeGUI();
                        });
                        ItemMeta kitMeta = kitItem.getItem().getItemMeta();
                        if (kitItem.getItem().getType() == Material.AIR)
                            kitItem.getItem().setType(Material.BARRIER);

                        kitMeta.setLore(kitItems.get().getStringList("KitDescription").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
                        if (knocker.getSelectedKit() == null) knocker.setSelectedKit(KnockKit.getFromString(kit));
                        if (knocker.getSelectedKit().equals(KnockKit.getFromString(kit))) {
                            kitMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                            kitMeta.setDisplayName(Knocktils.translateColors(kitMeta.getDisplayName()) + " §8(§aSelected§8)");
                        } else {
                            kitMeta.removeEnchant(Enchantment.DURABILITY);
                            kitMeta.setDisplayName(Knocktils.translateColors(kitMeta.getDisplayName()).replace(" §8(§aSelected§8)", ""));
                        }
                        kitItem.getItem().setItemMeta(kitMeta);
                        setButton(kitItem, kitsList.indexOf(kit));
                    }
                }
            }
        }

    }
}
