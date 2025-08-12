package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.PlayMaidSoundAtPosPackage;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static net.minecraft.world.entity.animal.Parrot.getPitch;

@Mixin(Parrot.class)
public abstract class ParrotMixin {
    @Inject(
            method = "imitateNearbyMobs",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;isSilent()Z"
            ),
            cancellable = true
    )
    private static void mobIsSilent(Level level, Entity parrot, CallbackInfoReturnable<Boolean> cir, @Local Mob mob) {
        // 因为女仆模组对应的客户端 Sound Instance 非常特殊，需要自行处理发包
        if (mob instanceof EntityMaid maid) {
            SoundEvent soundevent = SoundUtil.environmentSound(maid, InitSounds.MAID_IDLE.get(), 0.5f);
            // 服务端发送在鹦鹉坐标播放女仆语音的包
            if (!level.isClientSide) {
                NetworkHandler.sendToNearby(parrot, new PlayMaidSoundAtPosPackage(
                        soundevent.getLocation(), maid.getSoundPackId(),
                        parrot.getX(), parrot.getY(), parrot.getZ(),
                        0.7F, getPitch(level.random)
                ), 16);
            }
            cir.setReturnValue(true);
        }
    }
}