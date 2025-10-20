package com.github.tartaricacid.touhoulittlemaid.compat.kubejs.register.builder;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.meal.MaidMealType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import com.github.tartaricacid.touhoulittlemaid.util.functional.QuadConsumer;
import com.github.tartaricacid.touhoulittlemaid.util.functional.QuadFunction;
import com.github.tartaricacid.touhoulittlemaid.util.functional.TriConsumer;
import com.github.tartaricacid.touhoulittlemaid.util.functional.TriFunction;
import com.google.common.collect.Maps;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableFloat;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.BiConsumer;

public class MaidBaubleBuilder {
    private final Map<Item, CustomKubeJSBauble> baubles = Maps.newHashMap();

    @Info("""
            Bind an item to a maid bauble. And specify a callback function that will be called every tick of the maid. <br>
            将物品与女仆饰品绑定。并且可以指定一个回调函数，在女仆每次 tick 时调用。
            """)
    @Deprecated
    public CustomKubeJSBauble bind(Item item, @Nullable BiConsumer<EntityMaid, ItemStack> onTick) {
        CustomKubeJSBauble bauble = new CustomKubeJSBauble();
        bauble.onTick = onTick;
        baubles.put(item, bauble);
        return bauble;
    }

    @Info("""
            Bind an item to a maid bauble. <br>
            将物品与女仆饰品绑定。
            """)
    public CustomKubeJSBauble bind(Item item) {
        CustomKubeJSBauble bauble = new CustomKubeJSBauble();
        baubles.put(item, bauble);
        return bauble;
    }

    @HideFromJS
    public void register(BaubleManager manager) {
        this.baubles.forEach(manager::bind);
        this.baubles.clear();
    }

    public static class CustomKubeJSBauble implements IMaidBauble {
        @HideFromJS
        private @Nullable BiConsumer<EntityMaid, ItemStack> onTick = null;
        @HideFromJS
        private @Nullable QuadFunction<EntityMaid, ItemStack, DamageSource, MutableFloat, Boolean> onInjured = null;
        @HideFromJS
        private @Nullable TriFunction<EntityMaid, ItemStack, DamageSource, Boolean> onDeath = null;
        @HideFromJS
        private @Nullable BiConsumer<EntityMaid, ItemStack> onPutOn = null;
        @HideFromJS
        private @Nullable BiConsumer<EntityMaid, ItemStack> onTakeOff = null;
        @HideFromJS
        private @Nullable TriConsumer<EntityMaid, ItemStack, Entity> onMeleeAttack = null;
        @HideFromJS
        private @Nullable TriConsumer<EntityMaid, ItemStack, IRangedAttackTask> onRangedAttack = null;
        @HideFromJS
        private @Nullable QuadConsumer<EntityMaid, ItemStack, ItemStack, MaidMealType> onMaidEat = null;
        @HideFromJS
        private @Nullable QuadConsumer<EntityMaid, ItemStack, Integer, Integer> onFavorabilityLevelChange = null;

        @Info("""
                When equipped as a bauble, this callback is called every tick. <br>
                当装备饰品时，每 tick 会调用此回调。
                """)
        public CustomKubeJSBauble triggerTick(@Nullable BiConsumer<EntityMaid, ItemStack> c) {
            this.onTick = c;
            return this;
        }

        @Info("""
                When the maid is injured while wearing this bauble, this callback is called. <br>
                Returning true indicates that subsequent damage processing is canceled. <br>
                当女仆在佩戴此饰品时受到伤害，会调用此回调。返回 true 表示取消后续伤害处理。
                """)
        public CustomKubeJSBauble triggerInjured(@Nullable QuadFunction<EntityMaid, ItemStack, DamageSource, MutableFloat, Boolean> f) {
            this.onInjured = f;
            return this;
        }

        @Info("""
                When the maid dies while wearing this bauble, this callback is called. <br>
                Returning true indicates that subsequent death processing is canceled. <br>
                当女仆在佩戴此饰品时死亡，会调用此回调。返回 true 表示取消后续死亡处理。
                """)
        public CustomKubeJSBauble triggerDeath(@Nullable TriFunction<EntityMaid, ItemStack, DamageSource, Boolean> f) {
            this.onDeath = f;
            return this;
        }

        @Info("""
                When player equips this bauble to the maid, this callback is called once. <br>
                Can be used to modify the maid's attributes. <br>
                当玩家给女仆装备此饰品时，会调用一次此回调，可用于修改女仆的 attribute。
                """)
        public CustomKubeJSBauble triggerPutOn(@Nullable BiConsumer<EntityMaid, ItemStack> c) {
            this.onPutOn = c;
            return this;
        }

