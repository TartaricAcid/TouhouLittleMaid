package com.github.tartaricacid.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.compat.cloth.MenuIntegration;
import com.github.tartaricacid.touhoulittlemaid.compat.iris.IrisCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.sodium.SodiumCompat;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment;
import com.github.tartaricacid.touhoulittlemaid.init.registry.CompatRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = TouhouLittleMaid.MOD_ID, dist = Dist.CLIENT)
public class TouhouLittleMaidClient {
    public TouhouLittleMaidClient(IEventBus modEventBus, ModContainer modContainer) {
        this.registerClientOnly();
        this.registerConfigMenu(modContainer);
    }

    private void registerClientOnly() {
        // 这个仅用于客户端，所以不需要在服务端注册
        InitDataAttachment.ATTACHMENT_TYPES.register("gecko_maid", () -> GeckoMaidEntity.TYPE);

        // 模组兼容
        SodiumCompat.init();
        IrisCompat.init();
    }

    private void registerConfigMenu(ModContainer modContainer) {
        ModFileInfo clothConfigInfo = LoadingModList.get().getModFileById(CompatRegistry.CLOTH_CONFIG);
        if (clothConfigInfo != null) {
            MenuIntegration.registerModsPage(modContainer);
        } else {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
    }
}
