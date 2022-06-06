package com.github.tartaricacid.touhoulittlemaid.client.gui.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.DirectButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.ImageButtonWithId;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SaveSwitcherDataMessage;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityModelSwitcher;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.UUID;

public class ModelSwitcherGui extends Screen {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/model_switcher.png");
    private static final ResourceLocation DEFAULT_MODEL_ID = new ResourceLocation("touhou_little_maid:hakurei_reimu");
    private final List<TileEntityModelSwitcher.ModeInfo> infoList;
    private final BlockPos pos;
    private final int maxRow = 6;
    private final UUID bindUuid;
    protected int imageWidth = 256;
    protected int imageHeight = 166;
    protected int leftPos;
    protected int topPos;
    private EntityMaid maid = null;
    private EditBox description;
    private int selectedIndex = -1;
    private int page;

    public ModelSwitcherGui(TileEntityModelSwitcher switcher) {
        super(new TextComponent("Model Switcher GUI"));
        this.infoList = switcher.getInfoList();
        this.pos = switcher.getBlockPos();
        this.bindUuid = switcher.getUuid();
        if (Minecraft.getInstance().level != null) {
            this.maid = new EntityMaid(Minecraft.getInstance().level);
        }
    }

    @Override
    protected void init() {
        this.getMinecraft().keyboardHandler.setSendRepeatsToGui(true);
        this.clearWidgets();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.selectedIndex = selectedIndex < infoList.size() ? selectedIndex : -1;
        this.page = page <= (infoList.size() - 1) / maxRow ? page : 0;

        this.addListChangeButton();
        this.addPageButton();
        this.addListButton();
        if (selectedIndex >= 0) {
            this.addEditButton();
        } else {
            this.description = null;
        }
    }

    private void addEditButton() {
        TileEntityModelSwitcher.ModeInfo info = this.infoList.get(selectedIndex);
        maid.setModelId(info.getModelId().toString());

        this.addRenderableWidget(new Button(leftPos + 55, topPos + 15, 76, 20, new TranslatableComponent("gui.touhou_little_maid.button.skin"), b -> {
            ModelSwitcherModelGui modelGui = new ModelSwitcherModelGui(this.maid, info, this);
            getMinecraft().setScreen(modelGui);
        }));
        this.addRenderableWidget(new DirectButton(leftPos + 55, topPos + 38, 76, 20, info.getDirection(),
                b -> info.setDirection(((DirectButton) b).getDirection())));
        this.addRenderableWidget(new Button(leftPos + 12, topPos + 135, 121, 20, new TranslatableComponent("selectWorld.edit.save"),
                b -> NetworkHandler.CHANNEL.sendToServer(new SaveSwitcherDataMessage(pos, this.infoList))));

        this.description = new EditBox(getMinecraft().font, leftPos + 12, topPos + 65, 119, 20,
                new TranslatableComponent("gui.touhou_little_maid.name_tag.edit_box"));
        this.description.setValue(info.getText());
        this.addWidget(this.description);
        this.setInitialFocus(this.description);
    }

    private void addListButton() {
        int startOffsetY = topPos + 24;
        for (int i = page * maxRow; i < Math.min(infoList.size(), (page + 1) * maxRow); i++) {
            ImageButtonWithId button;
            if (i != selectedIndex) {
                button = new ImageButtonWithId(i, leftPos + 141, startOffsetY, 108, 19, 0, 166, 19, BG, b -> {
                    selectedIndex = ((ImageButtonWithId) b).getIndex();
                    this.init();
                });
            } else {
                button = new ImageButtonWithId(i, leftPos + 141, startOffsetY, 108, 19, 108, 166, 0, BG, b -> {
                    selectedIndex = -1;
                    this.init();
                });
            }
            this.addRenderableWidget(button);
            startOffsetY += 19;
        }
    }

    private void addPageButton() {
        this.addRenderableWidget(new ImageButton(leftPos + 141, topPos + 7, 13, 16, 0, 204, 16, BG, b -> {
            if (page > 0) {
                page = page - 1;
                this.init();
            }
        }));
        this.addRenderableWidget(new ImageButton(leftPos + 236, topPos + 7, 13, 16, 13, 204, 16, BG, b -> {
            if ((page + 1) <= (infoList.size() - 1) / maxRow) {
                page = page + 1;
                this.init();
            }
        }));
    }

    private void addListChangeButton() {
        this.addRenderableWidget(new Button(leftPos + 141, topPos + 139, 53, 20, new TranslatableComponent("gui.touhou_little_maid.model_switcher.list.add"), b -> {
            this.infoList.add(new TileEntityModelSwitcher.ModeInfo(DEFAULT_MODEL_ID, "", Direction.NORTH));
            this.init();
        }));
        this.addRenderableWidget(new Button(leftPos + 196, topPos + 139, 53, 20, new TranslatableComponent("selectWorld.deleteButton"), b -> {
            if (-1 < selectedIndex && selectedIndex < this.infoList.size()) {
                this.infoList.remove(selectedIndex);
                selectedIndex = -1;
                this.init();
            }
        }));
    }

    @Override
    public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
        String value = "";
        if (this.description != null) {
            value = this.description.getValue();
        }
        super.resize(pMinecraft, width, height);
        if (this.description != null) {
            this.description.setValue(value);
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.maid == null) {
            return;
        }
        this.renderBackground(pPoseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BG);
        blit(pPoseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        if (bindUuid != null) {
            drawCenteredString(pPoseStack, font, bindUuid.toString(), leftPos + 128, topPos - 10, 0xffffff);
        } else {
            drawCenteredString(pPoseStack, font, new TranslatableComponent("gui.touhou_little_maid.model_switcher.uuid.empty"), leftPos + 128, topPos - 10, 0xffffff);
        }
        drawCenteredString(pPoseStack, font, String.format("%d/%d", page + 1, (infoList.size() - 1) / maxRow + 1), leftPos + 195, topPos + 12, 0xffffff);
        if (this.description != null) {
            InventoryScreen.renderEntityInInventory(leftPos + 30, topPos + 60, 25, leftPos - 150, topPos - 50, maid);
            this.description.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderListButtonName(pPoseStack);
    }

    private void renderListButtonName(PoseStack pPoseStack) {
        int startOffsetY = topPos + 29;
        for (int i = page * maxRow; i < Math.min(infoList.size(), (page + 1) * maxRow); i++) {
            String modelId = infoList.get(i).getModelId().toString();
            if (CustomPackLoader.MAID_MODELS.getInfo(modelId).isPresent()) {
                MaidModelInfo info = CustomPackLoader.MAID_MODELS.getInfo(modelId).get();
                TranslatableComponent component = new TranslatableComponent(ParseI18n.getI18nKey(info.getName()));
                drawCenteredString(pPoseStack, font, component, leftPos + 193, startOffsetY, 0xffffff);
            }
            startOffsetY += 19;
        }
    }

    @Override
    public void tick() {
        if (this.description != null) {
            this.description.tick();
            if (0 <= selectedIndex && selectedIndex < infoList.size()) {
                infoList.get(selectedIndex).setText(description.getValue());
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.description != null && this.description.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.description);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void insertText(String text, boolean overwrite) {
        if (this.description != null) {
            if (overwrite) {
                this.description.setValue(text);
            } else {
                this.description.insertText(text);
            }
        }
    }

    @Override
    public void onClose() {
        NetworkHandler.CHANNEL.sendToServer(new SaveSwitcherDataMessage(pos, this.infoList));
        super.onClose();
    }
}
