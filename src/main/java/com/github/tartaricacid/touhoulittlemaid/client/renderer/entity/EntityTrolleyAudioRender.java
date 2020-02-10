package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityTrolleyAudioModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTrolleyAudio;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2020/1/20 16:03
 **/
public class EntityTrolleyAudioRender extends RenderLivingBase<EntityTrolleyAudio> {
    public static final EntityTrolleyAudioRender.Factory FACTORY = new EntityTrolleyAudioRender.Factory();
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/trolley_audio.png");

    private EntityTrolleyAudioRender(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
    }

    @Override
    protected boolean canRenderName(EntityTrolleyAudio entity) {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.renderManager.pointedEntity);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityTrolleyAudio entity) {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityTrolleyAudio> {
        @Override
        public Render<? super EntityTrolleyAudio> createRenderFor(RenderManager manager) {
            return new EntityTrolleyAudioRender(manager, new EntityTrolleyAudioModel(), 0.5f);
        }
    }
}
