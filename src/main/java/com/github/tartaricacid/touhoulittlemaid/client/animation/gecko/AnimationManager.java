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
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.LinkedList;

public final class AnimationManager {
    private static AnimationManager MANAGER;
    private final Int2ObjectOpenHashMap<LinkedList<AnimationState>> data = new Int2ObjectOpenHashMap<>();

    public static AnimationManager getInstance() {
        if (MANAGER == null) {
            MANAGER = new AnimationManager();
        }
        return MANAGER;
    }

    @Nonnull
    private static <P extends IAnimatable> PlayState playLoopAnimation(AnimationEvent<P> event, String animationName) {
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
        if (data.containsKey(state.getPriority())) {
            data.get(state.getPriority()).add(state);
        } else {
            LinkedList<AnimationState> states = Lists.newLinkedList();
            states.add(state);
            data.put(state.getPriority(), states);
        }
    }

    public PlayState predicateParallel(AnimationEvent<GeckoMaidEntity> event, String animationName) {
        if (Minecraft.getInstance().isPaused()) {
            return PlayState.STOP;
        }
        return playLoopAnimation(event, animationName);
    }

    @Nonnull
    public PlayState predicateMain(AnimationEvent<GeckoMaidEntity> event) {
        EntityMaid maid = event.getAnimatable().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        for (int i = Priority.HIGHEST; i <= Priority.LOWEST; i++) {
            if (!data.containsKey(i)) {
                continue;
            }
            LinkedList<AnimationState> states = data.get(i);
            for (AnimationState state : states) {
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
        if (!maid.getOffhandItem().isEmpty() && checkSwingAndUse(maid, Hand.OFF_HAND)) {
            ResourceLocation id = event.getAnimatable().getAnimation();
            ConditionalHold conditionalHold = ConditionManager.getHoldOffhand(id);
            if (conditionalHold != null) {
                String name = conditionalHold.doTest(maid, Hand.OFF_HAND);
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
            ItemStack mainHandItem = maid.getItemInHand(Hand.MAIN_HAND);
            if (mainHandItem.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(mainHandItem)) {
                return playAnimation(event, "hold_mainhand:charged_crossbow", ILoopType.EDefaultLoopTypes.LOOP);
            }
            ItemStack offhandItem = maid.getItemInHand(Hand.OFF_HAND);
            if (offhandItem.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(offhandItem)) {
                return playAnimation(event, "hold_offhand:charged_crossbow", ILoopType.EDefaultLoopTypes.LOOP);
            }
        }

        if (!maid.getMainHandItem().isEmpty() && checkSwingAndUse(maid, Hand.MAIN_HAND)) {
            ResourceLocation id = event.getAnimatable().getAnimation();
            ConditionalHold conditionalHold = ConditionManager.getHoldMainhand(id);
            if (conditionalHold != null) {
                String name = conditionalHold.doTest(maid, Hand.MAIN_HAND);
                if (StringUtils.isNoneBlank(name)) {
                    return playAnimation(event, name, ILoopType.EDefaultLoopTypes.LOOP);
                }
            }
        }
        return PlayState.STOP;
    }

    public PlayState predicateSwing(AnimationEvent<GeckoMaidEntity> event) {
        EntityMaid maid = event.getAnimatable().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        if (maid.swinging && !maid.isSleeping()) {
            if (maid.swingTime == 0) {
                event.getController().shouldResetTick = true;
                event.getController().adjustTick(0);
            }
            ResourceLocation id = event.getAnimatable().getAnimation();
            ConditionalSwing conditionalSwing = ConditionManager.getSwing(id);
            if (conditionalSwing != null) {
                String name = conditionalSwing.doTest(maid, maid.swingingArm);
                if (StringUtils.isNoneBlank(name)) {
                    return playAnimation(event, name, ILoopType.EDefaultLoopTypes.LOOP);
                }
            }
            return playAnimation(event, "swing_hand", ILoopType.EDefaultLoopTypes.LOOP);
        }
        return PlayState.STOP;
    }

    public PlayState predicateUse(AnimationEvent<GeckoMaidEntity> event) {
        EntityMaid maid = event.getAnimatable().getMaid();
        if (maid == null) {
            return PlayState.STOP;
        }
        if (maid.isUsingItem() && !maid.isSleeping()) {
            if (maid.getTicksUsingItem() == 1) {
                event.getController().shouldResetTick = true;
                event.getController().adjustTick(0);
            }
            if (maid.getUsedItemHand() == Hand.MAIN_HAND) {
                ResourceLocation id = event.getAnimatable().getAnimation();
                ConditionalUse conditionalUse = ConditionManager.getUseMainhand(id);
                if (conditionalUse != null) {
                    String name = conditionalUse.doTest(maid, Hand.MAIN_HAND);
                    if (StringUtils.isNoneBlank(name)) {
                        return playAnimation(event, name, ILoopType.EDefaultLoopTypes.LOOP);
                    }
                }
                return playAnimation(event, "use_mainhand", ILoopType.EDefaultLoopTypes.LOOP);
            } else {
                ResourceLocation id = event.getAnimatable().getAnimation();
                ConditionalUse conditionalUse = ConditionManager.getUseOffhand(id);
                if (conditionalUse != null) {
                    String name = conditionalUse.doTest(maid, Hand.OFF_HAND);
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

    public PlayState predicateArmor(AnimationEvent<GeckoMaidEntity> event, EquipmentSlotType slot) {
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

    private boolean checkSwingAndUse(EntityMaid maid, Hand hand) {
        if (maid.swinging && maid.swingingArm == hand) {
            return false;
        }
        return !maid.isUsingItem() || maid.getUsedItemHand() != hand;
    }
}