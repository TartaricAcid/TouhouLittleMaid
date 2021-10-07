package com.github.tartaricacid.touhoulittlemaid.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;

import javax.annotation.Nullable;
import java.util.Optional;

@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public final class NBTToJson {
    public static Optional<JsonElement> getJson(@Nullable INBT nbt) {
        JsonElement element = null;
        if (nbt != null) {
            element = Dynamic.convert(NBTDynamicOps.INSTANCE, JsonOps.INSTANCE, nbt);
        }
        return Optional.ofNullable(element);
    }
}
