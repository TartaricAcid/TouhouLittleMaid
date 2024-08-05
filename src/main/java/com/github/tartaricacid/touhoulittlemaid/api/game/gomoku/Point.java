/*
 * https://github.com/anlingyi/xechat-idea
 *
 *        Apache License
 *   Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package com.github.tartaricacid.touhoulittlemaid.api.game.gomoku;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * 棋子点位
 *
 * @author anlingyi
 * @date 2021/11/7 5:59 下午
 */
public class Point {
    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    public static final Point NULL = new Point(-1, -1, 0);
    public static final StreamCodec<ByteBuf,Point> POINT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,Point::getX,
            ByteBufCodecs.INT,Point::getY,
            ByteBufCodecs.INT,Point::getType,
            Point::new
    );
    /**
     * 横坐标
     */
    public final int x;
    /**
     * 纵坐标
     */
    public final int y;
    /**
     * 棋子类型 1.黑 2.白
     */
    public int type;
    /**
     * 得分
     */
    public int score;

    public Point(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    public static CompoundTag toTag(Point point) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", point.x);
        tag.putInt("y", point.y);
        tag.putInt("type", point.type);
        return tag;
    }

    public static Point fromTag(CompoundTag tag) {
        int x = tag.getInt("x");
        int y = tag.getInt("y");
        int type = tag.getInt("type");
        return new Point(x, y, type);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (this == other) {
            return true;
        } else if (other instanceof Point otherPoint) {
            return otherPoint.x == this.x && otherPoint.y == this.y && otherPoint.type == this.type;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return (type == 1 ? "Black" : "White") + "[" + x + "," + y + ']';
    }
}
