package me.gameisntover.knockbackffa.listener;

import com.cryptomorin.xseries.XMaterial;
import lombok.SneakyThrows;
import me.gameisntover.knockbackffa.KnockbackFFA;
import me.gameisntover.knockbackffa.arena.ArenaConfiguration;
import me.gameisntover.knockbackffa.arena.ArenaManager;
import me.gameisntover.knockbackffa.arena.Cuboid;
import me.gameisntover.knockbackffa.bukkitevents.PlayerInteractAtVillagerEvent;
import me.gameisntover.knockbackffa.configurations.ItemConfiguration;
import me.gameisntover.knockbackffa.configurations.Messages;
import me.gameisntover.knockbackffa.configurations.Sounds;
import me.gameisntover.knockbackffa.entity.NPCVillager;
import me.gameisntover.knockbackffa.gui.guis.MainMenuGUI;
import me.gameisntover.knockbackffa.player.Knocker;
import me.gameisntover.knockbackffa.util.Items;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

public class GameRulesListener implements Listener {
    @EventHandler
    public void onPlayerItemPickup(PlayerPickupItemEvent e) {
        Knocker knocker = Knocker.getKnocker(e.getPlayer().getUniqueId());
        if (knocker.isInGame()) e.setCancelled(true);

    }

    @EventHandler
    public void playerChatFormat(AsyncPlayerChatEvent e) {
        Knocker knocker = Knocker.getKnocker(e.getPlayer().getUniqueId());
        if (knocker.isInGame())
            e.setFormat(Messages.CHAT_FORMAT.toString().replace("%player%", knocker.getPlayer().getName()).replace("%message%", e.getMessage()));

    }

