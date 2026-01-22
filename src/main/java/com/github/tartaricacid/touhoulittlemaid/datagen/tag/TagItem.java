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
    /**
     * 能够驯服女仆的物品
     * <p>
     * 默认已经把所有带有 <code>#forge:cakes</code> 和 <code>#c:cakes</code> 标签的物品加入其中了
     */
    public static final TagKey<Item> MAID_TAMED_ITEM = createTagKey("maid_tamed_item");

    /**
     * 物品拥有经验修补后，女仆在吸收经验或者 P 点时能够进行修复；
     * <p>
     * 但是部分物品不能这么做，可将其加入此 tag 下
     */
    public static final TagKey<Item> MAID_MENDING_BLOCKLIST_ITEM = createTagKey("maid_mending_blocklist_item");

    /**
     * 女仆和玩家类似，在穿戴拥有消失诅咒附魔的装备（或者饰品）后死亡，其对应的物品会直接消失；
     * <p>
     * 但是部分物品不能这么做，可将其加入此 tag 下
     */
    public static final TagKey<Item> MAID_VANISHING_BLOCKLIST_ITEM = createTagKey("maid_vanishing_blocklist_item");

    /**
     * 女仆进食黑名单，与配置文件协同作用，方便拓展兼容
     * <p>
     * 全局的，适用于工作餐、回血餐和家庭餐
     */
    public static final TagKey<Item> MAID_EAT_BLOCKLIST_ITEM = createTagKey("maid_eat_blocklist_item");

    public TagItem(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider,
                   CompletableFuture<TagLookup<Block>> pBlockTags, String modId,
                   @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, modId, existingFileHelper);
    }

    public static TagKey<Item> createTagKey(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(TouhouLittleMaid.MOD_ID, name));
    }

    public static TagKey<Item> createTagKey(ResourceLocation resourceLocation) {
        return TagKey.create(Registries.ITEM, resourceLocation);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
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

        // 森罗物语辣椒
        tag(MAID_EAT_BLOCKLIST_ITEM)
                .addOptional(new ResourceLocation("kaleidoscope_cookery:red_chili"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:green_chili"));
    }
}
