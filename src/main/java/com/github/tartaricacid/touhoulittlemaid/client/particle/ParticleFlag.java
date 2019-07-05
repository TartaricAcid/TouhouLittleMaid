package com.github.tartaricacid.touhoulittlemaid.client.particle;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * @author TartaricAcid
 * @date 2019/7/5 20:09
 **/
@SideOnly(Side.CLIENT)
public class ParticleFlag extends Particle {
    public static final ParticleFlag.Factory FACTORY = new ParticleFlag.Factory();
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/particle/flag.png");

    public ParticleFlag(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn);
        this.particleMaxAge = 1;
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        float x = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
        float y = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
        float z = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
        buffer.pos((double) (x - rotationX * 0.5F - rotationXY * 0.5F), (double) (y - rotationZ * 0.5F), (double) (z - rotationYZ * 0.5F - rotationXZ * 0.5F)).tex(1, 1).endVertex();
        buffer.pos((double) (x - rotationX * 0.5F + rotationXY * 0.5F), (double) (y + rotationZ * 0.5F), (double) (z - rotationYZ * 0.5F + rotationXZ * 0.5F)).tex(1, 0).endVertex();
        buffer.pos((double) (x + rotationX * 0.5F + rotationXY * 0.5F), (double) (y + rotationZ * 0.5F), (double) (z + rotationYZ * 0.5F + rotationXZ * 0.5F)).tex(0, 0).endVertex();
        buffer.pos((double) (x + rotationX * 0.5F - rotationXY * 0.5F), (double) (y - rotationZ * 0.5F), (double) (z + rotationYZ * 0.5F - rotationXZ * 0.5F)).tex(0, 1).endVertex();

        Tessellator.getInstance().draw();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory {
        @Override
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            return new ParticleFlag(worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }
}
