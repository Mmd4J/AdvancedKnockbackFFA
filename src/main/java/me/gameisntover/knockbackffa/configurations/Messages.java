package me.gameisntover.knockbackffa.configurations;

import me.gameisntover.knockbackffa.kit.KnockbackFFALegacy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public enum Messages {
    ITEMS_REMOEVD("itemremoved", "&eEvery items that were on ground were&b removed!"),
    LEAVE_ARENA("leave-arena", "&cYou have left the game!"),
    CANNOTUSELEAVE("cannotuseleave", "&cYou cannot use this command right now! probably because you're not in a game!"),
    JOIN_MESSAGE("joinmessage", "&f -=&7[&4+&7]&f=- &7hey &2%player_name% &7welcome to knockbackFFA"),
    LEAVE_MESSAGE("leavemessage", "&f -=&7[&4-&7]&f=- &7Hope to see you again &4%player_name% &7:(!"),
    ARENA_CHANGE("arenachangemsg", "&eThe arena has changed to &b%arena%"),
    PRIZE("prize", "&c+ &f%prize%"),
    KILLSTREAK_RECORD("killstreakrecord", "&c You've beaten your previous record of %killstreak% kills!"),
    DEATH_MESSAGE("deathmessage", "&c %player_name%&e was killed by &a %killer%"),
    SUICIDE("suicide", "&bYou died by falling into the void"),
    FELL_VOID_MESSAGE("fellvoidmsg", "&c %player_name% &e fell in to the void"),
    BOW_USE("bowuse", "&a You have used your bow! you need to wait 10 seconds to use it again!"),
    ARROW_GET("gotarrow", "&a You have got an arrow! you can use it now!"),
    CHAT_FORMAT("chatformat", "&7[&8%player%&7]&f : %message%"),
    NOT_ENOUGH_MONEY("not-enough-money", "&cYou don't have enough money to purchase this item!"),
    ALREADY_OWNED("already-owned", "&cYou already own this item!"),
    PURCHASE_SUCCESS("purchase-success", "&aYou have successfully purchased %cosmetic%"),
    NO_READY_ARENA("no-arena-ready", "&c&lSorry! &f&6but there is no arena ready to play ..."),
    ARENA_JOIN("join-arena", "&eYou have joined the game!"),
    ALREADY_IN_GAME("alreadyingame", "&cYou're already in game isnt it?");
    private static final FileConfiguration messages = YamlConfiguration.loadConfiguration(new File(KnockbackFFALegacy.getInstance().getDataFolder(), "messages.yml"));
    private final String path, defaultmsg;

    Messages(String path, String defaultmsg) {
        this.path = path;
        this.defaultmsg = defaultmsg;
        File file = new File(KnockbackFFALegacy.getInstance().getDataFolder(), "messages.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                Files.copy(KnockbackFFALegacy.getInstance().getResource("messages.yml"), file.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {

            }
        }
    }

    public static void save() {
        try {
            messages.save(new File(KnockbackFFALegacy.getInstance().getDataFolder(), "messages.yml"));
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

    @Override
    public String toString() {
        if (messages.isSet(path)) return ChatColor.translateAlternateColorCodes('&', messages.getString(path));
        else return ChatColor.translateAlternateColorCodes('&', defaultmsg);
    }
}
