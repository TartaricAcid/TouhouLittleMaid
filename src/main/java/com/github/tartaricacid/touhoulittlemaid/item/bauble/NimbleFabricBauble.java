package com.github.tartaricacid.touhoulittlemaid.item.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAttackEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NimbleFabricBauble implements IMaidBauble {
    private static final int MAX_RETRY = 16;

    public NimbleFabricBauble() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingDamage(MaidAttackEvent event) {
        EntityMaid maid = event.getMaid();
        DamageSource source = event.getSource();
        if (source instanceof IndirectEntityDamageSource) {
            int slot = ItemsUtil.getBaubleSlotInMaid(maid, this);
            if (slot >= 0) {
                event.setCanceled(true);
                ItemStack stack = maid.getMaidBauble().getStackInSlot(slot);
                stack.hurtAndBreak(1, maid, m -> maid.sendItemBreakMessage(stack));
                maid.getMaidBauble().setStackInSlot(slot, stack);
                for (int i = 0; i < MAX_RETRY; ++i) {
                    if (teleport(maid)) {
                        return;
                    }
                }
            }
        }
    }

    protected boolean teleport(EntityMaid maid) {
        if (!maid.level.isClientSide() && maid.isAlive()) {
            double x = maid.getX() + (maid.getRandom().nextDouble() - 0.5) * 16;
            double y = maid.getY() + maid.getRandom().nextInt(16) - 8;
            double z = maid.getZ() + (maid.getRandom().nextDouble() - 0.5) * 16;
            return teleport(maid, x, y, z);
        } else {
            return false;
        }
    }

    private boolean teleport(EntityMaid maid, double x, double y, double z) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(x, y, z);
        while (blockPos.getY() > maid.level.getMinBuildHeight() && !maid.level.getBlockState(blockPos).getMaterial().blocksMotion()) {
            blockPos.move(Direction.DOWN);
        }
        BlockState blockState = maid.level.getBlockState(blockPos);
        boolean isMotion = blockState.getMaterial().blocksMotion();
        boolean isWater = blockState.getFluidState().is(FluidTags.WATER);
        if (isMotion && !isWater) {
            boolean teleportIsSuccess = maid.randomTeleport(x, y, z, true);
            if (teleportIsSuccess && !maid.isSilent()) {
                maid.level.playSound(null, maid.xo, maid.yo, maid.zo, SoundEvents.ENDERMAN_TELEPORT, maid.getSoundSource(), 1.0F, 1.0F);
                maid.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
            return teleportIsSuccess;
        } else {
            return false;
        }
    }
}
