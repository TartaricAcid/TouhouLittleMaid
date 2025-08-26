package com.github.tartaricacid.touhoulittlemaid.entity.item.control;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IBroomControl;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PlayerBroomControl implements IBroomControl {
    private final EntityBroom broom;

    public PlayerBroomControl(EntityBroom broom) {
        this.broom = broom;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean inControl(Player player, @Nullable EntityMaid maid) {
        return true;
    }

    @Override
    public void travel(Player player, EntityMaid maid) {
        boolean keyForward = player.zza > 0;
        boolean keyBack = player.zza < 0;
        boolean keyLeft = player.xxa > 0;
        boolean keyRight = player.xxa < 0;
        boolean keySneak = player.isShiftKeyDown();
        boolean keyJump = IBroomControl.keyJump(player);

        Vec3 currentMotion = broom.getDeltaMovement();
        boolean hasInput = keyForward || keyBack || keyLeft || keyRight || keyJump || keySneak;

        if (hasInput) {
            float strafe = keyLeft ? 0.2f : (keyRight ? -0.2f : 0);
            float vertical = 0;

            // 垂直移动控制
            if (keyJump) {
                vertical = 0.25f;
            } else if (keySneak) {
                vertical = -0.2f;
            } else if (keyForward) {
                // 原有的俯仰控制
                vertical = -(player.getXRot() - 10) / 360f;
            }

            float forward = keyForward ? 0.375f : (keyBack ? -0.2f : 0);

            // 玩家基础速度是 0.1，速度二效果是 0.14，为了增加速度效果带来的增益，故这样计算
            double playerSpeed = player.getAttributeValue(Attributes.MOVEMENT_SPEED);
            double speed = Math.max(playerSpeed - 0.1, 0) * 2.5 + 0.1;
            Vec3 targetMotion = new Vec3(strafe, vertical, forward).scale(speed * 20);
            targetMotion = targetMotion.yRot((float) (-broom.getYRot() * Math.PI / 180.0));

            // 插值到目标速度，而不是直接累加
            Vec3 newMotion = currentMotion.lerp(targetMotion, 0.25f);
            broom.setDeltaMovement(newMotion);
        } else {
            // 没有输入时，快速减速
            broom.setDeltaMovement(currentMotion.scale(0.75));
        }
    }

    @Override
    public void tickRot(Player player, EntityMaid maid) {
        broom.yRotO = broom.yBodyRot = broom.yHeadRot = broom.getYRot();
        broom.setRot(player.getYRot(), player.getXRot());
    }
}
