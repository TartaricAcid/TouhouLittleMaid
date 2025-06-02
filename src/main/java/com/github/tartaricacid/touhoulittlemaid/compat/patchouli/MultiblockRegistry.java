package com.github.tartaricacid.touhoulittlemaid.compat.patchouli;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.TagBlock;
import net.minecraft.resources.ResourceLocation;
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
        IStateMatcher pillarMatcher = api.tagMatcher(TagBlock.ALTAR_PILLAR);
        IStateMatcher toriiMatcher = api.tagMatcher(TagBlock.ALTAR_TORII);
        api.registerMultiblock(ID, api.makeMultiblock(TEMPLATE, 'O', pillarMatcher, 'R', toriiMatcher, ' ', api.anyMatcher()));
    }
}
