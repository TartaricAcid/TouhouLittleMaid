package com.github.tartaricacid.simplebedrockmodel.client.manager;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.AbstractBedrockEntityModel;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@SuppressWarnings({"unchecked", "rawtypes"})
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BedrockEntityModelRegister<T extends AbstractBedrockEntityModel<? extends Entity>> {
    public static BedrockEntityModelRegister INSTANCE = null;
    private final BedrockEntityModelSet<T> modelSet;

    private BedrockEntityModelRegister(BedrockEntityModelSet<T> modelSet) {
        this.modelSet = modelSet;
    }

    @SubscribeEvent
    public static void onRegisterClientReloadListenersEvent(RegisterClientReloadListenersEvent event) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof ReloadableResourceManager manager) {
            INSTANCE = new BedrockEntityModelRegister<>(new BedrockEntityModelSet<>());
            ModLoader.get().postEvent(new BedrockEntityModelRegisterEvent(INSTANCE.modelSet));
            // 将注册冻结
            INSTANCE.modelSet.immutableKnowLocations();
            // 添加到最前面，避免实体读取模型时模型还没加载完成
            manager.listeners.add(0, INSTANCE.modelSet);
        }
    }

    public AbstractBedrockEntityModel<? extends Entity> getModel(ResourceLocation location) {
        return modelSet.getModels().get(location);
    }

    public Set<ResourceLocation> getAllModelKeys() {
        return modelSet.getModels().keySet();
    }
}
