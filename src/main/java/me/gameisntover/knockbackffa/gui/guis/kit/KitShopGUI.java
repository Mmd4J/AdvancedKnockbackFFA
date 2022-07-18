package me.gameisntover.knockbackffa.gui.guis.kit;

import me.gameisntover.knockbackffa.configurations.Messages;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KitShopGUI extends LightGUI {
    public KitShopGUI(Knocker knocker) {
        super("Kit Shop", 5);
        List<String> cosmetics = Arrays.stream(Objects.requireNonNull(KitManager.getfolder().list())).map(s -> s.replace(".yml", "")).collect(Collectors.toList());
        List<KnockKit> cList = knocker.getOwnedKits();
        for (String cosmetic : cosmetics) {
            if (cosmetic != null) {
                KnockKit kit = KitManager.load(cosmetic);
                String kitIcon = kit.get().getString("icon");
                String kitName = Knocktils.translateColors(kit.get().getString("name"));
                LightButton kitsItem = LightButtonManager.createButton(ItemBuilder.builder().material(Material.getMaterial(kitIcon)).name(kitName).coolMeta().build(), event1 -> {
                    double playerBal = knocker.getBalance();
                    if (playerBal >= kit.get().getInt("Price")) {
                        List<KnockKit> ownedKits = knocker.getOwnedKits();
                        if (!ownedKits.contains(KnockKit.getFromString(cosmetics.get(event1.getSlot())))) {
                            knocker.removeBalance(kit.get().getInt("Price"));
                            ownedKits.add(KnockKit.getFromString(cosmetics.get(event1.getSlot())));
                            knocker.setOwnedKits(ownedKits);
                            knocker.closeGUI();
                            knocker.sendMessage(Messages.PURCHASE_SUCCESS.toString().replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                        } else {
                            knocker.sendMessage(Messages.ALREADY_OWNED.toString().replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                            knocker.closeGUI();
                        }
                    } else {
                        knocker.sendMessage(Messages.NOT_ENOUGH_MONEY.toString());
                        knocker.closeGUI();
                    }
                });
                if (kitsItem.getItem().getType() == Material.AIR)
                    kitsItem.getItem().setType(Material.BARRIER);
                ItemMeta kitsMeta = kitsItem.getItem().getItemMeta();
                List<String> lore = kit.get().getStringList("KitDescription").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                lore.add("§7Cost: §a" + kit.get().getInt("Price"));
                kitsMeta.setLore(lore);
                if (cList.contains(cosmetic)) {
                    kitsMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                    kitsMeta.setDisplayName(kitsMeta.getDisplayName().replace("&", "§") + " §8(§aOwned§8)");
                    kitsItem.getItem().setItemMeta(kitsMeta);
                } else {
                    kitsMeta.removeEnchant(Enchantment.DURABILITY);
                    kitsMeta.setDisplayName(kitsMeta.getDisplayName().replace("&", "§").replace(" §8(§aOwned§8)", ""));
                    kitsItem.getItem().setItemMeta(kitsMeta);
                }
                setButton(kitsItem, cosmetics.indexOf(cosmetic));
            }
        }
    }
}
