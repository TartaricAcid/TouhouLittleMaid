package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.config;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.MaidAIChatManager;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.SupportLanguage;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.MaidAIChatConfigButton;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.config.MaidAIChatConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SaveMaidAIDataMessage;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Map;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class MaidAIChatConfigContainerGui extends AbstractMaidContainerGui<MaidAIChatConfigContainer> {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_ai_chat_config.png");

    private final MaidAIChatManager manager;
    private final Map<String, Map<String, String>> llmSites;
    private final Map<String, Map<String, String>> ttsSites;

    private boolean isEditSetting = false;
    private EditBox ownerName;
    private EditBox customSetting;

    public MaidAIChatConfigContainerGui(MaidAIChatConfigContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.maid.getAiChatManager().readFromTag(screenContainer.getConfigData());
        this.manager = this.maid.getAiChatManager();
        this.llmSites = screenContainer.getLLMSites();
        this.ttsSites = screenContainer.getTTSSites();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.renderBg(graphics, partialTicks, x, y);
        if (isEditSetting) {
            graphics.blit(ICON, leftPos + 80, topPos + 28, 0, 0, 176, 24);
        } else {
            graphics.blit(ICON, leftPos + 80, topPos + 28, 0, 0, 176, 137);
        }
    }

    @Override
    protected void initAdditionWidgets() {
        int buttonLeft = leftPos + 86;
        int buttonTop = topPos + 52;
        if (!this.isEditSetting) {
            this.addConfigButtons(buttonLeft, buttonTop);
        }
        this.addInput(buttonLeft, buttonTop);
        this.addOtherButtons(buttonLeft);
    }

    @Override
    protected void renderAddition(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.drawCenteredString(font, Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config"),
                leftPos + 167, topPos + 41, 0xFFFFFF);
        if (this.isEditSetting) {
            graphics.drawString(font, Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.owner_name"),
                    leftPos + 90, topPos + 59, 0x777777, false);
            graphics.drawString(font, Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.custom_setting"),
                    leftPos + 90, topPos + 91, 0x777777, false);
            this.ownerName.render(graphics, mouseX, mouseY, partialTicks);
            this.customSetting.render(graphics, mouseX, mouseY, partialTicks);
        }
    }

    private void addInput(int buttonLeft, int buttonTop) {
        this.ownerName = new EditBox(this.font, buttonLeft + 3, buttonTop + 18, 158, 16, Component.literal("Owner Name"));
        this.ownerName.setMaxLength(128);
        this.ownerName.setValue(this.manager.ownerName);
        this.ownerName.setResponder(s -> this.manager.ownerName = s);
        this.ownerName.setVisible(this.isEditSetting);
        this.addWidget(this.ownerName);

        this.customSetting = new EditBox(this.font, buttonLeft + 3, buttonTop + 50, 158, 16, Component.literal("Custom Setting"));
        this.customSetting.setMaxLength(1024);
        this.customSetting.setValue(this.manager.customSetting);
        this.customSetting.setResponder(s -> this.manager.customSetting = s);
        this.customSetting.setVisible(this.isEditSetting);
        this.addWidget(this.customSetting);
    }

    private void addOtherButtons(int buttonLeft) {
        MutableComponent edit = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.edit_custom_setting.edit");
        MutableComponent save = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.edit_custom_setting.save");
        MutableComponent buttonName = this.isEditSetting ? save : edit;

        this.addRenderableWidget(Button.builder(buttonName, button -> {
            this.isEditSetting = !this.isEditSetting;
            this.init();
            this.saveConfig();
        }).bounds(buttonLeft, topPos + 125, 164, 30).build());
    }

    private void addConfigButtons(int buttonLeft, int buttonTop) {
        MutableComponent noSiteSelectedName = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.no_site_selected");
        MutableComponent llmSiteName = Component.literal(manager.llmSite);
        MutableComponent ttsSiteName = Component.literal(manager.ttsSite);

        this.addRenderableWidget(new MaidAIChatConfigButton(buttonLeft, buttonTop,
                Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.llm_site"),
                manager.llmSite.isBlank() ? noSiteSelectedName : llmSiteName,
                button -> {
                    manager.llmSite = onClickSites(this.llmSites, manager.llmSite, button, true);
                    this.init();
                    this.saveConfig();
                },
                button -> {
                    manager.llmSite = onClickSites(this.llmSites, manager.llmSite, button, false);
                    this.init();
                    this.saveConfig();
                }
        ));
        buttonTop += 13;

        Map<String, String> llmModels = this.llmSites.get(manager.llmSite);
        if (llmModels != null && !llmModels.isEmpty()) {
            String name = llmModels.get(manager.llmModel);
            if (StringUtils.isBlank(name)) {
                manager.llmModel = llmModels.keySet().iterator().next();
                name = llmModels.get(manager.llmModel);
            }
            this.addRenderableWidget(new MaidAIChatConfigButton(buttonLeft, buttonTop,
                    Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.llm_model"), Component.literal(name),
                    button -> {
                        manager.llmModel = onClickModels(llmModels, manager.llmModel, button, true);
                        this.saveConfig();
                    },
                    button -> {
                        manager.llmModel = onClickModels(llmModels, manager.llmModel, button, false);
                        this.saveConfig();
                    }
            ));
            buttonTop += 13;
        }

        this.addRenderableWidget(new MaidAIChatConfigButton(buttonLeft, buttonTop,
                Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.tts_site"),
                manager.ttsSite.isBlank() ? noSiteSelectedName : ttsSiteName,
                button -> {
                    manager.ttsSite = onClickSites(this.ttsSites, manager.ttsSite, button, true);
                    this.init();
                    this.saveConfig();
                },
                button -> {
                    manager.ttsSite = onClickSites(this.ttsSites, manager.ttsSite, button, false);
                    this.init();
                    this.saveConfig();
                }
        ));
        buttonTop += 13;

        Map<String, String> ttsModels = this.ttsSites.get(manager.ttsSite);
        if (ttsModels != null && !ttsModels.isEmpty()) {
            String name = ttsModels.get(manager.ttsModel);
            if (StringUtils.isBlank(name)) {
                manager.ttsModel = ttsModels.keySet().iterator().next();
                name = ttsModels.get(manager.ttsModel);
            }
            this.addRenderableWidget(new MaidAIChatConfigButton(buttonLeft, buttonTop,
                    Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.tts_model"),
                    Component.literal(name),
                    button -> {
                        manager.ttsModel = onClickModels(ttsModels, manager.ttsModel, button, true);
                        this.saveConfig();
                    },
                    button -> {
                        manager.ttsModel = onClickModels(ttsModels, manager.ttsModel, button, false);
                        this.saveConfig();
                    }
            ));
            buttonTop += 13;
        }

        if (StringUtils.isBlank(manager.ttsLanguage)) {
            manager.ttsLanguage = SupportLanguage.SUPPORTED_LANGUAGES.get(0);
        }
        this.addRenderableWidget(new MaidAIChatConfigButton(buttonLeft, buttonTop,
                Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.tts_language"),
                SupportLanguage.getLanguageName(manager.ttsLanguage),
                button -> {
                    manager.ttsLanguage = SupportLanguage.findPrev(manager.ttsLanguage);
                    button.setValue(SupportLanguage.getLanguageName(manager.ttsLanguage));
                    this.saveConfig();
                },
                button -> {
                    manager.ttsLanguage = SupportLanguage.findNext(manager.ttsLanguage);
                    button.setValue(SupportLanguage.getLanguageName(manager.ttsLanguage));
                    this.saveConfig();
                }
        ));
    }

    private String onClickSites(Map<String, Map<String, String>> sites, String site,
                                MaidAIChatConfigButton button, boolean isLeft) {
        if (sites.isEmpty()) {
            button.setValue(Component.empty());
            return StringUtils.EMPTY;
        }
        List<String> keys = Lists.newArrayList(sites.keySet());
        if (keys.size() == 1) {
            button.setValue(Component.literal(keys.get(0)));
            return keys.get(0);
        }
        int index = keys.indexOf(site);
        if (index < 0) {
            button.setValue(Component.literal(keys.get(0)));
            return keys.get(0);
        }
        if (isLeft) {
            index--;
            if (index < 0) {
                index = keys.size() - 1;
            }
        } else {
            index++;
            if (index >= keys.size()) {
                index = 0;
            }
        }
        String id = keys.get(index);
        button.setValue(Component.literal(id));
        return id;
    }

    private String onClickModels(Map<String, String> models, String model,
                                 MaidAIChatConfigButton button, boolean isLeft) {
        if (models.isEmpty()) {
            button.setValue(Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.default_model"));
            return StringUtils.EMPTY;
        }
        List<String> keys = Lists.newArrayList(models.keySet());
        if (keys.size() == 1) {
            String id = keys.get(0);
            button.setValue(Component.literal(models.get(id)));
            return id;
        }
        int index = keys.indexOf(model);
        if (index < 0) {
            String id = keys.get(0);
            button.setValue(Component.literal(models.get(id)));
            return id;
        }
        if (isLeft) {
            index--;
            if (index < 0) {
                index = keys.size() - 1;
            }
        } else {
            index++;
            if (index >= keys.size()) {
                index = 0;
            }
        }
        String id = keys.get(index);
        button.setValue(Component.literal(models.get(id)));
        return id;
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String ownerNameValue = this.ownerName.getValue();
        String customSettingValue = this.customSetting.getValue();
        super.resize(minecraft, width, height);
        this.ownerName.setValue(ownerNameValue);
        this.customSetting.setValue(customSettingValue);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.ownerName.tick();
        this.customSetting.tick();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.getMinecraft().player != null) {
            this.getMinecraft().player.closeContainer();
        }
        if (this.ownerName.keyPressed(keyCode, scanCode, modifiers) || this.customSetting.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (this.ownerName.canConsumeInput() || this.customSetting.canConsumeInput()) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void saveConfig() {
        NetworkHandler.CHANNEL.sendToServer(new SaveMaidAIDataMessage(this.maid.getId(), this.manager));
    }
}