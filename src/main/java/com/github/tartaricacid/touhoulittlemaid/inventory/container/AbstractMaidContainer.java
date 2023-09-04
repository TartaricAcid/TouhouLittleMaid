package com.github.tartaricacid.touhoulittlemaid.inventory.container;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import javax.annotation.Nullable;

public abstract class AbstractMaidContainer extends AbstractContainerMenu {
    protected final EntityMaid maid;

    public AbstractMaidContainer(@Nullable MenuType<?> type, int id, Inventory inventory, int entityId) {
        super(type, id);
        this.maid = (EntityMaid) inventory.player.level.getEntity(entityId);
        if (maid != null) {
            this.addPlayerInv(inventory);
            maid.guiOpening = true;
        }
    }

    private void addPlayerInv(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 88 + col * 18, 174 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 88 + col * 18, 232));
        }
    }

    public EntityMaid getMaid() {
        return maid;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        maid.guiOpening = false;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return maid.isOwnedBy(playerIn) && !maid.isSleeping() && maid.isAlive() && maid.distanceTo(playerIn) < 5.0F;
    }
}