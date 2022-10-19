package me.gameisntover.knockbackffa.player;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.knockbackffa.KnockbackFFA;
import me.gameisntover.knockbackffa.arena.Arena;
import me.gameisntover.knockbackffa.arena.ArenaConfiguration;
import me.gameisntover.knockbackffa.arena.ArenaManager;
import me.gameisntover.knockbackffa.arena.world.VoidChunkGenerator;
import me.gameisntover.knockbackffa.bukkitevents.PlayerJoinArenaEvent;
import me.gameisntover.knockbackffa.configurations.ScoreboardConfiguration;
import me.gameisntover.knockbackffa.cosmetics.Cosmetic;
import me.gameisntover.knockbackffa.cosmetics.TrailCosmetic;
import me.gameisntover.knockbackffa.database.Database;
import me.gameisntover.knockbackffa.gui.LightGUI;
import me.gameisntover.knockbackffa.kit.KnockKit;
import me.gameisntover.knockbackffa.util.Items;
import me.gameisntover.knockbackffa.util.Knocktils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Knocker {

    private boolean inGame;
    private boolean inArena;
    private BukkitTask scoreboardTask;
    private Cosmetic selectedCosmetic = null;
    private TrailCosmetic selectedTrail =  null;
    private List<Cosmetic> ownedCosmetics = new ArrayList<>();
    private List<KnockKit> ownedKits = new ArrayList<>();
    private KnockKit selectedKit = KnockKit.defaultKit();
    private int kills, deaths, maxks = 0;
    private int elo = 600;
    private double balance = 0;

    private final UUID uniqueID;
    public static Map<UUID, Knocker> knockerMap = new HashMap<>();
    private Location positionA;
    private Location positionB;

    protected Knocker(UUID uuid) {
        this.uniqueID = uuid;
        Database.getDatabase().insertData(this);
        Database.getDatabase().loadData(this);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueID);
    }

    public static Knocker getKnocker(UUID uuid) {
        if (knockerMap.containsKey(uuid)) return knockerMap.get(uuid);
        else {
            Knocker knocker = new Knocker(uuid);
            knockerMap.put(uuid, knocker);
            return knocker;
        }
    }

    public static Knocker getKnocker(String name) {
        return getKnocker(Bukkit.getPlayer(name).getUniqueId());
    }


    public void addBalance(double bal) {
        setBalance(balance + bal);
    }

    public void removeBalance(int balance) {
        setBalance(getBalance() - balance);
    }

    public Location getLocation() {
        return getPlayer().getLocation();
    }

    public void playSound(Sound sound, float pitch, float volume) {
        getPlayer().playSound(getLocation(), sound, volume, pitch);
    }

    public void sendMessage(String msg) {
        getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public void sendMessageWithPrefix(String msg) {
        getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&l &eKnockback&6FFA >> &r" + msg));
    }

    public void give(Items item) {
        getPlayer().getInventory().addItem(item.getItem());
    }

    public void openGUI(LightGUI gui) {
        getPlayer().openInventory(gui.getInventory());
    }


    public void loadCosmetic(Cosmetic cosmetic) {
        if (cosmetic == null) return;
        cosmetic.onLoad();
    }

    public void loadTrails(TrailCosmetic cosmetic, PlayerMoveEvent e) {
        if (cosmetic == null) return;
        cosmetic.setMoveEvent(e);
        cosmetic.onLoad();
    }


    public void leaveCurrentArena() {
        if (ArenaConfiguration.get().getString("mainlobby.world") == null) return;
        double x = ArenaConfiguration.get().getDouble("mainlobby.x");
        double y = ArenaConfiguration.get().getDouble("mainlobby.y");
        double z = ArenaConfiguration.get().getDouble("mainlobby.z");
        World world = Bukkit.getWorld(ArenaConfiguration.get().getString("mainlobby.world"));
        if (world != null) getPlayer().teleport(new Location(world, x, y, z));
        else {
            String worldname =ArenaConfiguration.get().getString("mainlobby.world");
            WorldCreator worldCreator = new WorldCreator(worldname);
            world = worldCreator.createWorld();
            getPlayer().teleport(new Location(world, x, y, z));
            getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
        }
    }

    public void teleportToArena(Arena arena) {
        if (ArenaManager.getEnabledArena().getName().equals(arena.getName())) {
            PlayerJoinArenaEvent event = new PlayerJoinArenaEvent(this, arena);
            Bukkit.getPluginManager().callEvent(event);
            getPlayer().teleport(arena.getSpawnLocation());
        }
    }


    public void teleportPlayerToArena() {
        if (!ArenaManager.folder.exists()) {
            System.out.println("Create an arena before trying to teleport player there :I");
            sendMessage("&cThere was an error while trying to send you to game you might need to tell admins to check the console");
        }
        else if (ArenaManager.folder.list().length > 0) {
            PlayerJoinArenaEvent event = new PlayerJoinArenaEvent(this, ArenaManager.getEnabledArena());
            Bukkit.getPluginManager().callEvent(event);
            Location spawnLoc = ArenaManager.getEnabledArena().getSpawnLocation();
            if (spawnLoc.getWorld() != null) if (!event.isCancelled()) getPlayer().teleport(spawnLoc);
            else {
                WorldCreator wc = new WorldCreator(ArenaManager.getEnabledArena().getConfig().getString("arena.spawn.world"));
                wc.generateStructures(false);
                wc.generator(new VoidChunkGenerator());
                World world1 = wc.createWorld();
                Bukkit.getWorlds().add(world1);
                world1.loadChunk(0, 0);
                if (!event.isCancelled())
                    getPlayer().teleport(new Location(world1, spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ()));
            }
        } else System.out.println("[KnockbackFFA] There are no arenas to teleport the player there!");

    }

    public void toggleScoreBoard(boolean toggle) {
        if (!toggle) return;
        FastBoard fastBoard = new FastBoard(getPlayer());
        List<String> titles = ScoreboardConfiguration.get().getStringList("title");
        final Integer[] a = {0};
        scoreboardTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() != null) {
                    if (a[0] > titles.size() - 1) a[0] = 0;
                    fastBoard.updateTitle(ChatColor.translateAlternateColorCodes('&', titles.get(a[0])));
                    List<String> strings = new ArrayList<>();
                    for (String s : ScoreboardConfiguration.get().getStringList("lines")){
                    strings.add(Knocktils.translateColors(s));
                    }
                    fastBoard.updateLines(PlaceholderAPI.setPlaceholders(getPlayer(), strings));
                    if (getPlayer().getScoreboard() != null) getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                }
            }
        }.runTaskTimer(KnockbackFFA.getInstance(), 5, 5);
    }

    public void giveKit(KnockKit kit) {
        if (kit.get().isSet("contents")) {
            List<ItemStack> kitContents = kit.getItems();
            getPlayer().getInventory().setContents(kitContents.toArray(new ItemStack[0]));
            for (ItemStack item : kitContents) {
                if (item.getType().name().contains("Helmet")) getPlayer().getInventory().setHelmet(item);
                if (item.getType().name().contains("Chestplate")) getPlayer().getInventory().setChestplate(item);
                if (item.getType().name().contains("Leggings")) getPlayer().getInventory().setLeggings(item);
                if (item.getType().name().contains("Boots")) getPlayer().getInventory().setBoots(item);
            }
        } else setOwnedKits(getOwnedKits().stream().filter(s -> s.getName().contains(kit.getName())).collect(Collectors.toList()));
    }

    public boolean isInMainLobby() {
        if (ArenaConfiguration.get().isSet("mainlobby.world"))
            return getLocation().getWorld().equals(Bukkit.getWorld(ArenaConfiguration.get().getString("mainlobby.world")));
        else return false;
    }

    public Inventory getInventory(){
        return getPlayer().getInventory();
    }

    public void closeGUI(){
        getPlayer().closeInventory();
    }

    @SneakyThrows
    public KnockerConnection getConnection(){
        return KnockerConnection.fromKnocker(this);
    }


}
