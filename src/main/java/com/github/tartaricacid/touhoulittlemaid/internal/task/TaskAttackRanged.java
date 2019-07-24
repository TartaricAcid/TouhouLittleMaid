package com.github.tartaricacid.touhoulittlemaid.internal.task;

import javax.annotation.Nullable;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.EntityMaidAttackRanged;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import com.github.tartaricacid.touhoulittlemaid.util.MiscUtil;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.IItemHandlerModifiable;

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
        EntityArrow entityArrow = getArrow(maid, distanceFactor);

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

    public static int findArrow(AbstractEntityMaid maid)
    {
        // 遍历女仆背包，找到第一个属于 arrow 的物品
        IItemHandlerModifiable handler = maid.getAvailableInv();
        return MiscUtil.findItem(handler, s -> s.getItem() == Items.ARROW || s.getItem() == Items.TIPPED_ARROW || s.getItem() == Items.SPECTRAL_ARROW);
    }

    @Nullable
    public static EntityArrow getArrow(AbstractEntityMaid maid, float chargeTime)
    {
        IItemHandlerModifiable handler = maid.getAvailableInv();
        int slot = findArrow(maid);
        if (slot < 0)
        {
            return null;
        }
        ItemStack stack = handler.getStackInSlot(slot);

        EntityArrow arrow;
        // 如果是光灵箭
        if (stack.getItem() == Items.SPECTRAL_ARROW)
        {
            arrow = new EntitySpectralArrow(maid.world, maid);
            arrow.setEnchantmentEffectsFromEntity(maid, chargeTime);
        }
        else // 如果是药水箭或者普通的箭
        {
            arrow = new EntityTippedArrow(maid.world, maid);
            arrow.setEnchantmentEffectsFromEntity(maid, chargeTime);
            ((EntityTippedArrow) arrow).setPotionEffect(stack);
        }
        // 无限附魔不存在或者小于 0 时
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, maid.getHeldItemMainhand()) <= 0)
        {
            stack.shrink(1);
            handler.setStackInSlot(slot, stack);
            // 记得把箭设置为可以拾起状态
            arrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
        }

        return arrow;
    }
}
