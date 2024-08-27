package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IFeedTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFeedOwnerTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.neoforged.neoforge.common.EffectCures;

import javax.annotation.Nullable;
import java.util.List;

public class TaskFeedOwner implements IFeedTask {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "feed");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.COOKED_BEEF.getDefaultInstance();
    }

    @Override
    public boolean isFood(ItemStack stack, Player owner) {
        if (stack.getItem() == Items.MILK_BUCKET) {
            for (MobEffectInstance effect : owner.getActiveEffects()) {
                if (isHarmfulEffect(effect) && effect.getDuration() > 60 && effect.getCures().contains(EffectCures.MILK)) {
                    return true;
                }
            }
            return false;
        }
        if (stack.getItem().getFoodProperties(stack, owner) != null) {
            FoodProperties food = stack.getItem().getFoodProperties(stack, owner);
            if (food != null) {
                return food.effects().isEmpty() ||
                        food.effects().stream().noneMatch(effect -> isHarmfulEffect(effect.effect()));
            }
        }
        return false;
    }

    @Override
    public Priority getPriority(ItemStack stack, Player owner) {
        if (stack.is(Items.MILK_BUCKET)) {
            return Priority.HIGH;
        }

        // 蜂蜜瓶可以清除中毒效果，所以当玩家拥有中毒效果时，应当优先使用
        if (stack.is(Items.HONEY_BOTTLE) && owner.getActiveEffects().stream().anyMatch(effect -> effect.getCures().contains(EffectCures.HONEY))) {
            return Priority.HIGH;
        }

        if (stack.is(Items.GOLDEN_APPLE)) {
            if (owner.getHealth() * 2 < owner.getMaxHealth()) {
                return Priority.HIGH;
            } else {
                return Priority.LOWEST;
            }
        }

        if (stack.getItem().getFoodProperties(stack, owner) != null) {
            FoodProperties food = stack.getItem().getFoodProperties(stack, owner);
            int heal = 0;
            if (food != null) {
                heal = food.nutrition();
            }
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
    public ItemStack feed(ItemStack stack, Player owner) {
        if (stack.getUseAnimation() == UseAnim.DRINK) {
            owner.level.playSound(null, owner, stack.getDrinkingSound(), SoundSource.NEUTRAL,
                    0.5f, owner.level.getRandom().nextFloat() * 0.1f + 0.9f);
        }
        return stack.getItem().finishUsingItem(stack, owner.level, owner);
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_FEED.get(), 0.3f);
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        return Lists.newArrayList(Pair.of(5, new MaidFeedOwnerTask(this, 2, 0.6f)));
    }

    private boolean isHarmfulEffect(MobEffectInstance effect) {
        return effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL;
    }
}
