package com.github.tartaricacid.touhoulittlemaid.compat.patchouli;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.PatchouliAPI;

/**
 * @author TartaricAcid
 * @date 2019/10/6 19:30
 **/
public final class MultiblockRegistry {
    public static void init() {
        PatchouliAPI.IPatchouliAPI api = PatchouliAPI.instance;
        api.registerMultiblock(new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar"),
                api.makeMultiblock(new String[][]{
                                {
                                        "        ",
                                        "       R",
                                        "       R",
                                        "       R",
                                        "       R",
                                        "       R",
                                        "       R",
                                        "        ",
                                },
                                {
                                        "        ",
                                        "        ",
                                        "       R",
                                        "        ",
                                        "        ",
                                        "       R",
                                        "        ",
                                        "        ",
                                },
                                {
                                        "        ",
                                        "       R",
                                        "       R",
                                        "       R",
                                        "       R",
                                        "       R",
                                        "       R",
                                        "        ",
                                },
                                {
                                        "  O  O  ",
                                        "        ",
                                        "O      R",
                                        "        ",
                                        "        ",
                                        "O      R",
                                        "        ",
                                        "  O  O  ",
                                },
                                {
                                        "  O  O  ",
                                        "        ",
                                        "O      R",
                                        "        ",
                                        "        ",
                                        "O      R",
                                        "        ",
                                        "  O  O  ",
                                },
                                {
                                        "  O  O  ",
                                        "        ",
                                        "O      R",
                                        "        ",
                                        "       0",
                                        "O      R",
                                        "        ",
                                        "  O  O  ",
                                }
                        },
                        'O', api.predicateMatcher(Blocks.LOG.getDefaultState(),
                                (state) -> {
                                    if (state.getBlock() == Blocks.LOG) {
                                        return state.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.OAK
                                                && state.getValue(BlockOldLog.LOG_AXIS) == BlockLog.EnumAxis.Y;
                                    }
                                    return false;
                                }),
                        'R', api.predicateMatcher(Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED),
                                (state) -> {
                                    if (state.getBlock() == Blocks.WOOL) {
                                        return state.getValue(BlockColored.COLOR) == EnumDyeColor.RED;
                                    }
                                    return false;
                                }),
                        ' ', api.anyMatcher()));
    }
}
