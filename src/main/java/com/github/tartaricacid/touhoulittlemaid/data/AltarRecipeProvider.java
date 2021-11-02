package com.github.tartaricacid.touhoulittlemaid.data;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.github.tartaricacid.touhoulittlemaid.util.NBTToJson;
import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.init.InitItems.*;

public class AltarRecipeProvider implements IDataProvider {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    protected final DataGenerator generator;
    private final List<AltarRecipe> recipes = Lists.newArrayList();

    public AltarRecipeProvider(DataGenerator generator) {
        this.generator = generator;
    }

    protected void registerRecipes() {
        Ingredient gunpowder = Ingredient.of(Tags.Items.GUNPOWDER);
        Ingredient blazePowder = Ingredient.of(Items.BLAZE_POWDER);
        Ingredient paper = Ingredient.of(Items.PAPER);
        Ingredient stick = Ingredient.of(Tags.Items.RODS_WOODEN);
        Ingredient obsidian = Ingredient.of(Tags.Items.OBSIDIAN);
        Ingredient redstone = Ingredient.of(Tags.Items.DUSTS_REDSTONE);
        Ingredient dyeCyan = Ingredient.of(Tags.Items.DYES_CYAN);
        Ingredient dyeOrange = Ingredient.of(Tags.Items.DYES_ORANGE);
        Ingredient dyeRed = Ingredient.of(Tags.Items.DYES_RED);
        Ingredient dyeBlue = Ingredient.of(Tags.Items.DYES_BLUE);
        Ingredient dyeYellow = Ingredient.of(Tags.Items.DYES_YELLOW);
        Ingredient dyeLime = Ingredient.of(Tags.Items.DYES_LIME);
        Ingredient shield = Ingredient.of(Items.SHIELD);
        Ingredient sugar = Ingredient.of(Items.SUGAR);
        Ingredient feather = Ingredient.of(Tags.Items.FEATHERS);
        Ingredient fishes = Ingredient.of(ItemTags.FISHES);
        Ingredient plankWood = Ingredient.of(ItemTags.PLANKS);
        Ingredient ingotGold = Ingredient.of(Tags.Items.INGOTS_GOLD);
        Ingredient ingotIron = Ingredient.of(Tags.Items.INGOTS_IRON);
        Ingredient clayBall = Ingredient.of(Items.CLAY_BALL);
        Ingredient redWool = Ingredient.of(Blocks.RED_WOOL);
        Ingredient pinkWool = Ingredient.of(Blocks.PINK_WOOL);
        Ingredient grayWool = Ingredient.of(Blocks.GRAY_WOOL);
        Ingredient anyWool = Ingredient.of(ItemTags.WOOL);
        Ingredient enderPearl = Ingredient.of(Tags.Items.ENDER_PEARLS);
        Ingredient gemDiamond = Ingredient.of(Tags.Items.GEMS_DIAMOND);
        Ingredient gemEmerald = Ingredient.of(Tags.Items.GEMS_EMERALD);
        Ingredient gemLapis = Ingredient.of(Tags.Items.GEMS_LAPIS);
        Ingredient coal = Ingredient.of(Items.COAL);
        Ingredient netherWart = Ingredient.of(Tags.Items.CROPS_NETHER_WART);

        {
            CompoundNBT extraData = new CompoundNBT();
            ListNBT passengerList = new ListNBT();
            CompoundNBT passenger = new CompoundNBT();
            passenger.putString("id", Objects.requireNonNull(InitEntities.MAID.get().getRegistryName()).toString());
            passengerList.add(passenger);
            extraData.put("Passengers", passengerList);
            addEntityRecipes(InitEntities.BOX.get(), extraData, 0.5f, gemDiamond, gemLapis, ingotGold, redstone, ingotIron, coal);
        }
        addEntityRecipes(EntityType.LIGHTNING_BOLT, 0.2f, gunpowder, gunpowder, gunpowder, blazePowder, blazePowder, blazePowder);
        addItemRecipes(HAKUREI_GOHEI, 0.15f, stick, stick, stick, paper, paper, paper);
        addItemRecipes(ULTRAMARINE_ORB_ELIXIR, 0.3f, gemEmerald, enderPearl, dyeCyan, dyeCyan, dyeCyan, dyeCyan);
        addItemRecipes(EXPLOSION_PROTECT_BAUBLE, netherWart, dyeOrange, obsidian, obsidian, obsidian, obsidian);
        addItemRecipes(FIRE_PROTECT_BAUBLE, netherWart, dyeRed, blazePowder, blazePowder, blazePowder, blazePowder);
        addItemRecipes(PROJECTILE_PROTECT_BAUBLE, netherWart, dyeBlue, shield, shield, shield, shield);
        addItemRecipes(MAGIC_PROTECT_BAUBLE, netherWart, dyeCyan, sugar, sugar, sugar, sugar);
        addItemRecipes(FALL_PROTECT_BAUBLE, netherWart, dyeYellow, feather, feather, feather, feather);
        addItemRecipes(DROWN_PROTECT_BAUBLE, netherWart, dyeLime, fishes, fishes, fishes, fishes);
        addItemRecipes(MAID_BACKPACK_SMALL, 0.1f, redWool, redWool, redWool, redWool, ingotIron, redWool);
        addItemRecipes(MAID_BACKPACK_MIDDLE, pinkWool, pinkWool, pinkWool, pinkWool, ingotGold, pinkWool);
        addItemRecipes(MAID_BACKPACK_BIG, 0.3f, grayWool, grayWool, grayWool, grayWool, gemDiamond, grayWool);
        addItemRecipes(EXTINGUISHER, clayBall, clayBall, clayBall, clayBall, ingotIron, dyeRed);
        addItemRecipes(MAID_BED, pinkWool, pinkWool, pinkWool, plankWood, plankWood, plankWood);
        addItemRecipes(NIMBLE_FABRIC, enderPearl, enderPearl, enderPearl, anyWool, anyWool, anyWool);
        addItemRecipes(MUTE_BAUBLE, anyWool, anyWool, anyWool, clayBall, clayBall, clayBall);
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

    public void addRecipes(AltarRecipe recipe) {
        recipes.add(recipe);
    }

    public void addEntityRecipes(String name, EntityType<?> entityType, @Nullable CompoundNBT extraData, float powerCost, Ingredient... inputs) {
        addRecipes(new AltarRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, name), entityType, extraData, powerCost, inputs));
    }

    public void addEntityRecipes(EntityType<?> entityType, @Nullable CompoundNBT extraData, float powerCost, Ingredient... inputs) {
        ResourceLocation registryName = entityType.getRegistryName();
        if (registryName != null) {
            addEntityRecipes("spawn_" + registryName.getPath(), entityType, extraData, powerCost, inputs);
        }
    }

    public void addEntityRecipes(EntityType<?> entityType, float powerCost, Ingredient... inputs) {
        addEntityRecipes(entityType, null, powerCost, inputs);
    }

    public void addItemRecipes(String name, ItemStack output, float powerCost, Ingredient... inputs) {
        CompoundNBT extraData = new CompoundNBT();
        extraData.put("Item", output.serializeNBT());
        addEntityRecipes(name, EntityType.ITEM, extraData, powerCost, inputs);
    }

    public void addItemRecipes(String name, Item output, float powerCost, Ingredient... inputs) {
        addItemRecipes(name, output.getDefaultInstance(), powerCost, inputs);
    }

    public void addItemRecipes(String name, RegistryObject<Item> output, float powerCost, Ingredient... inputs) {
        addItemRecipes(name, output.get(), powerCost, inputs);
    }

    public void addItemRecipes(RegistryObject<Item> output, float powerCost, Ingredient... inputs) {
        ResourceLocation registryName = output.get().getRegistryName();
        if (registryName != null) {
            addItemRecipes("craft_" + registryName.getPath(), output, powerCost, inputs);
        }
    }

    public void addItemRecipes(RegistryObject<Item> output, Ingredient... inputs) {
        addItemRecipes(output, 0.2f, inputs);
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
            NBTToJson.getJson(recipe.getExtraData()).ifPresent(e -> output.add("nbt", e));
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
