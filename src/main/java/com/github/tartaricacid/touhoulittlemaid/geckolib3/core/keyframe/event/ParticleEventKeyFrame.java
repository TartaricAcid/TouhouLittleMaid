package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.event;

public class ParticleEventKeyFrame extends EventKeyFrame<String> {
    public final String effect;
    public final String locator;
    public final String script;

    public ParticleEventKeyFrame(double startTick, String eventData, String effect, String locator, String script) {
        super(startTick, eventData);
        this.effect = effect;
        this.locator = locator;
        this.script = script;
    }
}