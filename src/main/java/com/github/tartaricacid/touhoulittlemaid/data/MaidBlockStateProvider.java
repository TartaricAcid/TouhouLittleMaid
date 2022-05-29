package com.github.tartaricacid.touhoulittlemaid.data;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBed;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MaidBlockStateProvider extends BlockStateProvider {
    public MaidBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.horizontalBlock(InitBlocks.MAID_BED.get(), (state -> {
            if (state.getValue(BlockMaidBed.PART) == BedPart.HEAD) {
                return new ModelFile.UncheckedModelFile(new ResourceLocation(TouhouLittleMaid.MOD_ID, "block/maid_bed_head"));
            } else {
                return new ModelFile.UncheckedModelFile(new ResourceLocation(TouhouLittleMaid.MOD_ID, "block/maid_bed_foot"));
            }
        }));
    }
}
