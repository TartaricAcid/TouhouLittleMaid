package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * @author TartaricAcid
 * @date 2019/8/6 15:41
 **/
public class ItemPhoto extends Item {
    public ItemPhoto() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".photo");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.TABS);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP) {
            // TODO: 2019/8/7 检查当前位置是否适合释放女仆，给予提示 
            return EnumActionResult.PASS;
        }
        if (!worldIn.isRemote) {
            ItemStack photo = player.getHeldItem(hand);
            if (photo.getTagCompound() == null || photo.getTagCompound().getCompoundTag("MaidInfo").isEmpty()) {
                // TODO: 2019/8/7 给予 NBT 信息错误的提示
                return EnumActionResult.FAIL;
            }
            EntityMaid maid = new EntityMaid(worldIn);
            maid.readEntityFromNBT(photo.getTagCompound().getCompoundTag("MaidInfo"));
            maid.setPosition(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
            worldIn.spawnEntity(maid);
            maid.spawnExplosionParticle();
            photo.shrink(1);
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (flagIn.isAdvanced() && GuiScreen.isShiftKeyDown() && stack.hasTagCompound()) {
            tooltip.add(Objects.requireNonNull(stack.getTagCompound()).toString());
        }
    }
}
