package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public final class CapabilityEvent {
    private static final ResourceLocation POWER_CAP = new ResourceLocation(TouhouLittleMaid.MOD_ID, "power");

    @SubscribeEvent
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            event.addCapability(POWER_CAP, new PowerCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        LazyOptional<PowerCapability> oldPowerCap = event.getOriginal().getCapability(PowerCapabilityProvider.POWER_CAP);
        LazyOptional<PowerCapability> newPowerCap = event.getPlayer().getCapability(PowerCapabilityProvider.POWER_CAP);
        newPowerCap.ifPresent((newPower) -> oldPowerCap.ifPresent((oldPower) -> {
            if (event.isWasDeath()) {
                newPower.set(oldPower.get() - 1);
            } else {
                newPower.set(oldPower.get());
            }
        }));
    }
}
