package com.github.tartaricacid.touhoulittlemaid.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ByteBufUtils {
    public static void writeStringSet(Set<String> set, FriendlyByteBuf buf) {
        buf.writeVarInt(set.size());
        for (String s : set) {
            buf.writeUtf(s);
        }
    }

    public static Set<String> readStringSet(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Set<String> set = Sets.newHashSet();
        for (int i = 0; i < size; i++) {
            set.add(buf.readUtf());
        }
        return set;
    }

    public static void writeStringList(List<String> list, FriendlyByteBuf buf) {
        buf.writeVarInt(list.size());
        for (String s : list) {
            buf.writeUtf(s);
        }
    }

    public static List<String> readStringList(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            list.add(buf.readUtf());
        }
        return list;
    }

    public static void writeObject2FloatOpenHashMap(Object2FloatOpenHashMap<String> map, FriendlyByteBuf buf) {
        buf.writeVarInt(map.size());
        map.forEach((key, value) -> {
            buf.writeUtf(key);
            buf.writeFloat(value);
        });
    }

    public static Object2FloatOpenHashMap<String> readObject2FloatOpenHashMap(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Object2FloatOpenHashMap<String> map = new Object2FloatOpenHashMap<>();
        for (int i = 0; i < size; i++) {
            map.put(buf.readUtf(), buf.readFloat());
        }
        return map;
    }

    public static void writeSites(Map<String, List<String>> sites, FriendlyByteBuf buf) {
        buf.writeVarInt(sites.size());
        sites.forEach((key, value) -> {
            buf.writeUtf(key);
            writeStringList(value, buf);
        });
    }

    public static Map<String, List<String>> readSites(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Map<String, List<String>> sites = Maps.newLinkedHashMap();
        for (int i = 0; i < size; i++) {
            sites.put(buf.readUtf(), readStringList(buf));
        }
        return sites;
    }
}
