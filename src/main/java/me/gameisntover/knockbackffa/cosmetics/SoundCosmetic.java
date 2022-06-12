package me.gameisntover.knockbackffa.cosmetics;

import me.gameisntover.knockbackffa.kit.KnockbackFFALegacy;
import me.gameisntover.knockbackffa.util.Knocker;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SoundCosmetic extends Cosmetic {
    private final List<String> sounds;

    public SoundCosmetic(String name, String description, Double price, Material icon, List<String> sounds) {
        super(CosmeticType.SOUND, name, description, price, icon);
        this.sounds = sounds;
    }

    @Override
    public void onLoad(Knocker knocker) {
        final int[] i = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                String[] soundFormat = sounds.get(i[0]).split(":");
                String sound = soundFormat[0];
                float pitch = Float.parseFloat(soundFormat[1]);
                float volume = Float.parseFloat(soundFormat[2]);
                knocker.playSound(Sound.valueOf(sound), volume, pitch);
                if (i[0] == sounds.size() - 1) cancel();
                else i[0]++;
            }
        }.runTaskTimer(KnockbackFFALegacy.getInstance(), 1L, Long.parseLong(sounds.get(i[0]).split(":")[3]));
    }
}
