package com.github.tartaricacid.touhoulittlemaid.compat.jade.provider;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.*;

import java.util.Collections;
import java.util.List;

public enum AltarProvider implements IServerExtensionProvider<Object, ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {

    INSTANCE;

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "altar");

    @Override
    public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> list) {
        return ClientViewGroup.map(list, ItemView::new, null);
    }

    @Override
    public @Nullable List<ViewGroup<ItemStack>> getGroups(ServerPlayer player, ServerLevel world, Object target, boolean showDetails) {
        if (target instanceof TileEntityAltar altar) {
            ItemStack storageItem = altar.getStorageItem();
            if (!storageItem.isEmpty()) {
                return List.of(new ViewGroup<>(Collections.singletonList(storageItem.copy())));
            }
        }
        return null;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
