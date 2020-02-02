package com.github.tartaricacid.touhoulittlemaid.client.audio;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTrolleyAudio;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2020/2/1 22:15
 **/
@SideOnly(Side.CLIENT)
public class TrolleyAudioSound extends MovingSound {
    private EntityTrolleyAudio trolleyAudio;

    public TrolleyAudioSound(SoundEvent soundEvent, EntityTrolleyAudio trolleyAudio) {
        super(soundEvent, SoundCategory.RECORDS);
        this.trolleyAudio = trolleyAudio;
    }

    @Override
    public void update() {
        if (!trolleyAudio.isEntityAlive() || trolleyAudio.isStop()) {
            this.donePlaying = true;
        } else {
            this.xPosF = (float) trolleyAudio.posX;
            this.yPosF = (float) trolleyAudio.posY;
            this.zPosF = (float) trolleyAudio.posZ;
            World world = trolleyAudio.world;
            if (trolleyAudio.ticksExisted % 8 == 0) {
                for (int i = 0; i < 2; i++) {
                    world.spawnParticle(EnumParticleTypes.NOTE,
                            trolleyAudio.posX + world.rand.nextDouble() / 2,
                            trolleyAudio.posY + world.rand.nextDouble() + 0.5,
                            trolleyAudio.posZ + world.rand.nextDouble() / 2,
                            world.rand.nextGaussian(), world.rand.nextGaussian(), world.rand.nextInt(3));
                }
            }
        }
    }
}
