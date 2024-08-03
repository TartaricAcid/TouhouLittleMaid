package com.github.tartaricacid.touhoulittlemaid.compat.carryon;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.fml.InterModComms;

public class BlackList {
    private static final String CARRY_ON_ID = "carryon";

    public static void addBlackList() {
        BuiltInRegistries.BLOCK.keySet().stream().filter(id -> id.getNamespace().equals(TouhouLittleMaid.MOD_ID))
                .forEach(id -> InterModComms.sendTo(CARRY_ON_ID, "blacklistBlock", id::toString));
        InterModComms.sendTo(CARRY_ON_ID, "blacklistEntity", () -> TouhouLittleMaid.MOD_ID + ":tombstone");
        InterModComms.sendTo(CARRY_ON_ID, "blacklistEntity", () -> TouhouLittleMaid.MOD_ID + ":sit");
        InterModComms.sendTo(CARRY_ON_ID, "blacklistEntity", () -> TouhouLittleMaid.MOD_ID + ":broom");
    }
}
