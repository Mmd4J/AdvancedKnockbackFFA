package me.gameisntover.knockbackffa.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.gameisntover.knockbackffa.KnockbackFFA;
import me.gameisntover.knockbackffa.arena.ArenaManager;
import me.gameisntover.knockbackffa.player.Knocker;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Expansion extends PlaceholderExpansion
{

    @Override
    public @NotNull String getAuthor() {
        return "GaMeIsNtOvEr";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "advancedknockbackffa";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.9";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        Player player1 = player.getPlayer();
        Knocker knocker = Knocker.getKnocker(player1.getUniqueId());
        if (params.equalsIgnoreCase("player_kills")) return String.valueOf(knocker.getKills());
        if (params.equalsIgnoreCase("player_deaths")) return String.valueOf(knocker.getDeaths());
        if (params.equalsIgnoreCase("player_balance")) return knocker.getBalance()+"";

        if (params.equalsIgnoreCase("current_map")) {
            String arenaName = ArenaManager.enabledArena.getName();
            if (arenaName == null) return "No Arena";
             else return arenaName;
        }
        if (params.equalsIgnoreCase("timer_nextmap")){
            int timer = KnockbackFFA.getInstance().timer;
            int seconds = timer % 60;
            int minutes = timer / 60;
            return minutes + ":" + seconds;
        }
        if (params.equalsIgnoreCase("next_map")){
            if (ArenaManager.folder.list()!=null && ArenaManager.folder.list().length>1){
                String arenaName = ArenaManager.getEnabledArena().getName();

                List<String> arenaList = Arrays.asList(ArenaManager.folder.list());
                int index = arenaList.indexOf(arenaName);
                if (arenaList.size() == 1){
                    return arenaList.get(0).replace(".yml","");
                } else {
                    return arenaList.get(index+1).replace(".yml","");
                }
            } else {
                return "No Arena";
            }
        }
        return null;
    }
}
