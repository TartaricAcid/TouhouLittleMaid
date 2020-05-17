package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.PortableAudioModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPortableAudio;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityPortableAudioRender extends RenderLivingBase<EntityPortableAudio> {
    public static final EntityPortableAudioRender.Factory FACTORY = new EntityPortableAudioRender.Factory();
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/portable_audio.png");

    private EntityPortableAudioRender(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
    }

    @Override
    protected boolean canRenderName(@Nonnull EntityPortableAudio entity) {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.renderManager.pointedEntity);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityPortableAudio entity) {
        return TEXTURE;
    }

    @Override
    protected void preRenderCallback(EntityPortableAudio portableAudio, float partialTickTime) {
        GlStateManager.scale(1.2, 1.2, 1.2);
    }

    public static class Factory implements IRenderFactory<EntityPortableAudio> {
        @Override
        public Render<? super EntityPortableAudio> createRenderFor(RenderManager manager) {
            return new EntityPortableAudioRender(manager, new PortableAudioModel(), 0.5f);
        }
    }
}
