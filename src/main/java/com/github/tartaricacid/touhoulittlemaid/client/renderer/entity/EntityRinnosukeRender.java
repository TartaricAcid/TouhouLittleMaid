package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityRinnosukeModel;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityRinnosuke;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2019/8/29 15:31
 **/
@SideOnly(Side.CLIENT)
public class EntityRinnosukeRender extends RenderLiving<EntityRinnosuke> {
    public static final EntityRinnosukeRender.Factory FACTORY = new EntityRinnosukeRender.Factory();
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/morichika_rinnosuke.png");

    private EntityRinnosukeRender(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityRinnosuke entity) {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityRinnosuke> {
        @Override
        public Render<? super EntityRinnosuke> createRenderFor(RenderManager manager) {
            return new EntityRinnosukeRender(manager, new EntityRinnosukeModel(), 0.5f);
        }
    }
}