        @Info("""
                When player removes this bauble from the maid, this callback is called once. <br>
                Can be used to revert the maid's attributes modified by `triggerPutOn`. <br>
                当玩家从女仆身上移除此饰品时，会调用一次此回调。可用于还原 `triggerPutOn` 修改的女仆 attribute。
                """)
        public CustomKubeJSBauble triggerTakeOff(@Nullable BiConsumer<EntityMaid, ItemStack> c) {
            this.onTakeOff = c;
            return this;
        }

        @Info("""
                When the maid performs a melee attack while wearing this bauble, this callback is called. <br>
                当女仆在佩戴此饰品时进行近战攻击，会调用此回调。
                """)
        public CustomKubeJSBauble triggerMeleeAttack(@Nullable TriConsumer<EntityMaid, ItemStack, Entity> c) {
            this.onMeleeAttack = c;
            return this;
        }

        @Info("""
                When the maid performs a ranged attack while wearing this bauble, this callback is called. <br>
                当女仆在佩戴此饰品时进行远程攻击，会调用此回调。
                """)
        public CustomKubeJSBauble triggerRangedAttack(@Nullable TriConsumer<EntityMaid, ItemStack, IRangedAttackTask> c) {
            this.onRangedAttack = c;
            return this;
        }

        @Info("""
                When the maid eats food while wearing this bauble, this callback is called. <br>
                当女仆在佩戴此饰品时进食食物，会调用此回调。
                """)
        public CustomKubeJSBauble triggerMaidEat(@Nullable QuadConsumer<EntityMaid, ItemStack, ItemStack, MaidMealType> c) {
            this.onMaidEat = c;
            return this;
        }

        @Info("""
                When the maid's favorability level changes while wearing this bauble, this callback is called. <br>
                当女仆在佩戴此饰品时好感度等级变化，会调用此回调。
                """)
        public CustomKubeJSBauble triggerFavorabilityLevelChange(@Nullable QuadConsumer<EntityMaid, ItemStack, Integer, Integer> c) {
            this.onFavorabilityLevelChange = c;
            return this;
        }

        @Override
        @HideFromJS
        public void onTick(EntityMaid maid, ItemStack baubleItem) {
            if (onTick != null) {
                onTick.accept(maid, baubleItem);
            }
        }

        @Override
        @HideFromJS
        public boolean onInjured(EntityMaid maid, ItemStack baubleItem, DamageSource source, MutableFloat amount) {
            if (onInjured != null) {
                Boolean ret = onInjured.apply(maid, baubleItem, source, amount);
                return ret != null && ret;
            }
            return false;
        }

        @Override
        @HideFromJS
        public boolean onDeath(EntityMaid maid, ItemStack baubleItem, DamageSource source) {
            if (onDeath != null) {
                Boolean ret = onDeath.apply(maid, baubleItem, source);
                return ret != null && ret;
            }
            return false;
        }

        @Override
        @HideFromJS
        public void onPutOn(EntityMaid maid, ItemStack baubleItem) {
            if (onPutOn != null) {
                onPutOn.accept(maid, baubleItem);
            }
        }

        @Override
        @HideFromJS
        public void onTakeOff(EntityMaid maid, ItemStack baubleItem) {
            if (onTakeOff != null) {
                onTakeOff.accept(maid, baubleItem);
            }
        }

        @Override
        @HideFromJS
        public void onMeleeAttack(EntityMaid maid, ItemStack baubleItem, Entity target) {
            if (onMeleeAttack != null) {
                onMeleeAttack.accept(maid, baubleItem, target);
            }
        }

        @Override
        @HideFromJS
        public void onRangedAttack(EntityMaid maid, ItemStack baubleItem, IRangedAttackTask task) {
            if (onRangedAttack != null) {
                onRangedAttack.accept(maid, baubleItem, task);
            }
        }

        @Override
        @HideFromJS
        public void onMaidEat(EntityMaid maid, ItemStack baubleItem, ItemStack foodItem, MaidMealType mealType) {
            if (onMaidEat != null) {
                onMaidEat.accept(maid, baubleItem, foodItem, mealType);
            }
        }

        @Override
        @HideFromJS
        public void onFavorabilityLevelChange(EntityMaid maid, ItemStack baubleItem, int oldLevel, int newLevel) {
            if (onFavorabilityLevelChange != null) {
                onFavorabilityLevelChange.accept(maid, baubleItem, oldLevel, newLevel);
            }
        }
    }
}
