package me.gameisntover.knockbackffa;

import lombok.SneakyThrows;
import me.gameisntover.knockbackffa.arena.ArenaConfiguration;
import me.gameisntover.knockbackffa.arena.ArenaManager;
import me.gameisntover.knockbackffa.arena.ArenaSettings;
import me.gameisntover.knockbackffa.commands.CommandManager;
import me.gameisntover.knockbackffa.configurations.ItemConfiguration;
import me.gameisntover.knockbackffa.configurations.Messages;
import me.gameisntover.knockbackffa.configurations.ScoreboardConfiguration;
import me.gameisntover.knockbackffa.configurations.Sounds;
import me.gameisntover.knockbackffa.cosmetics.Cosmetic;
import me.gameisntover.knockbackffa.kit.KitManager;
import me.gameisntover.knockbackffa.listener.*;
import me.gameisntover.knockbackffa.util.Config;
import me.gameisntover.knockbackffa.util.Expansion;
import me.gameisntover.knockbackffa.util.KBFFAKit;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class KnockbackFFA extends JavaPlugin {
    public static KnockbackFFA INSTANCE;
    int ArenaID = 0;
    public Integer timer = 0;

    public static KnockbackFFA getInstance() {
        return INSTANCE;
    }


    @Override
    public void onEnable() {
        INSTANCE = this;
        if (Bukkit.getOnlinePlayers().size() > 0)
            for (Player player : Bukkit.getOnlinePlayers())
                Knocker.getKnocker(player.getUniqueId()).setInGame(BungeeMode());
        getLogger().info("Loading Commands");
        loadCommands();
        getLogger().info("Loading Configuration Files");
        loadConfig();
        getLogger().info("Loading Java Classes");
        loadListeners();
        getLogger().info("Loading example cosmetics if not exists");
        List<String> cosmetics = Arrays.asList("piano.yml","frozentrail.yml");
        cosmetics.forEach(s -> {
           if (!new File(getDataFolder(),s).exists()) saveResource(s,true);
             new File(getDataFolder(),s).renameTo(new File(Cosmetic.getFolder(),s));
        });
        getLogger().info("Loading Tasks");
        loadTasks();
        getLogger().info("Enjoy using plugin :)");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (BungeeMode() || Knocker.getKnocker(p.getUniqueId()).isInGame()) {
                if (p.getInventory().contains(Material.BOW) && !p.getInventory().contains(Material.ARROW)) {
                    KBFFAKit kitManager = new KBFFAKit();
                    p.getInventory().addItem(kitManager.kbbowArrow());
                }
            }
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Expansion(this).register();
        } else getLogger().warning("Could not find placeholder API. This plugin is needed!");

    }

    @SneakyThrows
    private void loadConfig() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            getLogger().info("[KnockbackFFA] : Creating DataFolder");
            dataFolder.mkdir();
        }
        File folder = new File(getDataFolder(), "Kits" + File.separator);
        if (!folder.exists()) {
            folder.mkdir();
            File file = new File(getDataFolder(), "Kits" + File.separator + "Default.yml");
            file.createNewFile();
            Files.copy(Objects.requireNonNull(KnockbackFFA.getInstance().getResource("Default.yml")), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            getLogger().info("[KnockbackFFA] : Default Kit Created");
        }
        ArenaConfiguration.setup();
        ScoreboardConfiguration.setup();
        ItemConfiguration.setup();
        if (!Cosmetic.getFolder().exists()) Cosmetic.getFolder().mkdir();
        if (!KitManager.folder.exists()) KitManager.folder.mkdir();
        if (!ArenaManager.getFolder().exists()) ArenaManager.getFolder().mkdir();
        new Config("database");
        saveDefaultConfig();
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
        } catch(Exception e) {e.printStackTrace();}

    }

    private void loadTasks() {
        if (ArenaManager.getFolder().listFiles() != null && ArenaManager.getFolder().listFiles().length != 0) {
            ArenaManager.setEnabledArena(ArenaManager.getArenaList().get(0));
            timer = getConfig().getInt("arena.change-timer");
            List<String> arenaList = Arrays.stream(Objects.requireNonNull(ArenaManager.getFolder().list())).map(String::toLowerCase).filter(s -> s.endsWith(".yml")).collect(Collectors.toList());
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (ArenaManager.getArenaList().size() > 0) {
                        String arenaName = ArenaManager.getArenaList().get(0).getName();
                        ArenaManager.changeArena(ArenaManager.load(arenaName));
                        cancel();
                        if (ArenaManager.getArenaList().size() > 1) ArenaID++;
                    }
                }
            }.runTaskTimer(this, 0, 1);
            new BukkitRunnable() {
                @Override
                public void run() {
                    timer--;
                    if (timer == 0) {
                        timer = getConfig().getInt("arena.change-timer");
                        if (ArenaManager.getArenaList().size() > 1) {
                            ArenaID++;
                            if (ArenaID <= ArenaManager.getArenaList().size())
                                ArenaManager.changeArena(ArenaManager.load(arenaList.get(ArenaID - 1).replace(".yml", "")));
                            else {
                                ArenaID = 1;
                                ArenaManager.changeArena(ArenaManager.load(arenaList.get(0).replace(".yml", "")));

                           }
                        } else if (ArenaManager.getArenaList().size() == 1)
                            ArenaManager.setEnabledArena(ArenaManager.getArenaList().get(0).getName());
                    }
                }
            }.runTaskTimer(this, 0, 20);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (BungeeMode() || Knocker.getKnocker(p.getUniqueId()).isInGame()) {
                        World world = p.getWorld();
                        List<Entity> entList = world.getEntities();

                        for (Entity current : entList)
                            if (current instanceof Item)
                                if (((Item) current).getItemStack().getType() == Material.GOLD_PLATE) current.remove();
                    }
                }
            }
        }.runTaskTimer(this, 0, 5);

        BukkitScheduler scheduler1 = Bukkit.getServer().getScheduler();
        if (getConfig().getBoolean("arena.clear-items.enabled")) {
            scheduler1.scheduleSyncRepeatingTask(this, () -> {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    Knocker knocker = Knocker.getKnocker(p.getUniqueId());
                    knocker.sendMessage(Messages.ITEMS_REMOEVD.toString());
                    if (BungeeMode() || knocker.isInGame()) {
                        World world = p.getWorld();
                        List<Entity> entList = world.getEntities();
                        for (Entity current : entList) if (current instanceof Item) {
                                current.remove();
                                knocker.playSound(Sounds.ITEM_REMOVED.getSound(), 1, 1);
                            }

                    }
                }
            }, getConfig().getInt("arena.clear-items.delay"), getConfig().getInt("arena.clear-items.period") * 20L);
        }
    }

    private void loadListeners() {
        Arrays.asList(new JoinLeaveListeners(),new DeathListener(),new WandListener(),
                new GameRulesListener(),new GUIItemInteractListener(),new KBFFAKit(),
                new ArenaSettings()).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener , this));
    }

    private void loadCommands() {
        CommandManager cmdManager = new CommandManager();
        if (BungeeMode()){
            cmdManager.getCommandMap().getCommand("kbffajoin").unregister(cmdManager.getCommandMap());
            cmdManager.getCommandMap().getCommand("kbffaleave").unregister(cmdManager.getCommandMap());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }




    public static boolean BungeeMode() {
        return KnockbackFFA.getInstance().getConfig().getBoolean("bungee-mode");
    }
}