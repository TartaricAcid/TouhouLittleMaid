package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.util.Util;
import com.github.tartaricacid.touhoulittlemaid.util.ItemFindUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/11/10 18:17
 **/
public class EntityMaidPlaceTorch extends EntityAIMoveToBlock {
    private AbstractEntityMaid entityMaid;

    public EntityMaidPlaceTorch(AbstractEntityMaid entityMaid, double speedIn, int length) {
        super(entityMaid, speedIn, length);
        this.entityMaid = entityMaid;
    }

    @Override
    public boolean shouldExecute() {
        return !entityMaid.isSitting() && super.shouldExecute() && !getTorchItem().isEmpty();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        this.entityMaid.getLookHelper().setLookPosition((double) this.destinationBlock.getX() + 0.5D,
                (double) (this.destinationBlock.getY() + 1),
                (double) this.destinationBlock.getZ() + 0.5D,
                10.0F,
                (float) this.entityMaid.getVerticalFaceSpeed());
        if (this.getIsAboveDestination()) {
            ItemStack torch = getTorchItem();
            if (!torch.isEmpty()) {
                torch.shrink(1);
                entityMaid.swingArm(EnumHand.MAIN_HAND);
                entityMaid.placeBlock(destinationBlock.up(), ((ItemBlock) torch.getItem()).getBlock().getDefaultState());
                entityMaid.playSound(SoundEvents.BLOCK_WOOD_PLACE, 1.0f, 1.0f);
            }
            this.runDelay = 20;
        }
    }

    @Override
    protected boolean shouldMoveTo(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        BlockPos posUp = pos.up();
        if (worldIn.getLightFromNeighbors(posUp) < 9 && entityMaid.canPlaceBlock(posUp, Blocks.TORCH.getDefaultState())) {
            IBlockState state = worldIn.getBlockState(pos);
            IBlockState stateUp = worldIn.getBlockState(posUp);
            return Blocks.TORCH.canPlaceTorchOnTop(state, worldIn, pos) && !stateUp.getMaterial().isLiquid();
        }
        return false;
    }

    private ItemStack getTorchItem() {
        IItemHandler itemHandler = entityMaid.getAvailableInv(false);
        return ItemFindUtil.getStack(itemHandler, stack -> stack.getItem() instanceof ItemBlock && Util.doesItemHaveOreName(stack, "torch"));
    }
}
