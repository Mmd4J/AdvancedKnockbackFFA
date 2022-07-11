package me.gameisntover.knockbackffa.util;

import org.bukkit.ChatColor;

public class Knocktils {
    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String translateColors(String str) {
        return color(str).replaceAll("%%blue%%", ChatColor.BLUE.toString()).
                replaceAll("%%green%%", ChatColor.GREEN.toString()).
                replaceAll("%%aqua%%", ChatColor.AQUA.toString()).
                replaceAll("%%red%%", ChatColor.RED.toString()).
                replaceAll("%%yellow%%", ChatColor.YELLOW.toString()).
                replaceAll("%%orange%%", ChatColor.GOLD.toString()).
                replaceAll("%%gold%%", ChatColor.GOLD.toString()).
                replaceAll("%%darkred%%", ChatColor.DARK_RED.toString()).
                replaceAll("%%darkblue%%", ChatColor.DARK_BLUE.toString()).
                replaceAll("%%darkgreen%%", ChatColor.DARK_GREEN.toString()).
                replaceAll("%%darkaqua%%", ChatColor.DARK_AQUA.toString()).
                replaceAll("%%darkgray%%", ChatColor.DARK_GRAY.toString()).
                replaceAll("%%darkpurple%%", ChatColor.DARK_PURPLE.toString()).
                replaceAll("%%purple%%", ChatColor.LIGHT_PURPLE.toString()).
                replaceAll("%%gray%%", ChatColor.GRAY.toString()).
                replaceAll("%%black%%", ChatColor.BLACK.toString()).
                replaceAll("%%white%%", ChatColor.WHITE.toString()).
                replaceAll("%%bold%%", ChatColor.BOLD.toString()).
                replaceAll("%%italic%%", ChatColor.ITALIC.toString()).
                replaceAll("%%magic%%", ChatColor.MAGIC.toString()).
                replaceAll("%%strikethrough%%", ChatColor.STRIKETHROUGH.toString());
    }
}
