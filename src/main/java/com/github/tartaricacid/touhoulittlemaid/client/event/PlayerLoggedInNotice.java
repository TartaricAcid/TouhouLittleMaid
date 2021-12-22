package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/10/20 17:59
 **/
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class PlayerLoggedInNotice {
    private static boolean notFirst = false;

    @SubscribeEvent
    public static void onEnterGame(PlayerEvent.PlayerLoggedInEvent event) {
        missingPatchouliNotice(event);
    }

    private static void missingPatchouliNotice(PlayerEvent.PlayerLoggedInEvent event) {
        boolean missingPatchouli = !Loader.isModLoaded("patchouli");
        if (notFirst) {
            return;
        }
        if (missingPatchouli) {
            String json = I18n.format("message.touhou_little_maid.missing_patchouli");
            try {
                ITextComponent component = ITextComponent.Serializer.fromJsonLenient(json);
                if (component != null) {
                    event.player.sendMessage(component);
                }
            } catch (JsonSyntaxException ignore) {
            }
        }
        notFirst = true;
    }
}
