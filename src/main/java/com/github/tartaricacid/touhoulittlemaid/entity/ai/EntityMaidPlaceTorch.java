package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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
        return !entityMaid.isSitting() && !getTorchItem(entityMaid).isEmpty() && getLowLightBlock() != null;
    }

    @Override
    public void startExecuting() {
        timeoutCounter = 0;
    }

    @Override
    public void updateTask() {
        if (pos != null) {
            if (entityMaid.getDistanceSq(pos.up()) < 16) {
                entityMaid.swingArm(EnumHand.MAIN_HAND);
                world.setBlockState(pos, Blocks.TORCH.getDefaultState());
                entityMaid.playSound(SoundEvents.BLOCK_WOOD_PLACE, 1.0f, 1.0f);
                getTorchItem(entityMaid).shrink(1);
                pos = null;
            } else {
                // 40 次进行一次寻路
                timeoutCounter++;
                if (timeoutCounter > 40) {
                    entityMaid.getLookHelper().setLookPosition(pos.getX() + 0.5d, pos.getY() + 1d, pos.getZ() + 0.5d, 10.0F, this.entityMaid.getVerticalFaceSpeed());
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
        return pos != null && !entityMaid.isSitting() && !getTorchItem(entityMaid).isEmpty();
    }

    @Override
    public void resetTask() {
        entityMaid.getNavigator().clearPath();
        pos = null;
    }

    private ItemStack getTorchItem(AbstractEntityMaid entityMaid) {
        IItemHandler itemHandler = entityMaid.getAvailableInv();
        for (int i = 0; i < itemHandler.getSlots(); ++i) {
            ItemStack itemstack = itemHandler.getStackInSlot(i);
            if (!itemstack.isEmpty() && itemstack.getItem() == Item.getItemFromBlock(Blocks.TORCH)) {
                return itemstack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Nullable
    private BlockPos getLowLightBlock() {
        int x = entityMaid.getRNG().nextInt(radius * 2) - radius;
        int y = entityMaid.getRNG().nextInt(heigh * 2) - heigh;
        int z = entityMaid.getRNG().nextInt(radius * 2) - radius;

        BlockPos posRandom = entityMaid.getPosition().add(x, y, z);
        if (world.getLightFromNeighbors(posRandom.up()) < 9 && world.isAirBlock(posRandom.up())
                && Blocks.TORCH.canPlaceTorchOnTop(world.getBlockState(posRandom), world, posRandom)) {
            pos = posRandom.up();
            return posRandom;
        }
        return null;
    }
}
