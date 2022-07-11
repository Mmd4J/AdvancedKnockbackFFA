package me.gameisntover.knockbackffa.cosmetics;

import me.gameisntover.knockbackffa.KnockbackFFA;
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
    public TrailCosmetic(String name,Knocker knocker,List<String> blocks) {
        super(name,knocker,true);
        this.blocks = blocks;
    }
    public TrailCosmetic(String name,Knocker knocker,List<String> blocks,boolean no) {
        super(name,knocker,no);
        this.blocks = blocks;
    }

    @Override
    public CosmeticType getType() {
        return CosmeticType.TRAIL;
    }

    @Override
    public void onLoad() {

            Block block = owner.getLocation().getWorld().getBlockAt(e.getFrom().getBlockX(), e.getFrom().getBlockY() - 1, e.getFrom().getBlockZ());
            if (block.getMetadata("block-type")==null || block.getMetadata("block-type").get(0).asString().equals("")) {
                if (block.getType() != Material.AIR) {
                    block.setMetadata("block-type",new FixedMetadataValue(KnockbackFFA.getInstance(),block.getType().name()));
                    if (blocks.size() == 1) {
                        Material material = Material.getMaterial(blocks.get(0).split(":")[0]);
                        long timer = Long.parseLong(blocks.get(0).split(":")[1]);
                        block.setType(material);
                        block.setMetadata("block-type",new FixedMetadataValue(KnockbackFFA.getInstance(),"trail"));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                block.setType(Material.getMaterial(block.getMetadata("material").get(0).asString()));
                                block.setMetadata("block-type",new FixedMetadataValue(KnockbackFFA.getInstance(),""));
                                cancel();
                            }
                        }.runTaskTimer(KnockbackFFA.getInstance(), timer * 20L, 1);
                    } else {
                        block.setType(Material.getMaterial(blocks.get(0).split(":")[0]));
                        block.setMetadata("block-type",new FixedMetadataValue(KnockbackFFA.getInstance(),"trail"));
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
                                    block.setMetadata("block-type",new FixedMetadataValue(KnockbackFFA.getInstance(),""));
                                    cancel();
                                }
                            }
                        }.runTaskTimer(KnockbackFFA.getInstance(), 1L, Long.parseLong(blocks.get(0).split(":")[1]));
                    }
                }
        }
    }
    public void setMoveEvent(PlayerMoveEvent e){
        this.e = e;
    }
}
