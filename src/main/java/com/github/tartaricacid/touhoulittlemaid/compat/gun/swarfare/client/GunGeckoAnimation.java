package com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.client;

import com.atsuishio.superbwarfare.item.gun.GunItem;
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

import static com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.SWarfareCompat.PISTOL;
import static com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.SWarfareCompat.RPG;

@OnlyIn(Dist.CLIENT)
public class GunGeckoAnimation {
    public static PlayState playGrenadeAnimation(AnimationEvent<GeckoMaidEntity<?>> event, InteractionHand hand) {
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
        IMaid maid = event.getAnimatableEntity().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        if (isMaidCarrying(maid)) {
            return PlayState.STOP;
        }
        Mob entity = maid.asEntity();
        if (!(heldItem.getItem() instanceof GunItem)) {
            return null;
        }
        if (!entity.isSwimming() && entity.getPose() == Pose.SWIMMING) {
            if (Math.abs(event.getLimbSwingAmount()) > 0.05) {
                return getGunTypeAnimation(event, heldItem, "tac:climb:");
            } else {
                return getGunTypeAnimation(event, heldItem, "tac:climbing:");
            }
        }
        if (entity.onGround() && entity.isSprinting()) {
            return getGunTypeAnimation(event, heldItem, "tac:run:");
        }
        return getGunTypeAnimation(event, heldItem, "tac:hold:");
    }

    @NotNull
    private static PlayState getGunTypeAnimation(AnimationEvent<GeckoMaidEntity<?>> event, ItemStack gun, String prefix) {
        ResourceLocation modelId = event.getAnimatableEntity().getAnimationFileLocation();
        IMaid maid = event.getAnimatableEntity().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        Mob entity = maid.asEntity();
        ConditionTAC condition = ConditionManager.getTAC(modelId);
        if (condition != null) {
            ItemStack stack = entity.getMainHandItem();
            String name = condition.doTest(stack, prefix);
            if (StringUtils.isNoneBlank(name)) {
                return playAnimation(event, name, ILoopType.EDefaultLoopTypes.LOOP);
            }
        }
        if (gun.is(PISTOL)) {
            return playLoopAnimation(event, prefix + "pistol");
        }
        if (gun.is(RPG)) {
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

    private static boolean isMaidCarrying(IMaid maid) {
        Mob entity = maid.asEntity();
        return entity.getVehicle() instanceof Player;
    }
}
