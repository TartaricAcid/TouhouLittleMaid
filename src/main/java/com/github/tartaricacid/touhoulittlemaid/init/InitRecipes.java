package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipeSerializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TouhouLittleMaid.MOD_ID);

    public static RegistryObject<RecipeSerializer<?>> ALTAR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("altar_crafting", AltarRecipeSerializer::new);
    public static RecipeType<AltarRecipe> ALTAR_CRAFTING;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<RecipeSerializer<?>> evt) {
        ALTAR_CRAFTING = register(TouhouLittleMaid.MOD_ID + ":altar_crafting");
    }

    private static <T extends Recipe<?>> RecipeType<T> register(final String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(key), new RecipeType<T>() {
            @Override
            public String toString() {
                return key;
            }
        });
    }
}
