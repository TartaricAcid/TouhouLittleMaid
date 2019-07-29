package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityMaidBeg extends EntityAIBase {
    private final EntityMaid entityMaid;
    private final World world;
    private final float maxPlayerDistance;
    private EntityPlayer player;

    public EntityMaidBeg(EntityMaid entityMaid, float maxPlayerDistance) {
        this.entityMaid = entityMaid;
        this.world = entityMaid.world;
        this.maxPlayerDistance = maxPlayerDistance;
        this.setMutexBits(1 | 2);
    }

    @Override
    public boolean shouldExecute() {
        this.player = this.world.getClosestPlayerToEntity(this.entityMaid, this.maxPlayerDistance);
        return !entityMaid.guiOpening && this.player != null && this.hasTemptationItemInHand(this.player);
    }

    @Override
    public boolean shouldContinueExecuting() {
        // 如果玩家正在打开UI/死了，不执行
        if (entityMaid.guiOpening || !this.player.isEntityAlive()) {
            return false;
        }
        // 女仆大于最大吸引距离了，也不执行
        if (this.entityMaid.getDistance(this.player) > this.maxPlayerDistance) {
            return false;
        }

        // 手上持有物品没有
        return this.hasTemptationItemInHand(this.player);
    }

    @Override
    public void startExecuting() {
        // 单纯设置模式
        this.entityMaid.setBegging(true);
    }

    @Override
    public void resetTask() {
        // 别忘了重置回去
        this.entityMaid.setBegging(false);
        this.entityMaid.getNavigator().clearPath();
        this.player = null;
    }


    @Override
    public void updateTask() {
        this.entityMaid.getLookHelper().setLookPosition(this.player.posX, this.player.posY + this.player.getEyeHeight(), this.player.posZ, 10.0F, this.entityMaid.getVerticalFaceSpeed());
        if (!this.entityMaid.isSitting()) {
            if (this.entityMaid.getDistance(player) > 2.5) {
                this.entityMaid.getNavigator().tryMoveToXYZ(this.player.posX, this.player.posY, this.player.posZ, 0.7);
            } else {
                this.entityMaid.getNavigator().clearPath();
            }
        }
    }

    private boolean hasTemptationItemInHand(EntityPlayer player) {
        Item temptationItem = Item.getByNameOrId(GeneralConfig.MAID_CONFIG.maidTemptationItem) == null ? Items.CAKE : Item.getByNameOrId(GeneralConfig.MAID_CONFIG.maidTemptationItem);
        for (EnumHand enumhand : EnumHand.values()) {
            ItemStack itemstack = player.getHeldItem(enumhand);
            if (this.entityMaid.isTamed() && itemstack.getItem() == temptationItem) {
                return true;
            }
        }
        return false;
    }
}
