package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBox;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.github.tartaricacid.touhoulittlemaid.item.ItemFilm;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class AltarRecipe extends ShapelessRecipe {
    private final String group;
    private final CraftingBookCategory category;
    private final float power;
    private final ItemStack result;
    private final ResourceLocation entityType;

    public AltarRecipe(String group, CraftingBookCategory category, NonNullList<Ingredient> ingredients, float power, ItemStack result, ResourceLocation entityType) {
        super(group, category, result, ingredients);
        this.group = group;
        this.category = category;
        this.power = power;
        this.result = result;
        this.entityType = entityType;
    }

    public ResourceLocation getId() {
        return InitRecipes.ALTAR_CRAFTING.getId();
    }

    public String getRecipeString() {
        String recipeId = this.result.get(InitDataComponent.RECIPES_ID_TAG);
        return Objects.requireNonNullElse(recipeId, "spawn_box");
    }

    public boolean isItemCraft() {
        return entityType.equals(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ITEM));
    }

    public void spawnOutputEntity(ServerLevel world, BlockPos pos, @Nullable List<ItemStack> list) {
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(entityType);

        if (type == EntityType.ITEM) {
            this.spawnItem(world, pos);
            return;
        }

        if (type == InitEntities.BOX.get()) {
            this.spawnBoxMaid(world, pos);
            return;
        }

        if (type == InitEntities.MAID.get()) {
            this.rebornMaid(world, pos, list);
            return;
        }

        type.spawn(world, pos, MobSpawnType.STRUCTURE);
    }

    private void rebornMaid(ServerLevel world, BlockPos pos, @Nullable List<ItemStack> list) {
        ItemStack itemFilm = ItemStack.EMPTY;
        if (list != null) {
            itemFilm = list.stream().filter(stack -> stack.getItem() instanceof ItemFilm).findFirst().orElse(ItemStack.EMPTY);
        }
        EntityMaid maid = InitEntities.MAID.get().create(world);
        CustomData compoundData = itemFilm.get(InitDataComponent.MAID_INFO);
        if (compoundData != null && maid != null) {
            CompoundTag maidCompound = compoundData.copyTag();
            maid.load(maidCompound);
            maid.spawnExplosionParticle();
            maid.setPos(pos.getX(), pos.getY(), pos.getZ());
            world.addFreshEntity(maid);
        }
    }

    private void spawnBoxMaid(ServerLevel world, BlockPos pos) {
        EntityBox box = new EntityBox(world);
        world.addFreshEntity(box);
        EntityMaid maid = new EntityMaid(world);
        maid.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null);
        world.addFreshEntity(maid);
        maid.startRiding(box);
        box.setPos(pos.getX(), pos.getY(), pos.getZ());
    }

    private void spawnItem(ServerLevel world, BlockPos pos) {
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), this.result.copy());
        world.addFreshEntity(itemEntity);
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return InitRecipes.ALTAR_CRAFTING.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return InitRecipes.ALTAR_RECIPE_SERIALIZER.get();
    }

    public float getPower() {
        return power;
    }

    @Override
    public String getGroup() {
        return group;
    }

    public CraftingBookCategory getCategory() {
        return category;
    }

    public ItemStack getResult() {
        return result;
    }

    public ResourceLocation getEntityType() {
        return entityType;
    }
}
