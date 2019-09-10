package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityYukkuriModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers.LayerYukkuriSlimeGel;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/8/29 15:31
 **/
@SideOnly(Side.CLIENT)
public class EntityYukkuriSlimeRender extends RenderLiving<EntitySlime> {
    public static final EntityYukkuriSlimeRender.Factory FACTORY = new EntityYukkuriSlimeRender.Factory();
    private static final ResourceLocation SLIME_TEXTURES = new ResourceLocation("textures/entity/slime/slime.png");
    private static final ResourceLocation YUKKURI_TEXTURES = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/yukkuri.png");
    private ModelBase modelSlime;
    private ModelBase modelYukkuri;

    private EntityYukkuriSlimeRender(RenderManager renderManager) {
        super(renderManager, new ModelSlime(16), 0.25F);
        modelSlime = this.getMainModel();
        modelYukkuri = new EntityYukkuriModel();
        this.addLayer(new LayerYukkuriSlimeGel(this));
    }

    @Override
    public void doRender(@Nonnull EntitySlime entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.shadowSize = 0.25F * (float) entity.getSlimeSize();
        if (GeneralConfig.VANILLA_CONFIG.changeSlimeModel) {
            this.mainModel = modelYukkuri;
        } else {
            this.mainModel = modelSlime;
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(EntitySlime entitySlime, float partialTickTime) {
        GlStateManager.scale(0.999F, 0.999F, 0.999F);
        float slimeSize = (float) entitySlime.getSlimeSize();
        float tmpValue = (entitySlime.prevSquishFactor + (entitySlime.squishFactor - entitySlime.prevSquishFactor) * partialTickTime) / (slimeSize * 0.5F + 1.0F);
        float scaleFactor = 1.0F / (tmpValue + 1.0F);
        GlStateManager.scale(scaleFactor * slimeSize, 1.0F / scaleFactor * slimeSize, scaleFactor * slimeSize);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntitySlime entity) {
        if (GeneralConfig.VANILLA_CONFIG.changeSlimeModel) {
            return YUKKURI_TEXTURES;
        } else {
            return SLIME_TEXTURES;
        }
    }

    public static class Factory implements IRenderFactory<EntitySlime> {
        @Override
        public Render<? super EntitySlime> createRenderFor(RenderManager manager) {
            return new EntityYukkuriSlimeRender(manager);
        }
    }
}
