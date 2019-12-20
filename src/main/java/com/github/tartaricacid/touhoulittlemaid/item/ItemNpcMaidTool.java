package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.entity.EntityCustomNpc;

/**
 * @author TartaricAcid
 * @date 2019/12/20 13:44
 **/
public class ItemNpcMaidTool extends Item {
    public ItemNpcMaidTool() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".npc_maid_tool");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
        if (CommonProxy.isNpcModLoad()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @SubscribeEvent
    @Optional.Method(modid = "customnpcs")
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Entity target = event.getTarget();
        EntityPlayer playerIn = event.getEntityPlayer();
        if (CommonProxy.isNpcModLoad() && target instanceof EntityCustomNpc && playerIn.getHeldItemMainhand().getItem() == MaidItems.NPC_MAID_TOOL) {
            EntityCustomNpc npc = (EntityCustomNpc) target;
            EntityLivingBase base = npc.modelData.getEntity(npc);
            if (base instanceof EntityMaid) {
                playerIn.openGui(TouhouLittleMaid.INSTANCE, MaidGuiHandler.OTHER_GUI.NPC_MAID_TOOL.getId(), playerIn.world,
                        npc.getEntityId(), 0, 0);
                event.setCanceled(true);
            } else {
                if (event.getWorld().isRemote) {
                    playerIn.sendMessage(new TextComponentTranslation("message.touhou_little_maid.npc_maid_tool.not_maid_model"));
                }
            }
        }
    }
}
