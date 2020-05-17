package com.github.tartaricacid.touhoulittlemaid.client.audio;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPortableAudio;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.net.URL;

/**
 * @author TartaricAcid
 */
@SideOnly(Side.CLIENT)
public class PortableAudioSound extends MovingSound {
    private final EntityPortableAudio audio;
    private final URL songUrl;

    public PortableAudioSound(EntityPortableAudio audio, URL songUrl) {
        super(MaidSoundEvent.PORTABLE_AUDIO, SoundCategory.RECORDS);
        this.audio = audio;
        this.songUrl = songUrl;
    }

    @Override
    public void update() {
        if (audio.isEntityAlive() && audio.isPlaying()) {
            this.xPosF = (float) audio.posX;
            this.yPosF = (float) audio.posY;
            this.zPosF = (float) audio.posZ;
            World world = audio.world;
            if (audio.ticksExisted % 8 == 0) {
                for (int i = 0; i < 2; i++) {
                    world.spawnParticle(EnumParticleTypes.NOTE,
                            audio.posX + world.rand.nextDouble() / 2,
                            audio.posY + world.rand.nextDouble() + 0.5,
                            audio.posZ + world.rand.nextDouble() / 2,
                            world.rand.nextGaussian(), world.rand.nextGaussian(), world.rand.nextInt(3));
                }
            }
        } else {
            this.donePlaying = true;
        }
    }

    public URL getSongUrl() {
        return songUrl;
    }
}
