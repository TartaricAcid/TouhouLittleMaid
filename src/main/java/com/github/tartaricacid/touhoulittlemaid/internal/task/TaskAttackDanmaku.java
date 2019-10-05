package com.github.tartaricacid.touhoulittlemaid.internal.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuShoot;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuType;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * @author Snownee
 * @date 2019/7/24 02:31
 */
public class TaskAttackDanmaku extends TaskAttackRanged {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "danmaku_attack");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(MaidItems.HAKUREI_GOHEI);
    }

    @Override
    public SoundEvent getAmbientSound(AbstractEntityMaid maid) {
        return MaidSoundEvent.MAID_DANMAKU_ATTACK;
    }

    @Override
    public void onRangedAttack(AbstractEntityMaid maid, EntityLivingBase target, float distanceFactor) {
        // 获取周围 -10~10 范围内怪物数量
        World world = maid.world;
        Random rand = maid.getRNG();
        List<Entity> entityList = world.getEntitiesInAABBexcluding(maid, maid.getEntityBoundingBox().expand(8, 3, 8).expand(-8, -3, -8), EntityMaid.IS_MOB);

        if (maid.hasSasimono()) {
            boolean targetIsTameableHasSameOwner = target instanceof EntityTameable && ((EntityTameable) target).getOwnerId() != null &&
                    ((EntityTameable) target).getOwnerId().equals(maid.getOwnerId());
            boolean targetIsPlayerOwner = target instanceof EntityPlayer && target.getUniqueID().equals(maid.getOwnerId());
            if (targetIsTameableHasSameOwner || targetIsPlayerOwner) {
                return;
            }
        }

        // 分为三档
        // 1 自机狙
        // <=5 60 度扇形
        // >5 120 度扇形
        if (entityList.size() <= 1) {
            DanmakuShoot.aimedShot(world, maid, target, 2 * (distanceFactor + 1), 0, 0.3f * (distanceFactor + 1), 0.2f, DanmakuType.getType(rand.nextInt(2)), DanmakuColor.getColor(rand.nextInt(7)));
        } else if (entityList.size() <= 5) {
            DanmakuShoot.fanShapedShot(world, maid, target, 2 * (distanceFactor + 1.2f), 0, 0.3f * (distanceFactor + 1), 0.2f, DanmakuType.getType(rand.nextInt(2)), DanmakuColor.getColor(rand.nextInt(7)), Math.PI / 3, 8);
        } else {
            DanmakuShoot.fanShapedShot(world, maid, target, 2 * (distanceFactor + 1.5f), 0, 0.3f * (distanceFactor + 1), 0.2f, DanmakuType.getType(rand.nextInt(2)), DanmakuColor.getColor(rand.nextInt(7)), Math.PI * 2 / 3, 32);
        }

        maid.getHeldItemMainhand().damageItem(1, maid);
    }
}
