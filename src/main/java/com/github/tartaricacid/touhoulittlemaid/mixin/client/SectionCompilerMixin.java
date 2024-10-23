package com.github.tartaricacid.touhoulittlemaid.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author Argon4W
 */
@Mixin(SectionCompiler.class)
public class SectionCompilerMixin {
    @Redirect(method = "getOrBeginLayer", at = @At(value = "FIELD", target = "Lcom/mojang/blaze3d/vertex/DefaultVertexFormat;BLOCK:Lcom/mojang/blaze3d/vertex/VertexFormat;", opcode = Opcodes.GETSTATIC))
    public VertexFormat formatBasedOnRenderType(@Local(argsOnly = true) RenderType renderTypeRef) {
        return renderTypeRef.format;
    }
}
