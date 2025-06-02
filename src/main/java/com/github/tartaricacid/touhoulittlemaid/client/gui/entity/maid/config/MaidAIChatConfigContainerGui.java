package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.config;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.MaidAIChatManager;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.SupportLanguage;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.HistoryAIChatScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.SettingEditScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.MaidAIChatConfigButton;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.config.MaidAIChatConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.network.message.SaveMaidAIDataPackage;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class MaidAIChatConfigContainerGui extends AbstractMaidContainerGui<MaidAIChatConfigContainer> {
    private static final ResourceLocation ICON = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/maid_ai_chat_config.png");

    private final MaidAIChatManager manager;
    private final Map<String, Map<String, String>> llmSites;
    private final Map<String, Map<String, String>> ttsSites;
    private final List<String> sttSites = Lists.newArrayList(AvailableSites.STT_SITES.keySet());

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
        graphics.blit(ICON, leftPos + 80, topPos + 28, 0, 0, 176, 137);
    }

    @Override
    protected void initAdditionWidgets() {
        int buttonLeft = leftPos + 86;
        int buttonTop = topPos + 38;
        this.addConfigButtons(buttonLeft, buttonTop);
        this.addOtherButtons(buttonLeft);
    }

    private void addOtherButtons(int buttonLeft) {
        MutableComponent edit = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.edit_custom_setting.edit");
        MutableComponent history = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.open_history_chat");

        this.addRenderableWidget(Button.builder(edit, button -> {
                    this.init();
                    this.saveConfig();
                    this.getMinecraft().setScreen(new SettingEditScreen(this.maid));
                }).bounds(buttonLeft + 2, topPos + 120, 160, 18)
                .tooltip(Tooltip.create(edit)).build());

        this.addRenderableWidget(Button.builder(history, button -> {
                    this.saveConfig();
                    this.getMinecraft().setScreen(new HistoryAIChatScreen(this.maid));
                }).bounds(buttonLeft + 2, topPos + 140, 160, 18)
                .tooltip(Tooltip.create(history)).build());
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
        buttonTop += 13;

        this.addSttButtons(buttonLeft, buttonTop);
    }

    private void addSttButtons(int buttonLeft, int buttonTop) {
        String sttType = AIConfig.STT_TYPE.get().getName();
        if (sttSites.isEmpty()) {
            sttType = StringUtils.EMPTY;
        } else if (!sttSites.contains(sttType)) {
            sttType = sttSites.get(0);
        }
        String finalSttType = sttType;
        this.addRenderableWidget(new MaidAIChatConfigButton(buttonLeft, buttonTop,
                Component.translatable("config.touhou_little_maid.global_ai.stt_type"),
                Component.literal(sttType),
                button -> {
                    if (sttSites.isEmpty()) {
                        return;
                    }
                    int index = sttSites.indexOf(finalSttType);
                    if (index != -1) {
                        index--;
                        if (index < 0) {
                            index = sttSites.size() - 1;
                        }
                        AIConfig.STT_TYPE.set(STTApiType.getByName(sttSites.get(index)));
                    } else {
                        AIConfig.STT_TYPE.set(STTApiType.PLAYER2);
                    }
                    button.setValue(Component.literal(AIConfig.STT_TYPE.get().getName()));
                    this.init();
                },
                button -> {
                    if (sttSites.isEmpty()) {
                        return;
                    }
                    int index = sttSites.indexOf(finalSttType);
                    if (index != -1) {
                        index++;
                        if (index >= sttSites.size()) {
                            index = 0;
                        }
                        AIConfig.STT_TYPE.set(STTApiType.getByName(sttSites.get(index)));
                    } else {
                        AIConfig.STT_TYPE.set(STTApiType.PLAYER2);
                    }
                    button.setValue(Component.literal(AIConfig.STT_TYPE.get().getName()));
                    this.init();
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

    private void saveConfig() {
        PacketDistributor.sendToServer(new SaveMaidAIDataPackage(this.maid.getId(), this.manager));
    }
}