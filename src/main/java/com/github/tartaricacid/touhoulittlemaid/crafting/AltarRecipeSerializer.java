//package com.github.tartaricacid.touhoulittlemaid.crafting;
//
//import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.DataResult;
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.core.Holder;
//import net.minecraft.core.NonNullList;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.crafting.*;
//import org.apache.commons.lang3.StringUtils;
//
//public class AltarRecipeSerializer implements RecipeSerializer<AltarRecipe> {
//    @SuppressWarnings("deprecation")
//    public static final Codec<Holder<EntityType<?>>> ENTITY_TYPE_CODEC = BuiltInRegistries.ENTITY_TYPE.holderByNameCodec().validate((result) -> {
//        return result.is(EntityType.ITEM.builtInRegistryHolder()) ? DataResult.error(() -> {
//            return "Item must not be minecraft:air";
//        }) : DataResult.success(result);
//    });
//
//    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<EntityType<?>>> ENTITY_TYPE_STREAM_CODEC = null;
//
//    private static final MapCodec<AltarRecipe> CODEC = RecordCodecBuilder.mapCodec(
//            recipeInstance -> recipeInstance.group(
//                    ResourceLocation.CODEC.fieldOf("type").forGetter(AltarRecipe::getId),
//                    ENTITY_TYPE_CODEC.fieldOf("type").forGetter(AltarRecipe::getEntityType),
//                    CompoundTag.CODEC.fieldOf("nbt").forGetter(AltarRecipe::getExtraData),
//                    Codec.FLOAT.fieldOf("power").forGetter(AltarRecipe::getPowerCost),
//                    Ingredient.CODEC.fieldOf("copy").forGetter(AltarRecipe::getCopyInput),
//                    Codec.STRING.fieldOf("tag").forGetter(AltarRecipe::getCopyTag),
//                            Ingredient.CODEC_NONEMPTY
//                                    .listOf()
//                                    .fieldOf("ingredients")
//                                    .flatXmap(
//                                            ingredientList -> {
//                                                Ingredient[] aingredient = ingredientList.toArray(Ingredient[]::new); // Neo skip the empty check and immediately create the array.
//                                                if (aingredient.length == 0) {
//                                                    return DataResult.error(() -> "No ingredients for shapeless recipe");
//                                                } else {
//                                                    return aingredient.length > ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth()
//                                                            ? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth()))
//                                                            : DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
//                                                }
//                                            },
//                                            DataResult::success
//                                    )
//                                    .forGetter(AltarRecipe::getIngredients)
//                    )
//                    .apply(recipeInstance, AltarRecipe::new));
//
//    @Override
//    public MapCodec<AltarRecipe> codec() {
//        return CODEC;
//    }
//
//    @Override
//    public StreamCodec<RegistryFriendlyByteBuf, AltarRecipe> streamCodec() {
//        return StreamCodec.of(this::toNetwork, this::fromNetwork);
//    }
//
//    public AltarRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
//            Holder<EntityType<?>> entityType = buffer.readJsonWithCodec(ENTITY_TYPE_CODEC);
//            CompoundTag extraData = buffer.readNbt();
//            float powerCost = buffer.readFloat();
//            Ingredient copyInput = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
//            String copyTag = buffer.readUtf();
//            int i = buffer.readVarInt();
//            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
//            nonnulllist.replaceAll(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
//            return new AltarRecipe(InitRecipes.ALTAR_CRAFTING.getId(), entityType, extraData, powerCost, copyInput, copyTag, nonnulllist);
//    }
//
//    public void toNetwork(RegistryFriendlyByteBuf buffer, AltarRecipe recipe) {
//        buffer.writeResourceLocation(InitRecipes.ALTAR_CRAFTING.getId());
//        buffer.writeJsonWithCodec(ENTITY_TYPE_CODEC, recipe.getEntityType());
//        buffer.writeNbt(recipe.getExtraData());
//        buffer.writeFloat(recipe.getPowerCost());
//        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getCopyInput());
//        if (StringUtils.isEmpty(recipe.getCopyTag())) {
//            buffer.writeUtf("");
//        } else {
//            buffer.writeUtf(recipe.getCopyTag());
//        }
//        for (Ingredient ingredient : recipe.getIngredients()) {
//            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
//        }
//    }
//}
