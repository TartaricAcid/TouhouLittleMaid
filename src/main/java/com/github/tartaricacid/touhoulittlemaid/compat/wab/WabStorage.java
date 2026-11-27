package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * 女仆快照的序列化：完整 NBT（背包、AI、日程、爱憎数值）压缩后交给 WAB，
 * 由 WAB 存进自己的存档目录。游戏侧不保留副本，避免两套数据分叉。
 */
public final class WabStorage {
    private WabStorage() {
    }

    public static JsonObject snapshot(EntityMaid maid) {
        JsonObject out = new JsonObject();
        out.addProperty("id", maid.getUUID().toString());
        out.addProperty("name", WabBridge.cleanName(maid));
        out.addProperty("model", maid.getModelId());
        out.addProperty("world", maid.level().dimension().location().toString());
        out.addProperty("savedAt", System.currentTimeMillis());
        out.addProperty("health", WabSensors.round(maid.getHealth()));
        out.addProperty("food", WabSensors.round(maid.getHunger()));

        try {
            CompoundTag tag = new CompoundTag();
            maid.saveWithoutId(tag);
            ByteArrayOutputStream compressed = new ByteArrayOutputStream();
            NbtIo.writeCompressed(tag, compressed);
            out.addProperty("nbt", Base64.getEncoder().encodeToString(compressed.toByteArray()));
            out.addProperty("nbtFormat", "gzip+base64");
        } catch (Exception e) {
            out.addProperty("nbt", "");
            out.addProperty("nbtError", String.valueOf(e.getMessage()));
        }

        JsonArray inventory = new JsonArray();
        var inv = maid.getAvailableInv(true);
        for (int i = 0; i < inv.getSlots(); i++) {
            var stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            JsonObject item = WabSensors.item(stack);
            item.addProperty("slot", i);
            inventory.add(item);
        }
        out.add("inventory", inventory);
        out.add("loveLoathe", WabSensors.loveLoathe(maid));
        out.add("status", WabSensors.status(WabBridge.get(), maid));
        return out;
    }
}
