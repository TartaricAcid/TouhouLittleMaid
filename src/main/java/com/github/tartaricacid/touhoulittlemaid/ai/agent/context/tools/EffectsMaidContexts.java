package com.github.tartaricacid.touhoulittlemaid.ai.agent.context.tools;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.AbstractMaidContext;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.GameContextRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.LIST_SEPARATORS;
import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.NONE;

public final class EffectsMaidContexts {
    public static final String CATEGORY = "effects";
    private static final String SUMMARY = "Self active effects.";

    private EffectsMaidContexts() {
    }

    public static void registerAll(GameContextRegister register) {
        register.registerCategory(CATEGORY, SUMMARY, false);
        register.registerContext(CATEGORY, new MaidEffectsContext());
    }

    private static final class MaidEffectsContext extends AbstractMaidContext {
        private MaidEffectsContext() {
            super("effects", "Self effects");
        }

        @Override
        public String getValue(EntityMaid maid) {
            List<String> names = Lists.newArrayList();
            maid.getActiveEffects().forEach(effect -> names.add(effect.toString()));
            if (names.isEmpty()) {
                return NONE;
            }
            return StringUtils.join(names, LIST_SEPARATORS);
        }
    }
}
