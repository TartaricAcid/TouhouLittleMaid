package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class EntityMaidAttack extends EntityAIAttackMelee {
    private final AbstractEntityMaid entityMaid;
    //切换武器的CD，女仆换武器就不用时间了吗？？！！
    private int pickUpItemCoolDown = 0;

    public EntityMaidAttack(AbstractEntityMaid entityMaid, double speedIn, boolean longMemoryIn) {
        super(entityMaid, speedIn, longMemoryIn);
        this.entityMaid = entityMaid;
    }

    @Override
    public boolean shouldExecute() {
        //切换武器的冷却时间
        if (pickUpItemCoolDown > 0) {
            pickUpItemCoolDown--;
        }
        //女仆没有待命时才能寻找物品栏更换武器
        if (!entityMaid.isSitting()) {
            //判断当前是否有攻击对象，以及对象是否存活
            EntityLivingBase attackTarget = entityMaid.getAttackTarget();
            if (attackTarget != null && attackTarget.isEntityAlive()) {
                boolean hasSword;
                //切换武器冷却完毕，切换武器冷却同时也是为了减少高频遍历物品
                if (pickUpItemCoolDown <= 0) {
                    //判断当前是否持有剑类攻击武器
                    //寻找伤害最高的剑类攻击武器
                    //交换物品到主手
                    hasSword = entityMaid.MoveItemsToMainhandForMaxPriority(i -> swordWeight(i) > 0, this::swordWeight);
                } else {
                    hasSword = swordWeight(entityMaid.getHeldItemMainhand()) > 0;
                }
                if (!hasSword) {
                    //没有武器难道赤手空拳去打吗？
                    if (pickUpItemCoolDown > 0) {
                        //着急换武器
                        pickUpItemCoolDown = pickUpItemCoolDown - 4;
                    }
                    return false;
                } else {
                    //在打人不得冷却一下？差不多128个红石刻？2秒？
                    pickUpItemCoolDown = 128;
                    return super.shouldExecute();
                }
            }
        }
        return false;
    }

    //计算该物品作为剑类武器的比重，0为不适合
    private int swordWeight(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item instanceof ItemSword) {
            ItemSword sword = (ItemSword) item;
            //确保分母不会为0，即使好像是多余的代码
            int effectiveDamage = Math.max(itemStack.getMaxDamage() - itemStack.getItemDamage(), 1);
            //高优先高攻击力武器，除非有个耐久剩余不多，先用先清背包
            float weight = 10000 * (sword.getAttackDamage() + 4) / (effectiveDamage + 7000);
            //冷不防有人给你无限攻击力呢？
            return weight < 0 ? Integer.MAX_VALUE : Math.round(weight);
        }
        return 0;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !entityMaid.isSitting() && super.shouldContinueExecuting();
    }
}
