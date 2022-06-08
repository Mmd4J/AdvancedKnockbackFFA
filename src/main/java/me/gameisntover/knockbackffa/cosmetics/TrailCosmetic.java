package me.gameisntover.knockbackffa.cosmetics;

import me.gameisntover.knockbackffa.kit.KnockbackFFALegacy;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TrailCosmetic extends Cosmetic{
    private PlayerMoveEvent e;
    List<String> blocks;
    public TrailCosmetic(String name, String description, Double price, Material icon,List<String> blocks) {
        super(CosmeticType.TRAIL, name, description, price, icon);
        this.blocks = blocks;
    }

    @Override
    public void onLoad(Knocker knocker) {

            Block block = knocker.getLocation().getWorld().getBlockAt(e.getFrom().getBlockX(), e.getFrom().getBlockY() - 1, e.getFrom().getBlockZ());
            if (block.getMetadata("block-type")==null || block.getMetadata("block-type").get(0).asString().equals("")) {
                if (block.getType() != Material.AIR) {
                    block.setMetadata("block-type",new FixedMetadataValue(KnockbackFFALegacy.getInstance(),block.getType().name()));
                    if (blocks.size() == 1) {
                        Material material = Material.getMaterial(blocks.get(0).split(":")[0]);
                        long timer = Long.parseLong(blocks.get(0).split(":")[1]);
                        block.setType(material);
                        block.setMetadata("block-type",new FixedMetadataValue(KnockbackFFALegacy.getInstance(),"trail"));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                block.setType(Material.getMaterial(block.getMetadata("material").get(0).asString()));
                                block.setMetadata("block-type",new FixedMetadataValue(KnockbackFFALegacy.getInstance(),""));
                                cancel();
                            }
                        }.runTaskTimer(KnockbackFFALegacy.getInstance(), timer * 20L, 1);
                    } else {
                        block.setType(Material.getMaterial(blocks.get(0).split(":")[0]));
                        block.setMetadata("block-type",new FixedMetadataValue(KnockbackFFALegacy.getInstance(),"trail"));
                        new BukkitRunnable() {
                            int i = 0;
                            @Override
                            public void run() {
                                if (i < blocks.size() - 1) {
                                    i++;
                                    String material = blocks.get(i).split(":")[0];
                                    block.setType(Material.getMaterial(material));
                                } else {
                                    block.setType(Material.getMaterial(block.getMetadata("material").get(0).asString()));
                                    block.setMetadata("block-type",new FixedMetadataValue(KnockbackFFALegacy.getInstance(),""));
                                    cancel();
                                }
                            }
                        }.runTaskTimer(KnockbackFFALegacy.getInstance(), 1L, Long.parseLong(blocks.get(0).split(":")[1]));
                    }
                }
        }
    }

    public void setMoveEvent(PlayerMoveEvent e){
        this.e = e;
    }
}
