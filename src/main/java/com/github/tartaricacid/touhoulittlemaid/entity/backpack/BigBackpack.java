package com.github.tartaricacid.touhoulittlemaid.entity.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.client.model.backpack.BigBackpackModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.BigBackpackContainer;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BigBackpack extends IMaidBackpack {
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "big_backpack");

    @Override
    public void onPutOn(ItemStack stack, PlayerEntity player, EntityMaid maid) {
    }

    @Override
    public void onTakeOff(ItemStack stack, PlayerEntity player, EntityMaid maid) {
        Item item = stack.getItem();
        if (item == InitItems.MAID_BACKPACK_SMALL.get()) {
            ItemsUtil.dropEntityItems(maid, maid.getMaidInv(), BackpackLevel.SMALL_CAPACITY);
            return;
        }
        if (item == InitItems.MAID_BACKPACK_MIDDLE.get()) {
            ItemsUtil.dropEntityItems(maid, maid.getMaidInv(), BackpackLevel.MIDDLE_CAPACITY);
            return;
        }
        if (item == InitItems.MAID_BACKPACK_BIG.get()) {
            return;
        }
        this.dropAllItems(maid);
    }

    @Override
    public void onSpawnTombstone(EntityMaid maid, EntityTombstone tombstone) {
    }

    @Override
    public INamedContainerProvider getGuiProvider(int entityId) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new StringTextComponent("Maid Big Container");
            }

            @Nullable
            @Override
            public Container createMenu(int index, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new BigBackpackContainer(index, playerInventory, entityId);
            }
        };
    }

    @Override
    public int getAvailableMaxContainerIndex() {
        return BackpackLevel.BIG_CAPACITY;
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public EntityModel<EntityMaid> getBackpackModel() {
        return new BigBackpackModel();
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getBackpackTexture() {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_big.png");
    }

    @OnlyIn(Dist.CLIENT)
    public void offsetBackpackItem(MatrixStack poseStack) {
        poseStack.translate(0, 0, -0.4);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Item getItem() {
        return InitItems.MAID_BACKPACK_BIG.get();
    }
}
