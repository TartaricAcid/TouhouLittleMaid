package com.github.tartaricacid.touhoulittlemaid.client.resource;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.flag.FeatureFlagSet;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.locating.IModFile;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;

public class LegacyPackRepositorySource implements RepositorySource {
    private static final String LEGACY_PACK_DIR_NAME = "legacy_pack";
    private static final String PACK_NAME = "touhou_little_maid_legacy_resources_pack";
    private final Pack legacyPack;

    public LegacyPackRepositorySource() {
        Pack.ResourcesSupplier supplier = getLegacyPack();
        MutableComponent title = Component.translatable("pack.touhou_little_maid.legacy_resources_pack.title");
        MutableComponent desc = Component.translatable("pack.touhou_little_maid.legacy_resources_pack.desc");
        PackLocationInfo info = new PackLocationInfo(PACK_NAME, title, PackSource.BUILT_IN, Optional.empty());
        Pack.Metadata metadata = new Pack.Metadata(desc, PackCompatibility.COMPATIBLE, FeatureFlagSet.of(), Collections.emptyList(), false);
        PackSelectionConfig config = new PackSelectionConfig(false, Pack.Position.TOP, false);
        this.legacyPack = new Pack(info, supplier, metadata, config);
    }

    private PathPackResources.PathResourcesSupplier getLegacyPack() {
        IModFile file = ModList.get().getModFileById(TouhouLittleMaid.MOD_ID).getFile();
        return new PathPackResources.PathResourcesSupplier(file.getSecureJar().getRootPath().resolve(LEGACY_PACK_DIR_NAME));
    }

    @Override
    public void loadPacks(Consumer<Pack> consumer) {
        consumer.accept(this.legacyPack);
    }
}
