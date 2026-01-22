package com.github.tartaricacid.touhoulittlemaid.datagen.tag;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
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

    /**
     * 零食柜会在上方摆放特定方块时，渲染出玻璃橱窗的效果
     * <p>
     * 在此标签中的方块才会让下方零食柜渲染完整玻璃橱窗
     */
    public static final TagKey<Block> SNACK_CABINET_FULL = createTagKey("snack_cabinet_full");

    /**
     * 在此标签中的方块才会让下方零食柜渲染半高玻璃橱窗
     */
    public static final TagKey<Block> SNACK_CABINET_HALF = createTagKey("snack_cabinet_half");

    public TagBlock(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                    String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    public static TagKey<Block> createTagKey(String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(TouhouLittleMaid.MOD_ID, name));
    }

    public static TagKey<Block> createTagKey(ResourceLocation resourceLocation) {
        return TagKey.create(Registries.BLOCK, resourceLocation);
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
                .add(InitBlocks.SNACK_CABINET.get())
                .addOptionalTag(new ResourceLocation("kaleidoscope_cookery:table"));

        tag(SNACK_CABINET_FULL)
                // 蛋糕全部是完整玻璃橱窗
                .add(Blocks.CAKE)
                .addOptionalTag(new ResourceLocation("forge:cakes"))
                .addOptionalTag(new ResourceLocation("c:cakes"))
                .addOptionalTag(new ResourceLocation("jmc:cakes"))
                // 农夫乐事的盛宴
                .addOptional(new ResourceLocation("farmersdelight:roast_chicken_block"))
                .addOptional(new ResourceLocation("farmersdelight:stuffed_pumpkin_block"))
                .addOptional(new ResourceLocation("farmersdelight:honey_glazed_ham_block"))
                .addOptional(new ResourceLocation("farmersdelight:shepherds_pie_block"))
                .addOptional(new ResourceLocation("farmersdelight:rice_roll_medley_block"));

        tag(SNACK_CABINET_HALF)
                // 农夫乐事的糕点
                .addOptional(new ResourceLocation("farmersdelight:apple_pie"))
                .addOptional(new ResourceLocation("farmersdelight:sweet_berry_cheesecake"))
                .addOptional(new ResourceLocation("farmersdelight:chocolate_pie"))
                // 森罗物语的方块菜，后续应该让森罗物语添加专门的 tag
                .addOptional(new ResourceLocation("kaleidoscope_cookery:dark_cuisine"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:suspicious_stir_fry"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:slime_ball_meal"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:fondant_pie"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:dongpo_pork"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:fondant_spider_eye"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:chorus_fried_egg"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:braised_fish"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:golden_salad"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:spicy_chicken"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:yakitori"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:pan_seared_knight_steak"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:stargazy_pie"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:sweet_and_sour_ender_pearls"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:crystal_lamb_chop"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:blaze_lamb_chop"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:frost_lamb_chop"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:nether_style_sashimi"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:end_style_sashimi"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:desert_style_sashimi"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:tundra_style_sashimi"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:cold_style_sashimi"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:shengjian_mantou"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:candied_potato"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:dough_drop_soup"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:stuffed_tiger_skin_pepper"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:spicy_rabbit_head"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:four_joy_meatball_soup"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:numbing_spicy_chicken"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:fried_caterpillar"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:fried_spring_roll"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:spicy_blood_stew"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:fruit_platter"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:braised_pork_ribs"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:cold_roasted_meat"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:oil_splashed_fish"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:brown_mushroom_pot_soup"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:red_mushroom_pot_soup"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:warped_fungus_pot_soup"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:crimson_fungus_pot_soup"))
                .addOptional(new ResourceLocation("kaleidoscope_cookery:buddha_jumps_over_the_wall"));

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
