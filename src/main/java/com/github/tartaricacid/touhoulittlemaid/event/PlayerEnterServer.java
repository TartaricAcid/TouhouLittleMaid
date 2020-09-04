package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.HasGuideHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.HasGuideSerializer;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SyncCustomSpellCardData;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

/**
 * @author TartaricAcid
 * @date 2019/12/1 16:51
 **/
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class PlayerEnterServer {
    @SubscribeEvent
    public static void onPlayerEnterServer(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            // syncSpellCard(player); 此功能被废弃
            giveGuideBook(player);
        }
    }

    private static void syncSpellCard(EntityPlayerMP player) {
        // 符卡数据同步
        TouhouLittleMaid.LOGGER.info("Sending custom spell data to {}", player.getDisplayNameString());
        CommonProxy.INSTANCE.sendTo(new SyncCustomSpellCardData(), player);
    }

    private static void giveGuideBook(EntityPlayerMP player) {
        HasGuideHandler hasGuide = player.getCapability(HasGuideSerializer.HAS_GUIDE_CAP, null);
        if (hasGuide != null && hasGuide.isFirst()) {
            // 给予手册
            Item item = Item.getByNameOrId("patchouli:guide_book");
            if (item != null && GeneralConfig.MISC_CONFIG.giveGuideBookFirst) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("patchouli:book", "touhou_little_maid:memorizable_gensokyo");
                ItemStack stack = new ItemStack(item);
                stack.setTagCompound(tag);
                EntityItem entityItem = new EntityItem(player.world, player.posX, player.posY, player.posZ, stack);
                player.world.spawnEntity(entityItem);
            }
            // 给予初始女仆一只
            if (GeneralConfig.MAID_CONFIG.giveInitMaid) {
                ItemStack stack = new ItemStack(MaidItems.SMART_SLAB);
                EntityItem entityItem = new EntityItem(player.world, player.posX, player.posY, player.posZ, stack);
                player.world.spawnEntity(entityItem);
            }
            hasGuide.setFirst(false);
        }
    }
}
