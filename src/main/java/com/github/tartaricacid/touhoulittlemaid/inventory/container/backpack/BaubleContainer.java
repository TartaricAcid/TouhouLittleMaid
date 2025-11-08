package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.ITriggerSlotChange;
import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidBaubleChangeEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class BaubleContainer extends MaidMainContainer {
    public static final MenuType<BaubleContainer> TYPE = IMenuTypeExtension.create((windowId, inv, data)
            -> new BaubleContainer(windowId, inv, data.readInt()));

    public BaubleContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }

    public static MenuProvider create(EntityMaid maid) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Maid Bauble Container");
            }

            @Override
            public AbstractContainerMenu createMenu(int index, Inventory inventory, Player player) {
                int entityId = maid.getId();
                return new BaubleContainer(index, inventory, entityId);
            }
        };
    }

    @Override
    protected void addMainDefaultInv() {
        // 留空，表示不添加女仆物品栏
    }

    @Override
    protected void addBackpackInv(Inventory inventory) {
        // 0 级和 1 级：只有前两层
        // 2 级，前四层
        // 3 级及以上，全部开放
        int level = this.maid.getFavorabilityManager().getLevel();
        // 以防万一，检测是否越界
        int maxSize = maid.getMaidBauble().getSlots();

        for (int y = 0; y < 6; y++) {
            if (level <= 1 && y >= 2) {
                break;
            }
            if (level == 2 && y >= 4) {
                break;
            }
            for (int x = 0; x < 5; x++) {
                int index = x + y * 5;
                if (index >= maxSize) {
                    return;
                }
                addSlot(new BaubleSlot(maid, index, 152 + 18 * x, 45 + 18 * y));
            }
        }
    }

    public static class BaubleSlot extends SlotItemHandler implements ITriggerSlotChange {
        private final EntityMaid maid;

        public BaubleSlot(EntityMaid maid, int index, int xPosition, int yPosition) {
            super(maid.getMaidBauble(), index, xPosition, yPosition);
            this.maid = maid;
        }

        @Override
        public void onShiftTakeoff(@Nullable Player player, ItemStack stack) {
            if (!maid.level.isClientSide && !stack.isEmpty()) {
                IMaidBauble bauble = BaubleManager.getBauble(stack);
                if (bauble != null) {
                    bauble.onTakeOff(maid, stack);
                    NeoForge.EVENT_BUS.post(new MaidBaubleChangeEvent.TakeOff(maid, stack));
                }
            }
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            super.onTake(player, stack);
            this.onShiftTakeoff(player, stack);
        }

        @Override
        public void setByPlayer(ItemStack stack) {
            super.setByPlayer(stack);
            if (!maid.level.isClientSide && !stack.isEmpty()) {
                IMaidBauble bauble = BaubleManager.getBauble(stack);
                if (bauble != null) {
                    bauble.onPutOn(maid, stack);
                    NeoForge.EVENT_BUS.post(new MaidBaubleChangeEvent.PutOn(maid, stack));
                }
            }
        }
    }
}