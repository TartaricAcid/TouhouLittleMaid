package com.github.tartaricacid.touhoulittlemaid.dataGen;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public class TagItem extends ItemTagsProvider {
    public static final TagKey<Item> GOHEI_ENCHANTABLE = TagKey.create(Registries.ITEM, getResourceLocation("gohei_enchantable"));
    public static final TagKey<Item> MAID_PLANTABLE_SEEDS = TagKey.create(Registries.ITEM, getResourceLocation("maid_plantable_seeds"));

    public TagItem(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(GOHEI_ENCHANTABLE).add(InitItems.HAKUREI_GOHEI.asItem());
        this.tag(GOHEI_ENCHANTABLE).add(InitItems.SANAE_GOHEI.asItem());

        this.tag(MAID_PLANTABLE_SEEDS).addTag(ItemTags.VILLAGER_PLANTABLE_SEEDS);
        this.tag(MAID_PLANTABLE_SEEDS).add(Items.NETHER_WART);
    }
}
