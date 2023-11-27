package com.github.tartaricacid.touhoulittlemaid.entity.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.client.model.CraftingTableBackpackModel;
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
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CraftingTableBackpack extends IMaidBackpack {
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "crafting_table");
    private static final ResourceLocation BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_crafting_table.png");

    @Override
    public void onPutOn(ItemStack stack, Player player, EntityMaid maid) {

    }

    @Override
    public void onTakeOff(ItemStack stack, @Nullable Player player, EntityMaid maid) {

    }

    @Override
    public List<Slot> getContainer(ItemStackHandler itemHandler) {
        return Collections.emptyList();
    }

    @Override
    public int getAvailableMaxContainerIndex() {
        return BackpackLevel.EMPTY_CAPACITY;
    }

    @Override
    public void drawBackpackScreen(GuiGraphics graphics, EntityMaid maid, int leftPos, int topPos) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKPACK);
        graphics.blit(BACKPACK, leftPos + 85, topPos + 36, 0, 0, 165, 122);
    }

    @Override
    public void offsetBackpackItem(PoseStack poseStack) {
    }

    @Nullable
    @Override
    public EntityModel<EntityMaid> getBackpackModel(EntityModelSet modelSet) {
        return new CraftingTableBackpackModel(modelSet.bakeLayer(CraftingTableBackpackModel.LAYER));
    }

    @Nullable
    @Override
    public ResourceLocation getBackpackTexture() {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/crafting_table_backpack.png");
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Item getItem() {
        return Items.CRAFTING_TABLE;
    }
}
