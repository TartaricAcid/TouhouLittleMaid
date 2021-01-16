package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemTrumpet extends Item {
    public ItemTrumpet() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".trumpet");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer && timeLeft >= 20) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            List<EntityMaid> maids = worldIn.getEntities(EntityMaid.class, m -> {
                if (m != null) {
                    return player.getUniqueID().equals(m.getOwnerId());
                }
                return false;
            });

            for (EntityMaid m : maids) {
                // 将所有女仆的 HOME 模式设置为关闭
                m.setHomeModeEnable(false);
                // 传送到玩家身边
                m.attemptTeleport(player.posX + itemRand.nextInt(3) - 1, player.posY, player.posZ + itemRand.nextInt(3) - 1);
            }
            player.getCooldownTracker().setCooldown(this, 200);
        }
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getMaxItemUseDuration(ItemStack stack) {
        return 100;
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            playerIn.setActiveHand(handIn);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltips.touhou_little_maid.trumpet.desc.usage"));
        tooltip.add(I18n.format("tooltips.touhou_little_maid.trumpet.desc.note"));
    }
}
