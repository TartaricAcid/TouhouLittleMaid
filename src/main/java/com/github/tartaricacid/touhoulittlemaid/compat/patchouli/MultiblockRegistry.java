package com.github.tartaricacid.touhoulittlemaid.compat.patchouli;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;

public final class MultiblockRegistry {
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "altar");
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
        IStateMatcher logMatcher = api.tagMatcher(BlockTags.LOGS);
        IStateMatcher redWoolMatcher = api.predicateMatcher(Blocks.RED_WOOL.defaultBlockState(), state -> state.is(Blocks.RED_WOOL));
        api.registerMultiblock(ID, api.makeMultiblock(TEMPLATE, 'O', logMatcher, 'R', redWoolMatcher, ' ', api.anyMatcher()));
    }
}
