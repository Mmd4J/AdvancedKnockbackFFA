package me.gameisntover.knockbackffa.commands.knockcommands.kits;

import me.gameisntover.knockbackffa.kit.KitManager;
import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;

@KFCommand(name = "kbffacreatekit",description = "creates a new kit",syntax = "/kbffacreatekit <name>",permissionDefault =  PermissionDefault.OP)
public class CreateKitCommand extends KnockCommand {
    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        return null;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
            if (args.length == 0) knocker.sendMessage(ChatColor.RED + "Usage: /createkit <kitname>");
             if (args.length == 1) {
                 String icon = "BARRIER";
                 if (knocker.getPlayer().getInventory().getItemInHand().getType()!= Material.AIR)
                 icon = knocker.getPlayer().getInventory().getItemInHand().getType().name();
                 KitManager.create(args[0],Arrays.asList(Arrays.stream(knocker.getPlayer().getInventory().getContents()).toArray(ItemStack[]::new)),icon);
                 knocker.sendMessage("&aSuccessfully created the kit \"" + args[0]+ "\" ! now you need to configure it! check out plugins/KnockbackFFA/kits/" + args[0]+ ".yml!");
            }

    }
}
