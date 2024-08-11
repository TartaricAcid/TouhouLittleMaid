package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.condition.*;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.compat.tacz.TacCompat;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.PlayState;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.AnimationBuilder;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public final class AnimationManager {
    private static AnimationManager MANAGER;
    @SuppressWarnings("unchecked")
    private final ReferenceArrayList<AnimationState>[] data = new ReferenceArrayList[Priority.LOWEST + 1];

    public AnimationManager() {
        for (int i = 0; i < data.length; i++) {
            data[i] = new ReferenceArrayList<>(6);
        }
    }

    public static AnimationManager getInstance() {
        if (MANAGER == null) {
            MANAGER = new AnimationManager();
        }
        return MANAGER;
    }

    @Nonnull
    public static PlayState playLoopAnimation(AnimationEvent<?> event, String animationName) {
        return playAnimation(event, animationName, ILoopType.EDefaultLoopTypes.LOOP);
    }

    @Nonnull
    private static PlayState playAnimation(AnimationEvent<?> event, String animationName, ILoopType loopType) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName, loopType));
        return PlayState.CONTINUE;
    }

    @Nonnull
    private static PlayState playAnimation(AnimationEvent<?> event, String animationName) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName));
        return PlayState.CONTINUE;
    }

    public void register(AnimationState state) {
        data[state.getPriority()].add(state);
    }

    public PlayState predicateParallel(AnimationEvent<?> event, String animationName) {
        if (Minecraft.getInstance().isPaused()) {
            return PlayState.STOP;
        }
        return playLoopAnimation(event, animationName);
    }

    @NotNull
    public PlayState predicateMain(AnimationEvent<GeckoMaidEntity<?>> event) {
        IMaid maid = event.getAnimatableEntity().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        for (int i = Priority.HIGHEST; i <= Priority.LOWEST; i++) {
            // 载具动画单独检查
            if (i == Priority.HIGH) {
                PlayState vehicleAnimation = getVehicleAnimation(event);
                if (vehicleAnimation != null) {
                    return vehicleAnimation;
                }
            }
            for (AnimationState state : data[i]) {
                if (state.getPredicate().test(maid, event)) {
                    String animationName = state.getAnimationName();
                    ILoopType loopType = state.getLoopType();
                    PlayState gunMainAnimation = TacCompat.playGunMainAnimation(maid, event, animationName, loopType);
                    return Objects.requireNonNullElseGet(gunMainAnimation, () -> playAnimation(event, animationName, loopType));
                }
            }
        }
        return PlayState.STOP;
    }

    public PlayState predicateOffhandHold(AnimationEvent<GeckoMaidEntity<?>> event) {
        IMaid maid = event.getAnimatableEntity().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        if (!maid.asEntity().swinging && !maid.asEntity().isUsingItem()) {
            ItemStack offhandItem = maid.asEntity().getItemInHand(InteractionHand.OFF_HAND);
            if (offhandItem.is(Items.CROSSBOW) && CrossbowItem.isCharged(offhandItem)) {
                return playAnimation(event, "hold_offhand:charged_crossbow", ILoopType.EDefaultLoopTypes.LOOP);
            }
        }
        if (checkSwingAndUse(maid, InteractionHand.OFF_HAND)) {
            ItemStack offhandItem = maid.asEntity().getItemInHand(InteractionHand.OFF_HAND);
            if (!isSameItem(maid, offhandItem, InteractionHand.OFF_HAND)) {
                maid.getHandItemsForAnimation()[InteractionHand.OFF_HAND.ordinal()] = offhandItem;
                playAnimation(event, "empty", ILoopType.EDefaultLoopTypes.LOOP);
            }

            ResourceLocation id = event.getAnimatableEntity().getAnimationFileLocation();
            ConditionalHold conditionalHold = ConditionManager.getHoldOffhand(id);
            if (conditionalHold != null) {
                String name = conditionalHold.doTest(maid, InteractionHand.OFF_HAND);
                if (StringUtils.isNoneBlank(name)) {
                    return playAnimation(event, name, ILoopType.EDefaultLoopTypes.LOOP);
                }
            }
        }
        return PlayState.STOP;
    }

    public PlayState predicateMainhandHold(AnimationEvent<GeckoMaidEntity<?>> event) {
        IMaid maid = event.getAnimatableEntity().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        if (!maid.asEntity().swinging && !maid.asEntity().isUsingItem()) {
            ItemStack mainHandItem = maid.asEntity().getItemInHand(InteractionHand.MAIN_HAND);
            PlayState gunHoldAnimation = TacCompat.playGunHoldAnimation(mainHandItem, event);
            if (gunHoldAnimation != null) {
                return gunHoldAnimation;
            }
            if (mainHandItem.is(Items.CROSSBOW) && CrossbowItem.isCharged(mainHandItem)) {
                return playAnimation(event, "hold_mainhand:charged_crossbow", ILoopType.EDefaultLoopTypes.LOOP);
            }
        }

        if (checkSwingAndUse(maid, InteractionHand.MAIN_HAND)) {
            ItemStack mainHandItem = maid.asEntity().getItemInHand(InteractionHand.MAIN_HAND);
            if (!isSameItem(maid, mainHandItem, InteractionHand.MAIN_HAND)) {
                maid.getHandItemsForAnimation()[InteractionHand.MAIN_HAND.ordinal()] = mainHandItem;
                playAnimation(event, "empty", ILoopType.EDefaultLoopTypes.LOOP);
            }

            ResourceLocation id = event.getAnimatableEntity().getAnimationFileLocation();
            ConditionalHold conditionalHold = ConditionManager.getHoldMainhand(id);
            if (conditionalHold != null) {
                String name = conditionalHold.doTest(maid, InteractionHand.MAIN_HAND);
                if (StringUtils.isNoneBlank(name)) {
                    return playAnimation(event, name, ILoopType.EDefaultLoopTypes.LOOP);
                }
            }
        }
        return PlayState.STOP;
    }

    private boolean isSameItem(IMaid maid, ItemStack maidItem, InteractionHand hand) {
        ItemStack preItem = maid.getHandItemsForAnimation()[hand.ordinal()];
        if (preItem.isDamaged()) {
            return ItemStack.isSame(maidItem, preItem);
        }
        return ItemStack.matches(maidItem, preItem);
    }

    public PlayState predicateSwing(AnimationEvent<GeckoMaidEntity<?>> event) {
        IMaid maid = event.getAnimatableEntity().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        if (maid.asEntity().swinging && !maid.asEntity().isSleeping()) {
            if (maid.asEntity().swingTime == 0) {
                // 空动画用于重置 PLAY_ONCE 动画
                playAnimation(event, "empty", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
            }
            ResourceLocation id = event.getAnimatableEntity().getAnimationFileLocation();
            ConditionalSwing conditionalSwing = (maid.asEntity().swingingArm == InteractionHand.MAIN_HAND) ? ConditionManager.getSwingMainhand(id) : ConditionManager.getSwingOffhand(id);
            if (conditionalSwing != null) {
                String name = conditionalSwing.doTest(maid, maid.asEntity().swingingArm);
                if (StringUtils.isNoneBlank(name)) {
                    return playAnimation(event, name, ILoopType.EDefaultLoopTypes.PLAY_ONCE);
                }
            }
            String defaultSwing = (maid.asEntity().swingingArm == InteractionHand.MAIN_HAND) ? "swing_hand" : "swing_offhand";
            return playAnimation(event, defaultSwing, ILoopType.EDefaultLoopTypes.PLAY_ONCE);
        }
        return PlayState.CONTINUE;
    }

    public PlayState predicateUse(AnimationEvent<GeckoMaidEntity<?>> event) {
        IMaid maid = event.getAnimatableEntity().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        if (maid.asEntity().isUsingItem() && !maid.asEntity().isSleeping()) {
            if (maid.asEntity().getTicksUsingItem() == 1) {
                playAnimation(event, "empty", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
            }
            if (maid.asEntity().getUsedItemHand() == InteractionHand.MAIN_HAND) {
                ResourceLocation id = event.getAnimatableEntity().getAnimationFileLocation();
                ConditionalUse conditionalUse = ConditionManager.getUseMainhand(id);
                if (conditionalUse != null) {
                    String name = conditionalUse.doTest(maid, InteractionHand.MAIN_HAND);
                    if (StringUtils.isNoneBlank(name)) {
                        return playAnimation(event, name, ILoopType.EDefaultLoopTypes.LOOP);
                    }
                }
                return playAnimation(event, "use_mainhand", ILoopType.EDefaultLoopTypes.LOOP);
            } else {
                ResourceLocation id = event.getAnimatableEntity().getAnimationFileLocation();
                ConditionalUse conditionalUse = ConditionManager.getUseOffhand(id);
                if (conditionalUse != null) {
                    String name = conditionalUse.doTest(maid, InteractionHand.OFF_HAND);
                    if (StringUtils.isNoneBlank(name)) {
                        return playAnimation(event, name, ILoopType.EDefaultLoopTypes.LOOP);
                    }
                }
                return playAnimation(event, "use_offhand", ILoopType.EDefaultLoopTypes.LOOP);
            }
        }
        return PlayState.STOP;
    }

    public PlayState predicateBeg(AnimationEvent<GeckoMaidEntity<?>> event) {
        IMaid maid = event.getAnimatableEntity().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        if (maid.isBegging()) {
            return playAnimation(event, "beg", ILoopType.EDefaultLoopTypes.LOOP);
        }
        return PlayState.STOP;
    }

    public <T extends Mob> PlayState predicateArmor(AnimationEvent<GeckoMaidEntity<T>> event, EquipmentSlot slot) {
        IMaid maid = event.getAnimatableEntity().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        ItemStack itemBySlot = maid.asEntity().getItemBySlot(slot);
        if (itemBySlot.isEmpty()) {
            return PlayState.STOP;
        }

        ResourceLocation id = event.getAnimatableEntity().getAnimationFileLocation();
        ConditionArmor conditionArmor = ConditionManager.getArmor(id);
        if (conditionArmor != null) {
            String name = conditionArmor.doTest(maid, slot);
            if (StringUtils.isNoneBlank(name)) {
                return playAnimation(event, name, ILoopType.EDefaultLoopTypes.LOOP);
            }
        }

        ResourceLocation animation = event.getAnimatableEntity().getAnimationFileLocation();
        String defaultName = slot.getName() + ":default";
        if (GeckoLibCache.getInstance().getAnimations().get(animation).animations().containsKey(defaultName)) {
            return playAnimation(event, defaultName, ILoopType.EDefaultLoopTypes.LOOP);
        }
        return PlayState.STOP;
    }

    @Nullable
    public PlayState getVehicleAnimation(AnimationEvent<GeckoMaidEntity<?>> event) {
        Mob mob = event.getAnimatableEntity().getMaid().asEntity();
        if (mob == null) {
            return null;
        }
        Entity vehicle = mob.getVehicle();
        if (vehicle == null || !vehicle.isAlive()) {
            return null;
        }
        ResourceLocation id = event.getAnimatableEntity().getAnimationFileLocation();
        ConditionalVehicle vehicleCondition = ConditionManager.getVehicle(id);
        if (vehicleCondition != null) {
            String name = vehicleCondition.doTest(mob);
            if (StringUtils.isNoneBlank(name)) {
                return playAnimation(event, name, ILoopType.EDefaultLoopTypes.LOOP);
            }
        }
        return null;
    }

    public PlayState predicatePassengerAnimation(AnimationEvent<GeckoMaidEntity<?>> event) {
        Mob mob = event.getAnimatableEntity().getMaid().asEntity();
        if (mob == null) {
            return PlayState.STOP;
        }
        Entity passenger = mob.getFirstPassenger();
        if (passenger == null || !passenger.isAlive()) {
            return PlayState.STOP;
        }

        ResourceLocation id = event.getAnimatableEntity().getAnimationFileLocation();
        ConditionalPassenger conditionalPassenger = ConditionManager.getPassenger(id);
        if (conditionalPassenger != null) {
            String name = conditionalPassenger.doTest(mob);
            if (StringUtils.isNoneBlank(name)) {
                return playAnimation(event, name, ILoopType.EDefaultLoopTypes.LOOP);
            }
        }
        return PlayState.STOP;
    }

    private boolean checkSwingAndUse(IMaid maid, InteractionHand hand) {
        if (maid.asEntity().swinging && maid.asEntity().swingingArm == hand) {
            return false;
        }
        return !maid.asEntity().isUsingItem() || maid.asEntity().getUsedItemHand() != hand;
    }
}