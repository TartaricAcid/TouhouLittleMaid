package com.github.tartaricacid.touhoulittlemaid.compat.jade.provider;

import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidBeacon;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.network.chat.TranslatableComponent;

public enum ShrineLampProvider implements IComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlockEntity() instanceof TileEntityMaidBeacon lamp) {
            float storagePower = lamp.getStoragePower();
            float costPower = lamp.getEffectCost() * 900;
            iTooltip.add(new TranslatableComponent("tooltips.touhou_little_maid.maid_beacon.desc", ItemMaidBeacon.DECIMAL_FORMAT.format(storagePower)));
            if (lamp.getPotionIndex() == -1) {
                iTooltip.add(new TranslatableComponent("gui.touhou_little_maid.maid_beacon.cost_power", ItemMaidBeacon.DECIMAL_FORMAT.format(0)));
            } else {
                iTooltip.add(new TranslatableComponent("gui.touhou_little_maid.maid_beacon.cost_power", ItemMaidBeacon.DECIMAL_FORMAT.format(costPower)));
            }
        }
    }
}
