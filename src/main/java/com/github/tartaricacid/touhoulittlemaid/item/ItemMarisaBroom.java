package com.github.tartaricacid.touhoulittlemaid.item;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/7/5 22:33
 **/
public class ItemMarisaBroom extends Item {
    public ItemMarisaBroom() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".marisa_broom");
        setRegistryName("marisa_broom");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.TABS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && player.isSneaking() && facing == EnumFacing.UP) {
            EntityMarisaBroom broom = new EntityMarisaBroom(worldIn);
            broom.setPosition(pos.getX() + 0.5, pos.up().getY(), pos.getZ() + 0.5);
            player.getHeldItem(hand).shrink(1);
            worldIn.spawnEntity(broom);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
}
