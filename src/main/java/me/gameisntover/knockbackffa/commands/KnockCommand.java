package me.gameisntover.knockbackffa.commands;

import me.gameisntover.knockbackffa.player.Knocker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

public abstract class KnockCommand extends Command {
    private Permission permission;
    public Permission getCmdPermission(){
        return permission;
    }
    protected KnockCommand() {
        super("kbffacmd");
        setup();
    }
    protected void setup() {
        String name = getClass().getAnnotation(KFCommand.class).name();
        String description = getClass().getAnnotation(KFCommand.class).description();
        PermissionDefault permissionDefault = getClass().getAnnotation(KFCommand.class).permissionDefault();
        setUsage(getClass().getAnnotation(KFCommand.class).syntax());
        setPermission("knockbackffa.command."+name);
        setName(name);
        setDescription(description);
        permission = new Permission("knockbackffa.command."+getName());
        permission.setDefault(permissionDefault);
        permission.setDescription(description);
        setPermission(permission.getName());
        Bukkit.getPluginManager().addPermission(permission);
        setPermissionMessage(ChatColor.RED +"You do not have enough permission to use this command!");

    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender.hasPermission(getPermission())) run(args,Knocker.getKnocker(sender.getName()));
        else sender.sendMessage(getPermissionMessage());
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return performTab(args, Knocker.getKnocker(sender.getName()));
    }

    public abstract List<String> performTab(String[] args, Knocker knocker);

    public abstract void run(String[] args, Knocker knocker);

}
