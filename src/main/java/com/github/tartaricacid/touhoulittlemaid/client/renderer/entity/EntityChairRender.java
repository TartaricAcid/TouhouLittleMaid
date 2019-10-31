package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;


import com.github.tartaricacid.touhoulittlemaid.client.model.DebugCharacterModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.DebugFloorModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers.LayerChairDebugCharacter;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers.LayerChairDebugFloor;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import net.minecraft.client.renderer.GlStateManager;
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
 * @date 2019/8/8 13:35
 **/

@SideOnly(Side.CLIENT)
public class EntityChairRender extends RenderLivingBase<EntityChair> {
    public static final EntityChairRender.Factory FACTORY = new EntityChairRender.Factory();
    private static final String DEFAULT_CHAIR_ID = "touhou_little_maid:cushion";
    private static final ResourceLocation DEFAULT_CHAIR_TEXTURE = new ResourceLocation("touhou_little_maid:textures/entity/cushion.png");
    private static final LayerChairDebugFloor DEBUG_FLOOR = new LayerChairDebugFloor(new DebugFloorModel());
    private static final LayerChairDebugCharacter DEBUG_CHARACTER = new LayerChairDebugCharacter(new DebugCharacterModel());
    private ResourceLocation modelRes;

    private EntityChairRender(RenderManager renderManager, EntityModelJson mainModel) {
        super(renderManager, mainModel, 0f);
        modelRes = DEFAULT_CHAIR_TEXTURE;
        addLayer(DEBUG_FLOOR);
        addLayer(DEBUG_CHARACTER);
    }

    @Override
    public void doRender(@Nonnull EntityChair chair, double x, double y, double z, float entityYaw, float partialTicks) {
        // 尝试读取模
        this.mainModel = ClientProxy.CHAIR_MODEL.getModel(DEFAULT_CHAIR_ID).orElseThrow(NullPointerException::new);
        ClientProxy.CHAIR_MODEL.getModel(chair.getModelId()).ifPresent(model -> this.mainModel = model);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(chair, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityChair chair) {
        this.modelRes = DEFAULT_CHAIR_TEXTURE;
        // 皮之不存，毛将焉附？
        // 先判定模型在不在，模型都不在，直接返回默认材质
        ClientProxy.CHAIR_MODEL.getInfo(chair.getModelId()).ifPresent(modelItem -> this.modelRes = modelItem.getTexture());
        return modelRes;
    }

    @Override
    protected void applyRotations(EntityChair chair, float ageInTicks, float rotationYaw, float partialTicks) {
        GlStateManager.rotate(180 - chair.rotationYaw, 0.0F, 1.0F, 0.0F);
    }

    @Override
    protected boolean canRenderName(EntityChair entity) {
        return entity.getAlwaysRenderNameTag();
    }

    public static class Factory implements IRenderFactory<EntityChair> {
        @Override
        public Render<? super EntityChair> createRenderFor(RenderManager manager) {
            return new EntityChairRender(manager, ClientProxy.CHAIR_MODEL.getModel(DEFAULT_CHAIR_ID).orElseThrow(NullPointerException::new));
        }
    }
}
