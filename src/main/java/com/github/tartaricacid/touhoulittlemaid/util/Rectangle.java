package com.github.tartaricacid.touhoulittlemaid.util;

public final class Rectangle {
    public double x;
    public double y;
    public double w;
    public double h;

    public Rectangle(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void set(Rectangle rectangle) {
        this.x = rectangle.x;
        this.y = rectangle.y;
        this.w = rectangle.w;
        this.h = rectangle.h;
    }

    public double right() {
        return this.x + this.w;
    }

    public double bottom() {
        return this.y + this.h;
    }

    public boolean contains(double x, double y) {
        return this.x <= x && x <= this.right() && this.y <= y && y <= this.bottom();
    }
}
