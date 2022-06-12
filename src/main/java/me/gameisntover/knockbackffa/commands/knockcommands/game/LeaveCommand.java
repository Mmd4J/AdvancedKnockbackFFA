package me.gameisntover.knockbackffa.commands.knockcommands.game;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.configurations.Messages;
import me.gameisntover.knockbackffa.kit.KnockbackFFALegacy;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;
import java.util.Objects;

@KFCommand(name = "kbffaleave", syntax = "/kbffaleave", description = "leaves the game", permissionDefault = PermissionDefault.TRUE)
public class LeaveCommand extends KnockCommand {
    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        return null;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        if (!KnockbackFFALegacy.BungeeMode() && knocker.isInGame()) {
            String leaveText = Messages.LEAVE_ARENA.toString();
            leaveText = PlaceholderAPI.setPlaceholders(knocker.getPlayer(), leaveText);
            knocker.sendMessage(leaveText);
            knocker.leaveCurrentArena();
            knocker.getPlayer().getInventory().clear();
            if (KnockbackFFALegacy.getInstance().getConfig().getBoolean("save-inventory-on-join")) {
                List<ItemStack> items = (List<ItemStack>) knocker.get().get("inventory");
                List<ItemStack> armor = (List<ItemStack>) knocker.get().get("armor");
                assert armor != null;
                armor.stream().filter(Objects::nonNull);
                assert items != null;
                knocker.getPlayer().getInventory().setContents(items.toArray(new ItemStack[0]));
                knocker.getPlayer().getInventory().setArmorContents(armor.toArray(new ItemStack[0]));
            }
            knocker.toggleScoreBoard(false);
            knocker.setInGame(false);
        } else knocker.sendMessage(Messages.CANNOTUSELEAVE.toString());
    }
}
