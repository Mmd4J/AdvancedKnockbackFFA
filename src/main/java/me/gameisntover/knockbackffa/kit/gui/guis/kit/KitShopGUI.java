package me.gameisntover.knockbackffa.kit.gui.guis.kit;

import com.cryptomorin.xseries.XMaterial;
import me.gameisntover.knockbackffa.configurations.Messages;
import me.gameisntover.knockbackffa.kit.gui.ItemBuilder;
import me.gameisntover.knockbackffa.kit.gui.LightButton;
import me.gameisntover.knockbackffa.kit.gui.LightButtonManager;
import me.gameisntover.knockbackffa.kit.gui.LightGUI;
import me.gameisntover.knockbackffa.kit.KitManager;
import me.gameisntover.knockbackffa.kit.KnockKit;
import me.gameisntover.knockbackffa.util.Knocker;
import me.gameisntover.knockbackffa.util.Knocktils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KitShopGUI extends LightGUI {
    public KitShopGUI(Knocker knocker) {
        super("Kit Shop", 5);
        List<String> kits = Arrays.stream(KitManager.getFolder().list()).map(s -> s.replace(".yml", "")).collect(Collectors.toList());
        List<KnockKit> cList = knocker.getOwnedKits();
        for (String kitstr : kits) {
            if (kitstr != null) {
                KnockKit kit = KitManager.load(kitstr);
                String kitIcon = kit.get().getString("icon");
                String kitName = Knocktils.translateColors(kit.get().getString("name"));
                LightButton kitsItem = LightButtonManager.createButton(ItemBuilder.builder().material(Material.getMaterial(kitIcon)).name(kitName).buttonMeta().build(), event1 -> {
                    double playerBal = knocker.getBalance();
                    if (playerBal >= kit.get().getInt("price")) {
                        List<KnockKit> ownedKits = knocker.getOwnedKits();
                             if (ownedKits != null && !ownedKits.contains(KnockKit.getFromString(kits.get(event1.getSlot())))) {
                            knocker.removeBalance(kit.get().getInt("price"));
                            ownedKits.add(KnockKit.getFromString(kits.get(event1.getSlot())));
                            knocker.setOwnedKits(ownedKits);
                            knocker.closeGUI();
                            knocker.sendMessage(Messages.PURCHASE_SUCCESS.toString().replace("%cosmetic%", kits.get(event1.getSlot())));
                        } else {
                            knocker.sendMessage(Messages.ALREADY_OWNED.toString().replace("%cosmetic%", kits.get(event1.getSlot())));
                            knocker.closeGUI();
                        }
                    } else {
                        knocker.sendMessage(Messages.NOT_ENOUGH_MONEY.toString());
                        knocker.closeGUI();
                    }
                });
                if (kitsItem.getItem().getType() == XMaterial.AIR.parseMaterial())
                    kitsItem.getItem().setType(XMaterial.BARRIER.parseMaterial());
                ItemMeta kitsMeta = kitsItem.getItem().getItemMeta();
                List<String> lore = kit.get().getStringList("lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                lore.add("§7Cost: §a" + kit.get().getInt("price"));
                kitsMeta.setLore(lore);
                if (cList.contains(KnockKit.getFromString(kitstr))) {
                    kitsMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                    kitsMeta.setDisplayName(kitsMeta.getDisplayName().replace("&", "§") + " §8(§aOwned§8)");
                    kitsItem.getItem().setItemMeta(kitsMeta);
                } else {
                    kitsMeta.removeEnchant(Enchantment.DURABILITY);
                    kitsMeta.setDisplayName(kitsMeta.getDisplayName().replace("&", "§").replace(" §8(§aOwned§8)", ""));
                    kitsItem.getItem().setItemMeta(kitsMeta);
                }
                setButton(kitsItem, kits.indexOf(kitstr));
            }
        }
    }
}
