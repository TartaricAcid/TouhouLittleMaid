package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.item.ReplaceableBakedModel;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.VanillaConfig;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class InitSpecialItemRender {
    private static final List<Triple<ModelResourceLocation, ModelResourceLocation, Supplier<Boolean>>> REPLACEABLE_MODEL_LIST = Lists.newArrayList();

    private static final ResourceLocation LIFE_POINT = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "life_point");
    private static final ResourceLocation POINT_ITEM = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "point_item");

    // 祭坛合成占位符的物品模型
    private static final ModelResourceLocation SPAWN_BOX = standalone("item/spawn_box");
    private static final ModelResourceLocation REBORN_MAID = standalone("item/reborn_maid");
    private static final ModelResourceLocation SPAWN_LIGHTNING_BOLT = standalone("item/spawn_lightning_bolt");

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            addReplaceableModel(Items.TOTEM_OF_UNDYING, LIFE_POINT, () -> VanillaConfig.REPLACE_TOTEM_TEXTURE.get());
            addReplaceableModel(Items.EXPERIENCE_BOTTLE, POINT_ITEM, () -> VanillaConfig.REPLACE_XP_BOTTLE_TEXTURE.get());
        }
    }

    @SubscribeEvent
    public static void onBakedModel(ModelEvent.BakingCompleted event) {
        Map<ModelResourceLocation, BakedModel> registry = event.getModelBakery().getBakedTopLevelModels();
        for (Triple<ModelResourceLocation, ModelResourceLocation, Supplier<Boolean>> triple : REPLACEABLE_MODEL_LIST) {
            ReplaceableBakedModel model = new ReplaceableBakedModel(registry.get(triple.getLeft()), registry.get(triple.getMiddle()), triple.getRight());
            registry.put(triple.getLeft(), model);
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional event) {
        REPLACEABLE_MODEL_LIST.forEach((triple) -> event.register(triple.getMiddle()));

        // 特殊需要额外注册的模型
        event.register(SPAWN_BOX);
        event.register(REBORN_MAID);
        event.register(SPAWN_LIGHTNING_BOLT);
    }

    public static void addReplaceableModel(Item item, ResourceLocation replacedModel, Supplier<Boolean> isReplace) {
        ResourceLocation res = BuiltInRegistries.ITEM.getKey(item);
        if (res != null) {
            ModelResourceLocation rawModelResourceLocation = ModelResourceLocation.inventory(res);
            ModelResourceLocation replacedModelResourceLocation = ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(replacedModel.getNamespace(), "item/" + replacedModel.getPath()));
            REPLACEABLE_MODEL_LIST.add(Triple.of(rawModelResourceLocation, replacedModelResourceLocation, isReplace));
        }
    }

    private static ModelResourceLocation standalone(String path) {
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, path);
        return ModelResourceLocation.standalone(loc);
    }
}
