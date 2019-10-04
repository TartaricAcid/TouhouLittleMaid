package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.github.tartaricacid.touhoulittlemaid.api.util.ProcessingInput;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author TartaricAcid
 * @date 2019/9/7 14:26
 **/
public class AltarRecipe {
    static final ResourceLocation ENTITY_ITEM_ID = new ResourceLocation("item");
    private static final int RECIPES_SIZE = 6;
    private ResourceLocation entityId;
    private float powerCost;
    private ItemStack output = ItemStack.EMPTY;
    private List<ProcessingInput> inputs;

    public AltarRecipe(ResourceLocation entityId, float powerCost, ItemStack output, ProcessingInput... inputs) {
        this.entityId = entityId;
        this.powerCost = powerCost;
        int recipesSize = RECIPES_SIZE;
        if (entityId.equals(ENTITY_ITEM_ID) && !output.isEmpty()) {
            this.output = output;
        }
        if (inputs.length == 0 || inputs.length > RECIPES_SIZE) {
            throw new IllegalAccessError();
        }
        this.inputs = Lists.newArrayList(inputs);
    }

    public Entity getOutputEntity(World world, BlockPos pos, List<ItemStack> inputItems) {
        Entity entity;
        // 特例：闪电，原版就这么做的
        if (EntityList.LIGHTNING_BOLT.equals(entityId)) {
            entity = new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false);
        } else {
            entity = Objects.requireNonNull(EntityList.createEntityByIDFromName(entityId, world));
            entity.setPosition(pos.getX(), pos.getY() + 0.8, pos.getZ());
            if (isItemCraft() && entity instanceof EntityItem) {
                ((EntityItem) entity).setItem(output.copy());
            } else if (entity instanceof EntityLiving) {
                ((EntityLiving) entity).onInitialSpawn(entity.getEntityWorld().getDifficultyForLocation(entity.getPosition()), null);
            }
        }
        return entity;
    }

    public float getPowerCost() {
        return powerCost;
    }

    public List<ProcessingInput> getRecipe() {
        return Collections.unmodifiableList(inputs);
    }

    public boolean isItemCraft() {
        return !output.isEmpty();
    }

    public ItemStack getOutputItemStack() {
        return output;
    }

    public boolean matches(List<ItemStack> stacks) {
        return RecipeMatcher.findMatches(stacks, inputs) != null;
    }
}
