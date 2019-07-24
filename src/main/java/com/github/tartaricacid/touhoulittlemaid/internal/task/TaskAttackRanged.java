package com.github.tartaricacid.touhoulittlemaid.internal.task;

import java.util.Random;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.EntityMaidAttackRanged;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;

public class TaskAttackRanged implements IMaidTask
{
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "ranged_attack");

    @Override
    public ResourceLocation getUid()
    {
        return UID;
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Items.BOW);
    }

    @Override
    public SoundEvent getAmbientSound(AbstractEntityMaid maid)
    {
        return MaidSoundEvent.MAID_RANGE_ATTACK;
    }

    @Override
    public EntityAIBase createAI(AbstractEntityMaid maid)
    {
        return new EntityMaidAttackRanged(maid, 0.6f, 2, 16);
    }

    @Override
    public boolean isAttack()
    {
        return true;
    }

    @Override
    public void onRangedAttack(AbstractEntityMaid maid, EntityLivingBase target, float distanceFactor)
    {
        EntityArrow entityArrow = maid.getArrow(distanceFactor);

        // 如果获取得到的箭为 null，不执行攻击
        if (entityArrow == null)
        {
            return;
        }

        double x = target.posX - maid.posX;
        double y = target.getEntityBoundingBox().minY + target.height / 3.0F - entityArrow.posY;
        double z = target.posZ - maid.posZ;
        double pitch = MathHelper.sqrt(x * x + z * z) * 0.15D;

        entityArrow.shoot(x, y + pitch, z, 1.6F, 1);
        maid.getHeldItemMainhand().damageItem(1, maid);
        maid.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (maid.getRNG().nextFloat() * 0.4F + 0.8F));
        maid.world.spawnEntity(entityArrow);
    }
}
