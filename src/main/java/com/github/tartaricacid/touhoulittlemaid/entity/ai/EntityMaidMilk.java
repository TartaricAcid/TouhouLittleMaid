package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaidPredicate;
import com.github.tartaricacid.touhoulittlemaid.util.ItemFindUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/11/11 22:34
 **/
public class EntityMaidMilk extends EntityAIBase {

    private AbstractEntityMaid entityMaid;
    private float speed;
    private World world;
    private Entity milkTarget = null;
    private int timeCount;

    public EntityMaidMilk(AbstractEntityMaid entityMaid, float speed) {
        this.entityMaid = entityMaid;
        this.world = entityMaid.world;
        this.speed = speed;
        this.timeCount = 20;
        this.setMutexBits(1 | 2);
    }

    @Override
    public boolean shouldExecute() {
        if (entityMaid.isSitting()) {
            return false;
        }

        // 计数
        if (timeCount > 0) {
            timeCount--;
            return false;
        }

        timeCount = 20;

        // 没有空桶，返回
        if (ItemFindUtil.findStackSlot(entityMaid.getAvailableInv(true), stack -> (stack.getItem() == Items.BUCKET)) == -1) {
            return false;
        }

        // 没有空位，返回
        if (ItemFindUtil.findStackSlot(entityMaid.getAvailableInv(true), stack -> (stack == ItemStack.EMPTY)) == -1) {
            return false;
        }

        // 开始判定 16 范围内的适合生物
        List<Entity> entityList = this.world.getEntitiesInAABBexcluding(entityMaid, entityMaid.getEntityBoundingBox()
                .grow(8, 2, 8), EntityMaidPredicate.IS_COW);
        for (Entity entity : entityList) {
            if (entityMaid.isWithinHomeDistanceFromPosition(new BlockPos(entity)) &&
                    entityMaid.getNavigator().getPathToEntityLiving(entity) != null) {
                milkTarget = entity;
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateTask() {
        if (milkTarget != null && milkTarget.isEntityAlive()) {
            // 先尝试移动到这只牛身边
            entityMaid.getNavigator().tryMoveToEntityLiving(milkTarget, speed);
            entityMaid.getLookHelper().setLookPositionWithEntity(milkTarget, 30f, entityMaid.getVerticalFaceSpeed());

            // 如果距离太远，还是先跳过后面的获取牛奶过程吧
            if (entityMaid.getDistance(milkTarget) > 3) {
                return;
            }

            ItemStack bucket = ItemFindUtil.getStack(entityMaid.getAvailableInv(false), stack -> stack.getItem() == Items.BUCKET);
            if (bucket != ItemStack.EMPTY) {
                bucket.shrink(1);
                ItemHandlerHelper.insertItemStacked(entityMaid.getAvailableInv(false), new ItemStack(Items.MILK_BUCKET), false);
            }

            // 手部动画
            entityMaid.swingArm(EnumHand.MAIN_HAND);
            entityMaid.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
        }
    }

    @Override
    public void resetTask() {
        milkTarget = null;
        entityMaid.getNavigator().clearPath();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return shouldExecute();
    }
}
