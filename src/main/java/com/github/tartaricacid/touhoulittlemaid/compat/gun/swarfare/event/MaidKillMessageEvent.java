package com.github.tartaricacid.touhoulittlemaid.compat.gun.swarfare.event;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.api.event.PreKillEvent;
import com.atsuishio.superbwarfare.config.server.MiscConfig;
import com.atsuishio.superbwarfare.network.message.receive.PlayerGunKillMessage;
import com.atsuishio.superbwarfare.tools.DamageTypeTool;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

public class MaidKillMessageEvent {
    @SubscribeEvent
    @SuppressWarnings("all")
    public void onMaidKill(PreKillEvent.SendKillMessage event) {
        DamageSource source = event.getSource();
        EntityMaid attacker = null;
        if (source.getEntity() instanceof EntityMaid maid) {
            attacker = maid;
        }
        if (source.getDirectEntity() instanceof Projectile projectile && projectile.getOwner() instanceof EntityMaid maid) {
            attacker = maid;
        }
        if (attacker != null && MiscConfig.SEND_KILL_FEEDBACK.get()) {
            LivingEntity owner = attacker.getOwner();
            if (!(owner instanceof Player player)) {
                return;
            }
            LivingEntity target = event.getTarget();
            ResourceKey<DamageType> damageTypeResourceKey = source.typeHolder().unwrapKey().isPresent() ? source.typeHolder().unwrapKey().get() : DamageTypes.GENERIC;
            if (DamageTypeTool.isHeadshotDamage(source)) {
                PlayerGunKillMessage message = new PlayerGunKillMessage(player.getId(), target.getId(), true, damageTypeResourceKey);
                Mod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), message);
            } else {
                PlayerGunKillMessage message = new PlayerGunKillMessage(player.getId(), target.getId(), false, damageTypeResourceKey);
                Mod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), message);
            }
            event.setCanceled(true);
        }
    }
}
