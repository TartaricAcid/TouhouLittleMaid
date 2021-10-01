package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IFeedTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFeedOwnerTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class TaskFeedOwner implements IFeedTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "feed");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.COOKED_BEEF.getDefaultInstance();
    }

    @Override
    public boolean isFood(ItemStack stack, PlayerEntity owner) {
        if (stack.getItem() == Items.MILK_BUCKET) {
            for (EffectInstance effect : owner.getActiveEffects()) {
                if (isHarmfulEffect(effect) && effect.getDuration() > 60 && effect.isCurativeItem(stack)) {
                    return true;
                }
            }
            return false;
        }
        if (stack.getItem().getFoodProperties() != null) {
            Food food = stack.getItem().getFoodProperties();
            return food.getEffects().isEmpty() ||
                    food.getEffects().stream().noneMatch(pair -> isHarmfulEffect(pair.getFirst()));
        }
        return false;
    }

    @Override
    public Priority getPriority(ItemStack stack, PlayerEntity owner) {
        if (stack.getItem() == Items.MILK_BUCKET) {
            return Priority.HIGH;
        }

        if (stack.getItem() == Items.GOLDEN_APPLE) {
            if (owner.getHealth() * 2 < owner.getMaxHealth()) {
                return Priority.HIGH;
            } else {
                return Priority.LOWEST;
            }
        }

        if (stack.getItem().getFoodProperties() != null) {
            Food food = stack.getItem().getFoodProperties();
            int heal = food.getNutrition();
            int hunger = 20 - owner.getFoodData().getFoodLevel();
            if (heal == hunger) {
                return Priority.HIGH;
            } else if (heal > hunger) {
                return Priority.LOWEST;
            } else {
                return Priority.LOW;
            }
        }

        return Priority.NONE;
    }

    @Override
    public ItemStack feed(ItemStack stack, PlayerEntity owner) {
        if (stack.getUseAnimation() == UseAction.DRINK) {
            owner.level.playSound(null, owner, stack.getDrinkingSound(), SoundCategory.NEUTRAL,
                    0.5f, owner.level.getRandom().nextFloat() * 0.1f + 0.9f);
        }
        return stack.getItem().finishUsingItem(stack, owner.level, owner);
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_FEED.get(), 0.1f);
    }

    @Override
    public List<Pair<Integer, Task<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        return Lists.newArrayList(Pair.of(5, new MaidFeedOwnerTask(this, 2, 0.6f)));
    }

    @Override
    public List<ITextComponent> getDescription(EntityMaid maid) {
        return Collections.emptyList();
    }

    private boolean isHarmfulEffect(EffectInstance effect) {
        return effect.getEffect().getCategory() == EffectType.HARMFUL;
    }
}
