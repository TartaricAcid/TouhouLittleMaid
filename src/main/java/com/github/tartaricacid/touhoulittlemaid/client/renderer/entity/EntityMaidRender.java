package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers.*;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomModelLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class EntityMaidRender extends RenderLiving<EntityMaid> {
    public static final Factory FACTORY = new Factory();
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
    private static final ResourceLocation DEFAULT_MODEL_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/hakurei_reimu.png");
    private ResourceLocation modelRes;

    private EntityMaidRender(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
        modelRes = DEFAULT_MODEL_TEXTURE;
        this.addLayer(new LayerMaidArrow(this));
        this.addLayer(new LayerMaidHeldItem(this));
        this.addLayer(new LayerMaidDebugFloor());
        this.addLayer(new LayerMaidDebugBroom());
        this.addLayer(new LayerHataSasimono());
        this.addLayer(new LayerMaidCustomHead(this));
        this.addLayer(new LayerMaidBackpack(this));
    }

    @Override
    public void doRender(@Nonnull EntityMaid entity, double x, double y, double z, float entityYaw, float partialTicks) {
        // 尝试读取模型
        this.mainModel = CustomModelLoader.MAID_MODEL.getModel(DEFAULT_MODEL_ID).orElseThrow(NullPointerException::new);
        CustomModelLoader.MAID_MODEL.getModel(entity.getModelId()).ifPresent(model -> this.mainModel = model);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        GlStateManager.popMatrix();
    }

    @Override
    protected boolean canRenderName(EntityMaid entity) {
        boolean hasCompass = Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == MaidItems.KAPPA_COMPASS;
        boolean isYourMaid = entity.getOwnerId() != null && entity.getOwnerId().equals(Minecraft.getMinecraft().player.getUniqueID());
        return (hasCompass && isYourMaid) || super.canRenderName(entity);
    }

    @Override
    protected void renderEntityName(@Nonnull EntityMaid entityIn, double x, double y, double z, @Nonnull String name, double distanceSq) {
        boolean hasCompass = Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == MaidItems.KAPPA_COMPASS;
        String str;
        if (hasCompass) {
            BlockPos pos = entityIn.getHomePos();
            // 判断坐标是否为 0,0,0，显示不同的字符
            if (pos.equals(BlockPos.ORIGIN)) {
                str = I18n.format("info.touhou_little_maid.maid.unset_pos");
            } else {
                str = I18n.format("info.touhou_little_maid.maid.set_pos", pos.getX(), pos.getY(), pos.getZ());
            }
        } else {
            str = name;
        }
        super.renderEntityName(entityIn, x, y, z, str, 16);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityMaid entity) {
        this.modelRes = DEFAULT_MODEL_TEXTURE;
        // 皮之不存，毛将焉附？
        // 先判定模型在不在，模型都不在，直接返回默认材质
        CustomModelLoader.MAID_MODEL.getInfo(entity.getModelId()).ifPresent(modelItem -> this.modelRes = modelItem.getTexture());
        return modelRes;
    }

    public static class Factory implements IRenderFactory<EntityMaid> {
        @Override
        public Render<? super EntityMaid> createRenderFor(RenderManager manager) {
            // 加载默认的灵梦模型
            return new EntityMaidRender(manager, CustomModelLoader.MAID_MODEL.getModel(DEFAULT_MODEL_ID).orElseThrow(NullPointerException::new), 0.5f);
        }
    }
}
