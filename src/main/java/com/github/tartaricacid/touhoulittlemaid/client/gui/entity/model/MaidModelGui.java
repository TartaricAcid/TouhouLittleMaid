package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.model;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.cache.CacheIconManager;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.detail.MaidModelDetailsGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.YsmModelButton;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.info.ServerCustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.MaidModelMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SetMaidSoundIdMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.YsmMaidModelMessage;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.github.tartaricacid.touhoulittlemaid.client.event.SpecialMaidRenderEvent.EASTER_EGG_MODEL;
import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

public class MaidModelGui extends AbstractModelGui<EntityMaid, MaidModelInfo> {
    private static int PAGE_INDEX = 0;
    private static int PACK_INDEX = 0;
    private static int ROW_INDEX = 0;
    private final List<YsmMaidInfo> ysmModels = Lists.newArrayList();
    private final List<YsmModelButton> ysmModelButtons = Lists.newArrayList();

    public MaidModelGui(EntityMaid maid) {
        super(maid, CustomPackLoader.MAID_MODELS.getPackList());

//        CacheIconManager.clearCache();

        // 构建YsmMaid信息
//        CacheIconManager.buildYsmMaidInfos();
    }

    @Override
    public void init() {
        super.init();
        ysmModelButtons.clear();
        this.addYsmModelTabButton();
        this.addYsmModelButtons();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderYsmModelTooltips(graphics, mouseX, mouseY, partialTicks);
    }

