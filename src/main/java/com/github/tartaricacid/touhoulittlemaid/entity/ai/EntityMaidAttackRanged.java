package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidMode;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;

public class EntityMaidAttackRanged extends EntityAIBase {
    private final EntityMaid entity;
    private final double moveSpeedAmp;
    private final float maxAttackDistance;
    private int attackCooldown;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public EntityMaidAttackRanged(EntityMaid entity, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn) {
        this.entity = entity;
        this.moveSpeedAmp = moveSpeedAmpIn;
        this.attackCooldown = attackCooldownIn;
        this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
        this.setMutexBits(3);
    }

    public void setAttackCooldown(int cooldown) {
        this.attackCooldown = cooldown;
    }

    @Override
    public boolean shouldExecute() {
        // 能够远程攻击：模式正确、主手持弓、身上有箭
        boolean canRangeAttack = this.entity.getMode() == MaidMode.RANGE_ATTACK && this.isBowInMainhand() && this.entity.hasArrow();
        // 能够弹幕攻击：模式正确、主手持御币
        boolean canDanmakuAttack = this.entity.getMode() == MaidMode.DANMAKU_ATTACK && this.isGoheiInMainhand();
        // 能够处理攻击：攻击目标不为空、上述两者攻击存在一个
        boolean canAttack = this.entity.getAttackTarget() != null && (canRangeAttack || canDanmakuAttack);
        return !entity.guiOpening && !entity.isSitting() && canAttack;
    }

    private boolean isBowInMainhand() {
        return this.entity.getHeldItemMainhand().getItem() instanceof ItemBow;
    }

    private boolean isGoheiInMainhand() {
        return this.entity.getHeldItemMainhand().getItem() == MaidItems.HAKUREI_GOHEI;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    @Override
    public void startExecuting() {
        this.entity.setSwingingArms(true);
    }

    @Override
    public void resetTask() {
        this.seeTime = 0;
        this.attackTime = -1;
        this.entity.resetActiveHand();
        this.entity.setSwingingArms(false);
        this.entity.getNavigator().clearPath();
    }

    @Override
    public void updateTask() {
        // 获取攻击目标
        EntityLivingBase entitylivingbase = this.entity.getAttackTarget();

        // 如果攻击目标不为空
        if (entitylivingbase != null) {
            // 到攻击目标的底部距离
            double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
            // 能否看见对方
            boolean canSee = this.entity.getEntitySenses().canSee(entitylivingbase);
            // 看见的时长是否大于 0
            boolean flag1 = this.seeTime > 0;

            // 如果两者不一致，重置看见时间
            if (canSee != flag1) {
                this.seeTime = 0;
            }

            // 如果看见了对方，增加看见时间，否则减少
            if (canSee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            // 如果在最大攻击距离之内，而且看见的时长足够长
            if (d0 <= (double) this.maxAttackDistance && this.seeTime >= 20) {
                // 清除先前的寻路路径，开始增加攻击时间
                this.entity.getNavigator().clearPath();
                ++this.strafingTime;
            } else {
                // 否则就尝试移动到对方那边，攻击时间重置
                this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
                this.strafingTime = -1;
            }

            // 如果攻击时间也足够长，随机对走位方向和前后走位进行反转
            if (this.strafingTime >= 20) {
                if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            // 如果攻击时间大于 -1
            if (this.strafingTime > -1) {
                // 依据距离远近决定是否前后走位
                if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double) (this.maxAttackDistance * 0.25F)) {
                    this.strafingBackwards = true;
                }

                // 应用走位
                this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.entity.faceEntity(entitylivingbase, 30.0F, 30.0F);
            } else {
                // 否则只朝向攻击目标
                this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
            }

            // 如果实体手部处于激活状态
            if (this.entity.isHandActive()) {
                // 如果看不见对方超时 60，重置激活状态
                if (!canSee && this.seeTime < -60) {
                    this.entity.resetActiveHand();
                } else if (canSee) {
                    // 否则开始进行远程攻击
                    int i = this.entity.getItemInUseMaxCount();

                    // 物品最大使用计数大于 20 才可以
                    // 这里大致解释下计数的意思，也就是蓄力，蓄力越长自然射的越远
                    // 只有蓄力超过 1 秒才会进行发射
                    if (i >= 20) {
                        this.entity.resetActiveHand();
                        this.entity.attackEntityWithRangedAttack(entitylivingbase, ItemBow.getArrowVelocity(i));
                        this.attackTime = this.attackCooldown;
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                // 非激活状态，但是时长合适，开始激活手部
                this.entity.setActiveHand(EnumHand.MAIN_HAND);
            }
        }
    }
}
