package com.github.tartaricacid.touhoulittlemaid.entity.item;

/**
 * @author TartaricAcid
 * @date 2019/7/5 22:38
 **/

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IEntityRidingMaid;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityMarisaBroom extends AbstractEntityFromItem implements IEntityRidingMaid {
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
    protected boolean canKillEntity(EntityPlayer player) {
        return true;
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_CLOTH_BREAK;
    }

    @Override
    protected Item getWithItem() {
        return MaidItems.MARISA_BROOM;
    }

    @Override
    protected ItemStack getKilledStack() {
        return new ItemStack(getWithItem());
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
            if (GeneralConfig.MISC_CONFIG.creativePlayerCanRideBroom) {
                if (player.isCreative()) {
                    player.startRiding(this);
                } else {
                    player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.broom.message"));
                }
            } else {
                player.startRiding(this);
            }
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

    @Override
    public void updatePassenger(AbstractEntityMaid maid) {
        if (maid.isPassenger(this)) {
            this.setPosition(maid.posX, maid.posY + 0.15, maid.posZ);
            // 视线也必须同步，因为扫把的朝向受视线限制
            // 只能以视线方向为中心左右各 90 度，不同步就会导致朝向错误
            this.rotationYawHead = maid.rotationYawHead;
            // 旋转方向同步，包括渲染的旋转方向
            this.rotationPitch = maid.rotationPitch;
            this.rotationYaw = maid.rotationYaw;
            this.renderYawOffset = maid.renderYawOffset;
            // fallDistance 永远为 0
            maid.fallDistance = 0;
            this.fallDistance = 0;
        }
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

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }

    /**
     * 不会踩坏庄稼
     */
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
}
