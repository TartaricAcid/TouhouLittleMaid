package com.github.tartaricacid.touhoulittlemaid.util;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JSONUtils;

import java.util.Optional;

public final class EntityCraftingHelper {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final String TYPE_TAG = "type";
    private static final String NBT_TAG = "nbt";

    public static Pair<EntityType<?>, CompoundNBT> getEntityData(JsonObject json) {
        try {
            Optional<EntityType<?>> optional = EntityType.byString(JSONUtils.getAsString(json, TYPE_TAG));
            if (optional.isPresent()) {
                EntityType<?> type = optional.get();
                JsonElement element = json.get(NBT_TAG);
                CompoundNBT outputData = new CompoundNBT();
                if (element == null) {
                    return Pair.of(type, outputData);
                }
                if (element.isJsonObject()) {
                    outputData = JsonToNBT.parseTag(GSON.toJson(element));
                } else {
                    outputData = JsonToNBT.parseTag(JSONUtils.convertToString(element, NBT_TAG));
                }
                return Pair.of(type, outputData);
            }
            throw new JsonParseException("Entity Type Tag Not Found");
        } catch (CommandSyntaxException e) {
            throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
        }
    }
}
