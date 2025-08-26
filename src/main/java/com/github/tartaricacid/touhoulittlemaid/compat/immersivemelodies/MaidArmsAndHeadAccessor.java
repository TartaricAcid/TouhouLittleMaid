package com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import immersive_melodies.client.animation.accessors.ModelAccessor;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;
import java.util.Optional;

public class MaidArmsAndHeadAccessor implements ModelAccessor<Mob> {
    private final Mob maid;
    private final @Nullable BedrockPart head;
    private final @Nullable BedrockPart hat;
    private final @Nullable BedrockPart leftArm;
    private final @Nullable BedrockPart rightArm;

    MaidArmsAndHeadAccessor(Mob maid, @Nullable BedrockPart head, @Nullable BedrockPart hat, @Nullable BedrockPart leftArm, @Nullable BedrockPart rightArm) {
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
        return getMaidHead().map(part -> part.yRot).orElse(0.0f);
    }

    @Override
    public void headYaw(float yaw) {
        getMaidHead().ifPresent(part -> part.yRot = (flipHands() ? -yaw : yaw));
        getMaidHat().ifPresent(part -> part.yRot = (flipHands() ? -yaw : yaw));
    }

    @Override
    public float headPitch() {
        return getMaidHead().map(part -> part.xRot).orElse(0.0f);
    }

    @Override
    public void headPitch(float pitch) {
        getMaidHead().ifPresent(part -> part.xRot = pitch);
        getMaidHat().ifPresent(part -> part.xRot = pitch);
    }

    @Override
    public float leftArmYaw() {
        return getMaidFlippedLeftArm().map(part -> part.yRot).orElse(0.0f);
    }

    @Override
    public void leftArmYaw(float yaw) {
        getMaidFlippedLeftArm().ifPresent(part -> part.yRot = (flipHands() ? -yaw : yaw));
    }

    @Override
    public float leftArmPitch() {
        return getMaidFlippedLeftArm().map(part -> part.xRot).orElse(0.0f);
    }

    @Override
    public void leftArmPitch(float pitch) {
        getMaidFlippedLeftArm().ifPresent(part -> part.xRot = pitch);
    }

    @Override
    public float leftArmRoll() {
        return getMaidFlippedLeftArm().map(part -> part.zRot).orElse(0.0f);
    }

    @Override
    public void leftArmRoll(float roll) {
        getMaidFlippedLeftArm().ifPresent(part -> part.zRot = (flipHands() ? -roll : roll));
    }

    @Override
    public float rightArmYaw() {
        return getMaidFlippedRightArm().map(part -> part.yRot).orElse(0.0f);
    }

    @Override
    public void rightArmYaw(float yaw) {
        getMaidFlippedRightArm().ifPresent(part -> part.yRot = (flipHands() ? -yaw : yaw));
    }

    @Override
    public float rightArmPitch() {
        return getMaidFlippedRightArm().map(part -> part.xRot).orElse(0.0f);
    }

    @Override
    public void rightArmPitch(float pitch) {
        getMaidFlippedRightArm().ifPresent(part -> part.xRot = pitch);
    }

    @Override
    public float rightArmRoll() {
        return getMaidFlippedRightArm().map(part -> part.zRot).orElse(0.0f);
    }

    @Override
    public void rightArmRoll(float roll) {
        getMaidFlippedRightArm().ifPresent(part -> part.zRot = (flipHands() ? -roll : roll));
    }

    private Optional<BedrockPart> getMaidHead() {
        return Optional.ofNullable(head);
    }

    private Optional<BedrockPart> getMaidHat() {
        return Optional.ofNullable(hat);
    }

    private Optional<BedrockPart> getMaidFlippedLeftArm() {
        return flipHands() ? getMaidRightArm() : getMaidLeftArm();
    }

    private Optional<BedrockPart> getMaidFlippedRightArm() {
        return flipHands() ? getMaidLeftArm() : getMaidRightArm();
    }

    private Optional<BedrockPart> getMaidLeftArm() {
        return Optional.ofNullable(leftArm);
    }

    private Optional<BedrockPart> getMaidRightArm() {
        return Optional.ofNullable(rightArm);
    }
}
