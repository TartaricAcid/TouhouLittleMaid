package com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import immersive_melodies.client.animation.EntityModelAnimator;
import immersive_melodies.client.animation.accessors.ModelAccessor;

import java.util.Optional;

public class MaidArmsAndHeadAccessor implements ModelAccessor<EntityMaid> {
    private final EntityMaid maid;
    private final BedrockPart head;
    private final BedrockPart hat;
    private final BedrockPart leftArm;
    private final BedrockPart rightArm;

    private MaidArmsAndHeadAccessor(EntityMaid maid, BedrockPart head, BedrockPart hat, BedrockPart leftArm, BedrockPart rightArm) {
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

    public Optional<BedrockPart> getMaidHead() {
        return Optional.ofNullable(head);
    }

    public Optional<BedrockPart> getMaidHat() {
        return Optional.ofNullable(hat);
    }

    public Optional<BedrockPart> getMaidFlippedLeftArm() {
        return flipHands() ? getMaidRightArm() : getMaidLeftArm();
    }

    public Optional<BedrockPart> getMaidFlippedRightArm() {
        return flipHands() ? getMaidLeftArm() : getMaidRightArm();
    }

    public Optional<BedrockPart> getMaidLeftArm() {
        return Optional.ofNullable(leftArm);
    }

    public Optional<BedrockPart> getMaidRightArm() {
        return Optional.ofNullable(rightArm);
    }

    public float headYaw() {
        return getMaidHead().map(h -> h.yRot).orElse(0.0f);
    }

    public void headYaw(float yaw) {
        getMaidHead().ifPresent(h -> h.yRot = (flipHands() ? -yaw : yaw));
        getMaidHat().ifPresent(h -> h.yRot = (flipHands() ? -yaw : yaw));
    }

    public float headPitch() {
        return getMaidHead().map(h -> h.xRot).orElse(0.0f);
    }

    public void headPitch(float pitch) {
        getMaidHead().ifPresent(h -> h.xRot = pitch);
        getMaidHat().ifPresent(h -> h.xRot = pitch);
    }

    public float leftArmYaw() {
        return getMaidFlippedLeftArm().map(l -> l.yRot).orElse(0.0f);
    }

    public void leftArmYaw(float yaw) {
        getMaidFlippedLeftArm().ifPresent(l -> l.yRot = (flipHands() ? -yaw : yaw));
    }

    public float leftArmPitch() {
        return getMaidFlippedLeftArm().map(l -> l.xRot).orElse(0.0f);
    }

    public void leftArmPitch(float pitch) {
        getMaidFlippedLeftArm().ifPresent(l -> l.xRot = pitch);
    }

    public float leftArmRoll() {
        return getMaidFlippedLeftArm().map(l -> l.zRot).orElse(0.0f);
    }

    public void leftArmRoll(float roll) {
        getMaidFlippedLeftArm().ifPresent(l -> l.zRot = (flipHands() ? -roll : roll));
    }

    public float rightArmYaw() {
        return getMaidFlippedRightArm().map(r -> r.yRot).orElse(0.0f);
    }

    public void rightArmYaw(float yaw) {
        getMaidFlippedRightArm().ifPresent(r -> r.yRot = (flipHands() ? -yaw : yaw));
    }

    public float rightArmPitch() {
        return getMaidFlippedRightArm().map(r -> r.xRot).orElse(0.0f);
    }

    public void rightArmPitch(float pitch) {
        getMaidFlippedRightArm().ifPresent(r -> r.xRot = pitch);
    }

    public float rightArmRoll() {
        return getMaidFlippedRightArm().map(r -> r.zRot).orElse(0.0f);
    }

    public void rightArmRoll(float roll) {
        getMaidFlippedRightArm().ifPresent(r -> r.zRot = (flipHands() ? -roll : roll));
    }

    static void setAngles(EntityMaid maid, BedrockPart head, BedrockPart hat, BedrockPart leftArm, BedrockPart rightArm) {
        EntityModelAnimator.setAngles(new MaidArmsAndHeadAccessor(maid, head, hat, leftArm, rightArm));
    }
}
