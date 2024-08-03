package com.github.tartaricacid.touhoulittlemaid.entity.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IBackpackData;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.client.model.backpack.TankBackpackModel;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.TankBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.TankBackpackContainer;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.github.tartaricacid.touhoulittlemaid.item.ItemTankBackpack;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class TankBackpack extends IMaidBackpack {
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "tank");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Item getItem() {
        return InitItems.TANK_BACKPACK.get();
    }

    @Override
    public void onPutOn(ItemStack stack, Player player, EntityMaid maid) {
        IBackpackData backpackData = maid.getBackpackData();
        if (backpackData instanceof TankBackpackData tankBackpackData) {
            ItemTankBackpack.setTankBackpack(maid, tankBackpackData, stack);
        }
    }

    @Override
    public void onTakeOff(ItemStack stack, Player player, EntityMaid maid) {
        IBackpackData backpackData = maid.getBackpackData();
        if (backpackData instanceof TankBackpackData tankBackpackData) {
            InvWrapper inv = new InvWrapper(tankBackpackData);
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
    public ItemStack getTakeOffItemStack(ItemStack stack, @Nullable Player player, EntityMaid maid) {
        IBackpackData backpackData = maid.getBackpackData();
        if (backpackData instanceof TankBackpackData tankBackpackData) {
            return ItemTankBackpack.getTankBackpack(tankBackpackData);
        }
        return super.getTakeOffItemStack(stack, player, maid);
    }

    @Override
    public void onSpawnTombstone(EntityMaid maid, EntityTombstone tombstone) {
        IBackpackData backpackData = maid.getBackpackData();
        if (backpackData instanceof TankBackpackData tankBackpackData) {
            InvWrapper inv = new InvWrapper(tankBackpackData);
            for (int i = 0; i < inv.getSlots(); i++) {
                int size = inv.getSlotLimit(i);
                tombstone.insertItem(inv.extractItem(i, size, false));
            }
        }
    }

    @Override
    public boolean hasBackpackData() {
        return true;
    }

    @Nullable
    @Override
    public IBackpackData getBackpackData(EntityMaid maid) {
        return new TankBackpackData(maid);
    }

    @Override
    public MenuProvider getGuiProvider(int entityId) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Maid Tank Container");
            }

            @Override
            public AbstractMaidContainer createMenu(int index, Inventory playerInventory, Player player) {
                return new TankBackpackContainer(index, playerInventory, entityId);
            }
        };
    }

    @Override
    public int getAvailableMaxContainerIndex() {
        return BackpackLevel.TANK_CAPACITY;
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public EntityModel<EntityMaid> getBackpackModel(EntityModelSet modelSet) {
        return new TankBackpackModel(modelSet.bakeLayer(TankBackpackModel.LAYER));
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getBackpackTexture() {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/tank_backpack.png");
    }

    @OnlyIn(Dist.CLIENT)
    public void offsetBackpackItem(PoseStack poseStack) {
        poseStack.mulPose(Axis.XP.rotationDegrees(-7.5F));
        poseStack.translate(0, 0.625, -0.25);
    }
}
