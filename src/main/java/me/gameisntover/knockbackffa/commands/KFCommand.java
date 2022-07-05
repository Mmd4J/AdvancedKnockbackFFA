package me.gameisntover.knockbackffa.commands;

import org.bukkit.permissions.PermissionDefault;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface KFCommand {
    String name();
    String description();
    String syntax();
    PermissionDefault permissionDefault();
}
