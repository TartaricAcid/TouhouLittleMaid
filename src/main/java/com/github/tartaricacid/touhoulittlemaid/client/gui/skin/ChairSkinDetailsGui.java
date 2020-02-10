package com.github.tartaricacid.touhoulittlemaid.client.gui.skin;

import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomModelLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ChairModelItem;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.ApplyChairSkinDataMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/8/11 14:11
 **/
@SideOnly(Side.CLIENT)
public class ChairSkinDetailsGui extends AbstractSkinDetailsGui<EntityChair, ChairModelItem> {
    private GuiButtonToggle characterButton;
    private ResourceLocation modelId;
    private GuiButton setGravityButton;

    public ChairSkinDetailsGui(EntityChair sourceEntity, ResourceLocation modelId) {
        super(sourceEntity, new EntityChair(sourceEntity.world), CustomModelLoader.CHAIR_MODEL.getInfo(modelId.toString()).orElseThrow(NullPointerException::new));
        this.modelId = modelId;
        guiEntity.setModelId(modelId.toString());
        guiEntity.setMountedHeight(modelItem.getMountedYOffset());
        guiEntity.setTameableCanRide(modelItem.isTameableCanRide());
        guiEntity.setNoGravity(modelItem.isNoGravity());
        guiEntity.isDebugFloorOpen = true;
    }

    @Override
    void initSideButton() {
        addButton(new GuiButton(BUTTON.APPLY_DATA.ordinal(), 2, 18, 128, 20, I18n.format("gui.touhou_little_maid.skin_details.apply_data")));
        addButton(new GuiButton(BUTTON.MIN_HEIGHT.ordinal(), 58, 40, 12, 12, "-"));
        addButton(new GuiButton(BUTTON.ADD_HEIGHT.ordinal(), 118, 40, 12, 12, "+"));

        characterButton = new GuiButtonToggle(BUTTON.SHOW_CHARACTER.ordinal(), 2, 54, 128, 12, guiEntity.isDebugCharacterOpen);
        characterButton.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
        addButton(characterButton);

        setGravityButton = new GuiButton(BUTTON.SET_GRAVITY.ordinal(), 2, 68, 128, 20, getGravityButtonText(guiEntity.hasNoGravity()));
        addButton(setGravityButton);
    }

    @Override
    void actionSideButtonPerformed(GuiButton button) {
        switch (BUTTON.getButtonFromOrdinal(button.id)) {
            case APPLY_DATA:
                CommonProxy.INSTANCE.sendToServer(new ApplyChairSkinDataMessage(sourceEntity.getUniqueID(),
                        modelId, guiEntity.getMountedHeight(), guiEntity.isTameableCanRide(), guiEntity.hasNoGravity()));
                return;
            case MIN_HEIGHT:
                // Shift 10 倍率减少
                if (isShiftKeyDown()) {
                    guiEntity.setMountedHeight(guiEntity.getMountedHeight() - 0.5f);
                } else {
                    guiEntity.setMountedHeight(guiEntity.getMountedHeight() - 0.0625f);
                }
                return;
            case ADD_HEIGHT:
                if (isShiftKeyDown()) {
                    guiEntity.setMountedHeight(guiEntity.getMountedHeight() + 0.5f);
                } else {
                    guiEntity.setMountedHeight(guiEntity.getMountedHeight() + 0.0625f);
                }
                return;
            case SHOW_CHARACTER:
                applyShowCharacterLogic();
                return;
            case SET_GRAVITY:
                applySetGravityLogic();
                return;
            default:
        }
    }

    @Override
    void applyFloorButtonLogic() {
        guiEntity.isDebugFloorOpen = !guiEntity.isDebugFloorOpen;
    }

    @Override
    void applyReturnButtonLogic() {
        mc.addScheduledTask(() -> mc.displayGuiScreen(new ChairSkinGui(sourceEntity)));
    }

    @Override
    void drawSideButtonText() {
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.mounted_height"), 5, 43, 0xcacad4);
        String data = String.format("%.2f", guiEntity.getMountedHeight() / 0.0625f + 3);
        fontRenderer.drawString(data, (188 - fontRenderer.getStringWidth(data)) / 2, 43, 0xcacad4);
        fontRenderer.drawString(I18n.format("gui.touhou_little_maid.skin_details.show_character"), 16, 57, 0xcacad4);
    }

    @Override
    void drawSideButtonTooltips(int mouseX, int mouseY) {
        boolean isInWidthRange = (58 < mouseX && mouseX < 70) || (118 < mouseX && mouseX < 138);
        boolean isInHeightRange = 40 < mouseY && mouseY < 60;
        if (isInWidthRange && isInHeightRange) {
            drawHoveringText(I18n.format("gui.touhou_little_maid.skin_details.mounted_height.button.tooltips"), mouseX, mouseY);
        }
    }

    private void applyShowCharacterLogic() {
        if (guiEntity.isDebugCharacterOpen) {
            guiEntity.isDebugCharacterOpen = false;
            characterButton.setStateTriggered(false);
        } else {
            guiEntity.isDebugCharacterOpen = true;
            characterButton.setStateTriggered(true);
        }
    }

    private void applySetGravityLogic() {
        guiEntity.setNoGravity(!guiEntity.hasNoGravity());
        setGravityButton.displayString = getGravityButtonText(guiEntity.hasNoGravity());
    }

    private String getGravityButtonText(boolean isNoGravity) {
        return I18n.format(String.format("gui.touhou_little_maid.skin_details.set_gravity.button.%s", isNoGravity));
    }

    enum BUTTON {
        // 增加，减少高度
        MIN_HEIGHT,
        ADD_HEIGHT,
        SHOW_CHARACTER,
        APPLY_DATA,
        SET_GRAVITY;

        /**
         * 通过序号获取对应的 BUTTON 枚举类型
         */
        static BUTTON getButtonFromOrdinal(int ordinal) {
            for (BUTTON button : BUTTON.values()) {
                if (button.ordinal() == ordinal) {
                    return button;
                }
            }
            return ADD_HEIGHT;
        }
    }
}
