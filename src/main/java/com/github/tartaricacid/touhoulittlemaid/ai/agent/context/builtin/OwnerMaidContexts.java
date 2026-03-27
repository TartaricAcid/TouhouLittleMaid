package com.github.tartaricacid.touhoulittlemaid.ai.agent.context.builtin;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.AbstractMaidContext;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.MaidContextRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.*;

public final class OwnerMaidContexts {
    public static final String CATEGORY = "owner";
    private static final String SUMMARY = "Owner identity, health, and equipment information.";

    private OwnerMaidContexts() {
    }

    public static void registerAll(MaidContextRegister register) {
        register.registerCategory(CATEGORY, SUMMARY);
        register.registerContext(CATEGORY, new OwnerNameContext());
        register.registerContext(CATEGORY, new OwnerHealthContext());
        register.registerContext(CATEGORY, new OwnerMainHandContext());
        register.registerContext(CATEGORY, new OwnerArmorContext());
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

    private static final class OwnerMainHandContext extends AbstractMaidContext {
        private OwnerMainHandContext() {
            super("owner_mainhand", "Owner main-hand item");
        }

        @Override
        public String getValue(EntityMaid maid) {
            LivingEntity owner = maid.getOwner();
            if (owner == null) {
                return EMPTY;
            }
            ItemStack stack = owner.getItemBySlot(EquipmentSlot.MAINHAND);
            if (stack.isEmpty()) {
                return EMPTY;
            }
            return ITEM_AND_COUNT_FORMAT.formatted(stack.getDisplayName().getString(), stack.getCount());
        }
    }

    private static final class OwnerArmorContext extends AbstractMaidContext {
        private OwnerArmorContext() {
            super("owner_armor", "Owner equipped armor");
        }

        @Override
        public String getValue(EntityMaid maid) {
            LivingEntity owner = maid.getOwner();
            if (owner == null) {
                return EMPTY;
            }
            java.util.List<String> names = com.google.common.collect.Lists.newArrayList();
            owner.getArmorSlots().forEach(stack -> {
                if (!stack.isEmpty()) {
                    names.add(ITEM_AND_COUNT_FORMAT.formatted(stack.getDisplayName().getString(), stack.getCount()));
                }
            });
            if (names.isEmpty()) {
                return EMPTY;
            }
            return StringUtils.join(names, LIST_SEPARATORS);
        }
    }
}
