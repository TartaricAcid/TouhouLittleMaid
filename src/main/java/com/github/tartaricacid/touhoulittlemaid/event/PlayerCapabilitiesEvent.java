package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.*;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SyncOwnerMaidNumMessage;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SyncPowerMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

import static com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig.MISC_CONFIG;

/**
 * @author TartaricAcid
 * @date 2019/8/28 17:14
 **/
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class PlayerCapabilitiesEvent {
    private static final ResourceLocation POWER_CAP = new ResourceLocation(TouhouLittleMaid.MOD_ID, "power");
    private static final ResourceLocation MAID_NUM_CAP = new ResourceLocation(TouhouLittleMaid.MOD_ID, "owner_maid_num");
    private static final ResourceLocation HAS_GUIDE_CAP = new ResourceLocation(TouhouLittleMaid.MOD_ID, "has_guide");

    /**
     * 附加 Capability 属性
     */
    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(POWER_CAP, new PowerSerializer());
            event.addCapability(MAID_NUM_CAP, new MaidNumSerializer());
            event.addCapability(HAS_GUIDE_CAP, new HasGuideSerializer());
        }
    }

    /**
     * 玩家从末地回到主世界，或者死亡时的属性变化
     */
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        EntityPlayer player = event.getEntityPlayer();

        PowerHandler power = player.getCapability(PowerSerializer.POWER_CAP, null);
        PowerHandler oldPower = event.getOriginal().getCapability(PowerSerializer.POWER_CAP, null);
        if (power != null && oldPower != null) {
            // 依据死亡或者切换维度进行不同的处理
            // 死亡 Power 减一
            if (event.isWasDeath()) {
                power.set(oldPower.get() - (float) MISC_CONFIG.playerDeathLossPowerPoint);
            } else {
                power.set(oldPower.get());
            }
        }

        MaidNumHandler num = player.getCapability(MaidNumSerializer.MAID_NUM_CAP, null);
        MaidNumHandler oldNum = event.getOriginal().getCapability(MaidNumSerializer.MAID_NUM_CAP, null);
        if (num != null && oldNum != null) {
            num.set(oldNum.get());
        }

        HasGuideHandler hasGuide = player.getCapability(HasGuideSerializer.HAS_GUIDE_CAP, null);
        HasGuideHandler oldHasGuide = event.getOriginal().getCapability(HasGuideSerializer.HAS_GUIDE_CAP, null);
        if (hasGuide != null && oldHasGuide != null) {
            hasGuide.setFirst(oldHasGuide.isFirst());
        }
    }

    /**
     * 其他跨越维度时候的更新提醒
     */
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();

            PowerHandler power = player.getCapability(PowerSerializer.POWER_CAP, null);
            if (power != null) {
                power.markDirty();
            }

            MaidNumHandler num = player.getCapability(MaidNumSerializer.MAID_NUM_CAP, null);
            if (num != null) {
                num.markDirty();
            }
        }
    }

    /**
     * 同步客户端服务端数据
     */
    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (event.side == Side.SERVER && event.phase == Phase.END) {
            if (player.hasCapability(PowerSerializer.POWER_CAP, null)) {
                PowerHandler power = player.getCapability(PowerSerializer.POWER_CAP, null);
                if (power != null && power.isDirty()) {
                    CommonProxy.INSTANCE.sendTo(new SyncPowerMessage(power.get()), (EntityPlayerMP) player);
                    power.setDirty(false);
                }
            }

            if (player.hasCapability(MaidNumSerializer.MAID_NUM_CAP, null)) {
                MaidNumHandler num = player.getCapability(MaidNumSerializer.MAID_NUM_CAP, null);
                if (num != null && num.isDirty()) {
                    CommonProxy.INSTANCE.sendTo(new SyncOwnerMaidNumMessage(num.get()), (EntityPlayerMP) player);
                    num.setDirty(false);
                }
            }
        }
    }
}
