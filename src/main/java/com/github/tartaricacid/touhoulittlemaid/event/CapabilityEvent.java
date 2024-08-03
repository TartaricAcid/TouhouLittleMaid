package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SyncCapabilityMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.neoforged.neoforge.event.AttachCapabilitiesEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public final class CapabilityEvent {
    private static final ResourceLocation POWER_CAP = new ResourceLocation(TouhouLittleMaid.MOD_ID, "power");
    private static final ResourceLocation MAID_NUM_CAP = new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid_num");

    @SubscribeEvent
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Player) {
            event.addCapability(POWER_CAP, new PowerCapabilityProvider());
            event.addCapability(MAID_NUM_CAP, new MaidNumCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player newPlayer = event.getEntity();
        original.reviveCaps();
        LazyOptional<PowerCapability> oldPowerCap = getPowerCap(original);
        LazyOptional<PowerCapability> newPowerCap = getPowerCap(newPlayer);
        newPowerCap.ifPresent((newPower) -> oldPowerCap.ifPresent((oldPower) -> {
            if (event.isWasDeath()) {
                newPower.set(oldPower.get() - MiscConfig.PLAYER_DEATH_LOSS_POWER_POINT.get().floatValue());
            } else {
                newPower.set(oldPower.get());
            }
        }));
        LazyOptional<MaidNumCapability> oldMaidNumCap = getMaidNumCap(original);
        LazyOptional<MaidNumCapability> newMaidNumCap = getMaidNumCap(newPlayer);
        newMaidNumCap.ifPresent((newMaidNum) -> oldMaidNumCap.ifPresent((oldMaidNum) -> newMaidNum.set(oldMaidNum.get())));
        original.invalidateCaps();
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            getPowerCap(player).ifPresent(PowerCapability::markDirty);
            getMaidNumCap(player).ifPresent(MaidNumCapability::markDirty);
        }
    }

    /**
     * 同步客户端服务端数据
     */
    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
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

    private static LazyOptional<MaidNumCapability> getMaidNumCap(Player player) {
        return player.getCapability(MaidNumCapabilityProvider.MAID_NUM_CAP);
    }

    private static LazyOptional<PowerCapability> getPowerCap(Player player) {
        return player.getCapability(PowerCapabilityProvider.POWER_CAP);
    }
}
