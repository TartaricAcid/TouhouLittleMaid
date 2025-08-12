package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.ParrotImitation;

import java.util.concurrent.CompletableFuture;

public class DataMapGenerator extends DataMapProvider {
    private final Builder<ParrotImitation, EntityType<?>> builder;

    public DataMapGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
        this.builder = builder(NeoForgeDataMaps.PARROT_IMITATIONS);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder.add(InitEntities.MAID, new ParrotImitation(InitSounds.MAID_IDLE.get()), false);
    }
}
