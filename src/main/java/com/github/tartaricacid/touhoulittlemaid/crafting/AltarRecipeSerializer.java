package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AltarRecipeSerializer implements RecipeSerializer<AltarRecipe> {
    public static final MapCodec<AltarRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(AltarRecipe::getGroup),
                    CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(AltarRecipe::getCategory),
                    Ingredient.CODEC.listOf().fieldOf("ingredients").flatXmap(AltarRecipeSerializer::checkIngredients, DataResult::success).forGetter(AltarRecipe::getIngredients),
                    Codec.FLOAT.fieldOf("power").forGetter(AltarRecipe::getPower),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(AltarRecipe::getResult),
                    ResourceLocation.CODEC.fieldOf("entity").forGetter(AltarRecipe::getEntityType)
            ).apply(instance, AltarRecipe::new)
    );

    @NotNull
    private static DataResult<NonNullList<Ingredient>> checkIngredients(List<Ingredient> ingredientList) {
        Ingredient[] aingredient = ingredientList.toArray(Ingredient[]::new);
        if (aingredient.length == 0) {
            return DataResult.error(() -> "No ingredients for shapeless recipe");
        } else {
            if (aingredient.length > 6) {
                return DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: 6");
            }
            return DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
        }
    }

    private AltarRecipe fromNetwork(RegistryFriendlyByteBuf byteBuf) {
        String group = byteBuf.readUtf();
        CraftingBookCategory category = byteBuf.readEnum(CraftingBookCategory.class);
        NonNullList<Ingredient> ingredients = NonNullList.withSize(byteBuf.readVarInt(), Ingredient.EMPTY);
        ingredients.replaceAll((ingredient) -> Ingredient.CONTENTS_STREAM_CODEC.decode(byteBuf));
        float power = byteBuf.readFloat();
        ItemStack result = ItemStack.STREAM_CODEC.decode(byteBuf);
        ResourceLocation entityType = byteBuf.readResourceLocation();
        return new AltarRecipe(group, category, ingredients, power, result, entityType);
    }

    private void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, AltarRecipe altarRecipe) {
        friendlyByteBuf.writeUtf(altarRecipe.getGroup());
        friendlyByteBuf.writeEnum(altarRecipe.getCategory());
        friendlyByteBuf.writeVarInt(altarRecipe.getIngredients().size());
        for (Ingredient ingredient : altarRecipe.getIngredients()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(friendlyByteBuf, ingredient);
        }
        friendlyByteBuf.writeFloat(altarRecipe.getPower());
        ItemStack.STREAM_CODEC.encode(friendlyByteBuf, altarRecipe.getResult());
        friendlyByteBuf.writeResourceLocation(altarRecipe.getEntityType());
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
