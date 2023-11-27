package com.github.tartaricacid.touhoulittlemaid.entity.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
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
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class EmptyBackpack extends IMaidBackpack {
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "empty");
    private static final ResourceLocation BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_backpack.png");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Item getItem() {
        return Items.AIR;
    }

    @Override
    public void onPutOn(ItemStack stack, Player player, EntityMaid maid) {
    }

    @Override
    public void onTakeOff(ItemStack stack, @Nullable Player player, EntityMaid maid) {
    }

    @Override
    public List<Slot> getContainer(ItemStackHandler itemStackHandler) {
        return Collections.emptyList();
    }

    @Override
    public int getAvailableMaxContainerIndex() {
        return BackpackLevel.EMPTY_CAPACITY;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawBackpackScreen(GuiGraphics graphics, EntityMaid maid, int leftPos, int topPos) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKPACK);
        graphics.blit(BACKPACK, leftPos + 85, topPos + 36, 0, 0, 165, 122);
        graphics.fill(leftPos + 142, topPos + 58, leftPos + 250, topPos + 76, 0xaa222222);
        graphics.blit(BACKPACK, leftPos + 190, topPos + 62, 165, 0, 11, 11);
        graphics.fill(leftPos + 142, topPos + 81, leftPos + 250, topPos + 117, 0xaa222222);
        graphics.blit(BACKPACK, leftPos + 190, topPos + 92, 165, 0, 11, 11);
        graphics.fill(leftPos + 142, topPos + 122, leftPos + 250, topPos + 158, 0xaa222222);
        graphics.blit(BACKPACK, leftPos + 190, topPos + 133, 165, 0, 11, 11);
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public EntityModel<EntityMaid> getBackpackModel(EntityModelSet modelSet) {
        return null;
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getBackpackTexture() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public void offsetBackpackItem(PoseStack poseStack) {
        poseStack.translate(0, 0.625, 0.2);
    }
}
