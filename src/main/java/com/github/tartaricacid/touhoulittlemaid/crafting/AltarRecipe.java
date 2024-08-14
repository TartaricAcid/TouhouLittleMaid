package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class AltarRecipe extends ShapedRecipe {
    String group;
    CraftingBookCategory category;
    float power;
    ItemStack result;
    ResourceLocation entityType;

    public AltarRecipe(String pGroup,
                       CraftingBookCategory pCategory,
                       float power,
                       ShapedRecipePattern pPattern,
                       ItemStack pResult,
                       ResourceLocation pEntityType) {
        super(pGroup, pCategory, pPattern, pResult);
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

    public boolean isItemCraft() {
        return entityType.equals(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ITEM));
    }

    public void spawnOutputEntity(ServerLevel world, BlockPos pos, @Nullable List<ItemStack> list) {
        EntityType type = BuiltInRegistries.ENTITY_TYPE.get(entityType);
        if (type == EntityType.ITEM) {
            ItemEntity itemEntity = new ItemEntity(world,pos.getX(),pos.getY(),pos.getZ(), this.result);
            world.addFreshEntity(itemEntity);
        }
    }

    public boolean matches(CraftingInput pInput, Level pLevel) {
        List<Item> list = pInput.items().stream().filter(itemStack -> !itemStack.isEmpty()).map(ItemStack::getItem).toList();
        // 如果配方所需的材料数量与输入数量不一致，立即返回 false
        if (this.getIngredients().size() != list.size()) {
            return false;
        }

        for ( Ingredient ingredient : this.getIngredients()) {
            if (Arrays.stream(ingredient.getItems()).noneMatch(itemStack -> list.contains(itemStack.getItem()))) return false;
        }
        return true;
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
                                Codec.FLOAT.fieldOf("power").forGetter(altarRecipe -> altarRecipe.power),
                                ShapedRecipePattern.MAP_CODEC.forGetter(altarRecipe -> altarRecipe.pattern),
                                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(altarRecipe -> altarRecipe.result),
                                ResourceLocation.CODEC.fieldOf("entity").forGetter(altarRecipe -> altarRecipe.entityType)
                        )
                        .apply(instance, AltarRecipe::new)
        );

        private AltarRecipe fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf) {
            String s = friendlyByteBuf.readUtf();
            CraftingBookCategory craftingbookcategory = friendlyByteBuf.readEnum(CraftingBookCategory.class);
            float power = friendlyByteBuf.readFloat();
            ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.STREAM_CODEC.decode(friendlyByteBuf);
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(friendlyByteBuf);
            ResourceLocation entityType = friendlyByteBuf.readResourceLocation();
            return new AltarRecipe(s, craftingbookcategory, power, shapedrecipepattern, itemstack, entityType);
        }

        private void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, AltarRecipe altarRecipe) {
            friendlyByteBuf.writeUtf(altarRecipe.group);
            friendlyByteBuf.writeEnum(altarRecipe.category);
            friendlyByteBuf.writeFloat(altarRecipe.power);
            ShapedRecipePattern.STREAM_CODEC.encode(friendlyByteBuf, altarRecipe.pattern);
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
