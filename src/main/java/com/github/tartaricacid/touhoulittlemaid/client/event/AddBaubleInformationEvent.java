package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public final class AddBaubleInformationEvent {
    @SubscribeEvent
    public static void onRenderTooltips(ItemTooltipEvent event) {
        if (BaubleManager.getBauble(event.getItemStack()) != null) {
            event.getToolTip().add(StringTextComponent.EMPTY);
            event.getToolTip().add(new TranslationTextComponent("tooltips.touhou_little_maid.bauble.desc"));
            event.getToolTip().add(new TranslationTextComponent("tooltips.touhou_little_maid.bauble.usage").withStyle(TextFormatting.GRAY));
        }
    }
}
