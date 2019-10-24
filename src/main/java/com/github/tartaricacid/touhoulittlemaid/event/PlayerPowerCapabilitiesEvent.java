package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.CapabilityPowerHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerHandler;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SyncPowerMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author TartaricAcid
 * @date 2019/8/28 17:14
 **/
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class PlayerPowerCapabilitiesEvent {
    private static final ResourceLocation POWER_CAP = new ResourceLocation(TouhouLittleMaid.MOD_ID, "power");

    /**
     * 附加 Capability 属性
     */
    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(POWER_CAP, new CapabilityPowerHandler());
        }
    }

    /**
     * 玩家跨越维度或者死亡时的属性变化
     */
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        EntityPlayer player = event.getEntityPlayer();
        PowerHandler power = player.getCapability(CapabilityPowerHandler.POWER_CAP, null);
        PowerHandler oldPower = event.getOriginal().getCapability(CapabilityPowerHandler.POWER_CAP, null);
        if (power == null || oldPower == null) {
            return;
        }

        // 依据死亡或者切换维度进行不同的处理
        // 死亡 Power 减一
        if (event.isWasDeath()) {
            power.set((oldPower.get() > 1.0f) ? (oldPower.get() - 1.0f) : 0.0f);
        } else {
            power.set(oldPower.get());
        }
    }

    /**
     * 同步客户端服务端数据
     */
    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (event.side == Side.SERVER && event.phase == Phase.END && player.hasCapability(CapabilityPowerHandler.POWER_CAP, null)) {
            PowerHandler power = player.getCapability(CapabilityPowerHandler.POWER_CAP, null);
            if (power != null && power.isDirty()) {
                CommonProxy.INSTANCE.sendTo(new SyncPowerMessage(power.get()), (EntityPlayerMP) player);
                power.setDirty(false);
            }
        }
    }

    /**
     * 击杀生物时获取相关点数
     */
    @SubscribeEvent
    public static void killSuitableLiving(LivingDeathEvent event) {
        // TODO：杀死特定生物获取 Power
    }
}