    @EventHandler @SuppressWarnings("Deprecation")
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        List<String> voids = ArenaConfiguration.get().getStringList("registered-voids");
        for (String vd : voids) {
            if (ArenaConfiguration.get().getString("voids." + vd + ".pos1.world") != null) {
                double x1 = ArenaConfiguration.get().getDouble("voids." + vd + ".pos1.x");
                double y1 = ArenaConfiguration.get().getDouble("voids." + vd + ".pos1.y");
                double z1 = ArenaConfiguration.get().getDouble("voids." + vd + ".pos1.z");
                double x2 = ArenaConfiguration.get().getDouble("voids." + vd + ".pos2.x");
                double y2 = ArenaConfiguration.get().getDouble("voids." + vd + ".pos2.y");
                double z2 = ArenaConfiguration.get().getDouble("voids." + vd + ".pos2.z");
                World world = Bukkit.getWorld(ArenaConfiguration.get().getString("voids." + vd + ".pos1.world"));
                Location pos1 = new Location(world, x1, y1, z1);
                Location pos2 = new Location(world, x2, y2, z2);
                Cuboid bb = new Cuboid(pos1, pos2);
                if (bb.contains(player.getLocation())) {
                    Integer damage = ArenaConfiguration.get().getInt("voids." + vd + ".damage");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (player.isDead() || !bb.contains(player.getLocation()) || player.getWorld() != pos1.getWorld())
                                cancel();
                            else {
                                player.damage(damage);
                                player.setLastDamageCause(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.VOID, damage));
                            }
                        }
                    }.runTaskTimer(KnockbackFFA.getInstance(), 0, 20);

                }
            }
        }
    }

    @EventHandler
    public void onBowUse(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Knocker knocker = Knocker.getKnocker(e.getEntity().getUniqueId());
            Player player = (Player) e.getEntity().getShooter();
            if (!knocker.isInGame())  return;
            if (player.getInventory().getItemInHand().getType().equals(XMaterial.BOW.parseMaterial())) return;
            player.sendMessage(Messages.BOW_USE.toString());
                    new BukkitRunnable() {
                        int timer = 10;
                        @Override
                        public void run() {
                            timer--;
                            if (timer == 10 || timer == 9 || timer == 8 || timer == 7 || timer == 6 || timer == 5 || timer == 4 || timer == 3 || timer == 2 || timer == 1) {
                                if (player.getInventory().contains(Material.ARROW) || !player.getInventory().contains(Material.BOW)) {
                                    cancel();
                                    timer = 10;
                                }
                            }
                            if (timer == 0) {
                                if (!player.getInventory().contains(XMaterial.ARROW.parseMaterial()) && player.getInventory().contains(Material.BOW)) {
                                    player.getInventory().addItem(Items.KB_ARROW.item);
                                    player.sendMessage(Messages.ARROW_GET.toString());
                                }
                                cancel();
                                timer = 10;
                            }
                        }
                    }.runTaskTimer(KnockbackFFA.getInstance(), 0, 20);
            }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (Knocker.getKnocker(e.getEntity().getUniqueId()).isInGame()) {
            if (e.getFoodLevel() != 20) e.setFoodLevel(20);
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onSign(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[KnockbackFFA]") && event.getLine(1).equalsIgnoreCase("Join")) {
            event.setLine(0, ChatColor.YELLOW + "[A]KnockbackFFA");
            event.setLine(1, ChatColor.GREEN + "Join");
        }
    }
    @SneakyThrows
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Knocker knocker = Knocker.getKnocker(player.getUniqueId());
        if (KnockbackFFA.BungeeMode() || knocker.isInGame()) {
            if (e.getBlockPlaced().getType() == XMaterial.WHITE_WOOL.parseMaterial()) {
                Block block = e.getBlockPlaced();
                block.setMetadata("block-type", new FixedMetadataValue(KnockbackFFA.getInstance(), "BuildingBlock"));
                String arenaName = ArenaManager.getEnabledArena().getName();
                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (ArenaManager.getEnabledArena().getName().equalsIgnoreCase(arenaName)) {
                            switch (XMaterial.matchXMaterial(block.getType())) {
                                case WHITE_WOOL:
                                    block.setType(XMaterial.YELLOW_WOOL.parseMaterial());
                                    break;
                                case YELLOW_WOOL:
                                    block.setType(XMaterial.ORANGE_WOOL.parseMaterial());
                                    break;
                                case ORANGE_WOOL:
                                    block.setType(XMaterial.RED_WOOL.parseMaterial());
                                    break;
                                case RED_WOOL:
                                    block.setType(XMaterial.AIR.parseMaterial());
                                    cancel();
                                    break;
                            }
                        } else {
                            block.setType(XMaterial.AIR.parseMaterial());
                            block.setMetadata("block-type", new FixedMetadataValue(KnockbackFFA.getInstance(), ""));
                        }
                    }
                };
                runnable.runTaskTimer(KnockbackFFA.getInstance(), 10L, 20L);
            }
            if (e.getBlockPlaced().getType() == XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.parseMaterial()) {
                Block block = e.getBlockPlaced();
                block.setMetadata("block-type", new FixedMetadataValue(KnockbackFFA.getInstance(), "jumpplate"));
                block.getDrops().clear();
                BukkitScheduler blockTimer = Bukkit.getServer().getScheduler();
                blockTimer.scheduleSyncDelayedTask(KnockbackFFA.getInstance(), () -> {
                    e.getBlock().setType(XMaterial.AIR.parseMaterial());
                    block.setMetadata("block-type", new FixedMetadataValue(KnockbackFFA.getInstance(), ""));
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
                if (e.getClickedBlock().getType().equals(XMaterial.LIGHT_WEIGHTED_PRESSURE_PLATE.parseMaterial())) {
                    Block block = e.getClickedBlock();
                    block.getDrops().clear();
                    player.setVelocity(player.getLocation().getDirection().setY(ItemConfiguration.get().getInt("SpecialItems.JumpPlate.jumpLevel")));
                    Knocker.getKnocker(player.getUniqueId()).playSound(Sounds.JUMP_PLATE.getSound(), 1, 1);
                }
            }
        }else{
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (e.getClickedBlock() instanceof Sign) {
                    Sign sign = (Sign) e.getClickedBlock().getState();
                    if (sign.getLine(0).equalsIgnoreCase(ChatColor.YELLOW + "[A]KnockbackFFA"))
                        if (sign.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Join"))
                            if (knocker.isInGame()) knocker.sendMessage(Messages.ALREADY_IN_GAME.toString());
                                else player.chat("/join");
                }
            }
        }
    }
    @EventHandler
    public void onEndermiteSpawn(CreatureSpawnEvent e) {
        if (e.getEntity() instanceof Endermite) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerVillagerInteract(PlayerInteractAtEntityEvent e){
        Knocker knocker = Knocker.getKnocker(e.getPlayer().getUniqueId());
        if (NPCVillager.villager != null) {
            if (e.getRightClicked().getUniqueId().equals(NPCVillager.villager.getBukkitEntity().getUniqueId())) {
                e.setCancelled(true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        knocker.closeGUI();
                    }
                }.runTaskLater(KnockbackFFA.getInstance(),1);
                PlayerInteractAtVillagerEvent event = new PlayerInteractAtVillagerEvent(knocker, NPCVillager.villager);
                Bukkit.getPluginManager().callEvent(event);
                if (knocker.isInGame() && !event.isCancelled()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            knocker.openGUI(new MainMenuGUI());
                        }
                    }.runTaskLater(KnockbackFFA.getInstance(),1);
            }
            }
        }
    }
}
