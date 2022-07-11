package me.gameisntover.knockbackffa.listener;

import me.gameisntover.knockbackffa.cosmetics.TrailCosmetic;
import me.gameisntover.knockbackffa.kit.KnockKit;
import me.gameisntover.knockbackffa.configurations.ItemConfiguration;
import me.gameisntover.knockbackffa.configurations.Messages;
import me.gameisntover.knockbackffa.configurations.Sounds;
import me.gameisntover.knockbackffa.cosmetics.Cosmetic;
import me.gameisntover.knockbackffa.cosmetics.CosmeticType;
import me.gameisntover.knockbackffa.gui.*;
import me.gameisntover.knockbackffa.kit.KitManager;
import me.gameisntover.knockbackffa.util.Knocker;
import me.gameisntover.knockbackffa.util.Knocktils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GUIItemInteractListener implements Listener {
    @EventHandler
    public void onPlayerItemInteract(PlayerInteractEvent e) {
        Knocker knocker = Knocker.getKnocker(e.getPlayer().getUniqueId());
        if (e.getItem() == null) return;
        ItemStack item = e.getItem();
        if (e.getItem().getItemMeta() == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        Player player = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
                if (Items.KITS_MENU.getItem().equals(item)) {
                    e.setCancelled(true);
                    LightGUI cosmeticMenu = new LightGUI("Cosmetic Menu", 5);
                    cosmeticMenu.setOpenEvent(event -> knocker.playSound(Sounds.GUI_OPEN.getSound(), 1, 1));
                    cosmeticMenu.setCloseEvent(event -> knocker.playSound(Sounds.GUI_CLOSE.getSound(), 1, 1));
                    List<Cosmetic> cList = knocker.getOwnedCosmetics();
                    if (cList.size() > 0) cList.forEach(cosmetic -> {
                                if (cosmetic != null) {
                                    String displayName = Knocktils.translateColors(cosmetic.getName());
                                    List<String> lore = cosmetic.getData().getStringList("lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
                                    LightButton cosmeticItem = LightButtonManager.createButton(ItemBuilder.builder().material(cosmetic.getIcon()).name(displayName).coolMeta().build(), event -> {
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
                                        player.closeInventory();
                                        cosmeticMenu.destroy();
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
                                                meta.setDisplayName(meta.getDisplayName().replace("&", "§") + " §8(§aSelected§8)");
                                                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                                            } else {
                                                meta.removeEnchant(Enchantment.DURABILITY);
                                                meta.setDisplayName(meta.getDisplayName().replace("&", "§"));
                                            }
                                            break;
                                    }
                                    meta.setLore(lore);
                                    cosmeticItem.getItem().setItemMeta(meta);
                                    cosmeticMenu.setButton(cosmeticItem, cList.indexOf(cosmetic));
                                } else knocker.setOwnedCosmetics(cList);
                            }
                    );
                    knocker.openGUI(cosmeticMenu);
                }
                if (Items.SHOP_MENU.getItem().equals(item)) {
                    e.setCancelled(true);
                    LightGUI shopMenu = new LightGUI("Shop Menu", 5);
                    String cIcon = ItemConfiguration.get().getString("ShopMenu.cosmetic.material");
                    String cName = ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.cosmetic.name"));
                    LightButton cosmeticItem = LightButtonManager.createButton(ItemBuilder.builder().material(Material.getMaterial(cIcon)).name(cName).coolMeta().build(), event -> {
                        LightGUI cosmeticShop = new LightGUI("Cosmetic Shop", 5);
                        List<String> cosmetics = Arrays.stream(Cosmetic.getFolder().list()).map(s -> s.replace(".yml","")).collect(Collectors.toList());
                        List<Cosmetic> cList = knocker.getOwnedCosmetics();
                        for (String cosmetic : cosmetics) {
                            LightButton cosmeticsItem = LightButtonManager.createButton(ItemBuilder.builder().material(Material.getMaterial(Cosmetic.fromString(cosmetic,knocker).getData().getString("icon"))).name(Cosmetic.fromString(cosmetic,knocker).getData().getString("name")).coolMeta().build(), event1 -> {
                                double playerBal = knocker.getBalance();
                                if (playerBal >= Cosmetic.fromString(cosmetic,knocker).getData().getInt("price")) {
                                    List<Cosmetic> ownedCosmetics = knocker.getOwnedCosmetics();
                                    if (!ownedCosmetics.contains(cosmetics.get(event1.getSlot()))) {
                                        knocker.removeBalance(ItemConfiguration.get().getInt(cosmetics.get(event1.getSlot()) + ".price"));
                                        ownedCosmetics.add(Cosmetic.fromString(cosmetics.get(event1.getSlot()),knocker));
                                        knocker.setOwnedCosmetics(ownedCosmetics);
                                        player.closeInventory();
                                        shopMenu.destroy();
                                        player.sendMessage(Messages.PURCHASE_SUCCESS.toString().replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                                    } else {
                                        player.sendMessage(Messages.ALREADY_OWNED.toString().replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                                        player.closeInventory();
                                        shopMenu.destroy();
                                    }
                                } else {
                                    player.sendMessage(Messages.NOT_ENOUGH_MONEY.toString());
                                    player.closeInventory();
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
                            cosmeticShop.setButton(cosmeticsItem, cosmetics.indexOf(cosmetic));
                        }
                        knocker.openGUI(cosmeticShop);
                    });
                    ItemMeta cosmeticMeta = cosmeticItem.getItem().getItemMeta();
                    cosmeticMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.cosmetic.name")));
                    cosmeticMeta.setLore(ItemConfiguration.get().getStringList("ShopMenu.cosmetic.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
                    cosmeticItem.getItem().setItemMeta(cosmeticMeta);
                    shopMenu.setButton(cosmeticItem, ItemConfiguration.get().getInt("ShopMenu.cosmetic.slot"));
                    String kIcon = ItemConfiguration.get().getString("ShopMenu.kit.material");
                    String kName = ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.kit.name"));
                    LightButton kitItem = LightButtonManager.createButton(ItemBuilder.builder().material(Material.getMaterial(kIcon)).name(kName).coolMeta().build(), event -> {
                        LightGUI kitShop = new LightGUI("Kit Shop", 5);
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
                                            player.closeInventory();
                                            player.sendMessage(Messages.PURCHASE_SUCCESS.toString().replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                                        } else {
                                            player.sendMessage(Messages.ALREADY_OWNED.toString().replace("%cosmetic%", cosmetics.get(event1.getSlot())));
                                            player.closeInventory();
                                        }
                                    } else {
                                        player.sendMessage(Messages.NOT_ENOUGH_MONEY.toString());
                                        player.closeInventory();
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
                                kitShop.setButton(kitsItem, cosmetics.indexOf(cosmetic));
                            }
                        }
                        knocker.openGUI(kitShop);

                    });
                    ItemMeta kitMeta = kitItem.getItem().getItemMeta();
                    kitMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ItemConfiguration.get().getString("ShopMenu.kit.name")));
                    kitMeta.setLore(ItemConfiguration.get().getStringList("ShopMenu.kit.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()));
                    kitItem.getItem().setItemMeta(kitMeta);
                    shopMenu.setButton(kitItem, ItemConfiguration.get().getInt("ShopMenu.kit.slot"));
                    knocker.openGUI(shopMenu);
                }
                if (Items.KITS_MENU.getItem().equals(item)) {
                    e.setCancelled(true);
                    LightGUI kitsMenu = new LightGUI("Kits Menu", 5);
                    kitsMenu.setOpenEvent(event -> Knocker.getKnocker(e.getPlayer().getUniqueId()).playSound(Sounds.GUI_OPEN.getSound(), 1, 1));
                    kitsMenu.setCloseEvent(event -> Knocker.getKnocker(e.getPlayer().getUniqueId()).playSound(Sounds.GUI_CLOSE.getSound(), 1, 1));

                    if (knocker.getOwnedKits() != null && knocker.getOwnedKits().size() > 0) {
                        if (knocker.getOwnedKits() == null)
                            knocker.setOwnedKits(new ArrayList<>());
                        List<String> kitsList = knocker.getOwnedKits().stream().map(Object::toString).collect(Collectors.toList());
                        for (String kit : kitsList) {
                            if (kit != null) {
                                KnockKit kitItems = KitManager.load(kit);
                                if (kitItems.get().getString("icon") != null || !Objects.equals(kitItems.get().getString("KitIcon"), "AIR")) {
                                    String icon = kitItems.get().getString("icon");
                                    String name = Knocktils.translateColors(kitItems.get().getString("name"));
                                    LightButton kitItem = LightButtonManager.createButton(ItemBuilder.builder().material(Material.getMaterial(icon)).name(name).coolMeta().build(), event -> {
                                        String selC = kitsList.get(event.getSlot());
                                        if (event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY))
                                            knocker.setSelectedKit(null);
                                        else knocker.setSelectedKit(KnockKit.getFromString(selC));
                                        player.closeInventory();
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
                                        kitMeta.setDisplayName(kitMeta.getDisplayName().replace("&", "§").replace(" §8(§aSelected§8)", ""));
                                    }
                                    kitItem.getItem().setItemMeta(kitMeta);
                                    kitsMenu.setButton(kitItem, kitsList.indexOf(kit));
                                }
                            }
                        }
                    }
                    knocker.openGUI(kitsMenu);
                }
            }
        }

    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) e.setCancelled(true);


    }
}
