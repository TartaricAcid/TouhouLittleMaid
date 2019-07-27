package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemFindUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/7/27 21:47
 **/
public class EntityMaidFeedAnimal extends EntityAIBase {
    private AbstractEntityMaid maid;
    private float speed;
    private int searchRange;
    private int timeCount = 100;
    private World world;
    private EntityAnimal entitySelected;
    private int maxAnimalCount;

    /**
     * 繁殖动物模式 AI
     *
     * @param maid           女仆
     * @param speed          移动速度
     * @param searchRange    搜索范围
     * @param maxAnimalCount 最大动物数，超过此数量不再进行繁殖操作
     */
    public EntityMaidFeedAnimal(AbstractEntityMaid maid, float speed, int searchRange, int maxAnimalCount) {
        this.maid = maid;
        this.speed = speed;
        this.searchRange = searchRange;
        this.maxAnimalCount = maxAnimalCount;
        this.world = maid.world;
        setMutexBits(1 | 2);
    }

    @Override
    public boolean shouldExecute() {
        if (maid.isSitting()) {
            return false;
        }

        // 计数
        if (timeCount > 0) {
            timeCount--;
            return false;
        }
        timeCount = 100;

        // 开始判定 searchRange 范围内的可繁殖生物
        List<Entity> entityList = this.world.getEntitiesInAABBexcluding(maid, maid.getEntityBoundingBox()
                .grow(searchRange, 2, searchRange), (entity) -> entity instanceof EntityAnimal);
        // 先判定生物数量是否超过上限了
        if (entityList.size() > maxAnimalCount) {
            return false;
        }
        // 选取第一个可喂养生物
        for (Entity entity : entityList) {
            if (entity instanceof EntityAnimal && entity.isEntityAlive() && ((EntityAnimal) entity).getGrowingAge() == 0 &&
                    !((EntityAnimal) entity).isInLove() && ItemFindUtil.isItemIn(maid.getAvailableInv(false), ((EntityAnimal) entity)::isBreedingItem)) {
                entitySelected = (EntityAnimal) entity;
                timeCount = 5;
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateTask() {
        if (entitySelected != null && entitySelected.isEntityAlive()) {
            // 先尝试移动到这里
            maid.getLookHelper().setLookPositionWithEntity(entitySelected, 30f, maid.getVerticalFaceSpeed());
            maid.getNavigator().tryMoveToEntityLiving(entitySelected, speed);

            // 如果距离太远，还是先跳过后面的繁殖过程吧
            if (maid.getDistance(entitySelected) > 3) {
                return;
            }

            // 开始遍历女仆背包，寻找合适的繁殖物品
            int slot = ItemFindUtil.findItem(maid.getAvailableInv(false), entitySelected::isBreedingItem);
            // 再次判定一些条件，尽可能减少间隔
            if (slot >= 0 && entitySelected.isEntityAlive() && entitySelected.getGrowingAge() == 0 && !entitySelected.isInLove()) {
                maid.getAvailableInv(false).getStackInSlot(slot).shrink(1);
                maid.swingArm(EnumHand.MAIN_HAND);
                entitySelected.setInLove(null);
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        boolean entityIsExisted = entitySelected != null && entitySelected.isEntityAlive();
        boolean entityCanFeed = entitySelected != null && entitySelected.getGrowingAge() == 0 && !entitySelected.isInLove();
        return !maid.isSitting() && entityIsExisted && entityCanFeed;
    }

    @Override
    public void resetTask() {
        entitySelected = null;
        maid.getNavigator().clearPath();
    }
}
