package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.config.WabRules;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

/**
 * F09：筷子可作燃料，燃烧时长为木棍的两倍。
 *
 * <p>1.21.1 NeoForge 与 1.20.1 Forge 都有 {@code FurnaceFuelBurnTimeEvent}，
 * 且 {@code getItemStack()/setBurnTime()} 签名一致，只有包名不同，
 * 因此本文件是双线唯一差异；判定与数值放共用的 {@link WabRules}。
 *
 * <p>餐具盛过一口后带着那一口食物，不该当柴烧掉 —— 用双线共有的
 * {@link WabFeeding#read(net.minecraft.world.item.ItemStack)} 判空。
 */
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class WabFuelEvents {
    private WabFuelEvents() {
    }

    @SubscribeEvent
    public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        if (!event.getItemStack().is(InitItems.CHOPSTICKS.get())) return;
        int ticks = WabRules.chopstickBurnTicks(WabFeeding.read(event.getItemStack()) != null);
        if (ticks > 0) event.setBurnTime(ticks);
    }
}
