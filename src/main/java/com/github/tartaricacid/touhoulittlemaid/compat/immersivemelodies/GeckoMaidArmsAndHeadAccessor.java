package com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoBone;
import immersive_melodies.client.animation.EntityModelAnimator;
import immersive_melodies.client.animation.accessors.ModelAccessor;

import java.util.Optional;

public class GeckoMaidArmsAndHeadAccessor implements ModelAccessor<EntityMaid> {
    private final EntityMaid maid;
    private final AnimatedGeoBone head;
    private final AnimatedGeoBone hat;
    private final AnimatedGeoBone leftArm;
    private final AnimatedGeoBone rightArm;

    private GeckoMaidArmsAndHeadAccessor(EntityMaid maid, AnimatedGeoBone head, AnimatedGeoBone hat, AnimatedGeoBone leftArm, AnimatedGeoBone rightArm) {
        this.maid = maid;
        this.head = head;
        this.hat = hat;
        this.leftArm = leftArm;
        this.rightArm = rightArm;
    }

    @Override
    public EntityMaid getEntity() {
        return this.maid;
    }

    public Optional<AnimatedGeoBone> getMaidHead() {
        return Optional.ofNullable(head);
    }

    public Optional<AnimatedGeoBone> getMaidHat() {
        return Optional.ofNullable(hat);
    }

    public Optional<AnimatedGeoBone> getMaidFlippedLeftArm() {
        return flipHands() ? getMaidRightArm() : getMaidLeftArm();
    }

    public Optional<AnimatedGeoBone> getMaidFlippedRightArm() {
        return flipHands() ? getMaidLeftArm() : getMaidRightArm();
    }

    public Optional<AnimatedGeoBone> getMaidLeftArm() {
        return Optional.ofNullable(leftArm);
    }

    public Optional<AnimatedGeoBone> getMaidRightArm() {
        return Optional.ofNullable(rightArm);
    }

    public float headYaw() {
        return getMaidHead().map(AnimatedGeoBone::getRotationY).orElse(0.0f);
    }

    public void headYaw(float yaw) {
        getMaidHead().ifPresent(h -> h.setRotationY(flipHands() ? yaw : -yaw));
        getMaidHat().ifPresent(h -> h.setRotationY(flipHands() ? yaw : -yaw));
    }

    public float headPitch() {
        return getMaidHat().map(AnimatedGeoBone::getRotationX).orElse(0.0f);
    }

    public void headPitch(float pitch) {
        getMaidHead().ifPresent(h -> h.setRotationX(-pitch));
        getMaidHat().ifPresent(h -> h.setRotationX(-pitch));
    }

    public float leftArmYaw() {
        return getMaidFlippedLeftArm().map(AnimatedGeoBone::getRotationY).orElse(0.0f);
    }

    public void leftArmYaw(float yaw) {
        getMaidFlippedLeftArm().ifPresent(l -> l.setRotationY(flipHands() ? yaw : -yaw));
    }

    public float leftArmPitch() {
        return getMaidFlippedLeftArm().map(AnimatedGeoBone::getRotationX).orElse(0.0f);
    }

    //为啥geckolib的和正常的模型是数值是相反的...
    //@link{ModelAccessor#leftArmYaw}
    public void leftArmPitch(float pitch) {
        getMaidFlippedLeftArm().ifPresent(l -> l.setRotationX(-pitch));
    }

    public float leftArmRoll() {
        return getMaidFlippedLeftArm().map(AnimatedGeoBone::getRotationZ).orElse(0.0f);
    }

    public void leftArmRoll(float roll) {
        getMaidFlippedLeftArm().ifPresent(l -> l.setRotationZ(flipHands() ? roll : -roll));
    }

    public float rightArmYaw() {
        return getMaidFlippedRightArm().map(AnimatedGeoBone::getRotationY).orElse(0.0f);
    }

    public void rightArmYaw(float yaw) {
        getMaidFlippedRightArm().ifPresent(r -> r.setRotationY(flipHands() ? yaw : -yaw));
    }

    public float rightArmPitch() {
        return getMaidFlippedRightArm().map(AnimatedGeoBone::getRotationX).orElse(0.0f);
    }

    public void rightArmPitch(float pitch) {
        getMaidFlippedRightArm().ifPresent(r -> r.setRotationX(-pitch));
    }

    public float rightArmRoll() {
        return getMaidFlippedRightArm().map(AnimatedGeoBone::getRotationZ).orElse(0.0f);
    }

    public void rightArmRoll(float roll) {
        getMaidFlippedRightArm().ifPresent(r -> r.setRotationY(flipHands() ? -roll : roll));
    }

    static void setAngles(EntityMaid maid, AnimatedGeoBone head, AnimatedGeoBone hat, AnimatedGeoBone leftArm, AnimatedGeoBone rightArm) {
        EntityModelAnimator.setAngles(new GeckoMaidArmsAndHeadAccessor(maid, head, hat, leftArm, rightArm));
    }
}
