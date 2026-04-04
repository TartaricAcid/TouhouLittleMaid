package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FallbackIngredient extends AbstractIngredient {
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "fallback_ingredient");

    private final Ingredient fallbacks;

    protected FallbackIngredient(Ingredient fallbacks) {
        this.fallbacks = fallbacks;
    }

    public static FallbackIngredient of(Ingredient fallbacks) {
        return new FallbackIngredient(fallbacks);
    }

    @Override
    public boolean isSimple() {
        return this.fallbacks.isSimple();
    }

    @Override
    public boolean isEmpty() {
        return this.fallbacks.isEmpty();
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return this.fallbacks.test(stack);
    }

    @Override
    public ItemStack[] getItems() {
        return this.fallbacks.getItems();
    }

    @Override
    public IntList getStackingIds() {
        return this.fallbacks.getStackingIds();
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public JsonElement toJson() {
        throw new UnsupportedOperationException("FallbackIngredient does not support datagen toJson");
    }

    public static class Serializer implements IIngredientSerializer<FallbackIngredient> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public FallbackIngredient parse(FriendlyByteBuf buffer) {
            ResourceLocation id = buffer.readResourceLocation();
            return new FallbackIngredient(CraftingHelper.getIngredient(id, buffer));
        }

        @Override
        @SuppressWarnings("all")
        public void write(FriendlyByteBuf buffer, FallbackIngredient fallback) {
            Ingredient ingredient = fallback.fallbacks;
            IIngredientSerializer serializer = ingredient.getSerializer();
            ResourceLocation id = Objects.requireNonNull(CraftingHelper.getID(serializer));
            buffer.writeResourceLocation(id);
            serializer.write(buffer, ingredient);
        }

        @Override
        public FallbackIngredient parse(JsonObject json) {
            JsonArray jsonArray = json.getAsJsonArray("fallbacks");
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String modId = jsonObject.get("modid").getAsString();
                if (ModList.get().isLoaded(modId)) {
                    Ingredient ingredient = Ingredient.fromJson(jsonObject.get("value"), true);
                    return new FallbackIngredient(ingredient);
                }
            }
            return new FallbackIngredient(Ingredient.EMPTY);
        }
    }
}
