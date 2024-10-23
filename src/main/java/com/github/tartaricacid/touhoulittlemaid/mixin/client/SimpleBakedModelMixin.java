package com.github.tartaricacid.touhoulittlemaid.mixin.client;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.sections.SimpleBakedModelExtension;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.neoforged.neoforge.client.RenderTypeGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Argon4W
 */
@Mixin(SimpleBakedModel.class)
public class SimpleBakedModelMixin implements SimpleBakedModelExtension {
    @Unique
    private RenderTypeGroup eyelib$renderTypeGroup;

    @Inject(method = "<init>(Ljava/util/List;Ljava/util/Map;ZZZLnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/block/model/ItemTransforms;Lnet/minecraft/client/renderer/block/model/ItemOverrides;Lnet/neoforged/neoforge/client/RenderTypeGroup;)V", at = @At("TAIL"))
    private void constructor(List pUnculledFaces, Map pCulledFaces, boolean pHasAmbientOcclusion, boolean pUsesBlockLight, boolean pIsGui3d, TextureAtlasSprite pParticleIcon, ItemTransforms pTransforms, ItemOverrides pOverrides, RenderTypeGroup renderTypes, CallbackInfo ci) {
        this.eyelib$renderTypeGroup = renderTypes;
    }

    @Unique
    @Override
    public RenderTypeGroup eyelib$getRenderTypeGroup() {
        return eyelib$renderTypeGroup;
    }
}
