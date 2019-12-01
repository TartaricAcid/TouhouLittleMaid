package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SetMaidSasimonoCRC32;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/8/14 10:42
 **/
public class ItemHataSasimono extends Item {
    public ItemHataSasimono() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".hata_sasimono");
        setMaxStackSize(16);
        setCreativeTab(MaidItems.MAIN_TABS);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (stack.getItem() == this && target instanceof EntityMaid && !((EntityMaid) target).hasSasimono()) {
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                Long crc32 = ClientProxy.HATA_NAME_MAP.keySet().stream().skip(itemRand.nextInt(ClientProxy.HATA_NAME_MAP.size())).findFirst().get();
                CommonProxy.INSTANCE.sendToServer(new SetMaidSasimonoCRC32(target.getUniqueID(), crc32, true));
            }
            stack.shrink(1);
            ((EntityMaid) target).setAttackTarget(null);
            target.setRevengeTarget(null);
            return true;
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltips.touhou_little_maid.hata_sasimono.desc"));
    }
}
