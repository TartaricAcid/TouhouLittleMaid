package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipeSerializer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitRecipes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TouhouLittleMaid.MOD_ID);

    public static RegistryObject<IRecipeSerializer<?>> ALTAR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("altar_crafting", AltarRecipeSerializer::new);
    public static IRecipeType<AltarRecipe> ALTAR_CRAFTING;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<IRecipeSerializer<?>> evt) {
        ALTAR_CRAFTING = register(TouhouLittleMaid.MOD_ID + ":altar_crafting");
    }

    private static <T extends IRecipe<?>> IRecipeType<T> register(final String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(key), new IRecipeType<T>() {
            @Override
            public String toString() {
                return key;
            }
        });
    }
}
