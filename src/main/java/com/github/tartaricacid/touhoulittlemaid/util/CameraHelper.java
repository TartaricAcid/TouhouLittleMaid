package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.compat.sable.SableCompat;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Quaterniondc;
import org.joml.Quaternionf;

public class CameraHelper {
    public static Quaternionf getCameraOrientationForBlockEntity(BlockEntity blockEntity) {
        Minecraft mc = Minecraft.getInstance();

        var baseOrientation = mc.getEntityRenderDispatcher().cameraOrientation();
        if (!SableCompat.isSableLoaded()) {
            return baseOrientation;
        }

        final SubLevel subLevel = Sable.HELPER.getContaining(blockEntity);
        if (subLevel == null) {
            return baseOrientation;
        }

        final Quaterniondc subLevelOrientation = ((ClientSubLevel) subLevel).renderPose().orientation();
        var copy = new Quaternionf(subLevelOrientation).conjugate();
        return baseOrientation.premul(copy, copy);

    }
}
