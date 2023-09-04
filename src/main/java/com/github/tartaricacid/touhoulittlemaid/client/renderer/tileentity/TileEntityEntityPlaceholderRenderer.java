package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityPlaceholderModel;
import com.github.tartaricacid.touhoulittlemaid.item.ItemEntityPlaceholder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class TileEntityEntityPlaceholderRenderer extends BlockEntityWithoutLevelRenderer {
    private static final EntityPlaceholderModel BASE_MODEL = new EntityPlaceholderModel();
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/items/entity_placeholder.png");

    public TileEntityEntityPlaceholderRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLight, int combinedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucent(getTexture(stack)));
        BASE_MODEL.renderToBuffer(poseStack, buffer, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    private ResourceLocation getTexture(ItemStack stack) {
        ResourceLocation recipeId = ItemEntityPlaceholder.getRecipeId(stack);
        if (recipeId != null) {
            Path path = Paths.get(recipeId.getPath().toLowerCase(Locale.US));
            String namespace = recipeId.getNamespace().toLowerCase(Locale.US);
            ResourceLocation texture = new ResourceLocation(namespace, String.format("textures/item/%s.png", path.getFileName().toString()));
            if (Minecraft.getInstance().getResourceManager().getResource(texture).isPresent()) {
                return texture;
            }
        }
        return TEXTURE;
    }
}
