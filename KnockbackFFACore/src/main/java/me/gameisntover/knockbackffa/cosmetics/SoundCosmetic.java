package me.gameisntover.knockbackffa.cosmetics;

import me.gameisntover.knockbackffa.KnockbackFFA;
import me.gameisntover.knockbackffa.player.Knocker;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SoundCosmetic extends Cosmetic {
    private final List<String> sounds;
    public SoundCosmetic(String name,Knocker knocker, List<String> sounds) {
        super(name,knocker,true);
        this.sounds = sounds;
    }
    public SoundCosmetic(String name,Knocker knocker,List<String> sounds,boolean no) {
        super(name,knocker,no);
        this.sounds = sounds;
    }
    @Override
    public CosmeticType getType() {
        return CosmeticType.SOUND;
    }

    @Override
    public void onLoad() {
        final int[] i = {0};
            new BukkitRunnable() {
                @Override
                public void run() {
                        String[] soundFormat = sounds.get(i[0]).split(":");
                        String sound = soundFormat[0];
                        float pitch = Float.parseFloat(soundFormat[1]);
                        float volume = Float.parseFloat(soundFormat[2]);
                        owner.playSound(Sound.valueOf(sound), volume, pitch);
                        if (i[0] == sounds.size() - 1) cancel();
                        else i[0]++;
                }
            }.runTaskTimer(KnockbackFFA.getInstance(),1L,Long.parseLong(sounds.get(i[0]).split(":")[3]));
    }
}
