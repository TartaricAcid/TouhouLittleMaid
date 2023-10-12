package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe;

public class ParticleEventKeyFrame extends EventKeyFrame<String> {
    public final String effect;
    public final String locator;
    public final String script;

    public ParticleEventKeyFrame(Double startTick, String effect, String locator, String script) {
        super(startTick, effect + "\n" + locator + "\n" + script);
        this.script = script;
        this.locator = locator;
        this.effect = effect;
    }
}