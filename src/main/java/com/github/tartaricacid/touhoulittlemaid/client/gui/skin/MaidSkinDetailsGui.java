package com.github.tartaricacid.touhoulittlemaid.client.gui.skin;

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

import static com.github.tartaricacid.touhoulittlemaid.client.gui.skin.MaidSkinDetailsGui.BUTTON.*;

/**
 * @author TartaricAcid
 * @date 2019/8/3 19:58
 **/
@SideOnly(Side.CLIENT)
public class MaidSkinDetailsGui extends AbstractSkinDetailsGui<EntityMaid> {
    /**
     * 相关物品实例，用于渲染实体持有效果，ARMOR_ITEM 仅为占位用物品
     */
    private static final ItemStack MAIN_HAND_SWORD = Items.DIAMOND_SWORD.getDefaultInstance();
    private static final ItemStack OFF_HAND_SHIELD = Items.SHIELD.getDefaultInstance();
    private static final ItemStack ARMOR_ITEM = Items.GOLDEN_APPLE.getDefaultInstance();

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

    public MaidSkinDetailsGui(EntityMaid sourceMaid, ResourceLocation modelId) {
        super(sourceMaid, new EntityMaid(sourceMaid.world), ClientProxy.ID_MODEL_INFO_MAP.get(modelId.toString()));
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

    private void applyBegButtonLogic() {
        if (guiEntity.isBegging()) {
            begButton.setStateTriggered(false);
            guiEntity.setBegging(false);
        } else {
            begButton.setStateTriggered(true);
            guiEntity.setBegging(true);
        }
    }

    private void applyWalkButtonLogic() {
        if (isEnableWalk) {
            isEnableWalk = false;
            walkButton.setStateTriggered(false);
        } else {
            applyConflictReset(WALK);
            isEnableWalk = true;
            walkButton.setStateTriggered(true);
        }
    }

    private void applySitButtonLogic() {
        if (guiEntity.isSitting()) {
            guiEntity.setSitting(false);
            sitButton.setStateTriggered(false);
        } else {
            // 不兼容的动作列表需要归位
            applyConflictReset(SIT);
            guiEntity.setSitting(true);
            sitButton.setStateTriggered(true);
        }
    }

    private void applyRideButtonLogic() {
        if (guiEntity.isRiding()) {
            guiEntity.dismountRidingEntity();
            rideButton.setStateTriggered(false);
        } else {
            // 不兼容的动作列表需要归位
            applyConflictReset(RIDE);
            guiEntity.startRiding(marisaBroom);
            rideButton.setStateTriggered(true);
        }
    }

    private void applyRideBroomButtonLogic() {
        if (guiEntity.isDebugBroomShow) {
            guiEntity.isDebugBroomShow = false;
            rideBroomButton.setStateTriggered(false);
        } else {
            // 不兼容的动作列表需要归位
            applyConflictReset(RIDE_BROOM);
            guiEntity.isDebugBroomShow = true;
            rideBroomButton.setStateTriggered(true);
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
        OFF_HAND;

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
