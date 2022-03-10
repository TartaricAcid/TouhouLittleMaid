package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SyncCapabilityMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class CapabilityEvent {
    private static final ResourceLocation POWER_CAP = new ResourceLocation(TouhouLittleMaid.MOD_ID, "power");
    private static final ResourceLocation MAID_NUM_CAP = new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid_num");

    @SubscribeEvent
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            event.addCapability(POWER_CAP, new PowerCapabilityProvider());
            event.addCapability(MAID_NUM_CAP, new MaidNumCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        LazyOptional<PowerCapability> oldPowerCap = getPowerCap(event.getOriginal());
        LazyOptional<PowerCapability> newPowerCap = getPowerCap(event.getPlayer());
        newPowerCap.ifPresent((newPower) -> oldPowerCap.ifPresent((oldPower) -> {
            if (event.isWasDeath()) {
                newPower.set(oldPower.get() - MiscConfig.PLAYER_DEATH_LOSS_POWER_POINT.get().floatValue());
            } else {
                newPower.set(oldPower.get());
            }
        }));

        LazyOptional<MaidNumCapability> oldMaidNumCap = getMaidNumCap(event.getOriginal());
        LazyOptional<MaidNumCapability> newMaidNumCap = getMaidNumCap(event.getPlayer());
        newMaidNumCap.ifPresent((newMaidNum) -> oldMaidNumCap.ifPresent((oldMaidNum) -> newMaidNum.set(oldMaidNum.get())));
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            getPowerCap(player).ifPresent(PowerCapability::markDirty);
            getMaidNumCap(player).ifPresent(MaidNumCapability::markDirty);
        }
    }

    /**
     * 同步客户端服务端数据
     */
    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END) {
            getPowerCap(player).ifPresent(power -> getMaidNumCap(player).ifPresent(maidNum -> {
                if (power.isDirty() || maidNum.isDirty()) {
                    NetworkHandler.sendToClientPlayer(new SyncCapabilityMessage(power.get(), maidNum.get()), player);
                    power.setDirty(false);
                    maidNum.setDirty(false);
                }
            }));
        }
    }

    private static LazyOptional<MaidNumCapability> getMaidNumCap(PlayerEntity player) {
        return player.getCapability(MaidNumCapabilityProvider.MAID_NUM_CAP);
    }

    private static LazyOptional<PowerCapability> getPowerCap(PlayerEntity player) {
        return player.getCapability(PowerCapabilityProvider.POWER_CAP);
    }
}
