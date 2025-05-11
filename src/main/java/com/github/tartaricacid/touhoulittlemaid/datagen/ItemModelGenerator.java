package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TouhouLittleMaid.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        addInHandModel(InitItems.HAKUREI_GOHEI.get());
        addInHandModel(InitItems.SANAE_GOHEI.get());
        addInHandModel(InitItems.EXTINGUISHER.get());
        addInHandModel(InitItems.CAMERA.get());
        addInHandModel(InitItems.MAID_BEACON.get());

        basicItem(InitItems.OWNER_CONVERSION_TOOL.get());
    }

    private void addInHandModel(Item item) {
        String name = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();
        String inHandName = name + "_in_hand";

        ItemModelBuilder inventory = new ItemModelBuilder(modLoc(name), this.existingFileHelper)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/" + name));
        ItemModelBuilder inHand = new ItemModelBuilder(modLoc(name), this.existingFileHelper)
                .parent(new ModelFile.UncheckedModelFile(modLoc("item/" + inHandName)));

        getBuilder(name)
                .guiLight(BlockModel.GuiLight.FRONT)
                .customLoader(SeparateTransformsModelBuilder::begin).base(inHand)
                .perspective(ItemDisplayContext.GUI, inventory)
                .perspective(ItemDisplayContext.FIXED, inventory);
    }
}
