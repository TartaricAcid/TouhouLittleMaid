package com.github.tartaricacid.touhoulittlemaid.datagen.tag;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

public class TagItem extends ItemTagsProvider {
    public static final TagKey<Item> GOHEI_ENCHANTABLE = TagKey.create(Registries.ITEM, getResourceLocation("gohei_enchantable"));
    public static final TagKey<Item> MAID_PLANTABLE_SEEDS = TagKey.create(Registries.ITEM, getResourceLocation("maid_plantable_seeds"));
    public static final TagKey<Item> MAID_TAMED_ITEM = TagKey.create(Registries.ITEM, getResourceLocation("maid_tamed_item"));
    public static final TagKey<Item> MAID_MENDING_BLOCKLIST_ITEM = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "maid_mending_blocklist_item"));
    public static final TagKey<Item> MAID_VANISHING_BLOCKLIST_ITEM = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "maid_vanishing_blocklist_item"));

    public TagItem(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(GOHEI_ENCHANTABLE).add(InitItems.HAKUREI_GOHEI.asItem());
        this.tag(GOHEI_ENCHANTABLE).add(InitItems.SANAE_GOHEI.asItem());

        this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(InitItems.HAKUREI_GOHEI.asItem())
                .add(InitItems.SANAE_GOHEI.asItem())
                .add(InitItems.ULTRAMARINE_ORB_ELIXIR.asItem())
                .add(InitItems.EXPLOSION_PROTECT_BAUBLE.asItem())
                .add(InitItems.FIRE_PROTECT_BAUBLE.asItem())
                .add(InitItems.PROJECTILE_PROTECT_BAUBLE.asItem())
                .add(InitItems.MAGIC_PROTECT_BAUBLE.asItem())
                .add(InitItems.FALL_PROTECT_BAUBLE.asItem())
                .add(InitItems.DROWN_PROTECT_BAUBLE.asItem())
                .add(InitItems.NIMBLE_FABRIC.asItem());

        this.tag(MAID_PLANTABLE_SEEDS).addTag(ItemTags.VILLAGER_PLANTABLE_SEEDS);
        this.tag(MAID_PLANTABLE_SEEDS).add(Items.NETHER_WART);

        this.tag(MAID_TAMED_ITEM)
                .add(Items.CAKE)
                .addOptionalTag(ResourceLocation.parse("forge:cakes"))
                .addOptionalTag(ResourceLocation.parse("c:cakes"))
                .addOptionalTag(ResourceLocation.parse("jmc:cakes"))
                .addOptional(ResourceLocation.parse("kawaiidishes:cheese_cake"))
                .addOptional(ResourceLocation.parse("kawaiidishes:honey_cheese_cake"))
                .addOptional(ResourceLocation.parse("kawaiidishes:chocolate_cheese_cake"))
                .addOptional(ResourceLocation.parse("kawaiidishes:piece_of_cake"))
                .addOptional(ResourceLocation.parse("kawaiidishes:piece_of_cheesecake"))
                .addOptional(ResourceLocation.parse("kawaiidishes:piece_of_chocolate_cheesecake"))
                .addOptional(ResourceLocation.parse("kawaiidishes:piece_of_honey_cheesecake"));

        tag(MAID_MENDING_BLOCKLIST_ITEM).add(InitItems.ULTRAMARINE_ORB_ELIXIR.get());
        tag(MAID_VANISHING_BLOCKLIST_ITEM).add(InitItems.ULTRAMARINE_ORB_ELIXIR.get());
    }
}
