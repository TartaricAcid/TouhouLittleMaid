package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.item.ReplaceableBakedModel;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.VanillaConfig;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitSpecialItemRender {
    private static final List<Triple<ModelResourceLocation, ModelResourceLocation, Supplier<Boolean>>> REPLACEABLE_MODEL_LIST = Lists.newArrayList();

    private static final ResourceLocation LIFE_POINT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "life_point");
    private static final ResourceLocation POINT_ITEM = new ResourceLocation(TouhouLittleMaid.MOD_ID, "point_item");

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            addReplaceableModel(Items.TOTEM_OF_UNDYING, LIFE_POINT, () -> VanillaConfig.REPLACE_TOTEM_TEXTURE.get());
            addReplaceableModel(Items.EXPERIENCE_BOTTLE, POINT_ITEM, () -> VanillaConfig.REPLACE_XP_BOTTLE_TEXTURE.get());
        }
    }

    @SubscribeEvent
    public static void onBakedModel(ModelEvent.BakingCompleted event) {
        Map<ResourceLocation, BakedModel> registry = event.getModelBakery().getBakedTopLevelModels();
        for (Triple<ModelResourceLocation, ModelResourceLocation, Supplier<Boolean>> triple : REPLACEABLE_MODEL_LIST) {
            ReplaceableBakedModel model = new ReplaceableBakedModel(registry.get(triple.getLeft()), registry.get(triple.getMiddle()), triple.getRight());
            registry.put(triple.getLeft(), model);
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional event) {
        REPLACEABLE_MODEL_LIST.forEach((triple) -> event.register(triple.getMiddle()));
    }

    public static void addReplaceableModel(Item item, ResourceLocation replacedModel, Supplier<Boolean> isReplace) {
        ResourceLocation res = ForgeRegistries.ITEMS.getKey(item);
        if (res != null) {
            ModelResourceLocation rawModelResourceLocation = new ModelResourceLocation(res, "inventory");
            ModelResourceLocation replacedModelResourceLocation = new ModelResourceLocation(replacedModel, "inventory");
            REPLACEABLE_MODEL_LIST.add(Triple.of(rawModelResourceLocation, replacedModelResourceLocation, isReplace));
        }
    }
}
