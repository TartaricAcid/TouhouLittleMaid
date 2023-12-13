package com.github.tartaricacid.touhoulittlemaid.api.backpack;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;


public abstract class IMaidBackpack {
    public abstract ResourceLocation getId();

    public abstract Item getItem();

    public abstract void onPutOn(ItemStack stack, PlayerEntity player, EntityMaid maid);

    public ItemStack getTakeOffItemStack(ItemStack stack, @Nullable PlayerEntity player, EntityMaid maid) {
        return this.getItem().getDefaultInstance();
    }

    public abstract void onTakeOff(ItemStack stack, PlayerEntity player, EntityMaid maid);

    public abstract void onSpawnTombstone(EntityMaid maid, EntityTombstone tombstone);

    public abstract INamedContainerProvider getGuiProvider(int entityId);

    public boolean hasBackpackData() {
        return false;
    }

    @Nullable
    public IBackpackData getBackpackData(EntityMaid maid) {
        return null;
    }

    public abstract int getAvailableMaxContainerIndex();

    @OnlyIn(Dist.CLIENT)
    public abstract void offsetBackpackItem(MatrixStack poseStack);

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public abstract EntityModel<EntityMaid> getBackpackModel();

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public abstract ResourceLocation getBackpackTexture();

    protected final void dropAllItems(EntityMaid maid) {
        ItemsUtil.dropEntityItems(maid, maid.getMaidInv(), BackpackLevel.EMPTY_CAPACITY);
    }
}
