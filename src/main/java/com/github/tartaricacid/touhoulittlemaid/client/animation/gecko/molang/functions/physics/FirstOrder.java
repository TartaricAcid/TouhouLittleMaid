package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.functions.physics;

public class FirstOrder implements IPhysics {
    private float input;
    private float response;
    private float lastSimulation = 0;

    public FirstOrder(float input, float response) {
        this.input = input;
        this.response = response;
    }

    @Override
    public void update(float timeStep) {
        lastSimulation = (1 - timeStep / response) * lastSimulation + timeStep / response * input;
    }

    @Override
    public void setArgs(float arg0, float arg1, float arg2, float arg3) {
        this.input = arg0;
        this.response = arg1;
    }

    @Override
    public float getValue() {
        return lastSimulation;
    }
}
