package com.github.tartaricacid.touhoulittlemaid.datagen.tag;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;


public class TagBlock extends BlockTagsProvider {
    public static final TagKey<Block> MAID_JUMP_FORBIDDEN_BLOCK = TagKey.create(ForgeRegistries.BLOCKS.getRegistryKey(), new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid_jump_forbidden_block"));
    public TagBlock(DataGenerator dataGenerator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, modId, existingFileHelper);
    }
    @Override
    protected void addTags() {
        tag(MAID_JUMP_FORBIDDEN_BLOCK)
                .addTag(BlockTags.DOORS)
                .addTag(BlockTags.FENCES)
                .addTag(BlockTags.CLIMBABLE);
    }
}
