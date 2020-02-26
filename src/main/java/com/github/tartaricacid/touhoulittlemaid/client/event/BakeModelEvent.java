package com.github.tartaricacid.touhoulittlemaid.client.event;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.item.PerspectiveBakedModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.item.ReplaceableBakedModel;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class BakeModelEvent {
    private static List<Pair<ModelResourceLocation, ModelResourceLocation>> PERSPECTIVE_MODEL_LIST = Lists.newArrayList();
    private static List<Triple<ModelResourceLocation, ModelResourceLocation, Supplier<Boolean>>> REPLACEABLE_MODEL_LIST = Lists.newArrayList();

    @SubscribeEvent
    public static void onBakedModel(ModelBakeEvent event) {
        for (Pair<ModelResourceLocation, ModelResourceLocation> pair : PERSPECTIVE_MODEL_LIST) {
            event.getModelRegistry().putObject(pair.getLeft(),
                    new PerspectiveBakedModel(
                            event.getModelRegistry().getObject(pair.getLeft()),
                            event.getModelRegistry().getObject(pair.getRight()))
            );
        }

        for (Triple<ModelResourceLocation, ModelResourceLocation, Supplier<Boolean>> triple : REPLACEABLE_MODEL_LIST) {
            event.getModelRegistry().putObject(triple.getLeft(),
                    new ReplaceableBakedModel(
                            event.getModelRegistry().getObject(triple.getLeft()),
                            event.getModelRegistry().getObject(triple.getMiddle()),
                            triple.getRight())
            );
        }
    }

    public static void addPerspectiveModel(ModelResourceLocation model2d, ModelResourceLocation model3d) {
        PERSPECTIVE_MODEL_LIST.add(Pair.of(model2d, model3d));
    }

    public static void addReplaceableModel(ModelResourceLocation rawModel, ModelResourceLocation replacedModel, Supplier<Boolean> isReplace) {
        REPLACEABLE_MODEL_LIST.add(Triple.of(rawModel, replacedModel, isReplace));
    }
}
