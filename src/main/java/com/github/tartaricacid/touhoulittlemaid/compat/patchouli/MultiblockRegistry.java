package com.github.tartaricacid.touhoulittlemaid.compat.patchouli;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.common.base.Predicates;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;

public final class MultiblockRegistry {
    private static final String PATCHOULI_MOD_ID = "patchouli";
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
        if (ModList.get().isLoaded(PATCHOULI_MOD_ID)) {
            PatchouliAPI.IPatchouliAPI api = PatchouliAPI.get();
            IStateMatcher oakLogMatcher = api.predicateMatcher(Blocks.OAK_LOG.defaultBlockState(), state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y);
            IStateMatcher redWoolMatcher = api.predicateMatcher(Blocks.RED_WOOL.defaultBlockState(), Predicates.alwaysTrue());
            api.registerMultiblock(ID, api.makeMultiblock(TEMPLATE, 'O', oakLogMatcher, 'R', redWoolMatcher, ' ', api.anyMatcher()));
        }
    }
}
