package me.gameisntover.knockbackffa.commands.knockcommands.util;

import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.util.Items;
import me.gameisntover.knockbackffa.player.Knocker;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

@KFCommand(name = "kbffaspecialitems", description = "opens a new inventory with special items", permissionDefault = PermissionDefault.OP, syntax = "/kbffaspecialitems")
public class SpecialItems extends KnockCommand {
    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        return null;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        Inventory specialItems = Bukkit.createInventory(null, 9, "Special Items");
        specialItems.addItem(Items.KB_STICK.item);
        specialItems.addItem(Items.KB_BOW.item);
        specialItems.addItem(Items.KB_ARROW.item);
        specialItems.addItem(Items.JUMP_PLATE.item);
        specialItems.addItem(Items.ENDER_PEARL.item);
        specialItems.addItem(Items.BUILDING_BLOCK.item);
        knocker.getPlayer().openInventory(specialItems);
    }
}
