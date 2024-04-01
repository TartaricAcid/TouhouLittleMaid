package com.github.tartaricacid.touhoulittlemaid.entity.favorability;

public class Type {
    public static final Type BOOKSHELF = new Type("BookShelf", 2, 24000);
    public static final Type COMPUTER = new Type("Computer", 2, 24000);
    public static final Type KEYBOARD = new Type("Keyboard", 2, 24000);
    public static final Type GOMOKU = new Type("Gomoku", 2, 24000);
    public static final Type SLEEP = new Type("Sleep", 2, 24000);
    public static final Type GOMOKU_WIN = new Type("GomokuWin", 8, 12000);
    public static final Type WORK_MEAL = new Type("WorkMeal", 1, 2 * 60 * 20);
    public static final Type ON_HOME_MEAL = new Type("OnHomeMeal", 1, 24000);
    public static final Type HOME_MEAL = new Type("HomeMeal", 1, 60 * 20);
    public static final Type DEATH = new Type("Death", -2, 12000);

    private final String typeName;
    private final int point;
    private final int cooldown;
    private final boolean isReduce;

    public Type(String typeName, int point, int cooldown) {
        this.typeName = typeName;
        this.point = Math.abs(point);
        this.cooldown = cooldown;
        this.isReduce = point < 0;
        FavorabilityManager.TYPES.put(typeName, this);
    }

    public String getTypeName() {
        return typeName;
    }

    public int getPoint() {
        return point;
    }

    public int getCooldown() {
        return cooldown;
    }

    public boolean isReduce() {
        return isReduce;
    }
}
