package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2019/8/30 14:57
 **/
@SideOnly(Side.CLIENT)
public class EntityChangeXPRender extends Render<EntityXPOrb> {
    public static final EntityChangeXPRender.Factory FACTORY = new EntityChangeXPRender.Factory();
    private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("textures/entity/experience_orb.png");
    private static final ResourceLocation POINT_ITEM_TEXTURES = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/point_item.png");

    private EntityChangeXPRender(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    @Override
    public void doRender(@Nonnull EntityXPOrb entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!this.renderOutlines) {
            if (GeneralConfig.VANILLA_CONFIG.changeXPTexture) {
                RenderHelper.drawEntityPoint(entity, x, y, z, renderManager, e -> bindEntityTexture((EntityXPOrb) e));
            } else {
                RenderHelper.drawEntityXP(entity, x, y, z, partialTicks, renderManager, e -> bindEntityTexture((EntityXPOrb) e));
            }
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityXPOrb entity) {
        if (GeneralConfig.VANILLA_CONFIG.changeXPTexture) {
            return POINT_ITEM_TEXTURES;
        }
        return EXPERIENCE_ORB_TEXTURES;
    }

    public static class Factory implements IRenderFactory<EntityXPOrb> {
        @Override
        public Render<? super EntityXPOrb> createRenderFor(RenderManager manager) {
            return new EntityChangeXPRender(manager);
        }
    }
}
