package com.github.tartaricacid.touhoulittlemaid.ai.agent.context.tools;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.AbstractMaidContext;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.GameContextRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.*;

public final class UserContexts {
    public static final String CATEGORY = "user";
    private static final String SUMMARY = "User identity, health, and equipment information.";

    private UserContexts() {
    }

    public static void registerAll(GameContextRegister register) {
        register.registerCategory(CATEGORY, SUMMARY, false);
        register.registerContext(CATEGORY, new UserNameContext());
        register.registerContext(CATEGORY, new UserHealthContext());
        register.registerContext(CATEGORY, new UserMainHandContext());
        register.registerContext(CATEGORY, new UserArmorContext());
    }

    private static final class UserNameContext extends AbstractMaidContext {
        private UserNameContext() {
            super("user_name", "User name");
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

    private static final class UserHealthContext extends AbstractMaidContext {
        private UserHealthContext() {
            super("user_healthy", "User health");
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

    private static final class UserMainHandContext extends AbstractMaidContext {
        private UserMainHandContext() {
            super("user_mainhand", "User main-hand item");
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

    private static final class UserArmorContext extends AbstractMaidContext {
        private UserArmorContext() {
            super("user_armor", "User equipped armor");
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
