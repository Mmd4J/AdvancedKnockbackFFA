package me.gameisntover.knockbackffa.gui;

import me.gameisntover.knockbackffa.multipleversion.KnockMaterial;
import me.gameisntover.knockbackffa.util.Knocktils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    public static ItemBuilderBuilder builder() {
        return new ItemBuilderBuilder();
    }
    public static class ItemBuilderBuilder{
        private ItemStack item;
        private ItemMeta meta;
        private boolean unbreakable = true;

        public ItemBuilderBuilder() {
            item = new ItemStack(Material.STONE);
            meta = item.getItemMeta();
        }

        public ItemStack build() {
            meta.spigot().setUnbreakable(unbreakable);
            item.setItemMeta(meta);
            return item;
        }

        public ItemBuilderBuilder name(String name) {
            if (meta == null) meta = item.getItemMeta();
            meta.setDisplayName(Knocktils.translateColors(name));
            return this;
        }

        public ItemBuilderBuilder amount(Integer amount) {
            item.setAmount(amount);
            return this;
        }

        public ItemBuilderBuilder enchants(List<KEnchant> enchants) {
            enchants.forEach(enchant -> meta.addEnchant(enchant.getEnchantment(), enchant.getLevel(), true));
            return this;
        }

        public ItemBuilderBuilder itemflags(List<ItemFlag> itemflags) {
            itemflags.forEach(itemflag -> meta.addItemFlags(itemflag));
            return this;
        }

        public ItemBuilderBuilder unbreakable(boolean a) {
            this.unbreakable = a;
            return this;
        }

        public ItemBuilderBuilder material(Material material) {
            item = new ItemStack(material);
            return this;
        }

        /**
         * made for legacy & non legacy support
         *
         * @param knockMaterial
         * @return
         */
        public ItemBuilderBuilder material(KnockMaterial knockMaterial) {
            if (Bukkit.getServer().getVersion().contains("1.8") || Bukkit.getServer().getVersion().contains("1.9") ||
                    Bukkit.getServer().getVersion().contains("1.10") || Bukkit.getServer().getVersion().contains("1.11") ||
                    Bukkit.getServer().getVersion().contains("1.12")) {
                item.setType(knockMaterial.legacy.toItemStack().getType());
                item.setData(knockMaterial.legacy);
            } else item.setType(Material.getMaterial(knockMaterial.nonLegacy));
            return this;
        }

        public ItemBuilderBuilder lore(String... lore) {
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
            return this;
        }

        public ItemBuilderBuilder lores(List<String> lores) {
            lores.forEach(lore -> ChatColor.translateAlternateColorCodes('&', lore));
            meta.setLore(lores);
            item.setItemMeta(meta);
            return this;
        }

        /**
         * made for buttons option
         *
         * @return this
         */
        public ItemBuilderBuilder buttonMeta() {
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DESTROYS);
            meta.spigot().setUnbreakable(true);
            item.setItemMeta(meta);
            return this;
        }


    }
}
