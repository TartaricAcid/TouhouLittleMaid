package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemAlbum;
import com.github.tartaricacid.touhoulittlemaid.item.ItemPhoto;
import com.github.tartaricacid.touhoulittlemaid.util.ItemFindUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

/**
 * @author TartaricAcid
 * @date 2019/8/19 13:18
 **/
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class PlayerPickupEvent {
    @SubscribeEvent
    public static void onPlayerPickup(EntityItemPickupEvent event) {
        if (event.getItem().getItem().getItem() instanceof ItemPhoto && event.getEntityPlayer().isEntityAlive() && event.getItem().isEntityAlive()) {
            PlayerInvWrapper playerInvWrapper = new PlayerInvWrapper(event.getEntityPlayer().inventory);
            ItemStack album = ItemFindUtil.getStack(playerInvWrapper,
                    (stack) -> stack.getItem() instanceof ItemAlbum && ItemAlbum.getAlbumPhotoNum(stack) < ItemAlbum.ALBUM_INV_SIZE);

            if (!album.isEmpty()) {
                EntityItem entityItem = event.getItem();
                ItemStack stackBefore = entityItem.getItem();
                ItemStackHandler itemStackHandler = ItemAlbum.getAlbumInv(album);
                ItemStack stackAfter = ItemHandlerHelper.insertItemStacked(itemStackHandler, stackBefore, false);

                if (stackAfter.isEmpty()) {
                    event.getEntityPlayer().onItemPickup(entityItem, stackBefore.getCount());
                    entityItem.setDead();
                    ItemAlbum.setAlbumInv(album, itemStackHandler);
                    event.setCanceled(true);
                }

                // 理论上此处判定应该不会执行，因为相片只允许单个堆叠
                // 但是以防万一呢？
                else if (stackAfter.getCount() != stackBefore.getCount()) {
                    event.getEntityPlayer().onItemPickup(entityItem, stackBefore.getCount() - stackAfter.getCount());
                    entityItem.setItem(stackAfter);
                    ItemAlbum.setAlbumInv(album, itemStackHandler);
                    event.setCanceled(true);
                }
            }
        }
    }
}
