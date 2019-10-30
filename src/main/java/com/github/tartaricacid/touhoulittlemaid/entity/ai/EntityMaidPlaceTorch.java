package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.util.Util;
import com.github.tartaricacid.touhoulittlemaid.util.ItemFindUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class EntityMaidPlaceTorch extends EntityAIBase {
    private AbstractEntityMaid entityMaid;
    private World world;
    private int radius;
    private int heigh;
    private BlockPos pos;
    private float speed;
    private int timeoutCounter;

    public EntityMaidPlaceTorch(AbstractEntityMaid entityMaid, int radius, int heigh, float speed) {
        this.entityMaid = entityMaid;
        this.world = entityMaid.getEntityWorld();
        this.radius = radius;
        this.heigh = heigh;
        this.speed = speed;
    }

    @Override
    public boolean shouldExecute() {
        return !entityMaid.isSitting() && !getTorchItem().isEmpty() && getLowLightBlock() != null;
    }

    @Override
    public void startExecuting() {
        timeoutCounter = 0;
    }

    @Override
    public void updateTask() {
        if (pos != null) {
            if (entityMaid.getDistanceSq(pos.up()) < 4) {
                ItemStack torch = getTorchItem();
                if (!torch.isEmpty()) {
                    torch.shrink(1);
                    entityMaid.swingArm(EnumHand.MAIN_HAND);
                    entityMaid.placeBlock(pos, ((ItemBlock) torch.getItem()).getBlock().getDefaultState());
                    entityMaid.playSound(SoundEvents.BLOCK_WOOD_PLACE, 1.0f, 1.0f);
                }
                pos = null;
            } else {
                entityMaid.getLookHelper().setLookPosition(pos.getX() + 0.5d, pos.getY() + 1d, pos.getZ() + 0.5d, 10.0F, this.entityMaid.getVerticalFaceSpeed());
                // 40 次进行一次寻路
                timeoutCounter++;
                if (timeoutCounter > 40) {
                    // 用来解决传送后停止插火把行为
                    // 我发现传送后，pos 还会是先前的 pos，此时太远无法寻路过去
                    // 所以这时候需要将其变为 null
                    if (!entityMaid.getNavigator().tryMoveToXYZ(pos.getX() + 0.5d, pos.getY() + 1d, pos.getZ() + 0.5d, speed)) {
                        pos = null;
                        timeoutCounter = 0;
                    }
                }
            }
        }

    }

    @Override
    public boolean shouldContinueExecuting() {
        return pos != null && !entityMaid.isSitting() && !getTorchItem().isEmpty();
    }

    @Override
    public void resetTask() {
        entityMaid.getNavigator().clearPath();
        pos = null;
    }

    private ItemStack getTorchItem() {
        IItemHandler itemHandler = entityMaid.getAvailableInv(false);
        return ItemFindUtil.getStack(itemHandler, stack -> stack.getItem() instanceof ItemBlock && Util.doesItemHaveOreName(stack, "torch"));
    }

    @Nullable
    private BlockPos getLowLightBlock() {
        // 获取随机坐标
        Vec3d posVec3d = RandomPositionGenerator.getLandPos(entityMaid, radius, heigh);
        if (posVec3d == null) {
            return null;
        }

        BlockPos posRandom = new BlockPos(posVec3d);
        BlockPos posUp = posRandom.up();
        if (world.getLightFromNeighbors(posUp) < 9 && entityMaid.canPlaceBlock(posUp, Blocks.TORCH.getDefaultState())) {
            IBlockState state = world.getBlockState(posRandom);
            IBlockState stateUp = world.getBlockState(posUp);
            if (Blocks.TORCH.canPlaceTorchOnTop(state, world, posRandom) && !stateUp.getMaterial().isLiquid()) {
                pos = posUp;
                return posRandom;
            }
        }
        return null;
    }
}
