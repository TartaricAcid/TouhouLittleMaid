package com.github.tartaricacid.touhoulittlemaid.client.gui.entity;

import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.ModelDetailsButton;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class MaidModelDetailsGui extends AbstractModelDetailsGui<EntityMaid, MaidModelInfo> {
    private static final ItemStack MAIN_HAND_SWORD = Items.DIAMOND_SWORD.getDefaultInstance();
    private static final ItemStack OFF_HAND_SHIELD = Items.SHIELD.getDefaultInstance();
    private static final ItemStack ARMOR_ITEM = Items.GOLDEN_HELMET.getDefaultInstance();
    private MinecartEntity minecart;
    private volatile boolean isEnableWalk = false;

    public MaidModelDetailsGui(EntityMaid sourceEntity, MaidModelInfo modelInfo) {
        super(sourceEntity, InitEntities.MAID.get().create(sourceEntity.level), modelInfo);
        this.guiEntity.setModelId(modelInfo.getModelId().toString());
        if (Minecraft.getInstance().level != null) {
            this.minecart = EntityType.MINECART.create(Minecraft.getInstance().level);
        }
    }

    @Override
    protected void applyReturnButtonLogic() {
        Minecraft.getInstance().setScreen(new MaidModelGui(sourceEntity));
    }

    @Override
    protected void initSideButton() {
        ModelDetailsButton begButton = new ModelDetailsButton(2, 17, "gui.touhou_little_maid.skin_details.beg",
                (isStateTriggered) -> guiEntity.setBegging(isStateTriggered));
        ModelDetailsButton walkButton = new ModelDetailsButton(2, 17 + 13, "gui.touhou_little_maid.skin_details.walk",
                (isStateTriggered) -> isEnableWalk = isStateTriggered);
        ModelDetailsButton sitButton = new ModelDetailsButton(2, 17 + 13 * 2, "gui.touhou_little_maid.skin_details.sit",
                (isStateTriggered) -> guiEntity.setInSittingPose(isStateTriggered));
        ModelDetailsButton rideButton = new ModelDetailsButton(2, 17 + 13 * 3, "gui.touhou_little_maid.skin_details.ride",
                this::applyRideButtonLogic);
        ModelDetailsButton helmetButton = new ModelDetailsButton(2, 17 + 13 * 4, "gui.touhou_little_maid.skin_details.helmet",
                (isStateTriggered) -> applyEquipmentButtonLogic(EquipmentSlotType.HEAD, isStateTriggered));
        ModelDetailsButton chestPlateButton = new ModelDetailsButton(2, 17 + 13 * 5, "gui.touhou_little_maid.skin_details.chest_plate",
                (isStateTriggered) -> applyEquipmentButtonLogic(EquipmentSlotType.CHEST, isStateTriggered));
        ModelDetailsButton leggingsButton = new ModelDetailsButton(2, 17 + 13 * 6, "gui.touhou_little_maid.skin_details.leggings",
                (isStateTriggered) -> applyEquipmentButtonLogic(EquipmentSlotType.LEGS, isStateTriggered));
        ModelDetailsButton bootsButton = new ModelDetailsButton(2, 17 + 13 * 7, "gui.touhou_little_maid.skin_details.boots",
                (isStateTriggered) -> applyEquipmentButtonLogic(EquipmentSlotType.FEET, isStateTriggered));
        ModelDetailsButton mainHandButton = new ModelDetailsButton(2, 17 + 13 * 8, "gui.touhou_little_maid.skin_details.main_hand",
                (isStateTriggered) -> applyEquipmentButtonLogic(EquipmentSlotType.MAINHAND, isStateTriggered));
        ModelDetailsButton offHandButton = new ModelDetailsButton(2, 17 + 13 * 9, "gui.touhou_little_maid.skin_details.off_hand",
                (isStateTriggered) -> applyEquipmentButtonLogic(EquipmentSlotType.OFFHAND, isStateTriggered));
        this.addButton(begButton);
        this.addButton(walkButton);
        this.addButton(sitButton);
        this.addButton(rideButton);
        this.addButton(helmetButton);
        this.addButton(chestPlateButton);
        this.addButton(leggingsButton);
        this.addButton(bootsButton);
        this.addButton(mainHandButton);
        this.addButton(offHandButton);
    }

    @Override
    public void tick() {
        // Tick count increment for some animations
        guiEntity.tickCount++;
        // For entity walk
        guiEntity.animationSpeedOld = guiEntity.animationSpeed;
        // Set walk speed
        float speed = isEnableWalk ? 0.5f : 0;
        guiEntity.animationSpeed += (speed - guiEntity.animationSpeed) * 0.4f;
        guiEntity.animationPosition += guiEntity.animationSpeed;
    }

    @Override
    protected void renderExtraEntity(EntityRendererManager manager, MatrixStack matrix, IRenderTypeBuffer bufferIn) {

    }

    private void applyRideButtonLogic(boolean isStateTriggered) {
        if (isStateTriggered && minecart != null) {
            guiEntity.startRiding(minecart, true);
        } else {
            guiEntity.removeVehicle();
        }
    }

    private void applyEquipmentButtonLogic(EquipmentSlotType slot, boolean isStateTriggered) {
        if (isStateTriggered) {
            if (slot == EquipmentSlotType.MAINHAND) {
                guiEntity.setItemSlot(slot, MAIN_HAND_SWORD);
            } else if (slot == EquipmentSlotType.OFFHAND) {
                guiEntity.setItemSlot(slot, OFF_HAND_SHIELD);
            } else {
                guiEntity.setItemSlot(slot, ARMOR_ITEM);
            }
        } else {
            guiEntity.setItemSlot(slot, ItemStack.EMPTY);
        }
    }
}
