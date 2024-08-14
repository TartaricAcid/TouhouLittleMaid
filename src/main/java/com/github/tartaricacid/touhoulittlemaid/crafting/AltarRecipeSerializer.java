package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class AltarRecipeSerializer implements RecipeSerializer<AltarRecipe> {
    @Override
    public MapCodec<AltarRecipe> codec() {
        return null;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AltarRecipe> streamCodec() {
        return null;
    }

    // TODO 祭坛配方
//    public AltarRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
//        Optional<EntityType<?>> typeOptional = EntityType.byString(buffer.readUtf());
//        if (typeOptional.isPresent()) {
//            EntityType<?> entityType = typeOptional.get();
//            CompoundTag extraData = buffer.readNbt();
//            float powerCost = buffer.readFloat();
//            Ingredient copyInput = Ingredient.fromNetwork(buffer);
//            String copyTag = buffer.readUtf();
//            Ingredient[] inputs = new Ingredient[buffer.readVarInt()];
//            for (int i = 0; i < inputs.length; i++) {
//                inputs[i] = Ingredient.fromNetwork(buffer);
//            }
//            return new AltarRecipe(recipeId, entityType, extraData, powerCost, copyInput, copyTag, inputs);
//        }
//        throw new JsonParseException("Entity Type Tag Not Found");
//    }
//
//    @Override
//    public void toNetwork(FriendlyByteBuf buffer, AltarRecipe recipe) {
//        ResourceLocation name = BuiltInRegistries.ENTITY_TYPE.getKey(recipe.getEntityType());
//        if (name == null) {
//            throw new JsonParseException("Entity Type Tag Not Found");
//        }
//        buffer.writeUtf(name.toString());
//        buffer.writeNbt(recipe.getExtraData());
//        buffer.writeFloat(recipe.getPowerCost());
//        recipe.getCopyInput().toNetwork(buffer);
//        if (StringUtils.isEmpty(recipe.getCopyTag())) {
//            buffer.writeUtf("");
//        } else {
//            buffer.writeUtf(recipe.getCopyTag());
//        }
//        buffer.writeVarInt(recipe.getIngredients().size());
//        for (Ingredient input : recipe.getIngredients()) {
//            input.toNetwork(buffer);
//        }
//    }
}
