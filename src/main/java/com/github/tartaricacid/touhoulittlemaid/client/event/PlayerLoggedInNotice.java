package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.registry.CompatRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.StringUtils;

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
            MutableComponent title = new TranslatableComponent("message.touhou_little_maid.missing_patchouli.title")
                    .withStyle(style -> style.withColor(ChatFormatting.GREEN).withBold(true));
            ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, I18n.get("message.touhou_little_maid.missing_patchouli.url"));
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("message.touhou_little_maid.missing_patchouli.url"));
            MutableComponent base = new TranslatableComponent("message.touhou_little_maid.missing_patchouli.click_here")
                    .withStyle(style -> style.withColor(ChatFormatting.GOLD).withBold(false).withUnderlined(true).withClickEvent(clickEvent).withHoverEvent(hoverEvent));
            event.getEntity().sendMessage(title.append(StringUtils.SPACE).append(base), Util.NIL_UUID);
        }
        notFirst = true;
    }
}
