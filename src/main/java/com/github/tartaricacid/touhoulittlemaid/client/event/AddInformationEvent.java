package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public final class AddInformationEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderTooltips(ItemTooltipEvent event) {
        if (BaubleManager.getBauble(event.getItemStack()) != null) {
            event.getToolTip().add(new StringTextComponent(" "));
            event.getToolTip().add(new TranslationTextComponent("tooltips.touhou_little_maid.bauble.desc"));
            event.getToolTip().add(new TranslationTextComponent("tooltips.touhou_little_maid.bauble.usage").withStyle(TextFormatting.GRAY));
        }
        if (BackpackManager.findBackpack(event.getItemStack()).isPresent()) {
            event.getToolTip().add(Component.literal(" "));
            event.getToolTip().add(Component.translatable("tooltips.touhou_little_maid.backpack.desc"));
            event.getToolTip().add(Component.translatable("tooltips.touhou_little_maid.backpack.usage").withStyle(ChatFormatting.GRAY));
        }
    }
}
