package me.gameisntover.knockbackffa.kit;

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
import me.gameisntover.knockbackffa.listener.*;
import me.gameisntover.knockbackffa.util.Expansion;
import me.gameisntover.knockbackffa.util.KBFFAKit;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.material.Wool;
import org.bukkit.metadata.FixedMetadataValue;
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

public final class KnockbackFFALegacy extends JavaPlugin implements Listener {
    public static KnockbackFFALegacy INSTANCE;
    public Integer timer = 0;
    int ArenaID = 0;

    public static KnockbackFFALegacy getInstance() {
        return INSTANCE;
    }

    public static boolean BungeeMode() {
        return KnockbackFFALegacy.getInstance().getConfig().getBoolean("Bungee-Mode");
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
            Bukkit.getPluginManager().registerEvents(this, this);
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
            Files.copy(Objects.requireNonNull(KnockbackFFALegacy.getInstance().getResource("Default.yml")), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            getLogger().info("[KnockbackFFA] : Default Kit Created");
        }
        Cosmetic.setup();
        ArenaConfiguration.setup();
        ScoreboardConfiguration.setup();
        ItemConfiguration.setup();
        saveDefaultConfig();

    }

    private void loadTasks() {
        if (ArenaManager.getfolder().listFiles() != null) {
            ArenaManager.setEnabledArena(ArenaManager.getArenaList().get(0));
            timer = getConfig().getInt("ArenaChangeTimer");
            List<String> arenaList = Arrays.stream(Objects.requireNonNull(ArenaManager.getfolder().list())).map(String::toLowerCase).filter(s -> s.endsWith(".yml")).collect(Collectors.toList());
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
                        //what should happen when timer is up
                        timer = getConfig().getInt("ArenaChangeTimer");
                        if (ArenaManager.getArenaList().size() > 1) { //checking if Arena.getArenaList() even has arenas
                            ArenaID++;
                            if (ArenaID <= ArenaManager.getArenaList().size())
                                ArenaManager.changeArena(ArenaManager.load(arenaList.get(ArenaID - 1).replace(".yml", "")));
                            else {
                                //arena changes to the first arena
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
        if (getConfig().getBoolean("ClearItems.enabled")) {
            scheduler1.scheduleSyncRepeatingTask(this, () -> {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    Knocker knocker = Knocker.getKnocker(p.getUniqueId());
                    knocker.sendMessage(Messages.ITEMS_REMOEVD.toString());
                    if (BungeeMode() || knocker.isInGame()) {
                        World world = p.getWorld();
                        List<Entity> entList = world.getEntities();
                        for (Entity current : entList)
                            if (current instanceof Item) {
                                current.remove();
                                knocker.playSound(Sounds.ITEM_REMOVED.getSound(), 1, 1);
                            }

                    }
                }
            }, getConfig().getInt("ClearItems.delay"), getConfig().getInt("ClearItems.period") * 20L);
        }
    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new JoinLeaveListeners(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new WandListener(), this);
        getServer().getPluginManager().registerEvents(new GameRulesListener(), this);
        getServer().getPluginManager().registerEvents(new GUIItemInteractListener(), this);
        getServer().getPluginManager().registerEvents(new KBFFAKit(), this);
        getServer().getPluginManager().registerEvents(new ArenaSettings(), this);
    }

    private void loadCommands() {
        CommandManager cmdManager = new CommandManager();
        if (BungeeMode()) {
            cmdManager.getCommandMap().getCommand("kbffajoin").unregister(cmdManager.getCommandMap());
            cmdManager.getCommandMap().getCommand("kbffaleave").unregister(cmdManager.getCommandMap());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Knocker knocker = Knocker.getKnocker(player.getUniqueId());
        if (knocker.get().getString("deaths") == null) {
            knocker.get().set("deaths", 0);
            knocker.get().set("kills", 0);
            knocker.get().set("owned-kits", knocker.get().getStringList("owned-kits").add("Default"));
            knocker.get().set("selected-kit", "Default");
            knocker.saveConfig();
        }
    }

    @EventHandler
    public void onSign(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[KnockbackFFA]") && event.getLine(1).equalsIgnoreCase("Join")) {
            event.setLine(0, ChatColor.YELLOW + "[A]KnockbackFFA");
            event.setLine(1, ChatColor.GREEN + "Join");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = Knocker.getKnocker(player.getUniqueId());
        if (BungeeMode() || knocker.isInGame()) {
            if (e.getBlockPlaced().getType() == Material.WOOL) {
                Block block = e.getBlockPlaced();
                BlockState bs = e.getBlockPlaced().getState();
                Wool wool = (Wool) bs.getData();
                block.setMetadata("block-type", new FixedMetadataValue(KnockbackFFALegacy.getInstance(), "BuildingBlock"));
                String arenaName = ArenaManager.getEnabledArena().getName();
                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (Objects.equals(ArenaManager.getEnabledArena().getName(), arenaName)) {
                            switch (wool.getColor()) {
                                case WHITE:
                                    wool.setColor(DyeColor.YELLOW);
                                    bs.update();
                                    break;
                                case YELLOW:
                                    wool.setColor(DyeColor.ORANGE);
                                    bs.update();
                                    break;
                                case ORANGE:
                                    wool.setColor(DyeColor.RED);
                                    bs.update();
                                    break;
                                case RED:
                                    block.setType(Material.AIR);
                                    cancel();
                                    break;
                            }
                        } else {
                            block.setType(Material.AIR);
                            block.setMetadata("block-type", new FixedMetadataValue(KnockbackFFALegacy.getInstance(), ""));
                        }
                    }
                };
                runnable.runTaskTimer(this, 10L, 20L);
            }
            if (e.getBlockPlaced().getType() == Material.GOLD_PLATE) {
                Block block = e.getBlockPlaced();
                block.setMetadata("block-type", new FixedMetadataValue(KnockbackFFALegacy.getInstance(), "jumpplate"));
                block.getDrops().clear();
                BukkitScheduler blockTimer = Bukkit.getServer().getScheduler();
                blockTimer.scheduleSyncDelayedTask(this, () -> {
                    e.getBlock().setType(Material.AIR);
                    block.setMetadata("block-type", new FixedMetadataValue(KnockbackFFALegacy.getInstance(), ""));
                }, ItemConfiguration.get().getInt("SpecialItems.JumpPlate.removeafter") * 20L);
            }
        }
    }

    @EventHandler
    public void onPressureButton(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = Knocker.getKnocker(e.getPlayer().getUniqueId());
        if (knocker.isInGame()) {
            if (e.getAction().equals(Action.PHYSICAL)) {
                if (e.getClickedBlock().getType().equals(Material.GOLD_PLATE)) {
                    Block block = e.getClickedBlock();
                    block.getDrops().clear();
                    player.setVelocity(player.getLocation().getDirection().setY(ItemConfiguration.get().getInt("SpecialItems.JumpPlate.jumpLevel")));
                    Knocker.getKnocker(player.getUniqueId()).playSound(Sounds.JUMP_PLATE.getSound(), 1, 1);
                }
            }
        } else {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (e.getClickedBlock() instanceof Sign) {
                    Sign sign = (Sign) e.getClickedBlock().getState();
                    if (sign.getLine(0).equalsIgnoreCase(ChatColor.YELLOW + "[A]KnockbackFFA")) {
                        if (sign.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Join")) {
                            if (knocker.isInGame()) knocker.sendMessage(Messages.ALREADY_IN_GAME.toString());
                            else player.chat("/join");
                        }
                    }
                }
            }
        }
    }
}