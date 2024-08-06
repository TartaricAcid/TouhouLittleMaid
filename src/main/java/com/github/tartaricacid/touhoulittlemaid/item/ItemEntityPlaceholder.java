package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.TileEntityEntityPlaceholderRenderer;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.github.tartaricacid.touhoulittlemaid.inventory.AltarRecipeInventory;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.function.Consumer;

public class ItemEntityPlaceholder extends Item {
    private static final DataComponentType<String> RECIPES_ID_TAG = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            "RecipeId",
            DataComponentType.<String>builder()
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                    .build()
    );
    public static final IClientItemExtensions itemExtensions = new IClientItemExtensions() {
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            Minecraft minecraft = Minecraft.getInstance();
            return new TileEntityEntityPlaceholderRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        }
    };

    public ItemEntityPlaceholder() {
        super(new Item.Properties().stacksTo(1));
    }

    @SuppressWarnings("all")
    public static ItemStack setRecipeId(ItemStack stack, ResourceLocation id) {
        stack.set(RECIPES_ID_TAG, id.toString());
        return stack;
    }

    @SuppressWarnings("all")
    @Nullable
    public static ResourceLocation getRecipeId(ItemStack stack) {
        if (stack.has(RECIPES_ID_TAG)) {
            return ResourceLocation.parse(stack.get(RECIPES_ID_TAG));
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public static void fillItemCategory(CreativeModeTab.Output items) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) {
            return;
        }
        world.getRecipeManager().getAllRecipesFor(InitRecipes.ALTAR_CRAFTING.get()).forEach(recipe -> {
            if (!recipe.value().isItemCraft()) {
                items.accept(setRecipeId(new ItemStack(InitItems.ENTITY_PLACEHOLDER.get()), recipe.value().getId()));
            }
        });
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() == Direction.UP) {
            ResourceLocation id = getRecipeId(context.getItemInHand());
            Level world = context.getLevel();
            if (id != null && world instanceof ServerLevel) {
                Recipe<AltarRecipeInventory> recipe = context.getLevel().getRecipeManager().byType(InitRecipes.ALTAR_CRAFTING).get(id);
                if (recipe instanceof AltarRecipe) {
                    AltarRecipe altarRecipe = (AltarRecipe) recipe;
                    altarRecipe.spawnOutputEntity((ServerLevel) world, context.getClickedPos().above(), null);
                    context.getItemInHand().shrink(1);
                }
            }
        }
        return super.useOn(context);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Component getName(ItemStack stack) {
        ResourceLocation recipeId = getRecipeId(stack);
        if (recipeId != null) {
            Path path = Paths.get(recipeId.getPath().toLowerCase(Locale.US));
            String namespace = recipeId.getNamespace().toLowerCase(Locale.US);
            String langKey = String.format("jei.%s.altar_craft.%s.result", namespace, path.getFileName());
            return Component.translatable(langKey);
        }
        return Component.translatable("item.touhou_little_maid.entity_placeholder");
    }
}
