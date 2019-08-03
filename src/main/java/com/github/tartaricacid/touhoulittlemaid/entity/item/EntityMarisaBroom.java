package com.github.tartaricacid.touhoulittlemaid.entity.item;

/**
 * @author TartaricAcid
 * @date 2019/7/5 22:38
 **/

import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;

public class EntityMarisaBroom extends EntityLivingBase {
    private boolean keyForward = false;
    private boolean keyBack = false;
    private boolean keyLeft = false;
    private boolean keyRight = false;

    public EntityMarisaBroom(World world) {
        super(world);
        this.preventEntitySpawning = true;
        setNoGravity(true);
        setSize(0.5f, 0.5f);
    }

    @SideOnly(Side.CLIENT)
    private static boolean keyForward() {
        return Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown();
    }

    @SideOnly(Side.CLIENT)
    private static boolean keyBack() {
        return Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown();
    }

    @SideOnly(Side.CLIENT)
    private static boolean keyLeft() {
        return Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown();
    }

    @SideOnly(Side.CLIENT)
    private static boolean keyRight() {
        return Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown();
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (!this.world.isRemote && !this.isDead) {
            // 如果实体是无敌的
            if (this.isEntityInvulnerable(source)) {
                return false;
            }
            // 应用打掉的逻辑
            if (source.getTrueSource() instanceof EntityPlayer) {
                return applyHitBroomLogic((EntityPlayer) source.getTrueSource());
            }
        }
        return false;
    }

    /**
     * 击打扫帚逻辑
     */
    private boolean applyHitBroomLogic(EntityPlayer player) {
        boolean isPlayerCreativeMode = player.capabilities.isCreativeMode;
        // 潜行状态才会运用击打逻辑
        if (player.isSneaking()) {
            this.removePassengers();
            if (isPlayerCreativeMode && !this.hasCustomName()) {
                // 如果是创造模式，而且扫帚没有命名，直接消失
                this.setDead();
            } else {
                // 否则应用实体转物品逻辑
                this.killBroom();
            }
        }
        return true;
    }

    /**
     * 将扫帚从实体状态转变为物品状态
     */
    private void killBroom() {
        this.setDead();
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            ItemStack itemstack = new ItemStack(MaidItems.MARISA_BROOM, 1);
            if (this.hasCustomName()) {
                itemstack.setStackDisplayName(this.getCustomNameTag());
            }
            this.entityDropItem(itemstack, 0.0F);
        }
    }

    /**
     * 与玩家操控扫帚有关系
     */
    @Override
    public void travel(float strafe, float vertical, float forward) {
        Entity entity = getControllingPassenger();
        if (entity instanceof EntityPlayer && this.isBeingRidden()) {
            EntityPlayer player = (EntityPlayer) entity;

            // 记得将 fall distance 设置为 0，否则会摔死
            player.fallDistance = 0;
            this.fallDistance = 0;

            // 与旋转有关系的一堆东西，用来控制扫帚朝向
            this.rotationYaw = player.rotationYaw;
            this.rotationPitch = player.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.rotationYaw;
            this.setRotation(this.rotationYaw, this.rotationPitch);

            if (world.isRemote) {
                // 不要问我为什么客户端数据能跑到服务端来
                // 一定是玄学
                keyForward = keyForward();
                keyBack = keyBack();
                keyLeft = keyLeft();
                keyRight = keyRight();
            }

            // 按键控制扫帚各个方向速度
            strafe = keyLeft ? 0.5f : (keyRight ? -0.5f : 0);
            vertical = keyForward ? -(player.rotationPitch - 10) / 45 : 0;
            forward = keyForward ? 3 : (keyBack ? -0.5f : 0);

            this.moveRelative(strafe, vertical, forward, 0.02f);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        } else if (entity == null && !this.isRiding() && !this.onGround) {
            // 玩家没有坐在扫帚上，那就让它掉下来
            super.travel(0, -0.3f, 0);
        } else {
            super.travel(strafe, vertical, forward);
        }
    }

    /**
     * 处理交互
     */
    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (player.getHeldItem(hand).getItem() == Items.NAME_TAG) {
            // 返回 false，交由玩家侧的右击事件进行处理
            return false;
        }
        if (!player.isSneaking() && !this.world.isRemote && !this.isBeingRidden() && !this.isRiding()) {
            player.startRiding(this);
            return true;
        }
        return super.processInitialInteract(player, hand);
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public double getMountedYOffset() {
        return 0.1d;
    }

    /**
     * 让扫帚朝向和玩家永远一致
     */
    @Override
    public boolean shouldRiderFaceForward(EntityPlayer player) {
        return true;
    }

    /**
     * 只有玩家能控制此实体，其他一概不允许
     */
    @Override
    public boolean canPassengerSteer() {
        Entity entity = this.getControllingPassenger();
        if (entity instanceof EntityPlayer) {
            return ((EntityPlayer) entity).isUser();
        } else {
            return false;
        }
    }

    /**
     * 永远不允许被其他实体推动
     */
    @Override
    public boolean canBePushed() {
        return false;
    }

    /**
     * 不会踩坏庄稼
     */
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    // ------------ EntityLivingBase 要求实现的几个抽象方法，因为全用不上，故返回默认值 ----------- //

    @Nonnull
    @Override
    public ItemStack getItemStackFromSlot(@Nonnull EntityEquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(@Nonnull EntityEquipmentSlot slotIn, @Nonnull ItemStack stack) {
    }

    @Nonnull
    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return Collections.emptyList();
    }

    @Nonnull
    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.LEFT;
    }
}
