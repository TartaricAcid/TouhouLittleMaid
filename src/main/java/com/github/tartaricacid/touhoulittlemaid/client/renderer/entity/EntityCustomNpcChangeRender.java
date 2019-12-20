package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.NPCRendererHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.layer.*;
import noppes.npcs.client.model.ModelBipedAlt;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author TartaricAcid
 * @date 2019/12/19 15:15
 * <p>
 * 因为 Custom NPC 模组的兼容性，导致渲染女仆模型会出现问题
 * 我只能在原有代码的基础上做修改，覆盖掉原 NPC 模组的渲染
 * 临时解决办法，希望不会存在某些版权冲突和代码冲突
 **/
@SuppressWarnings("all")
@SideOnly(Side.CLIENT)
public class EntityCustomNpcChangeRender<T extends EntityCustomNpc> extends RenderNPCInterface<T> {
    private float partialTicks;
    private EntityLivingBase entity;
    private RenderLivingBase renderEntity;
    public ModelBiped npcmodel;

    public EntityCustomNpcChangeRender(ModelBiped model) {
        super(model, 0.5F);
        this.npcmodel = (ModelBiped) this.mainModel;
        this.layerRenderers.add(new LayerEyes(this));
        this.layerRenderers.add(new LayerHeadwear(this));
        this.layerRenderers.add(new LayerHead(this));
        this.layerRenderers.add(new LayerArms(this));
        this.layerRenderers.add(new LayerLegs(this));
        this.layerRenderers.add(new LayerBody(this));
        this.layerRenderers.add(new LayerNpcCloak(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerCustomHead(this.npcmodel.bipedHead));
        LayerBipedArmor armor = new LayerBipedArmor(this);
        this.addLayer(armor);
        ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, armor, new ModelBipedAlt(0.5F), 1);
        ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, armor, new ModelBipedAlt(1.0F), 2);
    }

    @Override
    public void doRender(T npc, double d, double d1, double d2, float f, float partialTicks) {
        this.partialTicks = partialTicks;
        this.entity = npc.modelData.getEntity(npc);
        if (this.entity != null) {
            Render render = this.renderManager.getEntityRenderObject(this.entity);
            if (render instanceof RenderLivingBase) {
                this.renderEntity = (RenderLivingBase) render;
            } else {
                this.renderEntity = null;
                this.entity = null;
            }
        } else {
            this.renderEntity = null;
            List<LayerRenderer<T>> list = this.layerRenderers;
            Iterator var11 = list.iterator();

            while (var11.hasNext()) {
                LayerRenderer layer = (LayerRenderer) var11.next();
                if (layer instanceof LayerPreRender) {
                    ((LayerPreRender) layer).preRender(npc);
                }
            }
        }

        this.npcmodel.rightArmPose = this.getPose(npc, npc.getHeldItemMainhand());
        this.npcmodel.leftArmPose = this.getPose(npc, npc.getHeldItemOffhand());
        super.doRender(npc, d, d1, d2, f, partialTicks);
    }

    public ModelBiped.ArmPose getPose(T npc, ItemStack item) {
        if (NoppesUtilServer.IsItemStackNull(item)) {
            return ModelBiped.ArmPose.EMPTY;
        } else {
            if (npc.getItemInUseCount() > 0) {
                EnumAction enumaction = item.getItemUseAction();
                if (enumaction == EnumAction.BLOCK) {
                    return ModelBiped.ArmPose.BLOCK;
                }

                if (enumaction == EnumAction.BOW) {
                    return ModelBiped.ArmPose.BOW_AND_ARROW;
                }
            }

            return ModelBiped.ArmPose.ITEM;
        }
    }

    @Override
    protected void renderModel(T npc, float par2, float par3, float par4, float par5, float par6, float par7) {
        if (this.renderEntity != null) {
            boolean flag = !npc.isInvisible();
            boolean flag1 = !flag && !npc.isInvisibleToPlayer(Minecraft.getMinecraft().player);
            if (!flag && !flag1) {
                return;
            }

            if (flag1) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.alphaFunc(516, 0.003921569F);
            }

            ModelBase model = this.renderEntity.getMainModel();
            if (PixelmonHelper.isPixelmon(this.entity)) {
                ModelBase pixModel = (ModelBase) PixelmonHelper.getModel(this.entity);
                if (pixModel != null) {
                    model = pixModel;
                    PixelmonHelper.setupModel(this.entity, pixModel);
                }
            }

            if (isMaid(this.entity)) {
                Optional<EntityModelJson> maidModel = ClientProxy.MAID_MODEL.getModel(((EntityMaid) entity).getModelId());
                if (maidModel.isPresent()) {
                    model = maidModel.get();
                } else {
                    model = ClientProxy.MAID_MODEL.getModel("touhou_little_maid:hakurei_reimu").orElseThrow(NullPointerException::new);
                }
            }

            model.swingProgress = 1.0F;
            model.isRiding = this.entity.isRiding() && this.entity.getRidingEntity() != null && this.entity.getRidingEntity().shouldRiderSit();
            model.setLivingAnimations(this.entity, par2, par3, this.partialTicks);
            model.setRotationAngles(par2, par3, par4, par5, par6, par7, this.entity);
            model.isChild = this.entity.isChild();
            NPCRendererHelper.renderModel(this.entity, par2, par3, par4, par5, par6, par7, this.renderEntity, model, this.getEntityTexture(npc));
            if (!npc.display.getOverlayTexture().isEmpty()) {
                GlStateManager.depthFunc(515);
                if (npc.textureGlowLocation == null) {
                    npc.textureGlowLocation = new ResourceLocation(npc.display.getOverlayTexture());
                }

                float f1 = 1.0F;
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(1, 1);
                GlStateManager.disableLighting();
                if (npc.isInvisible()) {
                    GlStateManager.depthMask(false);
                } else {
                    GlStateManager.depthMask(true);
                }

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.pushMatrix();
                GlStateManager.scale(1.001F, 1.001F, 1.001F);
                NPCRendererHelper.renderModel(this.entity, par2, par3, par4, par5, par6, par7, this.renderEntity, model, npc.textureGlowLocation);
                GlStateManager.popMatrix();
                GlStateManager.enableLighting();
                GlStateManager.color(1.0F, 1.0F, 1.0F, f1);
                GlStateManager.depthFunc(515);
                GlStateManager.disableBlend();
            }

            if (flag1) {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
            }
        } else {
            super.renderModel(npc, par2, par3, par4, par5, par6, par7);
        }

    }

    @Override
    protected void renderLayers(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn) {
        if (this.entity != null && this.renderEntity != null) {
            NPCRendererHelper.drawLayers(this.entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn, this.renderEntity);
        } else {
            super.renderLayers(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn);
        }
    }

    @Override
    protected void preRenderCallback(T npc, float f) {
        if (this.renderEntity != null) {
            this.renderColor(npc);
            int size = npc.display.getSize();
            if (this.entity instanceof EntityNPCInterface) {
                ((EntityNPCInterface) this.entity).display.setSize(5);
            }
            NPCRendererHelper.preRenderCallback(this.entity, f, this.renderEntity);
            npc.display.setSize(size);
            GlStateManager.scale(0.2F * (float) npc.display.getSize(), 0.2F * (float) npc.display.getSize(), 0.2F * (float) npc.display.getSize());
        } else {
            super.preRenderCallback(npc, f);
        }
    }

    @Override
    protected float handleRotationFloat(T par1EntityLivingBase, float par2) {
        return this.renderEntity != null ? NPCRendererHelper.handleRotationFloat(this.entity, par2, this.renderEntity) : super.handleRotationFloat(par1EntityLivingBase, par2);
    }

    private static boolean isMaid(Entity entity) {
        String s = EntityList.getEntityString(entity);
        return s != null && s.contains("touhou_little_maid.maid");
    }
}
