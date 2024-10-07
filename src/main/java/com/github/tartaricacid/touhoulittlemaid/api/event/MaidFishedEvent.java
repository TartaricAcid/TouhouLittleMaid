package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nonnegative;
import java.util.List;

public class MaidFishedEvent extends Event {
    private final EntityMaid maid;
    private final NonNullList<ItemStack> drops = NonNullList.create();
    private final MaidFishingHook hook;
    private int rodDamage;

    public MaidFishedEvent(List<ItemStack> drops, int rodDamage, MaidFishingHook hook) {
        this.maid = hook.getMaidOwner();
        this.drops.addAll(drops);
        this.rodDamage = rodDamage;
        this.hook = hook;
    }

    public void damageRodBy(@Nonnegative int rodDamage) {
        this.rodDamage = rodDamage;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public NonNullList<ItemStack> getDrops() {
        return drops;
    }

    public MaidFishingHook getHook() {
        return hook;
    }

    public int getRodDamage() {
        return rodDamage;
    }
}
