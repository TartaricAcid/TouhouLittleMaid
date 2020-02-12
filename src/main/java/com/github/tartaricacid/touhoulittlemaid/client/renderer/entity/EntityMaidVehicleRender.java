package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityCatCartModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityRickshawModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityWheelChairModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMaidVehicle;
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
 * @date 2020/2/3 13:56
 **/
public class EntityMaidVehicleRender extends RenderLivingBase<EntityMaidVehicle> {
    public static final EntityMaidVehicleRender.Factory FACTORY = new EntityMaidVehicleRender.Factory();
    private static ModelBase[] MODEL_LIST = new ModelBase[]{new EntityCatCartModel(), new EntityWheelChairModel(), new EntityRickshawModel()};
    private static ResourceLocation[] TEXTURE_LIST = new ResourceLocation[]{
            new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/cat_cart.png"),
            new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/wheel_chair.png"),
            new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/rickshaw.png")
    };

    private EntityMaidVehicleRender(RenderManager renderManager, float shadowSize) {
        super(renderManager, MODEL_LIST[0], shadowSize);
    }

    @Override
    protected boolean canRenderName(EntityMaidVehicle entity) {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.renderManager.pointedEntity);
    }

    @Override
    public void doRender(@Nonnull EntityMaidVehicle entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.mainModel = MODEL_LIST[entity.getModelId()];
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityMaidVehicle entity) {
        return TEXTURE_LIST[entity.getModelId()];
    }

    public static class Factory implements IRenderFactory<EntityMaidVehicle> {
        @Override
        public Render<? super EntityMaidVehicle> createRenderFor(RenderManager manager) {
            return new EntityMaidVehicleRender(manager, 0.5f);
        }
    }
}
