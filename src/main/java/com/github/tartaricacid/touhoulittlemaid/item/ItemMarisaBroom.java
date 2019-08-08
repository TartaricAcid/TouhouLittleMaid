package com.github.tartaricacid.touhoulittlemaid.item;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        setMaxStackSize(1);
        setCreativeTab(MaidItems.TABS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.isSneaking() && facing == EnumFacing.UP) {
            ItemStack itemstack = player.getHeldItem(hand);
            EntityMarisaBroom broom = new EntityMarisaBroom(worldIn);
            broom.setPosition(pos.getX() + 0.5, pos.up().getY(), pos.getZ() + 0.5);
            // 应用命名
            if (itemstack.hasDisplayName()) {
                broom.setCustomNameTag(itemstack.getDisplayName());
            }
            // 物品消耗，实体生成
            player.getHeldItem(hand).shrink(1);
            if (!worldIn.isRemote) {
                worldIn.spawnEntity(broom);
            }
            broom.playSound(SoundEvents.BLOCK_CLOTH_PLACE, 1.0f, 1.0f);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
}
