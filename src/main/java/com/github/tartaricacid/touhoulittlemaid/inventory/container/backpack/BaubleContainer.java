package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
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
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.SlotItemHandler;

public class BaubleContainer extends MaidMainContainer {
    public static final MenuType<BaubleContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new BaubleContainer(windowId, inv, data.readInt()));

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
        for (int i = 0; i < 6; i++) {
            addSlot(new BaubleSlot(maid, i, 143 + 18 * i, 37));
        }
        for (int i = 0; i < 6; i++) {
            addSlot(new BaubleSlot(maid, 6 + i, 143 + 18 * i, 59));
        }
        for (int i = 0; i < 6; i++) {
            addSlot(new BaubleSlot(maid, 12 + i, 143 + 18 * i, 82));
        }
        for (int i = 0; i < 6; i++) {
            addSlot(new BaubleSlot(maid, 18 + i, 143 + 18 * i, 100));
        }
    }

    public static class BaubleSlot extends SlotItemHandler {
        private final EntityMaid maid;

        public BaubleSlot(EntityMaid maid, int index, int xPosition, int yPosition) {
            super(maid.getMaidBauble(), index, xPosition, yPosition);
            this.maid = maid;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            super.onTake(player, stack);
            if (!maid.level.isClientSide && !stack.isEmpty()) {
                IMaidBauble bauble = BaubleManager.getBauble(stack);
                if (bauble != null) {
                    bauble.onTakeOff(maid, stack);
                }
            }
        }

        @Override
        public void setByPlayer(ItemStack stack) {
            super.setByPlayer(stack);
            if (!maid.level.isClientSide && !stack.isEmpty()) {
                IMaidBauble bauble = BaubleManager.getBauble(stack);
                if (bauble != null) {
                    bauble.onPutOn(maid, stack);
                }
            }
        }
    }
}