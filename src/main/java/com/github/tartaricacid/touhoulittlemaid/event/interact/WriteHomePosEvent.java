package com.github.tartaricacid.touhoulittlemaid.event.interact;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class WriteHomePosEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        ItemStack itemstack = event.getStack();
        EntityPlayer player = event.getPlayer();
        AbstractEntityMaid maid = event.getMaid();
        World world = event.getWorld();

        if (itemstack.getItem() == MaidItems.KAPPA_COMPASS) {
            BlockPos pos = ItemKappaCompass.getPos(itemstack);
            if (pos != null) {
                if (player.isSneaking()) {
                    maid.setHomePos(BlockPos.ORIGIN);
                } else {
                    maid.setHomePos(pos);
                    if (!world.isRemote) {
                        // 如果尝试移动失败，那就尝试传送
                        if (maid.isSitting() || !maid.getNavigator().tryMoveToXYZ(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.6f)) {
                            maid.attemptTeleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                        }
                        player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.kappa_compass.write_success"));
                    }
                }
            } else if (!world.isRemote) {
                player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.kappa_compass.write_fail"));
            }
            event.setCanceled(true);
        }
    }
}
