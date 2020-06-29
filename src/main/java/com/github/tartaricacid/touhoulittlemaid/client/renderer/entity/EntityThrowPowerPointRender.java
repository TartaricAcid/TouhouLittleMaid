package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityThrowPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityThrowPowerPointRender {
    public static final EntityThrowPowerPointRender.Factory FACTORY = new EntityThrowPowerPointRender.Factory();

    public static class Factory implements IRenderFactory<EntityThrowPowerPoint> {
        @Override
        public Render<? super EntityThrowPowerPoint> createRenderFor(RenderManager manager) {
            return new RenderSnowball(manager, MaidItems.POWER_POINT, Minecraft.getMinecraft().getRenderItem());
        }
    }
}
