package com.github.tartaricacid.touhoulittlemaid.entity.item;

/**
 * @author TartaricAcid
 * @date 2019/7/5 22:38
 **/

import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityMarisaBroom extends EntityLiving {
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

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!world.isRemote && source.getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) source.getTrueSource();
            if (player.isSneaking()) {
                EntityItem item = new EntityItem(world, this.posX, this.posY, this.posZ, MaidItems.MARISA_BROOM.getDefaultInstance());
                item.setPickupDelay(10);
                this.setDead();
                world.spawnEntity(item);
                return true;
            }
        }
        return false;
    }

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
        } else if (entity == null && !this.onGround) {
            // 玩家没有坐在扫帚上，那就让它掉下来
            super.travel(0, -0.3f, 0);
        }
    }

    @Override
    public void dismountRidingEntity() {
        super.dismountRidingEntity();
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (!player.isSneaking() && !this.world.isRemote && !this.isBeingRidden()) {
            player.startRiding(this);
        }
        return true;
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public double getMountedYOffset() {
        return 0.05d;
    }

    @Override
    public boolean shouldRiderFaceForward(EntityPlayer player) {
        return true;
    }

    @Override
    public boolean canBeSteered() {
        return true;
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

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
}
