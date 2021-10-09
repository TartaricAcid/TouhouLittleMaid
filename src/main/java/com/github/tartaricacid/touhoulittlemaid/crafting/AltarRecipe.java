package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.github.tartaricacid.touhoulittlemaid.inventory.AltarRecipeInventory;
import com.google.common.base.Preconditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.RecipeMatcher;

import javax.annotation.Nullable;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.inventory.AltarRecipeInventory.RECIPES_SIZE;

public class AltarRecipe implements IRecipe<AltarRecipeInventory> {
    private final ResourceLocation id;
    private final EntityType<?> entityType;
    @Nullable
    private final CompoundNBT extraData;
    private final float powerCost;
    private final NonNullList<Ingredient> inputs;
    private final boolean isItemCraft;
    private final ItemStack resultItem;

    public AltarRecipe(ResourceLocation id, EntityType<?> entityType, @Nullable CompoundNBT extraData, float powerCost, Ingredient... inputs) {
        Preconditions.checkArgument(0 < inputs.length && inputs.length <= RECIPES_SIZE, "Ingredients count is illegal!");
        this.id = id;
        this.entityType = entityType;
        this.isItemCraft = (entityType == EntityType.ITEM);
        if (this.isItemCraft && extraData != null) {
            this.resultItem = ItemStack.of(extraData.getCompound("Item"));
        } else {
            this.resultItem = ItemStack.EMPTY;
        }
        this.extraData = extraData;
        this.powerCost = powerCost;
        this.inputs = NonNullList.of(Ingredient.EMPTY, inputs);
    }

    @Override
    public boolean matches(AltarRecipeInventory inv, World worldIn) {
        return RecipeMatcher.findMatches(inv.getItems(), inputs) != null;
    }

    @Override
    public ItemStack assemble(AltarRecipeInventory inv) {
        return getResultItem().copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputs;
    }

    @Override
    public ItemStack getResultItem() {
        return resultItem;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return InitRecipes.ALTAR_RECIPE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return InitRecipes.ALTAR_CRAFTING;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack getToastSymbol() {
        return InitItems.HAKUREI_GOHEI.get().getDefaultInstance();
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    @Nullable
    public CompoundNBT getExtraData() {
        return extraData;
    }

    public float getPowerCost() {
        return powerCost;
    }

    public void spawnOutputEntity(ServerWorld world, BlockPos pos, AltarRecipeInventory inventory) {
        if (extraData != null) {
            CompoundNBT nbt = this.extraData.copy();
            nbt.putString("id", Objects.requireNonNull(entityType.getRegistryName()).toString());
            Entity resultEntity = EntityType.loadEntityRecursive(nbt, world, (e) -> {
                e.moveTo(pos.getX(), pos.getY(), pos.getZ(), e.yRot, e.xRot);
                this.finalizeSpawn(world, pos, e);
                return e;
            });
            if (resultEntity != null) {
                this.finalizeSpawn(world, pos, resultEntity);
                world.tryAddFreshEntityWithPassengers(resultEntity);
            }
            return;
        }
        entityType.spawn(world, null, null, null, pos, SpawnReason.SPAWN_EGG, true, true);
    }

    public boolean isItemCraft() {
        return isItemCraft;
    }

    private void finalizeSpawn(ServerWorld world, BlockPos pos, @Nullable Entity entity) {
        if (entity instanceof MobEntity) {
            ((MobEntity) entity).finalizeSpawn(world, world.getCurrentDifficultyAt(pos), SpawnReason.SPAWN_EGG, null, null);
        }
    }
}
