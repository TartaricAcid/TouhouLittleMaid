package com.github.tartaricacid.touhoulittlemaid.event.interact;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class WriteCompassPosEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        ItemStack itemstack = event.getStack();
        EntityPlayer player = event.getPlayer();
        World world = event.getWorld();

        if (itemstack.getItem() == MaidItems.KAPPA_COMPASS && event.getMaid() instanceof EntityMaid) {
            event.setCanceled(true);
            EntityMaid maid = (EntityMaid) event.getMaid();
            if (player.isSneaking()) {
                maid.setCompassMode(ItemKappaCompass.Mode.NONE);
                sendMessageFromServer(world, player, "message.touhou_little_maid.kappa_compass.usage.result.reset");
                return;
            }
            ItemKappaCompass.Mode mode = ItemKappaCompass.getMode(itemstack);
            List<BlockPos> posList = ItemKappaCompass.getPos(itemstack);
            if (maid.setCompassPosList(posList, mode)) {
                maid.setCompassMode(mode);
                maid.setCurrentIndex(0);
                maid.setDescending(false);
                BlockPos pos = posList.get(0);
                // 如果尝试移动失败，那就尝试传送
                if (maid.isSitting() || !maid.getNavigator().tryMoveToXYZ(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.6f)) {
                    maid.attemptTeleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                }
                sendMessageFromServer(world, player, "message.touhou_little_maid.kappa_compass.usage.result.success");
                player.playSound(MaidSoundEvent.COMPASS_POINT, 0.8f, 0.8f);
            } else {
                sendMessageFromServer(world, player, "message.touhou_little_maid.kappa_compass.usage.result.fail");
            }
        }
    }

    private static void sendMessageFromServer(World world, EntityPlayer player, String i18nKey) {
        if (!world.isRemote) {
            player.sendMessage(new TextComponentTranslation(i18nKey));
        }
    }
}
