package com.github.tartaricacid.touhoulittlemaid.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

/**
 * @author Argon4W
 */
@Mixin(SectionBufferBuilderPack.class)
public class SectionBufferBuilderPackMixin {
    @WrapOperation(method = "buffer", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    public Object wrapBufferGet(Map<RenderType, ByteBufferBuilder> instance, Object o, Operation<Object> original) {
        return instance.computeIfAbsent((RenderType) o, renderType -> new ByteBufferBuilder(renderType.bufferSize()));
    }
}
