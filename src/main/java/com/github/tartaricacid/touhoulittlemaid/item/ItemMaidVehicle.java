package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMaidVehicle;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

/**
 * @author TartaricAcid
 * @date 2020/2/3 21:05
 **/
public class ItemMaidVehicle extends Item {
    public ItemMaidVehicle() {
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (EntityMaidVehicle.Type type : EntityMaidVehicle.Type.values()) {
                items.add(new ItemStack(this, 1, type.ordinal()));
            }
        }
    }

    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        String key = String.format("item.touhou_little_maid.%s.name", EntityMaidVehicle.Type.getTypeByNum(stack.getMetadata()).name().toLowerCase(Locale.US));
        return net.minecraft.util.text.translation.I18n.translateToLocal(key).trim();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing == EnumFacing.UP && hand == EnumHand.MAIN_HAND) {
            EntityMaidVehicle vehicle = new EntityMaidVehicle(worldIn);
            vehicle.setPosition(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
            vehicle.setModelId(player.getHeldItemMainhand().getMetadata());
            if (!worldIn.isRemote) {
                worldIn.spawnEntity(vehicle);
            }
            player.getHeldItemMainhand().shrink(1);
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltips.touhou_little_maid.chair.place.desc"));
        tooltip.add(I18n.format("tooltips.touhou_little_maid.chair.destroy.desc"));
    }
}
