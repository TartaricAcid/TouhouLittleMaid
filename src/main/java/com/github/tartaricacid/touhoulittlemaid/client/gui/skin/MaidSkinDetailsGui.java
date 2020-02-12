package com.github.tartaricacid.touhoulittlemaid.client.gui.skin;

import com.github.tartaricacid.touhoulittlemaid.client.animation.CustomJsAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomModelLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelItem;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Random;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.skin.MaidSkinDetailsGui.BUTTON.*;

/**
 * @author TartaricAcid
 * @date 2019/8/3 19:58
 **/
@SideOnly(Side.CLIENT)
public class MaidSkinDetailsGui extends AbstractSkinDetailsGui<EntityMaid, MaidModelItem> {
    /**
     * 相关物品实例，用于渲染实体持有效果，ARMOR_ITEM 仅为占位用物品
     */
    private static final ItemStack MAIN_HAND_SWORD = Items.DIAMOND_SWORD.getDefaultInstance();
    private static final ItemStack OFF_HAND_SHIELD = Items.SHIELD.getDefaultInstance();
    private static final ItemStack ARMOR_ITEM = Items.GOLDEN_HELMET.getDefaultInstance();
    private static final Random RANDOM = new Random();

    /**
     * 必须的部分变量
     */
    private EntityMarisaBroom marisaBroom;

    /**
     * 所有的 GuiButtonToggle 按钮
     */
    private GuiButtonToggle begButton;
    private GuiButtonToggle walkButton;
    private GuiButtonToggle sitButton;
    private GuiButtonToggle rideButton;
    private GuiButtonToggle rideBroomButton;
    private GuiButtonToggle helmetButton;
    private GuiButtonToggle chestPlateButton;
    private GuiButtonToggle leggingsButton;
    private GuiButtonToggle bootsButton;
    private GuiButtonToggle mainHandButton;
    private GuiButtonToggle offHandButton;
    private GuiButtonToggle hataSasimonoButton;
    private GuiButtonToggle arrowButton;

    MaidSkinDetailsGui(EntityMaid sourceMaid, ResourceLocation modelId) {
        super(sourceMaid, new EntityMaid(sourceMaid.world), CustomModelLoader.MAID_MODEL.getInfo(modelId.toString()).orElseThrow(NullPointerException::new));
        this.marisaBroom = new EntityMarisaBroom(sourceMaid.world);
        guiEntity.setModelId(modelId.toString());
        guiEntity.isDebugFloorOpen = true;
    }

    @Override
    void initSideButton() {
        // 所有的侧边栏按钮
        begButton = new GuiButtonToggle(BEG.ordinal(), 2, 17, 128, 12, guiEntity.isBegging());
        begButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        walkButton = new GuiButtonToggle(WALK.ordinal(), 2, 17 + 13, 128, 12, isEnableWalk);
        walkButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        sitButton = new GuiButtonToggle(SIT.ordinal(), 2, 17 + 13 * 2, 128, 12, guiEntity.isSitting());
        sitButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        rideButton = new GuiButtonToggle(RIDE.ordinal(), 2, 17 + 13 * 3, 128, 12, guiEntity.isRiding());
        rideButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        rideBroomButton = new GuiButtonToggle(RIDE_BROOM.ordinal(), 2, 17 + 13 * 4, 128, 12, guiEntity.isDebugBroomShow);
        rideBroomButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        helmetButton = new GuiButtonToggle(HELMET.ordinal(), 2, 17 + 13 * 5, 128, 12, !guiEntity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty());
        helmetButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        chestPlateButton = new GuiButtonToggle(CHEST_PLATE.ordinal(), 2, 17 + 13 * 6, 128, 12, !guiEntity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty());
        chestPlateButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        leggingsButton = new GuiButtonToggle(LEGGINGS.ordinal(), 2, 17 + 13 * 7, 128, 12, !guiEntity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty());
        leggingsButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        bootsButton = new GuiButtonToggle(BOOTS.ordinal(), 2, 17 + 13 * 8, 128, 12, !guiEntity.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty());
        bootsButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        mainHandButton = new GuiButtonToggle(MAIN_HAND.ordinal(), 2, 17 + 13 * 9, 128, 12, !guiEntity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty());
        mainHandButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        offHandButton = new GuiButtonToggle(OFF_HAND.ordinal(), 2, 17 + 13 * 10, 128, 12, !guiEntity.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty());
        offHandButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        hataSasimonoButton = new GuiButtonToggle(HATA_SASIMONO.ordinal(), 2, 17 + 13 * 11, 128, 12, guiEntity.isShowSasimono());
        hataSasimonoButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        arrowButton = new GuiButtonToggle(ARROW.ordinal(), 2, 17 + 13 * 12, 128, 12, guiEntity.getArrowCountInEntity() > 0);
        arrowButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        addButton(begButton);
        addButton(walkButton);
        addButton(sitButton);
        addButton(rideButton);
        addButton(rideBroomButton);
        addButton(helmetButton);
        addButton(chestPlateButton);
        addButton(leggingsButton);
        addButton(bootsButton);
        addButton(mainHandButton);
        addButton(offHandButton);
        addButton(hataSasimonoButton);
        addButton(arrowButton);
    }

