package com.github.tartaricacid.touhoulittlemaid.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;

import javax.annotation.Nullable;
import java.util.Optional;

@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public final class NBTToJson {
    public static Optional<JsonElement> getJson(@Nullable INBT nbt) {
        JsonElement element = null;
        if (nbt instanceof CollectionNBT) {
            element = collection((CollectionNBT<?>) nbt);
        }
        if (nbt instanceof CompoundNBT) {
            element = compound((CompoundNBT) nbt);
        }
        if (nbt instanceof StringNBT) {
            element = string((StringNBT) nbt);
        }
        if (nbt instanceof NumberNBT) {
            element = number((NumberNBT) nbt);
        }
        return Optional.ofNullable(element);
    }

    private static JsonElement collection(CollectionNBT<?> nbt) {
        JsonArray array = new JsonArray();
        nbt.forEach(e -> getJson(e).ifPresent(array::add));
        return array;
    }

    private static JsonElement compound(CompoundNBT nbt) {
        JsonObject object = new JsonObject();
        nbt.getAllKeys().forEach(k -> getJson(nbt.get(k)).ifPresent(e -> object.add(k, e)));
        return object;
    }

    private static JsonElement string(StringNBT nbt) {
        return new JsonPrimitive(nbt.getAsString());
    }

    private static JsonElement number(NumberNBT nbt) {
        return new JsonPrimitive(nbt.getAsNumber());
    }
}
