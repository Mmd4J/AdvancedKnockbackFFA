package me.gameisntover.knockbackffa.commands;

import lombok.Getter;
import lombok.SneakyThrows;
import me.gameisntover.knockbackffa.commands.knockcommands.arena.*;
import me.gameisntover.knockbackffa.commands.knockcommands.game.JoinCommand;
import me.gameisntover.knockbackffa.commands.knockcommands.game.LeaveCommand;
import me.gameisntover.knockbackffa.commands.knockcommands.kits.CreateKitCommand;
import me.gameisntover.knockbackffa.commands.knockcommands.kits.DelKitCommand;
import me.gameisntover.knockbackffa.commands.knockcommands.util.ReloadCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Getter
public class CommandManager {
    private final List<KnockCommand> commands;
    private final CommandMap commandMap;
    private final Field f;
    @SneakyThrows
    public CommandManager(){
        commands = Arrays.asList(new CreateArenaCommand(),new GotoWorldCommand(),new EditArenaCommand(),new ResetArenaCommand(),
                new WandCommand(), new CreateWorldCommand(),new ReloadCommand(),new LeaveCommand(),new JoinCommand(),new SetMainLobbyCommand(),new CreateKitCommand(),new DelKitCommand());
        f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        f.setAccessible(true);
        commandMap = (CommandMap) f.get(Bukkit.getServer());
        for (KnockCommand cmd : commands) commandMap.register("knockbackffa",cmd);
    }
}
