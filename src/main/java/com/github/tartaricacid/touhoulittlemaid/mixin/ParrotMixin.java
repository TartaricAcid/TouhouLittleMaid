package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.PlayMaidSoundAtPosPackage;
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
public abstract class ParrotMixin extends ShoulderRidingEntity implements VariantHolder<Parrot.Variant>, FlyingAnimal {

    private static Logger logger = TouhouLittleMaid.LOGGER;

    @Shadow @Final
    private static Predicate<Mob> NOT_PARROT_PREDICATE;

    @Shadow
    private static SoundEvent getImitatedSound(EntityType<?> type) {
        throw new AssertionError("Shadow method not implemented");
    }


    protected ParrotMixin(EntityType<? extends ShoulderRidingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow @Final
    static Map<EntityType<?>, SoundEvent> MOB_SOUND_MAP;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onClassInit(CallbackInfo ci) {
        //maidSound实际上不会被访问
        EntityType<EntityMaid> maid = EntityMaid.TYPE;
        SoundEvent maidSound = InitSounds.MAID_IDLE.get();

        MOB_SOUND_MAP.put(maid, maidSound);
    }


    @Inject(method = "imitateNearbyMobs", at = @At("HEAD"), cancellable = true)
    private static void imitateNearbyMobs(Level level, Entity parrot,CallbackInfoReturnable<Boolean> cir) {
        if (parrot.isAlive() && !parrot.isSilent() && level.random.nextInt(2) == 0) {
            List<Mob> list = level.getEntitiesOfClass(Mob.class, parrot.getBoundingBox().inflate((double) 20.0F), NOT_PARROT_PREDICATE);
            if (!list.isEmpty()) {
                Mob mob = (Mob) list.get(level.random.nextInt(list.size()));
                if (!mob.isSilent()) {
                    if(mob instanceof EntityMaid maid){
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
                    } else {
                        SoundEvent soundevent = getImitatedSound(mob.getType());
                        level.playSound((Player) null, parrot.getX(), parrot.getY(), parrot.getZ(), soundevent, parrot.getSoundSource(), 0.7F, getPitch(level.random));
                        cir.setReturnValue(true);
                    }
                }
            }
            cir.setReturnValue(false);
        } else {
            cir.setReturnValue(false);
        }
    }
}