package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.ITriggerSlotChange;
import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidBaubleChangeEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SyncBaubleMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

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

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack1 = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack2 = slot.getItem();
            stack1 = stack2.copy();

            if (index < PLAYER_INVENTORY_SIZE) {
                // 先尝试移动到饰品栏
                int baubleSlots = PLAYER_INVENTORY_SIZE + 6;
                if (!this.moveItemStackTo(stack2, baubleSlots, this.slots.size(), false)) {
                    // 如果失败，再尝试移动到女仆物品栏
                    if (!this.moveItemStackTo(stack2, PLAYER_INVENTORY_SIZE, baubleSlots, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(stack2, 0, PLAYER_INVENTORY_SIZE, true)) {
                return ItemStack.EMPTY;
            }

            if (stack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack2.getCount() == stack1.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack2);
            // 触发 Shift 点击取出事件
            if (slot instanceof ITriggerSlotChange slotChange) {
                slotChange.onShiftTakeoff(player, stack1);
            }

            // 用来修正护甲值不变化的问题
            if (PLAYER_INVENTORY_SIZE <= index && index < PLAYER_INVENTORY_SIZE + 4) {
                EquipmentSlot equipmentSlot = SLOT_IDS[index - PLAYER_INVENTORY_SIZE];
                maid.setLastArmorItem(equipmentSlot, stack1);
            }
            // 还有主副手
            if (PLAYER_INVENTORY_SIZE + 4 <= index && index < PLAYER_INVENTORY_SIZE + 6) {
                int slotIndex = index - PLAYER_INVENTORY_SIZE - 4;
                EquipmentSlot equipmentSlot = slotIndex == 0 ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                maid.setLastHandItem(equipmentSlot, stack1);
            }
        }
        return stack1;
    }

    public static class BaubleSlot extends SlotItemHandler implements ITriggerSlotChange {
        private final EntityMaid maid;

        public BaubleSlot(EntityMaid maid, int index, int xPosition, int yPosition) {
            super(maid.getMaidBauble(), index, xPosition, yPosition);
            this.maid = maid;
        }

        @Override
        public void onShiftTakeoff(@Nullable Player player, ItemStack stack) {
            if (maid.level.isClientSide || stack.isEmpty()) {
                return;
            }
            IMaidBauble bauble = BaubleManager.getBauble(stack);
            if (bauble == null) {
                return;
            }
            bauble.onTakeOff(maid, stack);
            MinecraftForge.EVENT_BUS.post(new MaidBaubleChangeEvent.TakeOff(maid, stack));
            // 如果是可同步，同步删除客户端信息
            if (bauble.syncClient(maid, stack)) {
                SyncBaubleMessage msg = SyncBaubleMessage.partialDel(maid.getId(), this.getContainerSlot());
                NetworkHandler.sendToTrackingEntity(msg, maid);
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
            if (maid.level.isClientSide || stack.isEmpty()) {
                return;
            }
            IMaidBauble bauble = BaubleManager.getBauble(stack);
            if (bauble == null) {
                return;
            }
            bauble.onPutOn(maid, stack);
            MinecraftForge.EVENT_BUS.post(new MaidBaubleChangeEvent.PutOn(maid, stack));
            // 如果是可同步，同步客户端信息
            if (bauble.syncClient(maid, stack)) {
                SyncBaubleMessage msg = SyncBaubleMessage.partialSync(maid.getId(), this.getContainerSlot(), stack);
                NetworkHandler.sendToTrackingEntity(msg, maid);
            }
        }
    }
}