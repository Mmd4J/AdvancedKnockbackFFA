package me.gameisntover.knockbackffa.commands.knockcommands.kits;

import com.cryptomorin.xseries.XMaterial;
import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.kit.KitManager;
import me.gameisntover.knockbackffa.kit.KnockKit;
import me.gameisntover.knockbackffa.player.Knocker;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@KFCommand(name = "kbffakit", syntax = "/kbffakit <create,delete,edit> <name>", description = "commands related to the kbffa kit", permissionDefault = PermissionDefault.OP)
public class KitCommands extends KnockCommand {
    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        if (args.length ==0) return Arrays.asList("create","delete","edit");
        else if (!args[0].equalsIgnoreCase("create") && KnockKit.folder.exists() && KnockKit.folder.list().length != 0) return Arrays.stream(KnockKit.folder.list()).map(s -> s = s.replace(".yml","")).collect(Collectors.toList());
        else return null;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        if (args.length < 1){
            knocker.sendMessageWithPrefix("Command arguements are not enough! "+ getUsage());
            return;
        }
        switch (args[0]){
            case "create":
                String icon = "BARRIER";
                if (knocker.getPlayer().getInventory().getItemInHand().getType() != XMaterial.AIR.parseMaterial())
                    icon = knocker.getPlayer().getInventory().getItemInHand().getType().name();
                KitManager.create(args[0], Arrays.asList(Arrays.stream(knocker.getPlayer().getInventory().getContents()).toArray(ItemStack[]::new)), icon);
                knocker.sendMessage("&aSuccessfully created the kit \"" + args[0] + "\" ! now you need to configure it! check out plugins/KnockbackFFA/kits/" + args[0] + ".yml!");
                break;
            case "edit":
                // TODO: 7/17/2022
                break;
            case "delete":
                KitManager.load(args[0]).getfile().delete();
                knocker.sendMessage(ChatColor.GREEN + "I've deleted the kit " + args[0] + "!");
                break;
        }
    }
}
