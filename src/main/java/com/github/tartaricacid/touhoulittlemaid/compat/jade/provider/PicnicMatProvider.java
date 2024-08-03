package com.github.tartaricacid.touhoulittlemaid.compat.jade.provider;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityPicnicMat;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.*;

import java.util.List;

public enum PicnicMatProvider implements IServerExtensionProvider<Object, ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
    INSTANCE;

    private static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "picnic_mat");

    @Override
    public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> list) {
        return ClientViewGroup.map(list, ItemView::new, null);
    }

    @Override
    public @Nullable List<ViewGroup<ItemStack>> getGroups(ServerPlayer player, ServerLevel world, Object target, boolean showDetails) {
        if (target instanceof TileEntityPicnicMat picnicMat) {
            if (world.getBlockEntity(picnicMat.getCenterPos()) instanceof TileEntityPicnicMat picnicMatCenter) {
                ItemStackHandler handler = picnicMatCenter.getHandler();
                List<ItemStack> list = Lists.newArrayList();
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    list.add(stack.copy());
                }
                return List.of(new ViewGroup<>(list));
            }
        }
        return null;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
