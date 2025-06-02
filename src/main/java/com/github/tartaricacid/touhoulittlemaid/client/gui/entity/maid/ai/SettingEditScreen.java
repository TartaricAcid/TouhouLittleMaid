package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.MaidAIChatManager;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.CharacterSetting;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.SettingReader;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.bean.MetaData;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.message.SaveMaidAIDataPackage;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.github.tartaricacid.touhoulittlemaid.client.event.SpecialMaidRenderEvent.EASTER_EGG_MODEL;
import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

public class SettingEditScreen extends Screen {
    private static final long MAX_TIP_TIME = 2000;

    private final EntityMaid maid;
    private final MaidAIChatManager manager;
    private EditBox ownerName;
    private MultiLineEditBox customSetting;
    private long tipTimestamp = -1;

    public SettingEditScreen(EntityMaid maid) {
        super(Component.literal("Setting Edit Screen"));
        this.maid = maid;
        this.manager = maid.getAiChatManager();
    }

    @Override
    protected void init() {
        this.clearWidgets();

        int posX = this.width / 2 - 195;
        int boxWidth = 256;

        this.ownerName = this.addRenderableWidget(new EditBox(font, posX + 1, 30,
                boxWidth - 2, 20, Component.literal("Owner Name Box")));
        this.ownerName.setValue(manager.ownerName);
        this.ownerName.setMaxLength(128);
        this.ownerName.setResponder(s -> manager.ownerName = s);

        this.customSetting = this.addRenderableWidget(new MultiLineEditBox(font,
                posX, 70, boxWidth, this.height - 100,
                Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.edit_custom_setting.edit"),
                Component.literal("Custom Setting Box")));
        this.customSetting.setValue(manager.customSetting);
        this.customSetting.setCharacterLimit(4096);
        this.customSetting.setValueListener(s -> manager.customSetting = s);

        MutableComponent export = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.edit_custom_setting.export");
        this.addRenderableWidget(Button.builder(export, b -> exportSetting(export))
                .bounds(posX + 265, ownerName.getY(), 128, 20).build());

        this.addRenderableWidget(Button.builder(Component.translatable("selectWorld.edit.save"), b -> {
            this.saveConfig();
            this.tipTimestamp = System.currentTimeMillis();
        }).bounds(posX + 265, customSetting.getY(), 128, 20).build());

        MutableComponent saveQuit = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.edit_custom_setting.save_and_quit");
        this.addRenderableWidget(Button.builder(saveQuit, b -> {
            this.saveConfig();
            Minecraft.getInstance().setScreen(null);
        }).bounds(posX + 265, customSetting.getY() + 25, 128, 20).build());
    }

    private void exportSetting(MutableComponent export) {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            String title = export.getString();
            String defaultFileName = "%s.yml".formatted(this.maid.getName().getString());
            String path = SettingReader.getSettingsFolder().resolve(defaultFileName).toString();
            String fileFilter = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.edit_custom_setting.export.format").getString();

            PointerBuffer filterPattern = memoryStack.mallocPointer(1);
            filterPattern.put(memoryStack.UTF8("*.yml"));
            filterPattern.flip();

            String result = TinyFileDialogs.tinyfd_saveFileDialog(title, path, filterPattern, fileFilter);
            if (StringUtils.isBlank(result)) {
                return;
            }

            File exportFile = new File(result);
            MetaData metaData = getMetaData();
            CharacterSetting setting = new CharacterSetting(metaData, this.customSetting.getValue());
            setting.save(exportFile);

            if (this.getMinecraft().player != null) {
                Component tip = Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.edit_custom_setting.export.success", result)
                        .withStyle(ChatFormatting.GRAY);
                this.getMinecraft().player.sendSystemMessage(tip);
            }
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Error saving setting", e);
        }
    }

    @NotNull
    private MetaData getMetaData() {
        String lang = this.getMinecraft().getLanguageManager().getSelected();
        String author = "Unknown";
        if (this.getMinecraft().player != null) {
            author = this.getMinecraft().player.getScoreboardName();
        }
        String modelId = this.maid.getModelId();
        return new MetaData(0, author, Collections.singletonList(modelId), lang);
    }

    @Override
    public void resize(Minecraft mc, int pWidth, int pHeight) {
        String ownerNameValue = this.ownerName.getValue();
        String customSettingValue = this.customSetting.getValue();
        super.resize(mc, pWidth, pHeight);
        this.ownerName.setValue(ownerNameValue);
        this.customSetting.setValue(customSettingValue);
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        return super.mouseReleased(x, y, button) || this.customSetting.mouseReleased(x, y, button);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        graphics.drawString(font, Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.owner_name"),
                ownerName.getX() + 2, ownerName.getY() - 12, 0xFFFFFF);
        graphics.drawString(font, Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.custom_setting"),
                customSetting.getX() + 2, customSetting.getY() - 12, 0xFFFFFF);
        drawMaid(graphics, customSetting.getX() + customSetting.getWidth() + 73, customSetting.getY() + 96, maid);

        long time = System.currentTimeMillis() - this.tipTimestamp;
        if (time < MAX_TIP_TIME) {
            double value = (double) (time) / MAX_TIP_TIME * Math.PI;
            int alpha = (int) (Math.sin(value) * 0xFF);
            alpha = Mth.clamp(alpha, 15, 240);
            graphics.drawCenteredString(font, Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.edit_custom_setting.saved"),
                    customSetting.getX() + customSetting.getWidth() + 73, customSetting.getY() - 12, (alpha << 24) + 0xFF1111);
        }
    }

    private void drawMaid(GuiGraphics graphics, int posX, int posY, EntityMaid rawMaid) {
        Level world = getMinecraft().level;
        if (world == null) {
            return;
        }
        Optional<MaidModelInfo> info = CustomPackLoader.MAID_MODELS.getInfo(rawMaid.getModelId());
        if (info.isEmpty()) {
            return;
        }
        MaidModelInfo modelInfo = info.get();

        EntityMaid maid;
        try {
            maid = (EntityMaid) EntityCacheUtil.ENTITY_CACHE.get(EntityMaid.TYPE, () -> {
                Entity e = EntityMaid.TYPE.create(world);
                return Objects.requireNonNullElseGet(e, () -> new EntityMaid(world));
            });
        } catch (ExecutionException | ClassCastException e) {
            e.fillInStackTrace();
            return;
        }

        clearMaidDataResidue(maid, false);
        if (modelInfo.getEasterEgg() != null) {
            maid.setModelId(EASTER_EGG_MODEL);
        } else {
            maid.setModelId(modelInfo.getModelId().toString());
        }
        // 女仆换皮肤界面需要指定 YSM 渲染为空
        maid.setIsYsmModel(false);
        float renderItemScale = modelInfo.getRenderItemScale();
        InventoryScreen.renderEntityInInventoryFollowsMouse(
                graphics,
                posX - 45,
                posY - 45,
                posX + 45,
                posY + 55,
                (int) (45 * renderItemScale),
                0.1F,
                posX - 15,
                posY,
                maid);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void saveConfig() {
        PacketDistributor.sendToServer(new SaveMaidAIDataPackage(this.maid.getId(), this.manager));
    }
}
