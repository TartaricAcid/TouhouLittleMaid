package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author TartaricAcid
 * @date 2019/9/2 19:44
 **/
public final class NBTUtils {
    private NBTUtils() {
    }

    public static NBTTagList writeAABBToTag(AxisAlignedBB aabb) {
        NBTTagList tagList = new NBTTagList();
        tagList.appendTag(new NBTTagDouble(aabb.minX));
        tagList.appendTag(new NBTTagDouble(aabb.minY));
        tagList.appendTag(new NBTTagDouble(aabb.minZ));
        tagList.appendTag(new NBTTagDouble(aabb.maxX));
        tagList.appendTag(new NBTTagDouble(aabb.maxY));
        tagList.appendTag(new NBTTagDouble(aabb.maxZ));
        return tagList;
    }

    public static AxisAlignedBB getAABBFromTag(NBTTagList tag) {
        return new AxisAlignedBB(tag.getDoubleAt(0), tag.getDoubleAt(1), tag.getDoubleAt(2),
                tag.getDoubleAt(3), tag.getDoubleAt(4), tag.getDoubleAt(5));
    }
}
