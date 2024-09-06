package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.cache;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.CacheIconTexture;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.IModelInfo;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.github.tartaricacid.touhoulittlemaid.util.IconCache;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.time.StopWatch;

import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@OnlyIn(Dist.CLIENT)
public class CacheScreen<T extends LivingEntity, E extends IModelInfo> extends Screen {
    private final Screen parent;
    private final EntityType<T> entityType;
    private final Queue<E> modelInfos;
    private final EntityRender<T, E> entityRender;
    private final int totalCount;
    private final StopWatch stopWatch;

    public CacheScreen(Screen parent, EntityType<T> entityType, Queue<E> modelInfos, EntityRender<T, E> entityRender) {
        super(new TextComponent("Cache Screen"));
        this.parent = parent;
        this.entityType = entityType;
        this.modelInfos = modelInfos;
        this.entityRender = entityRender;
        this.totalCount = modelInfos.size();
        this.stopWatch = StopWatch.createStarted();
    }

    @SuppressWarnings("unchecked")
    private void drawEntity(PoseStack pPoseStack, int posX, int posY, int posZ, E modelInfo, int scaleModified) {
        Level world = getMinecraft().level;
        if (world == null) {
            return;
        }
        T entity;
        try {
            entity = (T) EntityCacheUtil.ENTITY_CACHE.get(entityType, () -> entityType.create(world));
        } catch (ExecutionException | ClassCastException e) {
            e.fillInStackTrace();
            return;
        }
        entityRender.render(pPoseStack, posX, posY, posZ, modelInfo, scaleModified, entity);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);

        if (modelInfos.isEmpty()) {
            stopWatch.stop();
            double timeCost = stopWatch.getTime(TimeUnit.MILLISECONDS) / 1000.0;
            TouhouLittleMaid.LOGGER.info("Cache icon time: {} seconds", timeCost);
            Minecraft.getInstance().setScreen(parent);
            return;
        }

        // 每帧尝试缓存 5 个
        poseStack.pushPose();
        int posZ = 0;
        for (int i = 0; i < 5; i++) {
            if (modelInfos.isEmpty()) {
                return;
            }
            posZ += 100;
            poseStack.translate(0, 0, 100);
            doCacheIcon(poseStack, posZ);
        }
        poseStack.popPose();

        int finishSize = totalCount - modelInfos.size();
        drawCenteredString(poseStack, font, new TranslatableComponent("gui.touhou_little_maid.cache_screen.progress", finishSize, totalCount), this.width / 2, this.height - 42, 0xFFFFFF);
        drawCenteredString(poseStack, font, new TranslatableComponent("gui.touhou_little_maid.cache_screen.desc"), this.width / 2, this.height - 30, 0xFFFFFF);
    }

    private void doCacheIcon(PoseStack poseStack, int posZ) {
        E modelInfo = modelInfos.poll();
        if (modelInfo != null) {
            double guiScale = Minecraft.getInstance().getWindow().getGuiScale();
            int scaleModified = (int) Math.ceil((256 / guiScale));

            fill(poseStack, 0, 0, scaleModified, scaleModified + 2, IconCache.BACKGROUND_COLOR);
            this.drawEntity(poseStack, 0, 0, posZ, modelInfo, scaleModified);
            NativeImage nativeImage = IconCache.exportImageFromScreenshot(256, IconCache.BACKGROUND_COLOR_SHIFTED);

            ResourceLocation modelId = modelInfo.getModelId();
            CacheIconTexture cacheIconTexture = new CacheIconTexture(modelId, nativeImage);
            Minecraft.getInstance().textureManager.register(modelInfo.getCacheIconId(), cacheIconTexture);
        }
    }

    public interface EntityRender<T extends LivingEntity, E extends IModelInfo> {
        void render(PoseStack pPoseStack, int posX, int posY, int posZ, E modelInfo, int scaleModified, T entity);
    }
}
