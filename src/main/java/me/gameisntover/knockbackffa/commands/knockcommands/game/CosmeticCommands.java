package me.gameisntover.knockbackffa.commands.knockcommands.game;

import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.cosmetics.Cosmetic;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@KFCommand(name = "kbffacosmetic",description = "command for managing kbffa cosmetics",syntax = "/kbffacosmetic <create,delete,edit> <cosmeticname>",permissionDefault = PermissionDefault.OP)
public class CosmeticCommands extends KnockCommand {
    @Override
    public List<String> performTab(String[] args, Knocker knocker) {
        if (args.length == 1) return Arrays.asList("create","delete","edit");
        else if (Cosmetic.getFolder().list().length != 0)
            return Arrays.stream(Objects.requireNonNull(Cosmetic.getFolder().list())).map(s -> s = s.replace(".yml","")).collect(Collectors.toList());
        else return Collections.singletonList("");
    }

    @Override
    public void run(String[] args, Knocker knocker) {
        switch (args[0]){
            case "create":
                // TODO: 7/12/2022  
                break;
            case "edit":
                // TODO: 7/12/2022  
                break;
            case "delete":
                // TODO: 7/12/2022  
                break;
        }
    }
}

