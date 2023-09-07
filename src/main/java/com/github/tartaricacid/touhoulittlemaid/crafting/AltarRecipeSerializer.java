package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.github.tartaricacid.touhoulittlemaid.util.EntityCraftingHelper;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class AltarRecipeSerializer implements RecipeSerializer<AltarRecipe> {
    @Override
    public AltarRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        EntityCraftingHelper.Output output = EntityCraftingHelper.getEntityData(GsonHelper.getAsJsonObject(json, "output"));
        float powerCost = GsonHelper.getAsFloat(json, "power");
        JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
        List<Ingredient> inputs = Lists.newArrayList();
        for (JsonElement e : ingredients) {
            inputs.add(Ingredient.fromJson(e));
        }
        return new AltarRecipe(recipeId, output.getType(), output.getData(), powerCost, output.getCopyInput(), output.getCopyTag(), inputs.toArray(new Ingredient[0]));
    }

    @Nullable
    @Override
    public AltarRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Optional<EntityType<?>> typeOptional = EntityType.byString(buffer.readUtf());
        if (typeOptional.isPresent()) {
            EntityType<?> entityType = typeOptional.get();
            CompoundTag extraData = buffer.readNbt();
            float powerCost = buffer.readFloat();
            Ingredient copyInput = Ingredient.fromNetwork(buffer);
            String copyTag = buffer.readUtf();
            Ingredient[] inputs = new Ingredient[buffer.readVarInt()];
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = Ingredient.fromNetwork(buffer);
            }
            return new AltarRecipe(recipeId, entityType, extraData, powerCost, copyInput, copyTag, inputs);
        }
        throw new JsonParseException("Entity Type Tag Not Found");
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, AltarRecipe recipe) {
        ResourceLocation name = ForgeRegistries.ENTITY_TYPES.getKey(recipe.getEntityType());
        if (name == null) {
            throw new JsonParseException("Entity Type Tag Not Found");
        }
        buffer.writeUtf(name.toString());
        buffer.writeNbt(recipe.getExtraData());
        buffer.writeFloat(recipe.getPowerCost());
        recipe.getCopyInput().toNetwork(buffer);
        if (StringUtils.isEmpty(recipe.getCopyTag())) {
            buffer.writeUtf("");
        } else {
            buffer.writeUtf(recipe.getCopyTag());
        }
        buffer.writeVarInt(recipe.getIngredients().size());
        for (Ingredient input : recipe.getIngredients()) {
            input.toNetwork(buffer);
        }
    }
}
