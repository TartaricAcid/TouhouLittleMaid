package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers.LayerMaidHeldItem;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class EntityMaidRender extends RenderLiving<EntityMaid> {
    public static final Factory FACTORY = new Factory();

    public EntityMaidRender(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
        this.addLayer(new LayerMaidHeldItem(this));
    }

    @Override
    public void doRender(EntityMaid entity, double x, double y, double z, float entityYaw, float partialTicks) {
        // 尝试读取模型
        EntityModelJson modelJson = ClientProxy.LOCATION_MODEL_MAP.get(entity.getModelLocation());
        // 如果模型不为空
        if (modelJson != null) {
            this.mainModel = modelJson;
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    protected boolean canRenderName(EntityMaid entity) {
        return Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == MaidItems.KAPPA_COMPASS &&
                entity.getOwnerId() != null && entity.getOwnerId().equals(Minecraft.getMinecraft().player.getUniqueID());
    }

    @Override
    protected void renderEntityName(EntityMaid entityIn, double x, double y, double z, String name, double distanceSq) {
        String str;
        BlockPos pos = entityIn.getHomePos();
        // 判断坐标是否为 0,0,0，显示不同的字符
        if (pos.equals(BlockPos.ORIGIN)) {
            str = I18n.format("info.touhou_little_maid.maid.unset_pos");
        } else {
            str = I18n.format("info.touhou_little_maid.maid.set_pos", pos.getX(), pos.getY(), pos.getZ());
        }
        super.renderEntityName(entityIn, x, y, z, str, 16);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityMaid entity) {
        // 皮之不存，毛将焉附？
        // 先判定模型在不在，模型都不在，直接返回默认材质
        if (ClientProxy.LOCATION_MODEL_MAP.get(entity.getModelLocation()) != null) {
            return new ResourceLocation(entity.getTextureLocation());
        } else {
            return new ResourceLocation("touhou_little_maid:textures/entity/hakurei_reimu.png");
        }
    }

    public static class Factory implements IRenderFactory<EntityMaid> {
        @Override
        public Render<? super EntityMaid> createRenderFor(RenderManager manager) {
            // 加载默认的灵梦模型
            return new EntityMaidRender(manager, ClientProxy.LOCATION_MODEL_MAP.get("touhou_little_maid:models/entity/hakurei_reimu.json"), 0.5f);
        }
    }
}
