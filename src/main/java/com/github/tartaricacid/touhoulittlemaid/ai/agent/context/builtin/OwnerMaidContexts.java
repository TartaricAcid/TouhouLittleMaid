package com.github.tartaricacid.touhoulittlemaid.ai.agent.context.builtin;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.AbstractMaidContext;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.MaidContextRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.StringUtils;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.DEFAULT_OWNER_NAME;
import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.HEALTHY_FORMAT;

public final class OwnerMaidContexts {
    public static final String CATEGORY = "owner";
    private static final String SUMMARY = "Owner identity and owner health information.";

    private OwnerMaidContexts() {
    }

    public static void registerAll(MaidContextRegister register) {
        register.registerCategory(CATEGORY, SUMMARY);
        register.registerContext(CATEGORY, new OwnerNameContext());
        register.registerContext(CATEGORY, new OwnerHealthContext());
    }

    private static final class OwnerNameContext extends AbstractMaidContext {
        private OwnerNameContext() {
            super("owner_name", "Owner name");
        }

        @Override
        public String getValue(EntityMaid maid) {
            String ownerName = maid.getAiChatManager().ownerName;
            if (StringUtils.isBlank(ownerName)) {
                return DEFAULT_OWNER_NAME;
            }
            return ownerName;
        }
    }

    private static final class OwnerHealthContext extends AbstractMaidContext {
        private OwnerHealthContext() {
            super("owner_healthy", "Owner health");
        }

        @Override
        public String getValue(EntityMaid maid) {
            LivingEntity owner = maid.getOwner();
            if (owner != null) {
                float maxHealth = owner.getMaxHealth();
                float health = owner.getHealth();
                return HEALTHY_FORMAT.formatted(health, maxHealth);
            }
            return StringUtils.EMPTY;
        }
    }
}
