package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBox;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.github.tartaricacid.touhoulittlemaid.item.ItemFilm;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
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

public class AltarRecipe extends ShapelessRecipe {
    String group;
    CraftingBookCategory category;
    float power;
    ItemStack result;
    ResourceLocation entityType;

    public AltarRecipe(String pGroup,
                       CraftingBookCategory pCategory,
                       NonNullList<Ingredient> Ingredients,
                       float power,
                       ItemStack pResult,
                       ResourceLocation pEntityType) {
        super(pGroup, pCategory, pResult, Ingredients);
        this.group = pGroup;
        this.category = pCategory;
        this.power = power;
        this.result = pResult;
        this.entityType = pEntityType;
    }

    public float getPowerCost() {
        return power;
    }

    public ResourceLocation getId() {
        return InitRecipes.ALTAR_CRAFTING.getId();
    }

    public String getRecipeString() {
        String recipeId = this.result.get(InitDataComponent.RECIPES_ID_TAG);
        if (recipeId != null) {
            return recipeId;
        } else return "spawn_box";
    }

    public boolean isItemCraft() {
        return entityType.equals(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ITEM));
    }

    public void spawnOutputEntity(ServerLevel world, BlockPos pos, @Nullable List<ItemStack> list) {
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(entityType);
        if (type == EntityType.ITEM) {
            ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), this.result);
            world.addFreshEntity(itemEntity);
        }

        if (type == InitEntities.BOX.get()) {
            EntityBox box = new EntityBox(world);
            world.addFreshEntity(box);
            EntityMaid maid = new EntityMaid(world);
            world.addFreshEntity(maid);
            maid.startRiding(box);
            box.setPos(pos.getX(), pos.getY(), pos.getZ());
            return;
        }

        if (type == InitEntities.MAID.get()) {
            ItemStack itemFilm = ItemStack.EMPTY;
            if (list != null) {
                for (ItemStack itemStack : list) {
                    if (itemStack.getItem() instanceof ItemFilm) {
                        itemFilm = itemStack;
                        break;
                    }
                }
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
            return;
        }

        type.spawn(world, pos, MobSpawnType.STRUCTURE);
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return InitRecipes.ALTAR_CRAFTING.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return InitRecipes.ALTAR_RECIPE_SERIALIZER.get();
    }

    public static class AltarRecipeSerializer implements RecipeSerializer<AltarRecipe> {
        public static final MapCodec<AltarRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
                                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(altarRecipe -> altarRecipe.category),
                                Ingredient.CODEC.listOf().fieldOf("ingredients").flatXmap((ingredientList) -> {
                                    Ingredient[] aingredient = ingredientList.toArray(Ingredient[]::new);
                                    if (aingredient.length == 0) {
                                        return DataResult.error(() -> "No ingredients for shapeless recipe");
                                    } else {
                                        return aingredient.length > 7 ? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: 7") : DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
                                    }
                                }, DataResult::success).forGetter(ShapelessRecipe::getIngredients),
                                Codec.FLOAT.fieldOf("power").forGetter(altarRecipe -> altarRecipe.power),
                                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(altarRecipe -> altarRecipe.result),
                                ResourceLocation.CODEC.fieldOf("entity").forGetter(altarRecipe -> altarRecipe.entityType)
                        )
                        .apply(instance, AltarRecipe::new)
        );

        private AltarRecipe fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf) {
            String s = friendlyByteBuf.readUtf();
            CraftingBookCategory craftingbookcategory = friendlyByteBuf.readEnum(CraftingBookCategory.class);
            int i = friendlyByteBuf.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            nonnulllist.replaceAll((ingredient) -> Ingredient.CONTENTS_STREAM_CODEC.decode(friendlyByteBuf));
            float power = friendlyByteBuf.readFloat();
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(friendlyByteBuf);
            ResourceLocation entityType = friendlyByteBuf.readResourceLocation();
            return new AltarRecipe(s, craftingbookcategory, nonnulllist, power, itemstack, entityType);
        }

        private void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, AltarRecipe altarRecipe) {
            friendlyByteBuf.writeUtf(altarRecipe.group);
            friendlyByteBuf.writeEnum(altarRecipe.category);
            friendlyByteBuf.writeVarInt(altarRecipe.getIngredients().size());
            for (Ingredient ingredient : altarRecipe.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(friendlyByteBuf, ingredient);
            }
            friendlyByteBuf.writeFloat(altarRecipe.power);
            ItemStack.STREAM_CODEC.encode(friendlyByteBuf, altarRecipe.result);
            friendlyByteBuf.writeResourceLocation(altarRecipe.entityType);
        }

        @Override
        public MapCodec<AltarRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AltarRecipe> streamCodec() {
            return StreamCodec.of(this::toNetwork, this::fromNetwork);
        }
    }
}
