package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.api.event.client.EntityMaidRenderable;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Mob;

public class LayerMaidBackpack extends RenderLayer<Mob, BedrockModel<Mob>> {
    private final EntityMaidRenderer renderer;

    public LayerMaidBackpack(EntityMaidRenderer renderer, EntityModelSet modelSet) {
        super(renderer);
        this.renderer = renderer;
        BackpackManager.initClient(modelSet);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, Mob mob, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityMaid maid = EntityMaidRenderable.convertToMaid(mob);
        if (maid == null) return;
        if (!renderer.getMainInfo().isShowBackpack() || mob.isSleeping() || mob.isInvisible()) {
            return;
        }
        // 稍微缩放，避免整数倍的 z-flight
        poseStack.scale(1.01f, 1.01f, 1.01f);
        // [-13, 41, 15]
        if (getParentModel().hasBackpackPositioningModel()) {
            BedrockPart renderer = getParentModel().getBackpackPositioningModel();
            poseStack.translate(renderer.x * 0.0625, 0.0625 * (renderer.y - 23 + 8), 0.0625 * (renderer.z + 4));
        } else {
            poseStack.translate(0, -0.5, 0.25);
        }
        IMaidBackpack type = InGameMaidConfig.INSTANCE.isShowBackpack() ? maid.getMaidBackpackType() : BackpackManager.getEmptyBackpack();
        BackpackManager.findBackpackModel(type.getId()).ifPresent(pair -> renderColoredCutoutModel(pair.getLeft(), pair.getRight(), poseStack, bufferIn, packedLightIn, maid, 1.0f, 1.0f, 1.0f));
    }
}
