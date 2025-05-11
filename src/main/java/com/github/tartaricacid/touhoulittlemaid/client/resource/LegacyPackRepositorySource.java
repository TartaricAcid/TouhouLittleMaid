package com.github.tartaricacid.touhoulittlemaid.client.resource;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.function.Consumer;

public class LegacyPackRepositorySource implements RepositorySource {
    private static final String LEGACY_PACK_DIR_NAME = "legacy_pack";
    private static final String PACK_NAME = "touhou_little_maid_legacy_resources_pack";
    private final Pack legacyPack;

    public LegacyPackRepositorySource() {
        Pack.ResourcesSupplier supplier = name -> getLegacyPack();
        MutableComponent desc = Component.translatable("pack.touhou_little_maid.legacy_resources_pack.desc");
        int packFormatVersion = SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES);
        Pack.Info info = new Pack.Info(desc, packFormatVersion, FeatureFlagSet.of());
        MutableComponent title = Component.translatable("pack.touhou_little_maid.legacy_resources_pack.title");
        this.legacyPack = Pack.create(PACK_NAME, title, false, supplier, info, PackType.CLIENT_RESOURCES,
                Pack.Position.TOP, false, PackSource.BUILT_IN);
    }

    private PathPackResources getLegacyPack() {
        IModFile file = ModList.get().getModFileById(TouhouLittleMaid.MOD_ID).getFile();
        return new PathPackResources(file.getFileName(), true, file.getFilePath()) {
            @NotNull
            @Override
            protected Path resolve(@NotNull String... paths) {
                String[] newPaths = new String[paths.length + 1];
                newPaths[0] = LEGACY_PACK_DIR_NAME;
                System.arraycopy(paths, 0, newPaths, 1, paths.length);
                return file.findResource(newPaths);
            }
        };
    }

    @Override
    public void loadPacks(Consumer<Pack> consumer) {
        consumer.accept(this.legacyPack);
    }
}
