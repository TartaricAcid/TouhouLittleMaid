package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * WAB 手机：右键立刻在客户端开屏，并向逻辑服发第一个 contacts 请求。
 * 服务端分支只走 super，绝不碰任何客户端类（开屏全在 @OnlyIn 具名方法里）。
 * 物品本身保持 stacksTo(1) + isFoil，与独立手机格 / 定时情境消息那套语义完全一致。
 */
public class ItemWabPhone extends Item {
    public ItemWabPhone() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            if (!WabClientState.connected()) {
                player.displayClientMessage(Component.translatable("wab.phone.offline"), true);
                return InteractionResultHolder.pass(stack);
            }
            openPhone();
            return InteractionResultHolder.sidedSuccess(stack, true);
        }
        return super.use(level, player, hand);
    }

    /** 客户端开屏 + 发第一个请求；整段带 @OnlyIn，专用服务器类校验时会被整体剥掉。 */
    @OnlyIn(Dist.CLIENT)
    private static void openPhone() {
        String id = WabPhoneAction.nextId();
        net.minecraft.client.Minecraft.getInstance().setScreen(new WabPhoneScreen(id, "contacts", null));
        NetworkHandler.CHANNEL.sendToServer(new WabPhoneAction(id, "contacts", "{}"));
    }
}
