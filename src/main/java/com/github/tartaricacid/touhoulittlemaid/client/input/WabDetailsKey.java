package com.github.tartaricacid.touhoulittlemaid.client.input;

import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabClientState;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabDetailsRequest;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

/**
 * 「女仆详细信息」键：准星对着自家女仆、或 8 格内最近的一只，向服务器要全量数值，
 * 回包后打开分页屏（动作栏那行太挤，数值全家桶放这里）。没连 WAB 时这个键什么都不做。
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class WabDetailsKey {
    public static final KeyMapping DETAILS_KEY = new KeyMapping("key.touhou_little_maid.wab_details.desc",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "key.category.touhou_little_maid");

    @SubscribeEvent
    public static void onPress(InputEvent.Key event) {
        if (!DETAILS_KEY.matches(event.getKey(), event.getScanCode())
                || !DETAILS_KEY.getKeyModifier().equals(KeyModifier.getActiveModifier())) {
            return;
        }
        if (event.getAction() != GLFW.GLFW_PRESS) return;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator() || !isInGame()) return;
        if (!WabClientState.connected()) return;
        DETAILS_KEY.consumeClick();
        EntityMaid maid = pickMaid(player);
        if (maid == null) {
            player.displayClientMessage(Component.translatable("wab.details.error.no_maid")
                    .withStyle(ChatFormatting.GRAY), true);
            return;
        }
        PacketDistributor.sendToServer(new WabDetailsRequest(maid.getUUID()));
    }

    /** 准星正对优先，其次 8 格内最近的自家女仆。 */
    @Nullable
    private static EntityMaid pickMaid(LocalPlayer player) {
        HitResult hit = player.pick(6, 1.0F, false);
        if (hit instanceof EntityHitResult entityHit
                && entityHit.getEntity() instanceof EntityMaid aimed
                && aimed.isOwnedBy(player)) {
            return aimed;
        }
        List<EntityMaid> maids = player.level().getEntitiesOfClass(EntityMaid.class,
                player.getBoundingBox().inflate(8), maid -> maid.isOwnedBy(player) && maid.isAlive());
        maids.sort(Comparator.comparingDouble(maid -> maid.distanceToSqr(player)));
        return maids.isEmpty() ? null : maids.get(0);
    }

    private static boolean isInGame() {
        Minecraft mc = Minecraft.getInstance();
        return mc.getOverlay() == null && mc.screen == null
                && mc.mouseHandler.isMouseGrabbed() && mc.isWindowActive();
    }
}
