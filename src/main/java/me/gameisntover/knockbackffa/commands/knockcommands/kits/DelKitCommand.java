package me.gameisntover.knockbackffa.commands.knockcommands.kits;

import me.gameisntover.knockbackffa.kit.KitManager;
import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.ChatColor;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

@KFCommand(name = "kbffadeletekit",syntax = "/kbffadeletekit <kitname>",description = "deletes the kit that user wants", permissionDefault = PermissionDefault.OP)
public class DelKitCommand extends KnockCommand {

    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        return null;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
            if (args.length == 0)  knocker.sendMessage(ChatColor.RED + "Usage: " + getUsage());
            else if (args.length == 1) {
                KitManager.load(args[0]).getfile().delete();
                knocker.sendMessage(ChatColor.GREEN + "I've deleted the kit " + args[0]+ "!");
            }
        }
    }
