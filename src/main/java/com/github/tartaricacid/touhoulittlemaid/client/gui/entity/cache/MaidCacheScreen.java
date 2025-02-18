package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.cache;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.model.MaidModelGui;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.CacheIconTexture;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.info.ServerCustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.IconCache;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.cache.CacheIconManager.translate;
import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

@SuppressWarnings("all")
@OnlyIn(Dist.CLIENT)
public class MaidCacheScreen extends CacheScreen<EntityMaid, MaidModelInfo> {
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
    private static final Optional<MaidModelInfo> DEFAULT_MODEL_INFO = ServerCustomPackLoader.SERVER_MAID_MODELS.getInfo(DEFAULT_MODEL_ID);
    private final Queue<MaidModelGui.YsmMaidInfo> ysmMaidInfos;
    private final int ysmModelTotalCount;

    public MaidCacheScreen(Screen parent, EntityType<EntityMaid> entityType, Queue<MaidModelInfo> modelInfos, EntityRender<EntityMaid, MaidModelInfo> entityRender) {
        super(parent, entityType, modelInfos, entityRender);

        // 构建YsmMaid信息
        CacheIconManager.buildYsmMaidInfos();
        this.ysmMaidInfos = new LinkedList<>(CacheIconManager.getYsmMaidInfos());
        this.ysmModelTotalCount = CacheIconManager.getYsmMaidInfos().size();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics);
        this.renderRenderables(graphics, mouseX, mouseY, partialTick);

        if (modelInfos.isEmpty()) {
            stopWatch.stop();
            double timeCost = stopWatch.getTime(TimeUnit.MILLISECONDS) / 1000.0;
            TouhouLittleMaid.LOGGER.info("Cache icon time: {} seconds", timeCost);
            Minecraft.getInstance().setScreen(parent);
            return;
        }

        // 每帧尝试缓存 5 个
        graphics.pose().pushPose();
        for (int i = 0; i < 5; i++) {
            if (modelInfos.isEmpty()) {
                return;
            }
            graphics.pose().translate(0, 0, 200);
            doCacheIcon(graphics);
        }
        // ysm
        for (int i = 0; i < 5; i++) {
            if (ysmMaidInfos.isEmpty()) {
                return;
            }
            graphics.pose().translate(0, 0, 200);
            doCacheIconFromYsmMaid(graphics);
        }
        graphics.pose().popPose();

        final int totalCount = this.totalCount + this.ysmModelTotalCount;
        int finishSize = totalCount - modelInfos.size();
        graphics.drawCenteredString(font, Component.translatable("gui.touhou_little_maid.cache_screen.progress", finishSize, totalCount), this.width / 2, this.height - 42, 0xFFFFFF);
        graphics.drawCenteredString(font, Component.translatable("gui.touhou_little_maid.cache_screen.desc"), this.width / 2, this.height - 30, 0xFFFFFF);
    }

    private void renderRenderables(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        for(Renderable renderable : this.renderables) {
            renderable.render(graphics, mouseX, mouseY, partialTick);
        }
    }

    @SuppressWarnings("all")
    private void drawYsmMaid(GuiGraphics graphics, int posX, int posY, MaidModelGui.YsmMaidInfo ysmMaidInfo, int scaleModified) {
        Level world = getMinecraft().level;
        if (world == null) {
            return;
        }
        EntityMaid maid = new EntityMaid(world);
        maid.setIsYsmModel(true);
        maid.setYsmModel(ysmMaidInfo.modelId(), ysmMaidInfo.textureId());
        clearMaidDataResidue(maid, false);
        int scale = scaleModified / 2;
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics,
                posX + scale,
                posY + scaleModified,
                (int) (scale * DEFAULT_MODEL_INFO.get().getRenderItemScale()),
                -25, -20, maid);
    }

    private void doCacheIconFromYsmMaid(GuiGraphics graphics) {
        MaidModelGui.YsmMaidInfo ysmMaidInfo = ysmMaidInfos.poll();
        if (ysmMaidInfo != null) {
            if (ysmMaidInfo.cacheIconId() == null) {
                return;
            }

            double guiScale = Minecraft.getInstance().getWindow().getGuiScale();
            int scaleModified = (int) Math.ceil((256 / guiScale));

            graphics.fill(0, 0, scaleModified, scaleModified + 2, IconCache.BACKGROUND_COLOR);
            this.drawYsmMaid(graphics, 0, 0, ysmMaidInfo, scaleModified);
            NativeImage nativeImage = IconCache.exportImageFromScreenshot(256, IconCache.BACKGROUND_COLOR_SHIFTED);

            String modelIdString = translate(ysmMaidInfo.modelId());
            String textrueIdString = translate(ysmMaidInfo.textureId());
            ResourceLocation modelId = new ResourceLocation(modelIdString, textrueIdString);
            CacheIconTexture cacheIconTexture = new CacheIconTexture(modelId, nativeImage);
            Minecraft.getInstance().textureManager.register(ysmMaidInfo.cacheIconId(), cacheIconTexture);
        }
    }
}
