package me.gameisntover.knockbackffa.commands.knockcommands.util;

import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.util.KBFFAKit;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

@KFCommand(name = "kbffaspecialitems",description = "opens a new inventory with special items",permissionDefault = PermissionDefault.OP,syntax = "/kbffaspecialitems")
public class SpecialItems extends KnockCommand {
    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        return null;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        Inventory specialItems = Bukkit.createInventory(null, 9, "Special Items");
        KBFFAKit kits = new KBFFAKit();
        specialItems.addItem(kits.kbStick());
        specialItems.addItem(kits.kbBow());
        specialItems.addItem(kits.kbbowArrow());
        specialItems.addItem(kits.JumpPlate());
        specialItems.addItem(kits.EnderPearl());
        specialItems.addItem(kits.BuildingBlock());
        knocker.getPlayer().openInventory(specialItems);
    }
}
