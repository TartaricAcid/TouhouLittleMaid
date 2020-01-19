package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

/**
 * @author TartaricAcid
 * @date 2020/1/17 22:20
 **/
public class ItemMaidBackpack extends Item {
    public ItemMaidBackpack() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".maid_backpack");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (EntityMaid.EnumBackPackLevel level : EntityMaid.EnumBackPackLevel.values()) {
                if (level == EntityMaid.EnumBackPackLevel.EMPTY) {
                    continue;
                }
                items.add(new ItemStack(this, 1, level.getLevel()));
            }
        }
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (target instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) target;
            EntityMaid.EnumBackPackLevel level = maid.getBackLevel();
            if (stack.getItem() == this && maid.getOwnerId() != null && maid.getOwnerId().equals(playerIn.getUniqueID())) {
                switch (level) {
                    case EMPTY:
                    case SMALL:
                    case MIDDLE:
                        EntityMaid.EnumBackPackLevel needLevel = EntityMaid.EnumBackPackLevel.getEnumLevelByNum(level.getLevel() + 1);
                        EntityMaid.EnumBackPackLevel itemLevel = EntityMaid.EnumBackPackLevel.getEnumLevelByNum(stack.getMetadata());
                        if (itemLevel == needLevel) {
                            maid.setBackpackLevel(EntityMaid.EnumBackPackLevel.getEnumLevelByNum(stack.getMetadata()));
                            stack.shrink(1);
                        } else {
                            if (playerIn.world.isRemote) {
                                TextComponentTranslation needLevelText = new TextComponentTranslation(String.format("message.touhou_little_maid.maid_backpack.%s",
                                        needLevel.name().toLowerCase(Locale.US)));
                                TextComponentTranslation itemLevelText = new TextComponentTranslation(String.format("message.touhou_little_maid.maid_backpack.%s",
                                        itemLevel.name().toLowerCase(Locale.US)));
                                playerIn.sendMessage(new TextComponentTranslation("message.touhou_little_maid.maid_backpack.level_not_match",
                                        needLevelText, itemLevelText));
                            }
                        }
                        break;
                    case BIG:
                    default:
                        if (playerIn.world.isRemote) {
                            playerIn.sendMessage(new TextComponentTranslation("message.touhou_little_maid.maid_backpack.level_is_biggest"));
                        }
                        break;

                }
                return true;
            }
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        EntityMaid.EnumBackPackLevel level = EntityMaid.EnumBackPackLevel.getEnumLevelByNum(stack.getMetadata());
        switch (level) {
            case SMALL:
            case MIDDLE:
            case BIG:
                tooltip.add(I18n.format(String.format("message.touhou_little_maid.maid_backpack.%s",
                        level.name().toLowerCase(Locale.US))));
                return;
            case EMPTY:
            default:
        }
    }
}
