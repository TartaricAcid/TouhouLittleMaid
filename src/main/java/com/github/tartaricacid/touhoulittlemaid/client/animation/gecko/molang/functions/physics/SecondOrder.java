package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.functions.physics;

import net.minecraft.util.Mth;

/**
 * @author MicroCraft
 *
 * <a href="https://www.youtube.com/watch?v=KPoeNZZ6H4s">Giving Personality to Procedural Animations using Math</a>
 */
public class SecondOrder implements IPhysics {
    private float inputFunction = 0;
    private float lastSimulation = 0;
    private float lastSimulationDot = 0;
    private float arg0;
    private float arg1;
    private float arg2;
    private float arg3;

    public SecondOrder(float input, float frequency, float coefficient, float response) {
        this.arg0 = input;
        this.arg1 = Mth.clamp(frequency, 0, 5);
        this.arg2 = Mth.clamp(coefficient, 0, 1);
        this.arg3 = response;
    }

    @Override
    public void update(float timeStep) {
        float input = arg0;
        float frequency = Mth.clamp(arg1, 0, 5);
        float coefficient = Mth.clamp(arg2, 0, 1);
        float response = arg3;

        float k1 = coefficient / Mth.PI / frequency;
        float k2 = 1 / (2 * Mth.PI * frequency) / (2 * Mth.PI * frequency);
        float k3 = response * coefficient / 2 / Mth.PI / frequency;

        float inputFunctionDot = (input - this.inputFunction) / timeStep;
        this.inputFunction = input;

        float maxTimeStep = (float) Math.sqrt(4 * k2 + k1 * k1) - k1;
        int cycleTime = (int) Math.ceil(timeStep / maxTimeStep);
        timeStep = timeStep / cycleTime;

        var lastSimulationDot = this.lastSimulationDot;
        var lastSimulation = this.lastSimulation;
        for (; cycleTime > 0; cycleTime--) {
            lastSimulation = lastSimulation + timeStep * lastSimulationDot;
            lastSimulationDot = lastSimulationDot + timeStep * (k3 * inputFunctionDot + input - lastSimulation - k1 * lastSimulationDot) / k2;
        }
        this.lastSimulation = lastSimulation;
        this.lastSimulationDot = lastSimulationDot;
    }

    @Override
    public void setArgs(float arg0, float arg1, float arg2, float arg3) {
        this.arg0 = arg0;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }

    @Override
    public float getValue() {
        return lastSimulation;
    }
}
