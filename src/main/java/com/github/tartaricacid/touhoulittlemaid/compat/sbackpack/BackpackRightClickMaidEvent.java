package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedcore.init.ModFluids;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.tank.TankUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.xppump.XpPumpUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.util.XpHelper;

public class BackpackRightClickMaidEvent {
    @SubscribeEvent
    public void onClickMaid(InteractMaidEvent event) {
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        ItemStack stack = event.getStack();
        if (!player.isShiftKeyDown()) {
            return;
        }
        if (!(stack.getItem() instanceof BackpackItem)) {
            return;
        }
        int maidXp = maid.getExperience();
        if (maidXp <= 0) {
            return;
        }
        stack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance()).ifPresent(backpack -> {
            UpgradeHandler handler = backpack.getUpgradeHandler();
            if (!handler.hasUpgrade(XpPumpUpgradeItem.TYPE) || !handler.hasUpgrade(TankUpgradeItem.TYPE)) {
                return;
            }
            backpack.getFluidHandler().ifPresent(fluid -> {
                int count = XpHelper.experienceToLiquid(maidXp);
                int filled = fluid.fill(ModFluids.EXPERIENCE_TAG, count, ModFluids.XP_STILL.get(), IFluidHandler.FluidAction.EXECUTE, true);
                if (filled > 0) {
                    maid.setExperience(maidXp - (int) XpHelper.liquidToExperience(filled));
                }
                event.setCanceled(true);
            });
        });
    }
}
