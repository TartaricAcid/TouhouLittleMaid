package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityPlaceholderModel;
import com.github.tartaricacid.touhoulittlemaid.item.ItemEntityPlaceholder;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class TileEntityEntityPlaceholderRenderer extends ItemStackTileEntityRenderer {
    private static final EntityPlaceholderModel BASE_MODEL = new EntityPlaceholderModel();
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/items/entity_placeholder.png");

    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        matrixStack.translate(0.5, 1.5, 0);
        matrixStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        IVertexBuilder buffer = bufferIn.getBuffer(RenderType.entityTranslucent(getTexture(stack)));
        BASE_MODEL.renderToBuffer(matrixStack, buffer, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }

    private ResourceLocation getTexture(ItemStack stack) {
        ResourceLocation recipeId = ItemEntityPlaceholder.getRecipeId(stack);
        if (recipeId != null) {
            Path path = Paths.get(recipeId.getPath().toLowerCase(Locale.US));
            String namespace = recipeId.getNamespace().toLowerCase(Locale.US);
            ResourceLocation texture = new ResourceLocation(namespace, String.format("textures/items/%s.png", path.getFileName().toString()));
            if (Minecraft.getInstance().getResourceManager().hasResource(texture)) {
                return texture;
            }
        }
        return TEXTURE;
    }
}
