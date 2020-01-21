package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySuitcase;
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
 * @date 2020/1/21 14:08
 **/
public class ItemSuitcase extends Item {
    public ItemSuitcase() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".suitcase");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing == EnumFacing.UP && hand == EnumHand.MAIN_HAND) {
            EntitySuitcase suitcase = new EntitySuitcase(worldIn);
            suitcase.setPosition(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
            if (!worldIn.isRemote) {
                worldIn.spawnEntity(suitcase);
            }
            player.getHeldItemMainhand().shrink(1);
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
