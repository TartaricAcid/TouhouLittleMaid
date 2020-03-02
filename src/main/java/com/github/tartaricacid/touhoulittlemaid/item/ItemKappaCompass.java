package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.compass.GuiKappaCompass;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemKappaCompass extends Item {
    public static final int MAX_DISTANCE = 16;
    public static final int MAX_POINT_NUM = 9;
    private static final String POS_TAG_NAME = "CompassPosList";
    private static final String MODE_TAG_NAME = "CompassMode";

    public ItemKappaCompass() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + "." + "kappa_compass");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
    }

    @SuppressWarnings("all")
    public static void setMode(ItemStack stack, Mode mode) {
        NBTTagCompound compound = new NBTTagCompound();
        if (stack.hasTagCompound()) {
            compound = stack.getTagCompound();
        }
        compound.setInteger(MODE_TAG_NAME, mode.ordinal());
        stack.setTagCompound(compound);
    }

    @SuppressWarnings("all")
    public static Mode getMode(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(MODE_TAG_NAME, Constants.NBT.TAG_INT)) {
            return Mode.getModeByIndex(stack.getTagCompound().getInteger(MODE_TAG_NAME));
        }
        return Mode.NONE;
    }

    @SuppressWarnings("all")
    @Nonnull
    public static List<BlockPos> getPos(ItemStack stack) {
        List<BlockPos> blockPosList = Lists.newArrayList();
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(POS_TAG_NAME, Constants.NBT.TAG_LIST)) {
            NBTTagList tagList = stack.getTagCompound().getTagList(POS_TAG_NAME, Constants.NBT.TAG_COMPOUND);
            for (NBTBase compound : tagList) {
                blockPosList.add(NBTUtil.getPosFromTag((NBTTagCompound) compound));
            }
        }
        return blockPosList;
    }

    @SuppressWarnings("all")
    public static Result setPos(ItemStack stack, BlockPos posIn) {
        List<BlockPos> blockPosList = getPos(stack);
        if (blockPosList.size() >= MAX_POINT_NUM) {
            return Result.FULL;
        }
        if (blockPosList.contains(posIn)) {
            return Result.DUPLICATE;
        }
        if (blockPosList.size() > 0) {
            BlockPos lastPos = blockPosList.get(blockPosList.size() - 1);
            if (lastPos.distanceSq(posIn) > Math.pow(MAX_DISTANCE, 2)) {
                return Result.TOO_FAR;
            }
        }
        blockPosList.add(posIn);
        NBTTagCompound compound = new NBTTagCompound();
        if (stack.hasTagCompound()) {
            compound = stack.getTagCompound();
        }
        NBTTagList tagList = new NBTTagList();
        for (BlockPos pos : blockPosList) {
            tagList.appendTag(NBTUtil.createPosTag(pos));
        }
        compound.setTag(POS_TAG_NAME, tagList);
        stack.setTagCompound(compound);
        return Result.SUCCESS;
    }


    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        if (worldIn.isRemote && handIn == EnumHand.MAIN_HAND) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiKappaCompass(playerIn.getHeldItemMainhand()));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @SuppressWarnings("all")
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.getHeldItem(hand).getItem() == this) {
            ItemStack stack = player.getHeldItem(hand);
            if (player.isSneaking()) {
                removePos(stack);
            } else {
                Result result = setPosAndSendMessage(pos, facing, stack);
                if (!worldIn.isRemote && result != Result.SUCCESS) {
                    String message = String.format("message.touhou_little_maid.kappa_compass.usage.result.%s",
                            result.name().toLowerCase(Locale.US));
                    player.sendMessage(new TextComponentTranslation(message));
                }
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    private Result setPosAndSendMessage(BlockPos pos, EnumFacing facing, ItemStack stack) {
        Mode mode = getMode(stack);
        Result result;
        switch (mode) {
            default:
            case NONE:
                removePos(stack);
                result = setPos(stack, pos.offset(facing));
                setMode(stack, Mode.SINGLE_POINT);
                break;
            case SINGLE_POINT:
                removePos(stack);
                result = setPos(stack, pos.offset(facing));
                break;
            case MULTI_POINT_REENTRY:
            case MULTI_POINT_CLOSURE:
                result = setPos(stack, pos.offset(facing));
                break;
            case SET_RANGE:
                List<BlockPos> posList = getPos(stack);
                if (posList.size() >= 2) {
                    result = Result.FULL;
                } else {
                    result = setPos(stack, pos.offset(facing));
                }
        }
        return result;
    }

    @SuppressWarnings("all")
    private void removePos(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(POS_TAG_NAME, Constants.NBT.TAG_LIST)) {
            stack.getTagCompound().removeTag(POS_TAG_NAME);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String modeKey = String.format("compass.touhou_little_maid.mode.%s", getMode(stack).name().toLowerCase(Locale.US));
        tooltip.add(I18n.format("tooltips.touhou_little_maid.kappa_compass.usage.desc.1"));
        tooltip.add(I18n.format("tooltips.touhou_little_maid.kappa_compass.usage.desc.2"));
        tooltip.add(I18n.format("tooltips.touhou_little_maid.kappa_compass.mode",
                I18n.format(modeKey)));
    }

    public enum Mode {
        // 单点模式
        SINGLE_POINT,
        // 多点折返模式
        MULTI_POINT_REENTRY,
        // 多点闭合模式
        MULTI_POINT_CLOSURE,
        // 设置范围
        SET_RANGE,
        // 无模式
        NONE;

        public static Mode getModeByIndex(int index) {
            return values()[MathHelper.clamp(index, 0, values().length - 1)];
        }
    }

    public enum Result {
        // 成功
        SUCCESS,
        // 点重复
        DUPLICATE,
        // 距离上个点过远
        TOO_FAR,
        // 记录的点满了
        FULL
    }
}
