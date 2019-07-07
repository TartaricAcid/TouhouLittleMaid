package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityMaidModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers.LayerMaidHeldItem;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
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
    private static ResourceLocation resourceLocation = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/reimu.png");

    public EntityMaidRender(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
        this.addLayer(new LayerMaidHeldItem(this));
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
        return resourceLocation;
    }

    public static class Factory implements IRenderFactory<EntityMaid> {
        @Override
        public Render<? super EntityMaid> createRenderFor(RenderManager manager) {
            return new EntityMaidRender(manager, new EntityMaidModel(), 0.5f);
        }
    }
}
