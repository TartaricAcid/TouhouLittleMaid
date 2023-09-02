package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityModelSwitcher;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemModelSwitcher extends BlockItem {
    private static final String STORAGE_DATA_TAG = "StorageData";
    private static final String FORGE_DATA_TAG = "ForgeData";

    public ItemModelSwitcher() {
        super(InitBlocks.MODEL_SWITCHER.get(), (new Item.Properties()).stacksTo(1).tab(MAIN_TAB));
    }

    public static ItemStack tileEntityToItemStack(TileEntityModelSwitcher switcher) {
        ItemStack stack = InitItems.MODEL_SWITCHER.get().getDefaultInstance();
        CompoundNBT stackTag = stack.getOrCreateTag();
        stackTag.put(STORAGE_DATA_TAG, switcher.save(new CompoundNBT()));
        return stack;
    }

    public static void itemStackToTileEntity(ItemStack stack, TileEntityModelSwitcher switcher) {
        CompoundNBT tag = stack.getOrCreateTagElement(STORAGE_DATA_TAG);
        if (tag.contains(FORGE_DATA_TAG, Constants.NBT.TAG_COMPOUND)) {
            switcher.load(switcher.getBlockState(), tag);
        }
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack pStack, PlayerEntity pPlayer, LivingEntity pInteractionTarget, Hand pUsedHand) {
        if (pInteractionTarget instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) pInteractionTarget;
            CompoundNBT tag = pStack.getOrCreateTagElement(STORAGE_DATA_TAG);
            CompoundNBT forgeData;
            if (tag.contains(FORGE_DATA_TAG, Constants.NBT.TAG_COMPOUND)) {
                forgeData = tag.getCompound(FORGE_DATA_TAG);
            } else {
                forgeData = new CompoundNBT();
            }
            forgeData.put(TileEntityModelSwitcher.ENTITY_UUID, NBTUtil.createUUID(maid.getUUID()));
            tag.put(FORGE_DATA_TAG, forgeData);
            return ActionResultType.SUCCESS;
        }
        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }

    private boolean hasMaidInfo(ItemStack stack) {
        CompoundNBT tag = stack.getTagElement(STORAGE_DATA_TAG);
        if (tag != null && tag.contains(FORGE_DATA_TAG, Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT forgeTag = tag.getCompound(FORGE_DATA_TAG);
            return forgeTag.contains(TileEntityModelSwitcher.ENTITY_UUID, Constants.NBT.TAG_INT_ARRAY);
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag) {
        if (hasMaidInfo(pStack)) {
            pTooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.model_switcher.bounded").withStyle(TextFormatting.GRAY));
        } else {
            pTooltip.add(new TranslationTextComponent("gui.touhou_little_maid.model_switcher.uuid.empty").withStyle(TextFormatting.DARK_RED));
        }
    }
}