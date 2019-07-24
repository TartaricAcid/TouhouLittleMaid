package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.IItemHandler;

public class EntityMaidFeedOwner extends EntityAIBase {
    private AbstractEntityMaid entityMaid;
    private int timeCount;
    private int distance;

    public EntityMaidFeedOwner(AbstractEntityMaid entityMaid, int distance) {
        this.entityMaid = entityMaid;
        timeCount = 60;
        this.distance = distance;
    }

    @Override
    public boolean shouldExecute() {
        timeCount--;
        return timeCount < 0 && !entityMaid.isSitting()
                && entityMaid.getOwner() instanceof EntityPlayer && entityMaid.getDistance(entityMaid.getOwner()) < distance;
    }

    @Override
    public void startExecuting() {
        if (entityMaid.getOwner() instanceof EntityPlayer && entityMaid.getOwner().isEntityAlive()) {
            EntityPlayer player = (EntityPlayer) entityMaid.getOwner();
            entityMaid.getLookHelper().setLookPositionWithEntity(player, 10, 40);
            if (player.getFoodStats().needFood()) {
                IItemHandler mainInv = entityMaid.getAvailableInv();
                for (int i = 0; i < mainInv.getSlots(); ++i) {
                    ItemStack stack = mainInv.getStackInSlot(i);
                    if (stack.getItem() instanceof ItemFood) {
                        // 手部动画
                        entityMaid.swingArm(EnumHand.MAIN_HAND);
                        stack.getItem().onItemUseFinish(stack, player.world, player);
                        timeCount = 5;
                        return;
                    }
                }
            }
        }
        timeCount = 60;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}
