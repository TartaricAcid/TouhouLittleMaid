package com.github.tartaricacid.touhoulittlemaid.entity.ai.goal;

import net.minecraft.world.entity.Mob;

public class OpenGateGoal extends GateInteractGoal{
    private final boolean closeFenceGate;
    private int forgetTime;
    public OpenGateGoal(Mob pMob, boolean pCloseFenceGate) {
        super(pMob);
        this.mob = pMob;
        this.closeFenceGate = pCloseFenceGate;
    }

    public boolean canContinueToUse() {
        return this.closeFenceGate && this.forgetTime > 0 && super.canContinueToUse();
    }

    public void start() {
        this.forgetTime = 20;
        this.setOpen(true);
    }

    public void stop() {
        this.setOpen(false);
    }

    public void tick() {
        --this.forgetTime;
        super.tick();
    }
}