    private void renderYsmModelTooltips(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        for (YsmModelButton ysmModelButton : this.ysmModelButtons) {
            ysmModelButton.renderTooltip(graphics, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    protected void drawLeftEntity(GuiGraphics graphics, int middleX, int middleY, float mouseX, float mouseY) {
        float renderItemScale = CustomPackLoader.MAID_MODELS.getModelRenderItemScale(entity.getModelId());
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, (middleX - 256 / 2) / 2, middleY + 90, (int) (45 * renderItemScale), (middleX - 256 / 2f) / 2 - mouseX, middleY + 80 - 40 - mouseY, entity);
    }

    @Override
    protected void drawRightEntity(GuiGraphics graphics, int posX, int posY, MaidModelInfo modelItem) {
        ResourceLocation cacheIconId = modelItem.getCacheIconId();
        var allTextures = Minecraft.getInstance().textureManager.byPath;
        if (MiscConfig.MODEL_ICON_CACHE.get() && allTextures.containsKey(cacheIconId)) {
            int textureSize = 24;
            graphics.blit(cacheIconId, posX - textureSize / 2, posY - textureSize, textureSize, textureSize, 0, 0, textureSize, textureSize, textureSize, textureSize);
        } else {
            drawEntity(graphics, posX, posY, modelItem);
        }
    }

    @Override
    protected void openDetailsGui(EntityMaid maid, MaidModelInfo modelInfo) {
        if (minecraft != null && modelInfo.getEasterEgg() == null) {
            minecraft.setScreen(new MaidModelDetailsGui(maid, modelInfo));
        }
    }

    @Override
    protected void notifyModelChange(EntityMaid maid, MaidModelInfo info) {
        if (info.getEasterEgg() == null) {
            NetworkHandler.CHANNEL.sendToServer(new MaidModelMessage(maid.getId(), info.getModelId()));
            String useSoundPackId = info.getUseSoundPackId();
            if (StringUtils.isNotBlank(useSoundPackId)) {
                NetworkHandler.CHANNEL.sendToServer(new SetMaidSoundIdMessage(maid.getId(), useSoundPackId));
            }
            // 切换模型时，重置手部动作
            maid.handItemsForAnimation[0] = ItemStack.EMPTY;
            maid.handItemsForAnimation[1] = ItemStack.EMPTY;
        }
    }

    @Override
    protected void addModelCustomTips(MaidModelInfo modelItem, List<Component> tooltips) {
        String useSoundPackId = modelItem.getUseSoundPackId();
        if (StringUtils.isNotBlank(useSoundPackId)) {
            tooltips.add(Component.translatable("gui.touhou_little_maid.skin.tooltips.maid_use_sound_pack_id", useSoundPackId).withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    protected int getPageIndex() {
        return PAGE_INDEX;
    }

    @Override
    protected void setPageIndex(int pageIndex) {
        PAGE_INDEX = pageIndex;
    }

    @Override
    protected int getPackIndex() {
        return PACK_INDEX;
    }

    @Override
    protected void setPackIndex(int packIndex) {
        PACK_INDEX = packIndex;
    }

    @Override
    protected int getRowIndex() {
        return ROW_INDEX;
    }

    @Override
    protected void setRowIndex(int rowIndex) {
        ROW_INDEX = rowIndex;
    }

    public void addYsmModelTabButton() {
        List<YsmMaidInfo> ysmModels = CacheIconManager.getYsmMaidInfos();
        if (ysmModels.isEmpty()) {
            return;
        } else {
            this.ysmModels.clear();
            this.ysmModels.addAll(ysmModels);
        }

        int startX = this.width / 2 + 50;
        int startY = this.height / 2 + 5;

        Button ysmTabButton = Button.builder(Component.literal("Y"), b -> {
            setRowIndex(0);
            setPageIndex(Mth.clamp(getPageIndex() - 1, 0, guiNumber.getPageSize() - 1));
            setPackIndex(-1);
            this.init();
        }).pos(startX - 119 - 20, startY - 101).size(20, 20).build();
        this.addRenderableWidget(ysmTabButton);
    }

    private void addYsmModelButtons() {
        if (this.getPackIndex() != -1) {
            return;
        }

        int startX = (this.width / 2) + 50;
        int startY = (this.height / 2) + 5;

        // 起始坐标 -100, -38
        int offsetX = -100 - 20 + 10;
        int offsetY = -38 - 30 + 10;

        for (YsmMaidInfo ysmModel : this.ysmModels) {
            YsmModelButton ysmModelButton = new YsmModelButton(ysmModel, startX + offsetX, startY + offsetY, 15, 24, Component.empty(), this.onModelButtonClick(ysmModel.modelId(), ysmModel.textureId()));
            ysmModelButtons.add(ysmModelButton);
            this.addRenderableWidget(ysmModelButton);

            // 往右绘制
            offsetX = offsetX + 20;

            // 如果超出一定限制，换行
            if (offsetX > 105) {
                offsetX = -100;
                offsetY = offsetY + 30;
            }
        }
    }

    private Button.OnPress onModelButtonClick(String modelId, String modelTexture) {
        final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
        Optional<MaidModelInfo> modelInfo = ServerCustomPackLoader.SERVER_MAID_MODELS.getInfo(DEFAULT_MODEL_ID);

        return (button) -> {
            if (hasShiftDown()) {
                if (minecraft != null) {
                    minecraft.setScreen(new YsmMaidModelDetailsGui(entity, modelInfo.get(), modelId, modelTexture));
                }
            } else {
                setYsmModel(entity, modelId, modelTexture);
            }
        };
    }

    protected void setYsmModel(EntityMaid maid, String modelId, String modelTexture) {
        NetworkHandler.CHANNEL.sendToServer(new YsmMaidModelMessage(maid.getId(), modelId, modelTexture));
        // 切换模型时，重置手部动作
        maid.handItemsForAnimation[0] = ItemStack.EMPTY;
        maid.handItemsForAnimation[1] = ItemStack.EMPTY;
    }

    private void drawEntity(GuiGraphics graphics, int posX, int posY, MaidModelInfo modelItem) {
        Level world = getMinecraft().level;
        if (world == null) {
            return;
        }

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
        if (modelItem.getEasterEgg() != null) {
            maid.setModelId(EASTER_EGG_MODEL);
        } else {
            maid.setModelId(modelItem.getModelId().toString());
        }
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, posX, posY, (int) (12 * modelItem.getRenderItemScale()), -25, -20, maid);
    }

    private static class YsmMaidModelDetailsGui extends MaidModelDetailsGui {
        public YsmMaidModelDetailsGui(EntityMaid sourceEntity, MaidModelInfo modelInfo, String modeId, String texture) {
            super(sourceEntity, modelInfo);
            this.guiEntity.setIsYsmModel(true);
            this.guiEntity.setYsmModel(modeId, texture);
        }
    }

    public static class YsmMaidBaseInfo {
        private final String modelId;
        private final String textureId;
        private final List<Component> tooltips;
        private final boolean needAuth;

        public YsmMaidBaseInfo(String modelId, String textureId, List<Component> tooltips, boolean needAuth) {
            this.modelId = modelId;
            this.textureId = textureId;
            this.tooltips = tooltips;
            this.needAuth = needAuth;
        }

        public String modelId() {
            return modelId;
        }

        public String textureId() {
            return textureId;
        }

        public List<Component> tooltips() {
            return tooltips;
        }

        public boolean needAuth() {
            return needAuth;
        }
    }

    public static class YsmMaidInfo extends YsmMaidBaseInfo {
        @Nullable
        private final ResourceLocation cacheIconId;

        public YsmMaidInfo(String modelId, String textureId, List<Component> tooltips, boolean needAuth, @Nullable ResourceLocation cacheIconId) {
            super(modelId, textureId, tooltips, needAuth);
            this.cacheIconId = cacheIconId;
        }

        @Nullable
        public ResourceLocation cacheIconId() {
            return cacheIconId;
        }
    }
}
