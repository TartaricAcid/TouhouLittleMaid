package com.github.tartaricacid.touhoulittlemaid.compat.patchouli;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;

public final class MultiblockRegistry {
    private static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar");
    private static final String[][] TEMPLATE = new String[][]{
            {"        ", "       R", "       R", "       R", "       R", "       R", "       R", "        ",},
            {"        ", "        ", "       R", "        ", "        ", "       R", "        ", "        ",},
            {"        ", "       R", "       R", "       R", "       R", "       R", "       R", "        ",},
            {"  O  O  ", "        ", "O      R", "        ", "        ", "O      R", "        ", "  O  O  ",},
            {"  O  O  ", "        ", "O      R", "        ", "        ", "O      R", "        ", "  O  O  ",},
            {"  O  O  ", "        ", "O      R", "        ", "       0", "O      R", "        ", "  O  O  ",}
    };

    public static void init() {
        PatchouliAPI.IPatchouliAPI api = PatchouliAPI.get();
        IStateMatcher oakLogMatcher = api.predicateMatcher(Blocks.OAK_LOG.defaultBlockState(), state -> state.is(Blocks.OAK_LOG) && state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y);
        IStateMatcher redWoolMatcher = api.predicateMatcher(Blocks.RED_WOOL.defaultBlockState(), state -> state.is(Blocks.RED_WOOL));
        api.registerMultiblock(ID, api.makeMultiblock(TEMPLATE, 'O', oakLogMatcher, 'R', redWoolMatcher, ' ', api.anyMatcher()));
    }
}
