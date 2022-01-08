package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public final class WirelessIORenderEvent {
    @SubscribeEvent
    public static void onRender(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        ItemStack mainStack = mc.player.getMainHandItem();
        if (mainStack.getItem() != InitItems.WIRELESS_IO.get()) {
            return;
        }
        BlockPos pos = ItemWirelessIO.getBindingPos(mainStack);
        if (pos == null) {
            return;
        }
        Vector3d position = TileEntityRendererDispatcher.instance.camera.getPosition();
        AxisAlignedBB aabb = new AxisAlignedBB(pos).move(-position.x, -position.y, -position.z);
        IVertexBuilder buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
        WorldRenderer.renderLineBox(event.getMatrixStack(), buffer, aabb, 1.0F, 0, 0, 1.0F);
    }
}
