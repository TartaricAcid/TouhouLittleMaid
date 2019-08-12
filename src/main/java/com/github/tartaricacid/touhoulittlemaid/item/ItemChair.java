package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.item.ItemChair.NBT.*;

/**
 * @author TartaricAcid
 * @date 2019/8/8 11:36
 **/
public class ItemChair extends Item {
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:cushion";
    private static final float DEFAULT_MOUNTED_HEIGHT = 0f;
    private static final boolean DEFAULT_TAMEABLE_CAN_RIDE = true;

    public ItemChair() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".chair");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.TABS);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing == EnumFacing.UP) {
            ItemStack itemstack = player.getHeldItem(hand);
            float yaw = (float) MathHelper.floor((MathHelper.wrapDegrees(player.rotationYaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;
            EntityChair chair = new EntityChair(worldIn, pos.getX() + 0.5, pos.up().getY(), pos.getZ() + 0.5, yaw);
            chair.setModelId(getChairModelId(itemstack));
            chair.setMountedHeight(getMountedHeight(itemstack));
            chair.setTameableCanRide(isTameableCanRide(itemstack));
            // 应用命名
            if (itemstack.hasDisplayName()) {
                chair.setCustomNameTag(itemstack.getDisplayName());
            }
            // 物品消耗，实体生成
            player.getHeldItem(hand).shrink(1);
            if (!worldIn.isRemote) {
                worldIn.spawnEntity(chair);
            }
            chair.rotationYawHead = yaw;
            chair.playSound(SoundEvents.BLOCK_CLOTH_PLACE, 1.0f, 1.0f);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }


    public static String getChairModelId(ItemStack stack) {
        if (stack.getItem() == MaidItems.CHAIR && stack.hasTagCompound() && stack.getTagCompound().hasKey(MODEL_ID.getName())) {
            return stack.getTagCompound().getString(MODEL_ID.getName());
        }
        return DEFAULT_MODEL_ID;
    }


    private static ItemStack setChairModelId(ItemStack stack, String modelId) {
        if (stack.getItem() == MaidItems.CHAIR) {
            if (stack.hasTagCompound()) {
                stack.getTagCompound().setString(MODEL_ID.getName(), modelId);
            } else {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString(MODEL_ID.getName(), modelId);
                stack.setTagCompound(tag);
            }
        }
        return stack;
    }

    public static float getMountedHeight(ItemStack stack) {
        if (stack.getItem() == MaidItems.CHAIR && stack.hasTagCompound() && stack.getTagCompound().hasKey(MOUNTED_HEIGHT.getName())) {
            return stack.getTagCompound().getFloat(MOUNTED_HEIGHT.getName());
        }
        return DEFAULT_MOUNTED_HEIGHT;
    }

    private static ItemStack setMountedHeight(ItemStack stack, float height) {
        if (stack.getItem() == MaidItems.CHAIR) {
            if (stack.hasTagCompound()) {
                stack.getTagCompound().setFloat(MOUNTED_HEIGHT.getName(), height);
            } else {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setFloat(MOUNTED_HEIGHT.getName(), height);
                stack.setTagCompound(tag);
            }
        }
        return stack;
    }

    public static boolean isTameableCanRide(ItemStack stack) {
        if (stack.getItem() == MaidItems.CHAIR && stack.hasTagCompound() && stack.getTagCompound().hasKey(TAMEABLE_CAN_RIDE.getName())) {
            return stack.getTagCompound().getBoolean(TAMEABLE_CAN_RIDE.getName());
        }
        return DEFAULT_TAMEABLE_CAN_RIDE;
    }

    private static ItemStack setTameableCanRide(ItemStack stack, boolean canRide) {
        if (stack.getItem() == MaidItems.CHAIR) {
            if (stack.hasTagCompound()) {
                stack.getTagCompound().setBoolean(TAMEABLE_CAN_RIDE.getName(), canRide);
            } else {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setBoolean(TAMEABLE_CAN_RIDE.getName(), canRide);
                stack.setTagCompound(tag);
            }
        }
        return stack;
    }

    public static ItemStack setAllTagData(ItemStack stack, String modelId, float height, boolean canRide) {
        setChairModelId(stack, modelId);
        setMountedHeight(stack, height);
        setTameableCanRide(stack, canRide);
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (String key : ClientProxy.ID_CHAIR_INFO_MAP.keySet()) {
                float height = ClientProxy.ID_CHAIR_INFO_MAP.get(key).getMountedYOffset();
                boolean canRide = ClientProxy.ID_CHAIR_INFO_MAP.get(key).isTameableCanRide();
                items.add(setAllTagData(new ItemStack(this), key, height, canRide));
            }
        }
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT &&
                ClientProxy.ID_CHAIR_INFO_MAP.containsKey(getChairModelId(stack))) {
            String name = ClientProxy.ID_CHAIR_INFO_MAP.get(getChairModelId(stack)).getName();
            return ParseI18n.parse(name);
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltips.touhou_little_maid.chair.place.desc"));
        tooltip.add(I18n.format("tooltips.touhou_little_maid.chair.destroy.desc"));
        tooltip.add(I18n.format("tooltips.touhou_little_maid.chair.gui.desc"));
        // 调试模式，不加国际化
        if (flagIn.isAdvanced() && GuiScreen.isShiftKeyDown() && stack.hasTagCompound()) {
            tooltip.add(String.format("Model Id: %s", getChairModelId(stack)));
            tooltip.add(String.format("Mounted Height: %f", getMountedHeight(stack)));
            tooltip.add(String.format("Tameable Can Ride: %s", isTameableCanRide(stack)));
        }
    }

    enum NBT {
        // 模型 ID
        MODEL_ID("ModelId"),
        // 实体坐上去的高度
        MOUNTED_HEIGHT("MountedHeight"),
        // 女仆能坐上去么？
        TAMEABLE_CAN_RIDE("TameableCanRide");

        private String name;

        NBT(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
