package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityModelSwitcher;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.STORAGE_DATA_TAG;

public class ItemModelSwitcher extends BlockItem {
    private static final String NEO_FORGE_DATA_TAG = "NeoForgeData";

    public ItemModelSwitcher() {
        super(InitBlocks.MODEL_SWITCHER.get(), (new Item.Properties()).stacksTo(1));
    }

    public static ItemStack tileEntityToItemStack(HolderLookup.Provider provider, TileEntityModelSwitcher switcher) {
        ItemStack itemStack = Objects.requireNonNull(InitItems.MODEL_SWITCHER.get().getDefaultInstance());
        itemStack.set(STORAGE_DATA_TAG, switcher.saveWithoutMetadata(provider));
        return itemStack;
    }

    public static void itemStackToTileEntity(HolderLookup.Provider provider, ItemStack stack, TileEntityModelSwitcher switcher) {
        CompoundTag tag = stack.get(STORAGE_DATA_TAG);
        if (tag != null && tag.contains(NEO_FORGE_DATA_TAG, Tag.TAG_COMPOUND)) {
            switcher.loadAdditional(tag, provider);
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (pInteractionTarget instanceof EntityMaid maid) {
            CompoundTag tag = stack.getOrDefault(STORAGE_DATA_TAG, new CompoundTag());
            CompoundTag forgeData;
            if (tag.contains(NEO_FORGE_DATA_TAG, Tag.TAG_COMPOUND)) {
                forgeData = tag.getCompound(NEO_FORGE_DATA_TAG);
            } else {
                forgeData = new CompoundTag();
            }
            forgeData.put(TileEntityModelSwitcher.ENTITY_UUID, NbtUtils.createUUID(maid.getUUID()));
            tag.put(NEO_FORGE_DATA_TAG, forgeData);
            stack.set(STORAGE_DATA_TAG, tag);
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, pPlayer, pInteractionTarget, pUsedHand);
    }

    private boolean hasMaidInfo(ItemStack stack) {
        CompoundTag tag = stack.get(STORAGE_DATA_TAG);
        if (tag != null && tag.contains(NEO_FORGE_DATA_TAG, Tag.TAG_COMPOUND)) {
            CompoundTag forgeTag = tag.getCompound(NEO_FORGE_DATA_TAG);
            return forgeTag.contains(TileEntityModelSwitcher.ENTITY_UUID, Tag.TAG_INT_ARRAY);
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Item.TooltipContext pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        if (hasMaidInfo(pStack)) {
            pTooltip.add(Component.translatable("tooltips.touhou_little_maid.model_switcher.bounded").withStyle(ChatFormatting.GRAY));
        } else {
            pTooltip.add(Component.translatable("gui.touhou_little_maid.model_switcher.uuid.empty").withStyle(ChatFormatting.DARK_RED));
        }
    }
}
