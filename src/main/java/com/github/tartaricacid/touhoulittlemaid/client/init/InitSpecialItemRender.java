package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.item.PerspectiveBakedModel;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitSpecialItemRender {
    private static final List<Pair<ModelResourceLocation, ModelResourceLocation>> PERSPECTIVE_MODEL_LIST = Lists.newArrayList();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event) {
        addInHandModel(InitItems.HAKUREI_GOHEI.get());
        addInHandModel(InitItems.EXTINGUISHER.get());
    }

    @SubscribeEvent
    public static void onBakedModel(ModelBakeEvent event) {
        Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();
        for (Pair<ModelResourceLocation, ModelResourceLocation> pair : PERSPECTIVE_MODEL_LIST) {
            PerspectiveBakedModel model = new PerspectiveBakedModel(registry.get(pair.getLeft()), registry.get(pair.getRight()));
            registry.put(pair.getLeft(), model);
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        PERSPECTIVE_MODEL_LIST.forEach((pair) -> ModelLoader.addSpecialModel(pair.getRight()));
    }

    public static void addInHandModel(Item item) {
        ResourceLocation res = item.getRegistryName();
        if (res != null) {
            ModelResourceLocation rawName = ModelLoader.getInventoryVariant(res.toString());
            ModelResourceLocation inHandName = ModelLoader.getInventoryVariant(res.toString() + "_in_hand");
            PERSPECTIVE_MODEL_LIST.add(Pair.of(rawName, inHandName));
        }
    }
}
