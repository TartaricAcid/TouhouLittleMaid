package com.github.tartaricacid.touhoulittlemaid.compat.patchouli;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

public class AltarRecipeComponent implements IComponentProcessor {
    private static final String RECIPE_ID = "recipe_id";
    private static final String INPUT = "input";
    private static final String POWER_COST = "power_cost";
    private static final String OUTPUT_ITEM = "output_item";
    private static final String OUTPUT_ENTITY = "output_entity";
    private static final String OUTPUT_DESC = "output_desc";

    private @Nullable AltarRecipe recipe;

    @Override
    public void setup(Level level, IVariableProvider variables) {
        ResourceLocation recipeId = new ResourceLocation(variables.get(RECIPE_ID).asString());
        List<AltarRecipe> allAltarRecipes = level.getRecipeManager().getAllRecipesFor(InitRecipes.ALTAR_CRAFTING);
        for (AltarRecipe recipe : allAltarRecipes) {
            if (recipe.getId().equals(recipeId)) {
                this.recipe = recipe;
                return;
            }
        }
        this.recipe = new AltarRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "empty"),
                EntityType.ITEM, null, 0, Ingredient.EMPTY);
        TouhouLittleMaid.LOGGER.error("Altar recipe not found: {}", recipeId);
    }

    @Nullable
    @Override
    public IVariable process(Level level, String key) {
        if (key.startsWith(INPUT)) {
            int index = Integer.parseInt(key.substring(INPUT.length())) - 1;
            if (index < 0 || index >= recipe.getIngredients().size()) {
                return IVariable.from(ItemStack.EMPTY);
            }
            Ingredient ingredient = recipe.getIngredients().get(index);
            ItemStack[] stacks = ingredient.getItems();
            if (stacks.length == 0) {
                return IVariable.from(ItemStack.EMPTY);
            }
            List<String> stackNames = Lists.newArrayList();
            for (ItemStack stack : stacks) {
                ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
                if (itemId != null) {
                    stackNames.add(itemId.toString());
                }
            }
            return IVariable.wrap(StringUtils.join(stackNames, ","));
        }

        switch (key) {
            case POWER_COST -> {
                float powerCost = recipe.getPowerCost();
                return IVariable.wrap(String.format("x%.2f", powerCost));
            }
            case OUTPUT_ITEM -> {
                return IVariable.from(recipe.getResultItem(level.registryAccess()));
            }
            case OUTPUT_DESC -> {
                String namespace = recipe.getId().getNamespace().toLowerCase(Locale.US);
                String langKey;
                if (recipe.isItemCraft()) {
                    langKey = String.format("jei.%s.altar_craft.%s.result", namespace, "item_craft");
                } else {
                    Path path = Paths.get(recipe.getId().getPath().toLowerCase(Locale.US));
                    langKey = String.format("jei.%s.altar_craft.%s.result", namespace, path.getFileName());
                }
                return IVariable.wrap(I18n.get(langKey));
            }
            case OUTPUT_ENTITY -> {
                EntityType<?> entityType = recipe.getEntityType();
                // 特判，女仆生成是实体对象是盒子，这里纠正为女仆
                if (entityType.equals(InitEntities.BOX.get())) {
                    entityType = InitEntities.MAID.get();
                }
                ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
                if (entityId != null) {
                    return IVariable.wrap(entityId.toString());
                } else {
                    return IVariable.wrap("minecraft:item");
                }
            }
        }

        return null;
    }
}
