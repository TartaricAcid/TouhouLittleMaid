package com.github.tartaricacid.touhoulittlemaid.datagen.tag;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TagItem extends ItemTagsProvider {
    public static final TagKey<Item> MAID_TAMED_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid_tamed_item"));
    public static final TagKey<Item> MAID_MENDING_BLOCKLIST_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid_mending_blocklist_item"));
    public static final TagKey<Item> MAID_VANISHING_BLOCKLIST_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid_vanishing_blocklist_item"));

    public TagItem(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(MAID_TAMED_ITEM)
                .add(Items.CAKE)
                .addOptionalTag(new ResourceLocation("forge:cakes"))
                .addOptionalTag(new ResourceLocation("c:cakes"))
                .addOptionalTag(new ResourceLocation("jmc:cakes"))
                .addOptional(new ResourceLocation("kawaiidishes:cheese_cake"))
                .addOptional(new ResourceLocation("kawaiidishes:honey_cheese_cake"))
                .addOptional(new ResourceLocation("kawaiidishes:chocolate_cheese_cake"))
                .addOptional(new ResourceLocation("kawaiidishes:piece_of_cake"))
                .addOptional(new ResourceLocation("kawaiidishes:piece_of_cheesecake"))
                .addOptional(new ResourceLocation("kawaiidishes:piece_of_chocolate_cheesecake"))
                .addOptional(new ResourceLocation("kawaiidishes:piece_of_honey_cheesecake"));

        tag(MAID_MENDING_BLOCKLIST_ITEM).add(InitItems.ULTRAMARINE_ORB_ELIXIR.get());
        tag(MAID_VANISHING_BLOCKLIST_ITEM).add(InitItems.ULTRAMARINE_ORB_ELIXIR.get());
    }
}
