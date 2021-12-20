package com.github.tartaricacid.touhoulittlemaid.event;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.AltarCraftEvent;
import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumSerializer;
import com.github.tartaricacid.touhoulittlemaid.crafting.SpawnMaidRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class MaidCraftCheckEvent {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAltarCraft(AltarCraftEvent event) {
        if (event.getAltarRecipe() instanceof SpawnMaidRecipe) {
            MaidNumHandler num = event.getPlayer().getCapability(MaidNumSerializer.MAID_NUM_CAP, null);
            if (num != null && !num.canAdd()) {
                if (!event.getWorld().isRemote) {
                    event.getPlayer().sendMessage(new TextComponentTranslation("message.touhou_little_maid.owner_maid_num.can_not_add", num.get(), num.getMaxNum()));
                }
                event.setCanceled(true);
            }
        }
    }
}
