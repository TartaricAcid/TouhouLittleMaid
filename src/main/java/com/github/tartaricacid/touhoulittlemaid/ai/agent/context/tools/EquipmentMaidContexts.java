package com.github.tartaricacid.touhoulittlemaid.ai.agent.context.tools;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.AbstractMaidContext;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.GameContextRegister;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IExtraStorageBackpack;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.*;

public final class EquipmentMaidContexts {
    public static final String CATEGORY = "equipment";
    private static final String SUMMARY = "Held items, backpack inventory, baubles, extra storage, and equipped armor.";

    private EquipmentMaidContexts() {
    }

    public static void registerAll(GameContextRegister register) {
        register.registerCategory(CATEGORY, SUMMARY, false);
        register.registerContext(CATEGORY, new MainHandItemContext());
        register.registerContext(CATEGORY, new OffHandItemContext());
        register.registerContext(CATEGORY, new InventoryItemsContext());
        register.registerContext(CATEGORY, new ArmorItemsContext());
    }

    private static final class MainHandItemContext extends AbstractMaidContext {
        private MainHandItemContext() {
            super("mainhand_item", "Main-hand item");
        }

        @Override
        public String getValue(EntityMaid maid) {
            return getSlotItemName(EquipmentSlot.MAINHAND, maid);
        }
    }

    private static final class OffHandItemContext extends AbstractMaidContext {
        private OffHandItemContext() {
            super("offhand_item", "Off-hand item");
        }

        @Override
        public String getValue(EntityMaid maid) {
            return getSlotItemName(EquipmentSlot.OFFHAND, maid);
        }
    }

    private static final class InventoryItemsContext extends AbstractMaidContext {
        private InventoryItemsContext() {
            super("inventory_items", "Backpack items");
        }

        @Override
        public String getValue(EntityMaid maid) {
            List<String> names = Lists.newArrayList();
            appendItemNames(names, maid.getAvailableBackpackInv());
            appendItemNames(names, maid.getMaidBauble());
            if (maid.getMaidBackpackType() instanceof IExtraStorageBackpack backpack) {
                IItemHandler extraStorage = backpack.getExtraStorage(maid);
                if (extraStorage != null) {
                    appendItemNames(names, extraStorage);
                }
            }
            if (names.isEmpty()) {
                return EMPTY;
            }
            return StringUtils.join(names, LIST_SEPARATORS);
        }
    }

    private static final class ArmorItemsContext extends AbstractMaidContext {
        private ArmorItemsContext() {
            super("armor_items", "Equipped armor");
        }

        @Override
        public String getValue(EntityMaid maid) {
            List<String> names = Lists.newArrayList();
            maid.getArmorSlots().forEach(stack -> {
                if (!stack.isEmpty()) {
                    String itemName = stack.getDisplayName().getString();
                    int count = stack.getCount();
                    names.add(ITEM_AND_COUNT_FORMAT.formatted(itemName, count));
                }
            });
            if (names.isEmpty()) {
                return EMPTY;
            }
            return StringUtils.join(names, LIST_SEPARATORS);
        }
    }

    private static String getSlotItemName(EquipmentSlot slot, EntityMaid maid) {
        ItemStack stack = maid.getItemBySlot(slot);
        if (stack.isEmpty()) {
            return EMPTY;
        }
        String itemName = stack.getDisplayName().getString();
        int count = stack.getCount();
        return ITEM_AND_COUNT_FORMAT.formatted(itemName, count);
    }

    private static void appendItemNames(List<String> names, IItemHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                String itemName = stack.getDisplayName().getString();
                int count = stack.getCount();
                names.add(ITEM_AND_COUNT_FORMAT.formatted(itemName, count));
            }
        }
    }
}
