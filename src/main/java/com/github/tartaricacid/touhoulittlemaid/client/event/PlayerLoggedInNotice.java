package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.registry.CompatRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public class PlayerLoggedInNotice {
    private static boolean notFirst = false;

    @SubscribeEvent
    public static void onEnterGame(PlayerEvent.PlayerLoggedInEvent event) {
        boolean missingPatchouli = !ModList.get().isLoaded(CompatRegistry.PATCHOULI);
        if (notFirst) {
            return;
        }
        if (missingPatchouli) {
            MutableComponent title = Component.translatable("message.touhou_little_maid.missing_patchouli.title")
                    .withStyle(style -> style.withColor(ChatFormatting.GREEN).withBold(true));
            ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, I18n.get("message.touhou_little_maid.missing_patchouli.url"));
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("message.touhou_little_maid.missing_patchouli.url"));
            MutableComponent base = Component.translatable("message.touhou_little_maid.missing_patchouli.click_here")
                    .withStyle(style -> style.withColor(ChatFormatting.GOLD).withBold(false).withUnderlined(true).withClickEvent(clickEvent).withHoverEvent(hoverEvent));
            event.getEntity().sendSystemMessage(title.append(CommonComponents.SPACE).append(base));
        }
        notFirst = true;
    }
}
