package me.gameisntover.knockbackffa.commands.knockcommands.game;

import me.gameisntover.knockbackffa.commands.KFCommand;
import me.gameisntover.knockbackffa.commands.KnockCommand;
import me.gameisntover.knockbackffa.configurations.Messages;
import me.gameisntover.knockbackffa.cosmetics.Cosmetic;
import me.gameisntover.knockbackffa.cosmetics.CosmeticType;
import me.gameisntover.knockbackffa.gui.LightButton;
import me.gameisntover.knockbackffa.gui.LightButtonManager;
import me.gameisntover.knockbackffa.gui.LightGUI;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.permissions.PermissionDefault;

import java.io.File;
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
        if (args.length <= 1){
            knocker.sendMessageWithPrefix(Messages.NEED_MORE_ARGS.toString().replace("syntax",getUsage()));
            return;
        }
        switch (args[0]) {
            case "create":
                LightGUI gui = new LightGUI("Select a cosmetic type", 1);
                for (CosmeticType type : CosmeticType.values()) {
                    LightButton button = LightButtonManager.createButton(type.item, e -> {
                        Cosmetic.createCosmetic(type, args[1], "&7cool cosmetic!", "&e" + args[1], type.item.getType(), 0);
                        knocker.sendMessageWithPrefix("Cosmetic with the name of " + args[1] + "has been successfully created! check plugins/KnockbackFFA/cosmetics/" + args[1] + ".yml for more info.");
                    });
                gui.addButton(button);
                knocker.openGUI(gui);
        }
                    break;
            case "edit":
                // TODO: 7/12/2022  
                break;
            case "delete":
                File file = new File(Cosmetic.getFolder(),args[1]+".yml");
                if (file.exists()) file.delete();
                knocker.sendMessageWithPrefix(args[1] + " has been successfully removed!");
                break;
            default:
                knocker.sendMessageWithPrefix(Messages.INVALID_ARG.toString().replace("syntax",getUsage()));
                break;
        }
    }
}

