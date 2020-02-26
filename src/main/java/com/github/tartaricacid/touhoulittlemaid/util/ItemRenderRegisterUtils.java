package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.client.event.BakeModelEvent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;
import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public final class ItemRenderRegisterUtils {
    private ItemRenderRegisterUtils() {
    }

    public static ModelResourceLocation getModelRl(Item item, int textureIndex) {
        return new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()) + "_" + textureIndex);
    }

    public static ModelResourceLocation getModelRl(String namespace, String path) {
        return new ModelResourceLocation(new ResourceLocation(namespace, path), null);
    }

    public static ModelResourceLocation getModelRl(Item item) {
        return getModelRl(item, null);
    }

    public static ModelResourceLocation getModelRl(Item item, String variant) {
        return new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), variant);
    }

    public static void registerRender(Block block) {
        registerRender(Item.getItemFromBlock(block));
    }

    public static void registerRender(Item item) {
        registerRender(item, 0);
    }

    public static void registerRender(Item item, int meta) {
        registerRender(item, meta, "inventory");
    }

    public static void registerRender(Item item, int meta, String variant) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelRl(item, variant));
    }

    public static void registerReplaceRender(Item item, ModelResourceLocation rawModel, ModelResourceLocation replaceModel, Supplier<Boolean> isReplace) {
        ModelLoader.setCustomModelResourceLocation(item, 0, rawModel);
        ModelBakery.registerItemVariants(item, replaceModel);
        BakeModelEvent.addReplaceableModel(rawModel, replaceModel, isReplace);
    }

    public static void register2d3dRender(Item item) {
        ModelResourceLocation model2d = new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()) + "_2d");
        ModelResourceLocation model3d = new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()) + "_3d");
        ModelLoader.setCustomModelResourceLocation(item, 0, model2d);
        ModelBakery.registerItemVariants(item, model3d);
        BakeModelEvent.addPerspectiveModel(model2d, model3d);
    }
}
