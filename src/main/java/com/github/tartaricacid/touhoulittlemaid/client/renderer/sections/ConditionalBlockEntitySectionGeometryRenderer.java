package com.github.tartaricacid.touhoulittlemaid.client.renderer.sections;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

/**
 * @author Argon4W
 */
public interface ConditionalBlockEntitySectionGeometryRenderer<T extends BlockEntity> extends BlockEntitySectionGeometryRenderer<T> {
    boolean shouldRender(T blockEntity, BlockPos blockPos, BlockPos regionOrigin, Vec3 cameraPos);
}
