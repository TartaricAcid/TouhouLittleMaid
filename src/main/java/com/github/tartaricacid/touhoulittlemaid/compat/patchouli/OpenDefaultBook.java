package com.github.tartaricacid.touhoulittlemaid.compat.patchouli;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.client.OpenPatchouliBookEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.patchouli.api.PatchouliAPI;

public final class OpenDefaultBook {
    @SubscribeEvent
    public void onPatchouliBookEvent(OpenPatchouliBookEvent event) {
        ResourceLocation uid = event.getTask().getUid();
        if (uid.getNamespace().equals(TouhouLittleMaid.MOD_ID)) {
            ResourceLocation location = new ResourceLocation(TouhouLittleMaid.MOD_ID, "memorizable_gensokyo");
            PatchouliAPI.get().openBookGUI(location);
        }
    }
}
