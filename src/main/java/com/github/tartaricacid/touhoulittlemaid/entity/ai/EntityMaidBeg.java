package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityMaidBeg extends EntityAIBase {
    private final EntityMaid entityMaid;
    private final World world;
    private final float minPlayerDistance;
    private EntityPlayer player;
    private int timeoutCounter;

    public EntityMaidBeg(EntityMaid entityMaid, float minDistance) {
        this.entityMaid = entityMaid;
        this.world = entityMaid.world;
        this.minPlayerDistance = minDistance;
        this.setMutexBits(1 | 2);
    }

    @Override
    public boolean shouldExecute() {
        this.player = this.world.getClosestPlayerToEntity(this.entityMaid, (double) this.minPlayerDistance);
        return this.player != null && this.hasTemptationItemInHand(this.player);
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (!this.player.isEntityAlive()) {
            return false;
        } else if (this.entityMaid.getDistanceSq(this.player) > (double) (this.minPlayerDistance * this.minPlayerDistance)) {
            return false;
        } else {
            return this.timeoutCounter > 0 && this.hasTemptationItemInHand(this.player);
        }
    }

    @Override
    public void startExecuting() {
        this.entityMaid.setBegging(true);
        this.timeoutCounter = 40 + this.entityMaid.getRNG().nextInt(40);
    }

    @Override
    public void resetTask() {
        this.entityMaid.setBegging(false);
        this.player = null;
    }


    @Override
    public void updateTask() {
        this.entityMaid.getLookHelper().setLookPosition(this.player.posX, this.player.posY + (double) this.player.getEyeHeight(), this.player.posZ, 10.0F, (float) this.entityMaid.getVerticalFaceSpeed());
        if (!this.entityMaid.isSitting()) {
            this.entityMaid.getMoveHelper().setMoveTo(this.player.posX, this.player.posY, this.player.posZ, 0.8);
        }
        --this.timeoutCounter;
    }

    private boolean hasTemptationItemInHand(EntityPlayer player) {
        for (EnumHand enumhand : EnumHand.values()) {
            ItemStack itemstack = player.getHeldItem(enumhand);
            if (this.entityMaid.isTamed() && itemstack.getItem() == Items.CAKE) {
                return true;
            }
        }
        return false;
    }
}
