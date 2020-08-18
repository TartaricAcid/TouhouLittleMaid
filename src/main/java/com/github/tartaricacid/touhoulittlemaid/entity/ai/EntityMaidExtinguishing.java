package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityExtinguishingAgent;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.List;

public class EntityMaidExtinguishing extends EntityAIBase {
    private final AbstractEntityMaid entityMaid;
    private final int distance;
    private int timeCount;

    public EntityMaidExtinguishing(AbstractEntityMaid entityMaid, int distance) {
        this.entityMaid = entityMaid;
        this.timeCount = 20;
        this.distance = distance;
        this.setMutexBits(1 | 2);
    }

    @Override
    public boolean shouldExecute() {
        timeCount--;
        return timeCount < 0 && !entityMaid.isSitting() && !entityMaid.isSleep() &&
                entityMaid.getHeldItemMainhand().getItem() == MaidItems.EXTINGUISHER;
    }

    @Override
    public void startExecuting() {
        World world = entityMaid.world;
        EntityLivingBase owner = entityMaid.getOwner();
        ItemStack stack = entityMaid.getHeldItemMainhand();

        // 主人着火了
        if (owner instanceof EntityPlayer
                && owner.isEntityAlive()
                && entityMaid.getDistance(owner) < distance
                && owner.isBurning()
                && entityMaid.getHeldItemMainhand().getItem() == MaidItems.EXTINGUISHER) {
            EntityPlayer player = (EntityPlayer) owner;
            entityMaid.getLookHelper().setLookPositionWithEntity(player, 10, 40);
            entityMaid.getNavigator().clearPath();
            entityMaid.getNavigator().tryMoveToEntityLiving(player, 0.8f);
            world.spawnEntity(new EntityExtinguishingAgent(world, player.getPositionVector()));
            stack.damageItem(1, entityMaid);
            entityMaid.swingArm(EnumHand.MAIN_HAND);
        }

        // 自己着火了
        if (entityMaid.isBurning() && entityMaid.getHeldItemMainhand().getItem() == MaidItems.EXTINGUISHER) {
            world.spawnEntity(new EntityExtinguishingAgent(world, entityMaid.getPositionVector()));
            stack.damageItem(1, entityMaid);
            entityMaid.swingArm(EnumHand.MAIN_HAND);
        }

        // 其他宠物着火了
        List<EntityTameable> list = world.getEntitiesWithinAABB(EntityTameable.class,
                entityMaid.getEntityBoundingBox().grow(2, 1, 2), Entity::isBurning);
        if (!list.isEmpty()
                && entityMaid.getHeldItemMainhand().getItem() == MaidItems.EXTINGUISHER) {
            world.spawnEntity(new EntityExtinguishingAgent(world, entityMaid.getPositionVector()));
            stack.damageItem(1, entityMaid);
            entityMaid.swingArm(EnumHand.MAIN_HAND);
        }

        timeCount = 20;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}
