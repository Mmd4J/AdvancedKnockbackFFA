package me.gameisntover.knockbackffa.commands.knockcommands.arena;

import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.util.Items;
import me.gameisntover.knockbackffa.player.Knocker;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

@KFCommand(name = "kbffawand", syntax = "/kbffawand", description = "gives the player a position selector wand", permissionDefault = PermissionDefault.OP)
public class WandCommand extends KnockCommand {
    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        return null;
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        knocker.give(Items.WAND);
        knocker.sendMessageWithPrefix("You got it! use the wand to select two positions.");
        knocker.sendMessageWithPrefix("Remember left click is for selecting first position and right click is for second position. then you're free to go to use /kbffacreatearena command!.");
    }
}
