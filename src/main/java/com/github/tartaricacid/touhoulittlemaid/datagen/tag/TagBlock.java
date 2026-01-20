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
    /**
     * 女仆有时候会在一些不该触发跳跃逻辑的方块上反复尝试跳来跳去，
     * 故添加此标签来将一些方块放入黑名单中
     */
    public static final TagKey<Block> MAID_JUMP_FORBIDDEN_BLOCK = createTagKey("maid_jump_forbidden_block");

    /**
     * 女仆避让方块标签，女仆在寻路、传送时会尽可能避让这些方块
     */
    public static final TagKey<Block> MAID_AVOID_BLOCK = createTagKey("maid_avoid_block");

    /**
     * 在修建祭坛时，可以当做祭坛鸟居部分的方块
     */
    public static final TagKey<Block> ALTAR_TORII = createTagKey("altar_torii");

    /**
     * 在修建祭坛时，可以当做祭坛柱子材料的方块；
     * <p>
     * 默认已经包含 <code>#minecraft:logs</code> 标签
     */
    public static final TagKey<Block> ALTAR_PILLAR = createTagKey("altar_pillar");

    /**
     * 女仆有偷吃方块食物的机制，但是这可能会误把一些拿来做装饰的食物方块也偷吃掉
     * <p>
     * 故我们现在为一些方块添加 tag，只有放在此方块上承载的食物方块女仆才会偷吃
     */
    public static final TagKey<Block> MAID_SNACK_STAND_BLOCK = createTagKey("maid_snack_stand_block");

    public TagBlock(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                    String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    public static TagKey<Block> createTagKey(String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(TouhouLittleMaid.MOD_ID, name));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(MAID_JUMP_FORBIDDEN_BLOCK)
                .addTag(BlockTags.DOORS)
                .addTag(BlockTags.FENCES)
                .addTag(BlockTags.CLIMBABLE);

        tag(ALTAR_TORII)
                .add(Blocks.RED_WOOL, Blocks.RED_CONCRETE)
                .addOptional(new ResourceLocation("biomesoplenty:redwood_planks"));

        tag(ALTAR_PILLAR).addTag(BlockTags.LOGS);

        tag(MAID_SNACK_STAND_BLOCK)
                .addOptional(new ResourceLocation("kaleidoscope_cookery:table_oak"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:table_spruce"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:table_acacia"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:table_bamboo"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:table_birch"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:table_cherry"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:table_crimson"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:table_dark_oak"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:table_jungle"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:table_mangrove"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:table_warped"));

        tag(MAID_AVOID_BLOCK)
                // 怎么能在吃饭的桌子上跳来跳去呢
                .addTag(MAID_SNACK_STAND_BLOCK)
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
                // MEK 反应堆的聚变堆和超临界移相器
                .addOptional(new ResourceLocation("mekanismgenerators:fusion_reactor_frame"))
                .addOptional(new ResourceLocation("mekanism:sps_casing"))
                // 机械动力附属的铁丝网
                .addOptional(new ResourceLocation("createaddition:barbed_wire"))
                // 沉浸工程的铁丝网
                .addOptional(new ResourceLocation("immersiveengineering:razor_wire"))
                // 铁魔法的两个火堆
                .addOptional(new ResourceLocation("irons_spellbooks:brazier"))
                .addOptional(new ResourceLocation("irons_spellbooks:brazier_soul"))
                // 刷怪塔实用设备的锥刺和研磨机
                .addOptional(new ResourceLocation("mob_grinding_utils:spikes"))
                .addOptional(new ResourceLocation("mob_grinding_utils:saw"));
    }
}
