package com.github.tartaricacid.touhoulittlemaid.datagen.tag;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TagBlock extends BlockTagsProvider {
    public static final TagKey<Block> MAID_JUMP_FORBIDDEN_BLOCK = TagKey.create(Registries.BLOCK, new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid_jump_forbidden_block"));
    public static final TagKey<Block> MAID_AVOID_BLOCK = TagKey.create(Registries.BLOCK, new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid_avoid_block"));

    public static final TagKey<Block> ALTAR_TORII = TagKey.create(Registries.BLOCK, new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_torii"));
    public static final TagKey<Block> ALTAR_PILLAR = TagKey.create(Registries.BLOCK, new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_pillar"));

    public TagBlock(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(MAID_JUMP_FORBIDDEN_BLOCK)
                .addTag(BlockTags.DOORS)
                .addTag(BlockTags.FENCES)
                .addTag(BlockTags.CLIMBABLE);

        tag(ALTAR_TORII).add(Blocks.RED_WOOL, Blocks.RED_CONCRETE).addOptional(new ResourceLocation("biomesoplenty:redwood_planks"));
        tag(ALTAR_PILLAR).addTag(BlockTags.LOGS);

        tag(MAID_AVOID_BLOCK)
                // 机械动力
                .addOptional(new ResourceLocation("create:mechanical_saw"))
                .addOptional(new ResourceLocation("create:crushing_wheel"))
                .addOptional(new ResourceLocation("create:crushing_wheel_controller"))
                // 黄蜂领域
                .addOptional(new ResourceLocation("the_bumblezone:heavy_air"))
                .addOptional(new ResourceLocation("the_bumblezone:windy_air"))
                // 农夫乐事
                .addOptional(new ResourceLocation("farmersdelight:stove"))
                // 暮色森林
                .addOptional(new ResourceLocation("twilightforest:hedge"))
                .addOptional(new ResourceLocation("twilightforest:fiery_block"))
                .addOptional(new ResourceLocation("twilightforest:knightmetal_block"))
                // Alex 的洞穴
                .addOptional(new ResourceLocation("alexscaves:primal_magma"))
                .addOptional(new ResourceLocation("alexscaves:primal_magma"))
                // MEK 反应堆
                .addOptional(new ResourceLocation("mekanismgenerators:fission_reactor_casing"))
                .addOptional(new ResourceLocation("mekanismgenerators:fusion_reactor_frame"))
                .addOptional(new ResourceLocation("mekanismgenerators:reactor_glass"));
    }
}
