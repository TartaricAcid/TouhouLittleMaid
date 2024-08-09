package com.github.tartaricacid.touhoulittlemaid.compat.tacz.client;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.condition.ConditionManager;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.condition.ConditionTAC;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.PlayState;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.AnimationBuilder;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.file.AnimationFile;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.item.GunTabType;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.resource.index.CommonGunIndex;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class GunGeckoAnimation {
    public static PlayState playGrenadeAnimation(AnimationEvent<GeckoMaidEntity<?>> event, InteractionHand hand) {
        // TODO 手雷还没有
        if (hand == InteractionHand.MAIN_HAND) {
            return playLoopAnimation(event, "tac:mainhand:grenade");
        }
        return playLoopAnimation(event, "tac:offhand:grenade");
    }

    /**
     * tac:idle
     * tac:run
     * tac:walk
     */
    public static PlayState playGunMainAnimation(AnimationEvent<GeckoMaidEntity<?>> event, String animationName, ILoopType loopType) {
        String tacName = "tac:" + animationName;
        var animatable = event.getAnimatableEntity();
        ResourceLocation animation = animatable.getAnimationFileLocation();
        AnimationFile animationFile = GeckoLibCache.getInstance().getAnimations().get(animation);
        if (!isMaidCarrying(animatable.getMaid()) && animationFile.animations().containsKey(tacName)) {
            return playAnimation(event, tacName, loopType);
        }
        return playAnimation(event, animationName, loopType);
    }

    /**
     * tac:hold:pistol
     * tac:aim:pistol
     * tac:reload:pistol
     * tac:aim_shoot:pistol
     * tac:hold_shoot:pistol
     * tac:run:pistol
     */
    public static PlayState playGunHoldAnimation(AnimationEvent<GeckoMaidEntity<?>> event, ItemStack heldItem) {
        IGun gun = IGun.getIGunOrNull(heldItem);
        if (gun == null) {
            return PlayState.STOP;
        }
        Optional<CommonGunIndex> indexOptional = TimelessAPI.getCommonGunIndex(gun.getGunId(heldItem));
        if (indexOptional.isEmpty()) {
            return PlayState.STOP;
        }
        CommonGunIndex gunIndex = indexOptional.get();
        String weaponType = gunIndex.getType();
        IMaid maid = event.getAnimatableEntity().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        if (isMaidCarrying(maid)) {
            return PlayState.STOP;
        }
        Mob entity = maid.asEntity();
        IGunOperator operator = IGunOperator.fromLivingEntity(entity);
        long fireTick = operator.getSynShootCoolDown();

        if (!entity.isSwimming() && entity.getPose() == Pose.SWIMMING) {
            if (Math.abs(event.getLimbSwingAmount()) > 0.05) {
                return getGunTypeAnimation(event, weaponType, "tac:climb:");
            } else {
                if (fireTick > 0) {
                    return getGunTypeAnimation(event, weaponType, "tac:climbing:fire:");
                }
                return getGunTypeAnimation(event, weaponType, "tac:climbing:");
            }
        }

        float reloadProgress = operator.getSynReloadState().getCountDown();
        if (reloadProgress > 0) {
            if (reloadProgress == 1) {
                event.getController().shouldResetTick = true;
                event.getController().adjustTick(0);
            }
            return getGunTypeAnimation(event, weaponType, "tac:reload:");
        }

        float aimProgress = operator.getSynAimingProgress();
        if (aimProgress > 0) {
            if (fireTick > 0) {
                return getGunTypeAnimation(event, weaponType, "tac:aim:fire:");
            }
            return getGunTypeAnimation(event, weaponType, "tac:aim:");
        } else {
            if (entity.onGround() && entity.isSprinting()) {
                return getGunTypeAnimation(event, weaponType, "tac:run:");
            }
            if (fireTick > 0) {
                return getGunTypeAnimation(event, weaponType, "tac:hold:fire:");
            }
            return getGunTypeAnimation(event, weaponType, "tac:hold:");
        }
    }

    @NotNull
    private static PlayState getGunTypeAnimation(AnimationEvent<GeckoMaidEntity<?>> event, String weaponType, String prefix) {
        ResourceLocation modelId = event.getAnimatableEntity().getAnimationFileLocation();
        IMaid maid = event.getAnimatableEntity().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        Mob entity = maid.asEntity();
        ConditionTAC conditionTAC = ConditionManager.getTAC(modelId);
        if (conditionTAC != null) {
            ItemStack stack = entity.getMainHandItem();
            String name = conditionTAC.doTest(stack, prefix);
            if (StringUtils.isNoneBlank(name)) {
                return playAnimation(event, name, ILoopType.EDefaultLoopTypes.LOOP);
            }
        }
        if (isType(weaponType, GunTabType.PISTOL)) {
            return playLoopAnimation(event, prefix + "pistol");
        }
        if (isType(weaponType, GunTabType.RPG)) {
            return playLoopAnimation(event, prefix + "rpg");
        }
        return playLoopAnimation(event, prefix + "rifle");
    }

    @NotNull
    private static PlayState playLoopAnimation(AnimationEvent<?> event, String animationName) {
        return playAnimation(event, animationName, ILoopType.EDefaultLoopTypes.LOOP);
    }

    @NotNull
    private static PlayState playAnimation(AnimationEvent<?> event, String animationName, ILoopType loopType) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName, loopType));
        return PlayState.CONTINUE;
    }

    private static boolean isType(String type, GunTabType tabType) {
        return type.equals(tabType.name().toLowerCase(Locale.ENGLISH));
    }

    private static boolean isMaidCarrying(IMaid maid) {
        Mob entity = maid.asEntity();
        return entity.getVehicle() instanceof Player;
    }
}
