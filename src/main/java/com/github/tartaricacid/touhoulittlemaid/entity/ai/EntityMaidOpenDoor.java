package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * 复制自原版开门逻辑，因为这一块会存在 PathNavigate 变动
 *
 * @author TartaricAcid
 * @date 2019/8/3 14:14
 **/
public class EntityMaidOpenDoor extends EntityAIBase {
    private AbstractEntityMaid entityMaid;
    private BlockPos doorPosition = BlockPos.ORIGIN;
    private BlockDoor doorBlock;
    /**
     * 设置为 true，实体就会停止门的交互，并结束此 ai
     */
    private boolean hasStoppedDoorInteraction;
    private float entityPositionX;
    private float entityPositionZ;
    /**
     * 实体是否需要关门
     */
    private boolean closeDoor;
    /**
     * 实体关上门之前的延迟（tick 计算）
     */
    private int closeDoorTemporisation;

    public EntityMaidOpenDoor(AbstractEntityMaid entityMaid, boolean shouldClose) {
        this.entityMaid = entityMaid;
        this.closeDoor = shouldClose;
    }

    @Override
    public boolean shouldExecute() {
        // 如果实体在水平方向上没有碰撞，返回
        if (!this.entityMaid.collidedHorizontally) {
            return false;
        }

        // 因为女仆的 PathNavigate 总是会变化，所以需要检查
        if (this.entityMaid.getNavigator() instanceof PathNavigateGround) {
            PathNavigateGround pathnavigateground = (PathNavigateGround) this.entityMaid.getNavigator();
            Path path = pathnavigateground.getPath();

            // PathNavigate 存在，并且它设置为能够开门
            if (path != null && !path.isFinished() && pathnavigateground.getEnterDoors()) {
                // 检索最近两个路径索引
                for (int i = 0; i < Math.min(path.getCurrentPathIndex() + 2, path.getCurrentPathLength()); ++i) {
                    // 获取得到门的坐标
                    PathPoint pathpoint = path.getPathPointFromIndex(i);
                    this.doorPosition = new BlockPos(pathpoint.x, pathpoint.y + 1, pathpoint.z);
                    // 水平面距离小于 1.5
                    if (this.entityMaid.getDistanceSq(this.doorPosition.getX(), this.entityMaid.posY, this.doorPosition.getZ()) <= 2.25D) {
                        // 尝试获取此方块是否为门
                        this.doorBlock = this.getBlockDoor(this.doorPosition);
                        // 为门的情况下，执行后续逻辑
                        if (this.doorBlock != null) {
                            return true;
                        }
                    }
                }

                // 再次检测上方一格是否为门
                this.doorPosition = (new BlockPos(this.entityMaid)).up();
                this.doorBlock = this.getBlockDoor(this.doorPosition);
                return this.doorBlock != null;
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.closeDoor && this.closeDoorTemporisation > 0 && !this.hasStoppedDoorInteraction;
    }

    @Override
    public void startExecuting() {
        this.closeDoorTemporisation = 20;
        this.doorBlock.toggleDoor(this.entityMaid.world, this.doorPosition, true);
        entityMaid.swingArm(EnumHand.MAIN_HAND);
    }

    @Override
    public void updateTask() {
        --this.closeDoorTemporisation;

        float f = (float) ((double) ((float) this.doorPosition.getX() + 0.5F) - this.entityMaid.posX);
        float f1 = (float) ((double) ((float) this.doorPosition.getZ() + 0.5F) - this.entityMaid.posZ);
        float f2 = this.entityPositionX * f + this.entityPositionZ * f1;

        if (f2 < 0.0F) {
            this.hasStoppedDoorInteraction = true;
        }
    }

    @Override
    public void resetTask() {
        if (this.closeDoor) {
            this.doorBlock.toggleDoor(this.entityMaid.world, this.doorPosition, false);
        }
    }

    private BlockDoor getBlockDoor(BlockPos pos) {
        IBlockState iblockstate = this.entityMaid.world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        return block instanceof BlockDoor && iblockstate.getMaterial() == Material.WOOD ? (BlockDoor) block : null;
    }
}
