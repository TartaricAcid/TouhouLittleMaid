package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public final class InitRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, TouhouLittleMaid.MOD_ID);

    public static DeferredHolder<RecipeSerializer<?>,RecipeSerializer<?>> ALTAR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("altar_crafting", AltarRecipeSerializer::new);
    public static RecipeType<AltarRecipe> ALTAR_CRAFTING;

    @SubscribeEvent
    public static void register(RegisterEvent evt) {
        if (evt.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
            ALTAR_CRAFTING = RecipeType.simple(new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_crafting"));
        }
    }
}
