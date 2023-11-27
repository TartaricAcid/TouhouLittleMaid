package com.github.tartaricacid.touhoulittlemaid.entity.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackBigModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class BigBackpack extends IMaidBackpack {
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "big_backpack");
    private static final ResourceLocation BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_backpack.png");

    @Override
    public void onPutOn(ItemStack stack, Player player, EntityMaid maid) {
    }

    @Override
    public void onTakeOff(ItemStack stack, @Nullable Player player, EntityMaid maid) {
        Item item = stack.getItem();
        if (item instanceof ItemMaidBackpack) {
            if (item == InitItems.MAID_BACKPACK_SMALL.get()) {
                ItemsUtil.dropEntityItems(maid, maid.getMaidInv(), BackpackLevel.SMALL_CAPACITY);
            }
            if (item == InitItems.MAID_BACKPACK_MIDDLE.get()) {
                ItemsUtil.dropEntityItems(maid, maid.getMaidInv(), BackpackLevel.MIDDLE_CAPACITY);
            }
        } else {
            this.dropAllItems(maid);
        }
    }

    @Override
    public List<Slot> getContainer(ItemStackHandler itemHandler) {
        List<Slot> container = Lists.newLinkedList();
        for (int i = 0; i < 6; i++) {
            container.add(new SlotItemHandler(itemHandler, 6 + i, 143 + 18 * i, 59));
        }
        for (int i = 0; i < 6; i++) {
            container.add(new SlotItemHandler(itemHandler, 12 + i, 143 + 18 * i, 82));
        }
        for (int i = 0; i < 6; i++) {
            container.add(new SlotItemHandler(itemHandler, 18 + i, 143 + 18 * i, 100));
        }
        for (int i = 0; i < 6; i++) {
            container.add(new SlotItemHandler(itemHandler, 24 + i, 143 + 18 * i, 123));
        }
        for (int i = 0; i < 6; i++) {
            container.add(new SlotItemHandler(itemHandler, 30 + i, 143 + 18 * i, 141));
        }
        return container;
    }

    @Override
    public int getAvailableMaxContainerIndex() {
        return BackpackLevel.BIG_CAPACITY;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawBackpackScreen(GuiGraphics graphics, EntityMaid maid, int leftPos, int topPos) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKPACK);
        graphics.blit(BACKPACK, leftPos + 85, topPos + 36, 0, 0, 165, 122);
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public EntityModel<EntityMaid> getBackpackModel(EntityModelSet modelSet) {
        return new MaidBackpackBigModel(modelSet.bakeLayer(MaidBackpackBigModel.LAYER));
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getBackpackTexture() {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_big.png");
    }

    @OnlyIn(Dist.CLIENT)
    public void offsetBackpackItem(PoseStack poseStack) {
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
