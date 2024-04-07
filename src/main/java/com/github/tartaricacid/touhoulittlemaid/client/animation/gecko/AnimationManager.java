package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko;

import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.condition.*;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.PlayState;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.AnimationBuilder;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

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
    public static <P extends IAnimatable> PlayState playLoopAnimation(AnimationEvent<P> event, String animationName) {
        return playAnimation(event, animationName, ILoopType.EDefaultLoopTypes.LOOP);
    }

    @Nonnull
    private static <P extends IAnimatable> PlayState playAnimation(AnimationEvent<P> event, String animationName, ILoopType loopType) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName, loopType));
        return PlayState.CONTINUE;
    }

    @Nonnull
    private static <P extends IAnimatable> PlayState playAnimation(AnimationEvent<P> event, String animationName) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName));
        return PlayState.CONTINUE;
    }

    public void register(AnimationState state) {
        data[state.getPriority()].add(state);
    }

    public PlayState predicateParallel(AnimationEvent<GeckoMaidEntity> event, String animationName) {
        if (Minecraft.getInstance().isPaused()) {
            return PlayState.STOP;
        }
        return playLoopAnimation(event, animationName);
    }

    @NotNull
    public PlayState predicateMain(AnimationEvent<GeckoMaidEntity> event) {
        EntityMaid maid = event.getAnimatable().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        for (int i = Priority.HIGHEST; i <= Priority.LOWEST; i++) {
            for (AnimationState state : data[i]) {
                if (state.getPredicate().test(maid, event)) {
                    String animationName = state.getAnimationName();
                    ILoopType loopType = state.getLoopType();
                    return playAnimation(event, animationName, loopType);
                }
            }
        }
        return PlayState.STOP;
    }

    public PlayState predicateOffhandHold(AnimationEvent<GeckoMaidEntity> event) {
        EntityMaid maid = event.getAnimatable().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        if (!maid.swinging && !maid.isUsingItem()) {
            ItemStack offhandItem = maid.getItemInHand(InteractionHand.OFF_HAND);
            if (offhandItem.is(Items.CROSSBOW) && CrossbowItem.isCharged(offhandItem)) {
                return playAnimation(event, "hold_offhand:charged_crossbow", ILoopType.EDefaultLoopTypes.LOOP);
            }
        }
        if (checkSwingAndUse(maid, InteractionHand.OFF_HAND)) {
            ItemStack offhandItem = maid.getItemInHand(InteractionHand.OFF_HAND);
            if (!isSameItem(maid, offhandItem, InteractionHand.OFF_HAND)) {
                maid.handItemsForAnimation[InteractionHand.OFF_HAND.ordinal()] = offhandItem;
                playAnimation(event, "empty", ILoopType.EDefaultLoopTypes.LOOP);
            }

            ResourceLocation id = event.getAnimatable().getAnimation();
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

    public PlayState predicateMainhandHold(AnimationEvent<GeckoMaidEntity> event) {
        EntityMaid maid = event.getAnimatable().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        if (!maid.swinging && !maid.isUsingItem()) {
            ItemStack mainHandItem = maid.getItemInHand(InteractionHand.MAIN_HAND);
            if (mainHandItem.is(Items.CROSSBOW) && CrossbowItem.isCharged(mainHandItem)) {
                return playAnimation(event, "hold_mainhand:charged_crossbow", ILoopType.EDefaultLoopTypes.LOOP);
            }
        }

        if (checkSwingAndUse(maid, InteractionHand.MAIN_HAND)) {
            ItemStack mainHandItem = maid.getItemInHand(InteractionHand.MAIN_HAND);
            if (!isSameItem(maid, mainHandItem, InteractionHand.MAIN_HAND)) {
                maid.handItemsForAnimation[InteractionHand.MAIN_HAND.ordinal()] = mainHandItem;
                playAnimation(event, "empty", ILoopType.EDefaultLoopTypes.LOOP);
            }

            ResourceLocation id = event.getAnimatable().getAnimation();
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

    private boolean isSameItem(EntityMaid maid, ItemStack maidItem, InteractionHand hand) {
        ItemStack preItem = maid.handItemsForAnimation[hand.ordinal()];
        if (preItem.isDamaged()) {
            return ItemStack.isSame(maidItem, preItem);
        }
        return ItemStack.matches(maidItem, preItem);
    }

    public PlayState predicateSwing(AnimationEvent<GeckoMaidEntity> event) {
        EntityMaid maid = event.getAnimatable().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        if (maid.swinging && !maid.isSleeping()) {
            if (maid.swingTime == 0) {
                // 空动画用于重置 PLAY_ONCE 动画
                playAnimation(event, "empty", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
            }
            ResourceLocation id = event.getAnimatable().getAnimation();
            ConditionalSwing conditionalSwing = (maid.swingingArm == InteractionHand.MAIN_HAND) ? ConditionManager.getSwingMainhand(id) : ConditionManager.getSwingOffhand(id);
            if (conditionalSwing != null) {
                String name = conditionalSwing.doTest(maid, maid.swingingArm);
                if (StringUtils.isNoneBlank(name)) {
                    return playAnimation(event, name, ILoopType.EDefaultLoopTypes.PLAY_ONCE);
                }
            }
            String defaultSwing = (maid.swingingArm == InteractionHand.MAIN_HAND) ? "swing_hand" : "swing_offhand";
            return playAnimation(event, defaultSwing, ILoopType.EDefaultLoopTypes.PLAY_ONCE);
        }
        return PlayState.CONTINUE;
    }

    public PlayState predicateUse(AnimationEvent<GeckoMaidEntity> event) {
        EntityMaid maid = event.getAnimatable().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        if (maid.isUsingItem() && !maid.isSleeping()) {
            if (maid.getTicksUsingItem() == 1) {
                playAnimation(event, "empty", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
            }
            if (maid.getUsedItemHand() == InteractionHand.MAIN_HAND) {
                ResourceLocation id = event.getAnimatable().getAnimation();
                ConditionalUse conditionalUse = ConditionManager.getUseMainhand(id);
                if (conditionalUse != null) {
                    String name = conditionalUse.doTest(maid, InteractionHand.MAIN_HAND);
                    if (StringUtils.isNoneBlank(name)) {
                        return playAnimation(event, name, ILoopType.EDefaultLoopTypes.LOOP);
                    }
                }
                return playAnimation(event, "use_mainhand", ILoopType.EDefaultLoopTypes.LOOP);
            } else {
                ResourceLocation id = event.getAnimatable().getAnimation();
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

    public PlayState predicateBeg(AnimationEvent<GeckoMaidEntity> event) {
        EntityMaid maid = event.getAnimatable().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        if (maid.isBegging()) {
            return playAnimation(event, "beg", ILoopType.EDefaultLoopTypes.LOOP);
        }
        return PlayState.STOP;
    }

    public PlayState predicateArmor(AnimationEvent<GeckoMaidEntity> event, EquipmentSlot slot) {
        EntityMaid maid = event.getAnimatable().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        ItemStack itemBySlot = maid.getItemBySlot(slot);
        if (itemBySlot.isEmpty()) {
            return PlayState.STOP;
        }

        ResourceLocation id = event.getAnimatable().getAnimation();
        ConditionArmor conditionArmor = ConditionManager.getArmor(id);
        if (conditionArmor != null) {
            String name = conditionArmor.doTest(maid, slot);
            if (StringUtils.isNoneBlank(name)) {
                return playAnimation(event, name, ILoopType.EDefaultLoopTypes.LOOP);
            }
        }

        ResourceLocation animation = event.getAnimatable().getAnimation();
        String defaultName = slot.getName() + ":default";
        if (GeckoLibCache.getInstance().getAnimations().get(animation).animations().containsKey(defaultName)) {
            return playAnimation(event, defaultName, ILoopType.EDefaultLoopTypes.LOOP);
        }
        return PlayState.STOP;
    }

    private boolean checkSwingAndUse(EntityMaid maid, InteractionHand hand) {
        if (maid.swinging && maid.swingingArm == hand) {
            return false;
        }
        return !maid.isUsingItem() || maid.getUsedItemHand() != hand;
    }
}