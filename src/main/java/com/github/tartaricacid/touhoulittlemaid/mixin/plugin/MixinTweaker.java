package com.github.tartaricacid.touhoulittlemaid.mixin.plugin;

import com.github.tartaricacid.touhoulittlemaid.compat.iris.IrisCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.sodium.SodiumCompat;
import com.google.common.collect.Lists;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinTweaker implements IMixinConfigPlugin {
    public MixinTweaker() {
        SodiumCompat.init();
        IrisCompat.init();
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        if (FMLEnvironment.dist == Dist.CLIENT && SodiumCompat.isInstalled()) {
            List<String> output = Lists.newArrayList(
                    "compat.sodium.ChunkBufferBuildersMixin",
                    "compat.sodium.ChunkBuilderMeshingTaskMixin",
                    "compat.sodium.DefaultMaterialsMixin",
                    "compat.sodium.DefaultShaderInterfaceMixin",
                    "compat.sodium.MaterialMixin",
                    "compat.sodium.RenderRegionManagerMixin",
                    "compat.sodium.ShaderChunkRendererMixin",
                    "compat.sodium.SodiumWorldRendererMixin",
                    "compat.sodium.TerrainRenderPassMixin"
            );
            if (IrisCompat.isInstalled()) {
                output.add("compat.iris.ShadowRendererMixin");
                output.add("compat.iris.SodiumProgramsMixin");
                output.add("compat.iris.SodiumShaderMixin");
                output.add("compat.iris.WorldRenderingPhaseMixin");
            }
            return output;
        } else {
            return null;
        }
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
