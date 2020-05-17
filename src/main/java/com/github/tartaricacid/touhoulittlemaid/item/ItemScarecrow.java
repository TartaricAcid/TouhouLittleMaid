package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityScarecrow;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemScarecrow extends Item {
    public ItemScarecrow() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".scarecrow");
        setMaxStackSize(8);
        setCreativeTab(MaidItems.MAIN_TABS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing == EnumFacing.DOWN) {
            return EnumActionResult.FAIL;
        } else {
            boolean replaceable = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
            BlockPos posOffset = replaceable ? pos : pos.offset(facing);
            ItemStack stack = player.getHeldItemMainhand();
            if (!player.canPlayerEdit(posOffset, facing, stack)) {
                return EnumActionResult.FAIL;
            } else {
                return checkPosition(player, worldIn, pos, posOffset, stack);
            }
        }
    }

    private EnumActionResult checkPosition(EntityPlayer player, World worldIn, BlockPos pos, BlockPos posOffset, ItemStack stack) {
        BlockPos posUp = posOffset.up();
        boolean cannotPlace = !worldIn.isAirBlock(posOffset) && !worldIn.getBlockState(posOffset).getBlock().isReplaceable(worldIn, posOffset);
        cannotPlace = cannotPlace | (!worldIn.isAirBlock(posUp) && !worldIn.getBlockState(posUp).getBlock().isReplaceable(worldIn, posUp));
        if (cannotPlace) {
            return EnumActionResult.FAIL;
        } else {
            return checkAABBEntity(player, worldIn, pos, posOffset, stack);
        }
    }

    private EnumActionResult checkAABBEntity(EntityPlayer player, World worldIn, BlockPos pos, BlockPos posOffset, ItemStack stack) {
        double x = posOffset.getX();
        double y = posOffset.getY();
        double z = posOffset.getZ();
        List<Entity> entityList = worldIn.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(x, y, z, x + 1.0D, y + 2.0D, z + 1.0D));
        if (!entityList.isEmpty()) {
            return EnumActionResult.FAIL;
        } else {
            spawnScarecrowEntity(player, worldIn, pos, stack, x, y, z);
            return EnumActionResult.SUCCESS;
        }
    }

    private void spawnScarecrowEntity(EntityPlayer player, World worldIn, BlockPos pos, ItemStack stack, double x, double y, double z) {
        float yaw = (float) MathHelper.floor((MathHelper.wrapDegrees(player.rotationYaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;
        EntityScarecrow scarecrow = new EntityScarecrow(worldIn, x + 0.5, y, z + 0.5, yaw);
        if (stack.hasTagCompound()) {
            NBTTagCompound compound = stack.getTagCompound();
            if (compound.hasKey(EntityScarecrow.CUSTOM_NAME_TAG_NAME, Constants.NBT.TAG_STRING)) {
                scarecrow.setCustomNameTag(compound.getString(EntityScarecrow.CUSTOM_NAME_TAG_NAME));
            }
            if (compound.hasKey(EntityScarecrow.TEXT_TAG_NAME, Constants.NBT.TAG_STRING)) {
                scarecrow.setText(compound.getString(EntityScarecrow.TEXT_TAG_NAME));
            }
            if (compound.hasKey(EntityScarecrow.SPECIAL_TAG_NAME, Constants.NBT.TAG_BYTE)) {
                scarecrow.setSpecial(compound.getBoolean(EntityScarecrow.SPECIAL_TAG_NAME));
            }
        }
        if (!worldIn.isRemote) {
            worldIn.spawnEntity(scarecrow);
            worldIn.playSound(null, scarecrow.posX, scarecrow.posY, scarecrow.posZ, SoundEvents.ENTITY_ARMORSTAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
        }
        scarecrow.rotationYawHead = yaw;
        stack.shrink(1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltips.touhou_little_maid.chair.place.desc"));
        tooltip.add(I18n.format("tooltips.touhou_little_maid.chair.destroy.desc"));

        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null && !compound.isEmpty()) {
            if (compound.hasKey(EntityScarecrow.CUSTOM_NAME_TAG_NAME, Constants.NBT.TAG_STRING)) {
                tooltip.add(I18n.format("tooltips.touhou_little_maid.scarecrow.name",
                        compound.getString(EntityScarecrow.CUSTOM_NAME_TAG_NAME)));
            }
            if (compound.hasKey(EntityScarecrow.TEXT_TAG_NAME, Constants.NBT.TAG_STRING)) {
                tooltip.add(I18n.format("tooltips.touhou_little_maid.scarecrow.text",
                        compound.getString(EntityScarecrow.TEXT_TAG_NAME)));
            }
            if (compound.hasKey(EntityScarecrow.SPECIAL_TAG_NAME, Constants.NBT.TAG_BYTE)) {
                tooltip.add(I18n.format("tooltips.touhou_little_maid.scarecrow.special"));
            }
        }
    }
}
