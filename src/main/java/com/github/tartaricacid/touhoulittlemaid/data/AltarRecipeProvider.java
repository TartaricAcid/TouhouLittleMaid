package com.github.tartaricacid.touhoulittlemaid.data;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class AltarRecipeProvider implements IDataProvider {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    protected final DataGenerator generator;
    private final List<AltarRecipe> recipes = Lists.newArrayList();

    public AltarRecipeProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void run(DirectoryCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        recipes.clear();
        this.registerRecipes();
        for (AltarRecipe recipe : recipes) {
            JsonObject jsonObject = recipeToJson(recipe);
            Path savePath = path.resolve("data/" + recipe.getId().getNamespace() + "/recipes/altar/" + recipe.getId().getPath() + ".json");
            IDataProvider.save(GSON, cache, jsonObject, savePath);
        }
    }

    protected void registerRecipes() {
        addItemRecipes(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_explosion_protect_bauble"),
                InitItems.EXPLOSION_PROTECT_BAUBLE.get().getDefaultInstance(), 0.2f,
                Ingredient.of(Items.NETHER_WART), Ingredient.of(Tags.Items.DYES_ORANGE), Ingredient.of(Tags.Items.OBSIDIAN),
                Ingredient.of(Tags.Items.OBSIDIAN), Ingredient.of(Tags.Items.OBSIDIAN), Ingredient.of(Tags.Items.OBSIDIAN));
    }

    public void addRecipes(AltarRecipe recipe) {
        recipes.add(recipe);
    }

    public void addItemRecipes(ResourceLocation id, ItemStack output, float powerCost, Ingredient... inputs) {
        CompoundNBT extraData = new CompoundNBT();
        extraData.put("Item", output.serializeNBT());
        addRecipes(new AltarRecipe(id, EntityType.ITEM, extraData, powerCost, inputs));
    }

    private JsonObject recipeToJson(AltarRecipe recipe) {
        JsonObject json = new JsonObject();
        json.addProperty("type", InitRecipes.ALTAR_CRAFTING.toString());
        {
            JsonObject output = new JsonObject();
            ResourceLocation name = recipe.getEntityType().getRegistryName();
            if (name == null) {
                throw new JsonParseException("Entity Registry Name Not Found");
            }
            output.addProperty("type", name.toString());
            if (recipe.getExtraData() != null) {
                output.addProperty("nbt", recipe.getExtraData().getAsString());
            }
            json.add("output", output);
        }
        json.addProperty("power", recipe.getPowerCost());
        {
            JsonArray ingredients = new JsonArray();
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredients.add(ingredient.toJson());
            }
            json.add("ingredients", ingredients);
        }
        return json;
    }

    @Override
    public String getName() {
        return "Maid Altar Recipe";
    }
}
