package me.gameisntover.knockbackffa.gui.guis.arena;

import me.gameisntover.knockbackffa.arena.Arena;
import me.gameisntover.knockbackffa.arena.ArenaManager;
import me.gameisntover.knockbackffa.arena.Cuboid;
import me.gameisntover.knockbackffa.gui.ItemBuilder;
import me.gameisntover.knockbackffa.gui.LightButton;
import me.gameisntover.knockbackffa.gui.LightButtonManager;
import me.gameisntover.knockbackffa.gui.LightGUI;
import me.gameisntover.knockbackffa.multipleversion.KnockMaterial;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EditArenaGUI extends LightGUI {
    public EditArenaGUI(String arenaName) {
        super("Arena Editor",3);
        Arena arena =  ArenaManager.load(arenaName);
        LightGUI arenaGUI = this;
        LightButton setspawn = LightButtonManager.createButton(ItemBuilder.builder().material(KnockMaterial.NETHER_STAR).name(ChatColor.GRAY + "Set spawnpoint").build(), e -> {
            Player player = (Player) e.getWhoClicked();
            arena.getConfig().set("arena.spawn.x", player.getLocation().getX());
            arena.getConfig().set("arena.spawn.y", player.getLocation().getY());
            arena.getConfig().set("arena.spawn.z", player.getLocation().getZ());
            arena.getConfig().set("arena.spawn.world", player.getLocation().getWorld());
            arena.save();
            player.sendMessage(ChatColor.GREEN + "Arena spawn location set!");
        });
        LightButton autoReset = LightButtonManager.createButton(ItemBuilder.builder().material(KnockMaterial.DISPENSER).name(ChatColor.GRAY + "Auto Reset").lore(ChatColor.GRAY + "Toggle whether or not the arena will reset blocks placed or broke automatically").build(), e -> {
            arena.getConfig().set("auto-reset", !arena.getConfig().getBoolean("auto-reset"));
            if (arena.getConfig().getString("blocks")==null) {
                Location loc1 = arena.getPos1();
                Location loc2 = arena.getPos2();
                List<String> blocks = new ArrayList<>();
                Cuboid region = new Cuboid(loc1, loc2);
                for (Block block : region.getBlocks()) blocks.add(block.getType().name());
                arena.getConfig().set("blocks", blocks);
                arena.save();
            }
            arena.save();
        });
        LightButton setpos = LightButtonManager.createButton(ItemBuilder.builder().material(KnockMaterial.REDSTONE_BLOCK).name(ChatColor.GRAY + "Set arena positions").lore(ChatColor.GRAY + "Sets the arena positions you need a 2 positions selected with the wand you have in your hand").build(), e -> {
            Knocker knocker = Knocker.getKnocker(e.getWhoClicked().getUniqueId());
            if (knocker.getPositionA() != null && knocker.getPositionB() != null) {
                Location loc1 = knocker.getPositionA();
                Location loc2 = knocker.getPositionB();
                Cuboid box = new Cuboid(loc1, loc2);
                e.getWhoClicked().getWorld().getWorldBorder().setCenter(box.getCenter().getX(), box.getCenter().getZ());
                e.getWhoClicked().getWorld().getWorldBorder().setSize(box.getUpperX() - box.getLowerX());
                arena.getConfig().set("arena.pos1.x", loc1.getX());
                arena.getConfig().set("arena.pos2.x", loc2.getX());
                arena.getConfig().set("arena.pos1.y", loc1.getY());
                arena.getConfig().set("arena.pos2.y", loc2.getY());
                arena.getConfig().set("arena.pos1.z", loc1.getZ());
                arena.getConfig().set("arena.pos2.z", loc2.getZ());
                arena.getConfig().set("arena.pos1.world", loc1.getWorld());
                arena.getConfig().set("arena.pos2.world", loc2.getWorld());
                String world = e.getWhoClicked().getWorld().getName();
                arena.getConfig().set("arena.spawn.world", world);
                List<String> blocks = new ArrayList<>();
                List<String> locations = new ArrayList<>();
                Cuboid region = new Cuboid(loc1, loc2);
                for (Block block : region.getBlocks()) {
                    blocks.add(block.getType().name());
                    locations.add(block.getLocation().toString());
                }
                arena.getConfig().set("blocks", blocks);
                arena.getConfig().set("locations", locations);
                arena.save();
                e.getWhoClicked().sendMessage(ChatColor.GREEN + "Arena positions set!");
            }
        });
        ItemMeta setposMeta = setpos.getItem().getItemMeta();
        LightButton worldBorder = LightButtonManager.createButton(ItemBuilder.builder().lore(ChatColor.GREEN + "Toggle worldborder in arena").material(Material.BARRIER).name(ChatColor.GRAY + "World Border").build(), e -> {
            arena.getConfig().set("world-border", !arena.getConfig().getBoolean("world-border"));
            arena.save();
            Location spawnLoc = arena.getSpawnLocation();
            boolean worldBorderBool = arena.getConfig().getBoolean("world-border");
            if (worldBorderBool) {
                Location loc1 = arena.getPos1();
                Location loc2 = arena.getPos2();
                Cuboid box = new Cuboid(loc1, loc2);
                spawnLoc.getWorld().getWorldBorder().setCenter(box.getCenter().getX(), box.getCenter().getZ());
                spawnLoc.getWorld().getWorldBorder().setSize(box.getUpperX() - box.getLowerX());
            } else {
                WorldBorder worldBorderr = spawnLoc.getWorld().getWorldBorder();
                worldBorderr.reset();
            }
        });
        arenaGUI.setButton(setspawn,10);
        arenaGUI.setButton(setpos,11);
        arenaGUI.setButton(worldBorder,12);
        arenaGUI.setButton(autoReset,13);
    }
}
