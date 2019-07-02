package com.github.tartaricacid.touhoulittlemaid.api;

public class AttackValue {
    private float damage;
    private int knockBack;
    private int fireAspect;

    public AttackValue(float damage, int knockBack, int fireAspect) {
        this.damage = damage;
        this.knockBack = knockBack;
        this.fireAspect = fireAspect;
    }

    public float getDamage() {
        return damage;
    }

    public int getKnockback() {
        return knockBack;
    }

    public int getFireAspect() {
        return fireAspect;
    }
}
