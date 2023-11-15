/*
 * https://github.com/anlingyi/xechat-idea
 *
 *        Apache License
 *   Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package com.github.tartaricacid.touhoulittlemaid.api.game.gomoku;

/**
 * 棋子点位
 *
 * @author anlingyi
 * @date 2021/11/7 5:59 下午
 */
public class Point {
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

    @Override
    public String toString() {
        return (type == 1 ? "Black" : "White") + "[" + x + "," + y + ']';
    }
}
