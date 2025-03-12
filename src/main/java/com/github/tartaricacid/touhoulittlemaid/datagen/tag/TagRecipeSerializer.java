package com.github.tartaricacid.touhoulittlemaid.datagen.tag;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.github.tartaricacid.touhoulittlemaid.init.InitRecipes.ALTAR_RECIPE_SERIALIZER;

public class TagRecipeSerializer extends TagsProvider<RecipeSerializer<?>> {
    public static final TagKey<RecipeSerializer<?>> AUTOMATION_IGNORE = TagKey.create(Registries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath("create","automation_ignore"));
    public TagRecipeSerializer(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.RECIPE_SERIALIZER, lookupProvider, TouhouLittleMaid.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(AUTOMATION_IGNORE).addOptional(ALTAR_RECIPE_SERIALIZER.getId());
    }
}
