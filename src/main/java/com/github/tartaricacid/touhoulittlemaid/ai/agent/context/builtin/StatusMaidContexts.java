package com.github.tartaricacid.touhoulittlemaid.ai.agent.context.builtin;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.AbstractMaidContext;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.MaidContextRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.*;

public final class StatusMaidContexts {
    public static final String CATEGORY = "status";
    private static final String SUMMARY = "Maid health and current status effects.";

    private StatusMaidContexts() {
    }

    public static void registerAll(MaidContextRegister register) {
        register.registerCategory(CATEGORY, SUMMARY);
        register.registerContext(CATEGORY, new MaidHealthContext());
        register.registerContext(CATEGORY, new MaidEffectsContext());
    }

    private static final class MaidHealthContext extends AbstractMaidContext {
        private MaidHealthContext() {
            super("healthy", "Maid health");
        }

        @Override
        public String getValue(EntityMaid maid) {
            float maxHealth = maid.getMaxHealth();
            float health = maid.getHealth();
            return HEALTHY_FORMAT.formatted(health, maxHealth);
        }
    }

    private static final class MaidEffectsContext extends AbstractMaidContext {
        private MaidEffectsContext() {
            super("effects", "Maid effects");
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
