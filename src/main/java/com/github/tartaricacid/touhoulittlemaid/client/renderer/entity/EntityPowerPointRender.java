package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * 来自于原版经验球的渲染代码
 *
 * @author TartaricAcid
 * @date 2019/8/29 17:05
 **/
@SideOnly(Side.CLIENT)
public class EntityPowerPointRender extends Render<EntityPowerPoint> {
    public static final EntityPowerPointRender.Factory FACTORY = new EntityPowerPointRender.Factory();
    private static final ResourceLocation POWER_POINT_TEXTURES = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/power_point.png");

    private EntityPowerPointRender(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    @Override
    public void doRender(@Nonnull EntityPowerPoint entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!this.renderOutlines) {
            RenderHelper.drawEntityPoint(entity, x, y, z, renderManager, e -> bindEntityTexture((EntityPowerPoint) e));
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityPowerPoint entity) {
        return POWER_POINT_TEXTURES;
    }

    public static class Factory implements IRenderFactory<EntityPowerPoint> {
        @Override
        public Render<? super EntityPowerPoint> createRenderFor(RenderManager manager) {
            return new EntityPowerPointRender(manager);
        }
    }
}
