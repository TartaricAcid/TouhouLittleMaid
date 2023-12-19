package com.github.tartaricacid.touhoulittlemaid.entity.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IBackpackData;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.client.model.backpack.FurnaceBackpackModel;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.FurnaceBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.FurnaceBackpackContainer;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class FurnaceBackpack extends IMaidBackpack {
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "furnace_backpack");

    @Override
    public void onPutOn(ItemStack stack, PlayerEntity player, EntityMaid maid) {
    }

    @Override
    public void onTakeOff(ItemStack stack, PlayerEntity player, EntityMaid maid) {
        IBackpackData backpackData = maid.getBackpackData();
        if (backpackData instanceof FurnaceBackpackData) {
            FurnaceBackpackData furnaceBackpackData = (FurnaceBackpackData) backpackData;
            InvWrapper inv = new InvWrapper(furnaceBackpackData);
            ItemsUtil.dropEntityItems(maid, inv);
        }

        Item item = stack.getItem();
        if (item == InitItems.MAID_BACKPACK_SMALL.get()) {
            ItemsUtil.dropEntityItems(maid, maid.getMaidInv(), BackpackLevel.SMALL_CAPACITY);
            return;
        }
        if (item == InitItems.MAID_BACKPACK_MIDDLE.get() || item == InitItems.MAID_BACKPACK_BIG.get()) {
            return;
        }
        this.dropAllItems(maid);
    }

    @Override
    public void onSpawnTombstone(EntityMaid maid, EntityTombstone tombstone) {
        IBackpackData backpackData = maid.getBackpackData();
        if (backpackData instanceof FurnaceBackpackData) {
            FurnaceBackpackData furnaceBackpackData = (FurnaceBackpackData) backpackData;
            InvWrapper inv = new InvWrapper(furnaceBackpackData);
            for (int i = 0; i < inv.getSlots(); i++) {
                int size = inv.getSlotLimit(i);
                tombstone.insertItem(inv.extractItem(i, size, false));
            }
        }
    }

    @Override
    public INamedContainerProvider getGuiProvider(int entityId) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new StringTextComponent("Maid Furnace Container");
            }

            @Override
            public AbstractMaidContainer createMenu(int index, PlayerInventory playerInventory, PlayerEntity player) {
                return new FurnaceBackpackContainer(index, playerInventory, entityId);
            }
        };
    }

    @Override
    public boolean hasBackpackData() {
        return true;
    }

    @Nullable
    @Override
    public IBackpackData getBackpackData(EntityMaid maid) {
        return new FurnaceBackpackData(maid);
    }

    @Override
    public int getAvailableMaxContainerIndex() {
        return BackpackLevel.FURNACE_CAPACITY;
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public EntityModel<EntityMaid> getBackpackModel() {
        return new FurnaceBackpackModel();
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getBackpackTexture() {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/furnace_backpack.png");
    }

    @OnlyIn(Dist.CLIENT)
    public void offsetBackpackItem(MatrixStack poseStack) {
        poseStack.translate(0, 0.625, 0.25);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Item getItem() {
        return InitItems.FURNACE_BACKPACK.get();
    }
}


