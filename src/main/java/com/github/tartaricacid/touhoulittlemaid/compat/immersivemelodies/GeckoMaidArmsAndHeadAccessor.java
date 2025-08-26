package com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoBone;
import immersive_melodies.client.animation.accessors.ModelAccessor;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;
import java.util.Optional;

public class GeckoMaidArmsAndHeadAccessor implements ModelAccessor<Mob> {
    private final Mob maid;
    private final @Nullable AnimatedGeoBone head;
    private final @Nullable AnimatedGeoBone hat;
    private final @Nullable AnimatedGeoBone leftArm;
    private final @Nullable AnimatedGeoBone rightArm;

    GeckoMaidArmsAndHeadAccessor(Mob maid, @Nullable AnimatedGeoBone head, @Nullable AnimatedGeoBone hat,
                                 @Nullable AnimatedGeoBone leftArm, @Nullable AnimatedGeoBone rightArm) {
        this.maid = maid;
        this.head = head;
        this.hat = hat;
        this.leftArm = leftArm;
        this.rightArm = rightArm;
    }

    @Override
    public Mob getEntity() {
        return this.maid;
    }

    @Override
    public float headYaw() {
        return getMaidHead().map(AnimatedGeoBone::getRotationY).orElse(0.0f);
    }

    @Override
    public void headYaw(float yaw) {
        getMaidHead().ifPresent(bone -> bone.setRotationY(flipHands() ? yaw : -yaw));
        getMaidHat().ifPresent(bone -> bone.setRotationY(flipHands() ? yaw : -yaw));
    }

    @Override
    public float headPitch() {
        return getMaidHat().map(AnimatedGeoBone::getRotationX).orElse(0.0f);
    }

    @Override
    public void headPitch(float pitch) {
        getMaidHead().ifPresent(bone -> bone.setRotationX(-pitch));
        getMaidHat().ifPresent(bone -> bone.setRotationX(-pitch));
    }

    @Override
    public float leftArmYaw() {
        return getMaidFlippedLeftArm().map(AnimatedGeoBone::getRotationY).orElse(0.0f);
    }

    @Override
    public void leftArmYaw(float yaw) {
        getMaidFlippedLeftArm().ifPresent(bone -> bone.setRotationY(flipHands() ? yaw : -yaw));
    }

    @Override
    public float leftArmPitch() {
        return getMaidFlippedLeftArm().map(AnimatedGeoBone::getRotationX).orElse(0.0f);
    }

    @Override
    public void leftArmPitch(float pitch) {
        // GeckoLib 的模型和和默认的的模型是数值是相反的
        getMaidFlippedLeftArm().ifPresent(bone -> bone.setRotationX(-pitch));
    }

    @Override
    public float leftArmRoll() {
        return getMaidFlippedLeftArm().map(AnimatedGeoBone::getRotationZ).orElse(0.0f);
    }

    @Override
    public void leftArmRoll(float roll) {
        getMaidFlippedLeftArm().ifPresent(bone -> bone.setRotationZ(flipHands() ? roll : -roll));
    }

    @Override
    public float rightArmYaw() {
        return getMaidFlippedRightArm().map(AnimatedGeoBone::getRotationY).orElse(0.0f);
    }

    @Override
    public void rightArmYaw(float yaw) {
        getMaidFlippedRightArm().ifPresent(bone -> bone.setRotationY(flipHands() ? yaw : -yaw));
    }

    @Override
    public float rightArmPitch() {
        return getMaidFlippedRightArm().map(AnimatedGeoBone::getRotationX).orElse(0.0f);
    }

    @Override
    public void rightArmPitch(float pitch) {
        // GeckoLib 的模型和和默认的的模型是数值是相反的
        getMaidFlippedRightArm().ifPresent(bone -> bone.setRotationX(-pitch));
    }

    @Override
    public float rightArmRoll() {
        return getMaidFlippedRightArm().map(AnimatedGeoBone::getRotationZ).orElse(0.0f);
    }

    @Override
    public void rightArmRoll(float roll) {
        getMaidFlippedRightArm().ifPresent(bone -> bone.setRotationY(flipHands() ? -roll : roll));
    }

    private Optional<AnimatedGeoBone> getMaidHead() {
        return Optional.ofNullable(head);
    }

    private Optional<AnimatedGeoBone> getMaidHat() {
        return Optional.ofNullable(hat);
    }

    private Optional<AnimatedGeoBone> getMaidFlippedLeftArm() {
        return flipHands() ? getMaidRightArm() : getMaidLeftArm();
    }

    private Optional<AnimatedGeoBone> getMaidFlippedRightArm() {
        return flipHands() ? getMaidLeftArm() : getMaidRightArm();
    }

    private Optional<AnimatedGeoBone> getMaidLeftArm() {
        return Optional.ofNullable(leftArm);
    }

    private Optional<AnimatedGeoBone> getMaidRightArm() {
        return Optional.ofNullable(rightArm);
    }
}
