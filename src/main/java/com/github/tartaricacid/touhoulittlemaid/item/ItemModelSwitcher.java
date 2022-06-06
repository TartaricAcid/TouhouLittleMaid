package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityModelSwitcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemModelSwitcher extends BlockItem {
    private static final String STORAGE_DATA_TAG = "StorageData";
    private static final String FORGE_DATA_TAG = "ForgeData";

    public ItemModelSwitcher() {
        super(InitBlocks.MODEL_SWITCHER.get(), (new Item.Properties()).stacksTo(1).tab(MAIN_TAB));
    }

    public static ItemStack tileEntityToItemStack(TileEntityModelSwitcher switcher) {
        ItemStack stack = InitItems.MODEL_SWITCHER.get().getDefaultInstance();
        CompoundTag stackTag = stack.getOrCreateTag();
        stackTag.put(STORAGE_DATA_TAG, switcher.saveWithoutMetadata());
        return stack;
    }

    public static void itemStackToTileEntity(ItemStack stack, TileEntityModelSwitcher switcher) {
        CompoundTag tag = stack.getOrCreateTagElement(STORAGE_DATA_TAG);
        if (tag.contains(FORGE_DATA_TAG, Tag.TAG_COMPOUND)) {
            switcher.load(tag);
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (pInteractionTarget instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) pInteractionTarget;
            CompoundTag tag = pStack.getOrCreateTagElement(STORAGE_DATA_TAG);
            CompoundTag forgeData;
            if (tag.contains(FORGE_DATA_TAG, Tag.TAG_COMPOUND)) {
                forgeData = tag.getCompound(FORGE_DATA_TAG);
            } else {
                forgeData = new CompoundTag();
            }
            forgeData.put(TileEntityModelSwitcher.ENTITY_UUID, NbtUtils.createUUID(maid.getUUID()));
            tag.put(FORGE_DATA_TAG, forgeData);
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }
}
