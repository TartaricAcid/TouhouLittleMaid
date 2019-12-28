package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author TartaricAcid
 * @date 2019/12/28 17:09
 **/
public class ItemSubstituteJizo extends Item {
    public ItemSubstituteJizo() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".substitute_jizo");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof AbstractEntityMaid) {
            AbstractEntityMaid maid = (AbstractEntityMaid) event.getTarget();
            ItemStack stack = event.getItemStack();
            if (maid.getOwnerId() != null && maid.getOwnerId().equals(event.getEntityPlayer().getUniqueID())
                    && stack.getItem() == MaidItems.SUBSTITUTE_JIZO && !maid.getIsInvulnerable()) {
                maid.setEntityInvulnerable(true);
                stack.shrink(1);
                event.setCanceled(true);
            }
        }
    }
}
