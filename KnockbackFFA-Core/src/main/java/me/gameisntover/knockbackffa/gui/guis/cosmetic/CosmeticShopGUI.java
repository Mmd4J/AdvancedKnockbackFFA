package me.gameisntover.knockbackffa.gui.guis.cosmetic;

import me.gameisntover.knockbackffa.configurations.ItemConfiguration;
import me.gameisntover.knockbackffa.configurations.Messages;
import me.gameisntover.knockbackffa.cosmetics.Cosmetic;
import me.gameisntover.knockbackffa.gui.ItemBuilder;
import me.gameisntover.knockbackffa.gui.LightButton;
import me.gameisntover.knockbackffa.gui.LightButtonManager;
import me.gameisntover.knockbackffa.gui.LightGUI;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CosmeticShopGUI extends LightGUI {
    public CosmeticShopGUI(Knocker knocker) {
        super("Cosmetic Shop", 5);
        List<String> cosmetics = Arrays.stream(Cosmetic.getFolder().list()).map(s -> s.replace(".yml","")).collect(Collectors.toList());
        List<Cosmetic> cList = knocker.getOwnedCosmetics();
        for (String cosmetic : cosmetics) {
            LightButton cosmeticsItem = LightButtonManager.createButton(ItemBuilder.builder().material(Material.getMaterial(Cosmetic.fromString(cosmetic,knocker).getData().getString("icon"))).name(Cosmetic.fromString(cosmetic,knocker).getData().getString("name")).buttonMeta().build(), event1 -> {
                double playerBal = knocker.getBalance();
                if (playerBal >= Cosmetic.fromString(cosmetic,knocker).getData().getInt("price")) {
                    List<Cosmetic> ownedCosmetics = knocker.getOwnedCosmetics();
                    if (!ownedCosmetics.contains(cosmetics.get(event1.getSlot()))) {
                        knocker.removeBalance(ItemConfiguration.get().getInt(cosmetics.get(event1.getSlot()) + ".price"));
                        ownedCosmetics.add(Cosmetic.fromString(cosmetics.get(event1.getSlot()),knocker));
                        knocker.setOwnedCosmetics(ownedCosmetics);
                        knocker.closeGUI();
                        destroy();
                        knocker.sendMessage(Messages.PURCHASE_SUCCESS.toString().replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                    } else {
                        knocker.sendMessage(Messages.ALREADY_OWNED.toString().replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                        knocker.closeGUI();
                        destroy();
                    }
                } else {
                    knocker.sendMessage(Messages.NOT_ENOUGH_MONEY.toString());
                    knocker.closeGUI();
                }

            });
            ItemMeta cosmeticMeta = cosmeticsItem.getItem().getItemMeta();
            List<String> lore = Cosmetic.fromString(cosmetic,knocker).getData().getStringList(cosmetic + ".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
            lore.add("§7Cost: §a" + Cosmetic.fromString(cosmetic,knocker).getData().getInt(cosmetic + ".price"));
            cosmeticMeta.setLore(lore);
            if (cList.contains(cosmetic)) {
                cosmeticMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                cosmeticMeta.setDisplayName(cosmeticMeta.getDisplayName().replace("&", "§") + " &8(§aOwned&8)");
            } else {
                cosmeticMeta.removeEnchant(Enchantment.DURABILITY);
                cosmeticMeta.setDisplayName(cosmeticMeta.getDisplayName().replace("&", "§").replace(" &8(§aOwned&8)", ""));
            }
            cosmeticsItem.getItem().setItemMeta(cosmeticMeta);
            setButton(cosmeticsItem, cosmetics.indexOf(cosmetic));
        }

    }
}
