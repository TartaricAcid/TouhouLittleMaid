package com.github.tartaricacid.touhoulittlemaid.init;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public class InitCapabilities {
    public static final EntityCapability<IItemHandler, @Nullable Direction> HAND_ITEM = EntityCapability.createSided(getResourceLocation("hand_item"), IItemHandler.class);
    public static final EntityCapability<IItemHandler, @Nullable Direction> ARMOR_ITEM = EntityCapability.createSided(getResourceLocation("armor_item"), IItemHandler.class);

    public static void registerGenericItemHandlers(RegisterCapabilitiesEvent event) {
        event.registerEntity(HAND_ITEM, InitEntities.MAID.get(), (o, direction) -> o.getHandsInvWrapper());
        event.registerEntity(ARMOR_ITEM, InitEntities.MAID.get(), (o, direction) -> o.getArmorInvWrapper());
    }
}
