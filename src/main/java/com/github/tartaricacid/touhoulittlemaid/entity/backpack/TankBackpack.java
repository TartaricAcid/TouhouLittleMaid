package com.github.tartaricacid.touhoulittlemaid.entity.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IBackpackData;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.client.model.backpack.MiddleBackpackModel;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.TankBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.TankBackpackContainer;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
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
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class TankBackpack extends IMaidBackpack {
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "tank");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Item getItem() {
        return Items.BUCKET;
    }

    @Override
    public void onPutOn(ItemStack stack, Player player, EntityMaid maid) {
    }

    @Override
    public void onTakeOff(ItemStack stack, Player player, EntityMaid maid) {
    }

    @Override
    public void onSpawnTombstone(EntityMaid maid, EntityTombstone tombstone) {
    }

    @Nullable
    @Override
    public IBackpackData getBackpackData(EntityMaid maid) {
        return new TankBackpackData();
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
        return new MiddleBackpackModel(modelSet.bakeLayer(MiddleBackpackModel.LAYER));
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getBackpackTexture() {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_middle.png");
    }

    @OnlyIn(Dist.CLIENT)
    public void offsetBackpackItem(PoseStack poseStack) {
        poseStack.mulPose(Axis.XP.rotationDegrees(-7.5F));
        poseStack.translate(0, 0.625, -0.25);
    }
}