    @Override
    void drawSideButtonText() {
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.beg"), 16, 19, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.walk"), 16, 19 + 13, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.sit"), 16, 19 + 13 * 2, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.ride"), 16, 19 + 13 * 3, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.ride_broom"), 16, 19 + 13 * 4, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.helmet"), 16, 19 + 13 * 5, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.chest_plate"), 16, 19 + 13 * 6, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.leggings"), 16, 19 + 13 * 7, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.boots"), 16, 19 + 13 * 8, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.main_hand"), 16, 19 + 13 * 9, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.off_hand"), 16, 19 + 13 * 10, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.hata_sasimono"), 16, 19 + 13 * 11, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.arrow"), 16, 19 + 13 * 12, 0xcacad4);
    }

    @Override
    void actionSideButtonPerformed(GuiButton button) {
        switch (BUTTON.getButtonFromOrdinal(button.id)) {
            case BEG:
                applyBegButtonLogic();
                return;
            case WALK:
                applyWalkButtonLogic();
                return;
            case SIT:
                applySitButtonLogic();
                return;
            case RIDE:
                applyRideButtonLogic();
                return;
            case RIDE_BROOM:
                applyRideBroomButtonLogic();
                return;
            case HELMET:
                applyEquipmentButtonLogic(EntityEquipmentSlot.HEAD);
                return;
            case CHEST_PLATE:
                applyEquipmentButtonLogic(EntityEquipmentSlot.CHEST);
                return;
            case LEGGINGS:
                applyEquipmentButtonLogic(EntityEquipmentSlot.LEGS);
                return;
            case BOOTS:
                applyEquipmentButtonLogic(EntityEquipmentSlot.FEET);
                return;
            case MAIN_HAND:
                applyEquipmentButtonLogic(EntityEquipmentSlot.MAINHAND);
                return;
            case OFF_HAND:
                applyEquipmentButtonLogic(EntityEquipmentSlot.OFFHAND);
                return;
            case HATA_SASIMONO:
                applyHataSasimonoButtonLogic();
                return;
            case ARROW:
                applyArrowButtonLogic();
                return;
            default:
                super.applyCloseButtonLogic();
        }
    }

    @Override
    public void applyFloorButtonLogic() {
        guiEntity.isDebugFloorOpen = !guiEntity.isDebugFloorOpen;
    }

    @Override
    public void applyReturnButtonLogic() {
        mc.addScheduledTask(() -> mc.displayGuiScreen(new MaidSkinGui(sourceEntity)));
    }

    @Override
    void drawSideButtonTooltips(int mouseX, int mouseY) {
    }

    @Override
    void loadAnimation(Object scriptObject) {
        CustomModelLoader.MAID_MODEL.putAnimation(modelItem.getModelId().toString(), scriptObject);
    }

    @Override
    void reloadModel() {
        CustomModelLoader.MAID_MODEL.putModel(modelItem.getModelId().toString(), CustomModelLoader.loadModel(modelItem.getModel()));
    }

    @Override
    void resetAnimationAndModel() {
        Object animation = CustomJsAnimationManger.getCustomAnimation(modelItem.getAnimation());
        if (animation != null) {
            CustomModelLoader.MAID_MODEL.putAnimation(modelItem.getModelId().toString(), animation);
        }
        reloadModel();
    }

    @Override
    void putDebugAnimation(File debugAnimationFile) {
        CustomModelLoader.MAID_MODEL.putDebugAnimation(modelItem.getModelId().toString(), debugAnimationFile.getAbsolutePath());
    }

    @Nullable
    @Override
    String getDebugAnimationFile() {
        return CustomModelLoader.MAID_MODEL.getDebugAnimationFilePath(modelItem.getModelId().toString());
    }

    @Override
    void removeDebugAnimationFile() {
        CustomModelLoader.MAID_MODEL.removeDebugAnimation(modelItem.getModelId().toString());
    }

    private void applyBegButtonLogic() {
        begButton.setStateTriggered(!begButton.isStateTriggered());
        guiEntity.setBegging(!guiEntity.isBegging());
    }

    private void applyWalkButtonLogic() {
        walkButton.setStateTriggered(!walkButton.isStateTriggered());
        isEnableWalk = !isEnableWalk;
        // 如果此时启用了行走，需要处理那些冲突的行为
        if (walkButton.isStateTriggered()) {
            applyConflictReset(WALK);
        }
    }

    private void applySitButtonLogic() {
        sitButton.setStateTriggered(!sitButton.isStateTriggered());
        guiEntity.setSitting(!guiEntity.isSitting());
        if (sitButton.isStateTriggered()) {
            // 不兼容的动作列表需要归位
            applyConflictReset(SIT);
        }
    }

    private void applyRideButtonLogic() {
        rideButton.setStateTriggered(!rideButton.isStateTriggered());
        if (rideButton.isStateTriggered()) {
            // 不兼容的动作列表需要归位
            applyConflictReset(RIDE);
            guiEntity.startRiding(marisaBroom);
        } else {
            guiEntity.dismountRidingEntity();
        }
    }

    private void applyRideBroomButtonLogic() {
        rideBroomButton.setStateTriggered(!rideBroomButton.isStateTriggered());
        guiEntity.isDebugBroomShow = !guiEntity.isDebugBroomShow;
        if (rideBroomButton.isStateTriggered()) {
            // 不兼容的动作列表需要归位
            applyConflictReset(RIDE_BROOM);
        }
    }

    private void applyEquipmentButtonLogic(EntityEquipmentSlot slot) {
        if (!guiEntity.getItemStackFromSlot(slot).isEmpty()) {
            guiEntity.setItemStackToSlot(slot, ItemStack.EMPTY);
            setEquipmentStateTriggered(slot, false);
        } else {
            if (slot == EntityEquipmentSlot.MAINHAND) {
                guiEntity.setItemStackToSlot(slot, MAIN_HAND_SWORD);
            } else if (slot == EntityEquipmentSlot.OFFHAND) {
                guiEntity.setItemStackToSlot(slot, OFF_HAND_SHIELD);
            } else {
                guiEntity.setItemStackToSlot(slot, ARMOR_ITEM);
            }
            setEquipmentStateTriggered(slot, true);
        }
    }

    private void applyHataSasimonoButtonLogic() {
        hataSasimonoButton.setStateTriggered(!hataSasimonoButton.isStateTriggered());
        if (hataSasimonoButton.isStateTriggered()) {
            Object[] key = ClientProxy.HATA_NAME_MAP.keySet().toArray();
            guiEntity.setSasimonoCRC32((Long) key[RANDOM.nextInt(key.length)]);
            guiEntity.setShowSasimono(true);
        } else {
            guiEntity.setShowSasimono(false);
        }
    }

    private void applyArrowButtonLogic() {
        arrowButton.setStateTriggered(!arrowButton.isStateTriggered());
        if (arrowButton.isStateTriggered()) {
            guiEntity.setArrowCountInEntity(9);
        } else {
            guiEntity.setArrowCountInEntity(0);
        }
    }

    private void setEquipmentStateTriggered(EntityEquipmentSlot slot, boolean state) {
        switch (slot) {
            case HEAD:
                helmetButton.setStateTriggered(state);
                return;
            case CHEST:
                chestPlateButton.setStateTriggered(state);
                return;
            case LEGS:
                leggingsButton.setStateTriggered(state);
                return;
            case FEET:
                bootsButton.setStateTriggered(state);
                return;
            case MAINHAND:
                mainHandButton.setStateTriggered(state);
                return;
            case OFFHAND:
                offHandButton.setStateTriggered(state);
                return;
            default:
        }
    }

    /**
     * 用于处理一些互相冲突的按钮，会将冲突的按键进行重置
     *
     * @param button 你当前按下的按键
     */
    private void applyConflictReset(BUTTON button) {
        if (button != WALK) {
            isEnableWalk = false;
            walkButton.setStateTriggered(false);
        }
        if (button != SIT) {
            guiEntity.setSitting(false);
            sitButton.setStateTriggered(false);
        }
        if (button != RIDE) {
            guiEntity.dismountRidingEntity();
            rideButton.setStateTriggered(false);
        }
        if (button != RIDE_BROOM) {
            guiEntity.isDebugBroomShow = false;
            rideBroomButton.setStateTriggered(false);
        }
    }

    @Override
    public void onGuiClosed() {
        guiEntity.dismountRidingEntity();
        super.onGuiClosed();
    }

    enum BUTTON {
        // 祈求动画按钮
        BEG,
        // 行走
        WALK,
        // 待命
        SIT,
        // 普通骑乘
        RIDE,
        // 扫帚骑乘
        RIDE_BROOM,
        // 头盔
        HELMET,
        // 胸甲
        CHEST_PLATE,
        // 护腿
        LEGGINGS,
        // 靴子
        BOOTS,
        // 主手持有物品
        MAIN_HAND,
        // 副手持有物品
        OFF_HAND,
        // 旗指物
        HATA_SASIMONO,
        // 插得箭
        ARROW;

        /**
         * 通过序号获取对应的 BUTTON 枚举类型
         */
        static BUTTON getButtonFromOrdinal(int ordinal) {
            for (BUTTON button : BUTTON.values()) {
                if (button.ordinal() == ordinal) {
                    return button;
                }
            }
            return BEG;
        }
    }
}
