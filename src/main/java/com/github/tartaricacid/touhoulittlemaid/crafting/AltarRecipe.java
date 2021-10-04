package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.github.tartaricacid.touhoulittlemaid.inventory.AltarRecipeInventory;
import com.google.common.base.Preconditions;
import net.minecraft.entity.EntityType;
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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.RecipeMatcher;

import javax.annotation.Nullable;

import static com.github.tartaricacid.touhoulittlemaid.inventory.AltarRecipeInventory.RECIPES_SIZE;

public class AltarRecipe implements IRecipe<AltarRecipeInventory> {
    private final ResourceLocation id;
    private final EntityType<?> entityType;
    @Nullable
    private final CompoundNBT extraData;
    private final float powerCost;
    private final NonNullList<Ingredient> inputs;

    public AltarRecipe(ResourceLocation id, EntityType<?> entityType, @Nullable CompoundNBT extraData, float powerCost, Ingredient... inputs) {
        Preconditions.checkArgument(0 < inputs.length && inputs.length <= RECIPES_SIZE, "Ingredients count is illegal!");
        this.id = id;
        this.entityType = entityType;
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
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return new AltarRecipeSerializer();
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
        entityType.spawn(world, extraData, StringTextComponent.EMPTY, null, pos, SpawnReason.SPAWN_EGG, true, true);
    }
}
