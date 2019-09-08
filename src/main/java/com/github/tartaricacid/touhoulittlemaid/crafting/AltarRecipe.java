package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @author TartaricAcid
 * @date 2019/9/7 14:26
 **/
public class AltarRecipe<T extends Entity> {
    private static final int RECIPES_SIZE = 6;
    private ResourceLocation entityId;
    private BiFunction<T, List<ItemStack>, T> entityFunc;
    private float powerCost;
    private LinkedList<ItemStack> recipe = Lists.newLinkedList();

    public AltarRecipe(ResourceLocation entityId, BiFunction<T, List<ItemStack>, T> entityFunc, float powerCost, ItemStack... recipe) {
        this.entityId = entityId;
        this.entityFunc = entityFunc;
        this.powerCost = powerCost;
        for (int i = 0; i < RECIPES_SIZE; i++) {
            this.recipe.add(Arrays.stream(recipe).skip(i).findFirst().orElse(ItemStack.EMPTY));
        }
    }

    @SuppressWarnings("unchecked")
    public T getOutputEntity(World world, BlockPos pos, List<ItemStack> inputItems) {
        Entity entity;
        // 特例：闪电，原版就这么做的
        if (EntityList.LIGHTNING_BOLT.equals(entityId)) {
            entity = new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false);
        } else {
            entity = Objects.requireNonNull(EntityList.createEntityByIDFromName(entityId, world));
        }
        entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
        return entityFunc.apply((T) entity, inputItems);
    }

    public float getPowerCost() {
        return powerCost;
    }

    public LinkedList<ItemStack> getRecipe() {
        return recipe;
    }
}
