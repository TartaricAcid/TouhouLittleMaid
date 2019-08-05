package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityMarisaBroomModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2019/7/5 22:59
 **/
@SideOnly(Side.CLIENT)
public class EntityMarisaBroomRender extends RenderLivingBase<EntityMarisaBroom> {
    public static final EntityMarisaBroomRender.Factory FACTORY = new EntityMarisaBroomRender.Factory();
    private static final ResourceLocation BROOM_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/marisa_broom.png");

    private EntityMarisaBroomRender(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
    }

    @Override
    protected boolean canRenderName(EntityMarisaBroom entity) {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.renderManager.pointedEntity);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityMarisaBroom entity) {
        return BROOM_TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityMarisaBroom> {
        @Override
        public Render<? super EntityMarisaBroom> createRenderFor(RenderManager manager) {
            return new EntityMarisaBroomRender(manager, new EntityMarisaBroomModel(), 0.5f);
        }
    }
}
