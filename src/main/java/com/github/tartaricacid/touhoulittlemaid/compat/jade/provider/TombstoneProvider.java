package com.github.tartaricacid.touhoulittlemaid.compat.jade.provider;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.*;

import java.util.List;

public enum TombstoneProvider implements IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {

    INSTANCE;

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "tombstone");

    @Override
    public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> list) {
        return ClientViewGroup.map(list, ItemView::new, null);
    }

    @Override
    public @Nullable List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
        if (accessor.getTarget() instanceof EntityTombstone tombstone) {
            List<ItemStack> list = Lists.newArrayList();
            ItemStackHandler items = tombstone.getItems();
            for (int i = 0; i < items.getSlots(); i++) {
                ItemStack stack = items.getStackInSlot(i);
                list.add(stack.copy());
            }
            return List.of(new ViewGroup<>(list));
        }
        return null;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
