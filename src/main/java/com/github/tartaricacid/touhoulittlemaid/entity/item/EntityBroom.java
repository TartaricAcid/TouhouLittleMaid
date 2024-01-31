package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class EntityBroom extends AbstractEntityFromItem {
    public static final EntityType<EntityBroom> TYPE = EntityType.Builder.<EntityBroom>of(EntityBroom::new, MobCategory.MISC)
            .sized(0.5f, 0.25f).clientTrackingRange(10).build("broom");

    private boolean keyForward = false;
    private boolean keyBack = false;
    private boolean keyLeft = false;
    private boolean keyRight = false;

    public EntityBroom(EntityType<EntityBroom> entityType, Level worldIn) {
        super(entityType, worldIn);
        this.setNoGravity(true);
    }

    public EntityBroom(Level worldIn) {
        this(TYPE, worldIn);
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean keyForward() {
        return Minecraft.getInstance().options.keyUp.isDown();
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean keyBack() {
        return Minecraft.getInstance().options.keyDown.isDown();
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean keyLeft() {
        return Minecraft.getInstance().options.keyLeft.isDown();
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean keyRight() {
        return Minecraft.getInstance().options.keyRight.isDown();
    }

    @Override
    public void travel(Vec3 vec3) {
        Entity entity = this.getControllingPassenger();
        if (entity instanceof Player player && this.isVehicle()) {
            if (level.isClientSide) {
                // 不要问我为什么客户端数据能跑到服务端来
                // 一定是玄学
                keyForward = keyForward();
                keyBack = keyBack();
                keyLeft = keyLeft();
                keyRight = keyRight();
            }

            // 按键控制扫帚各个方向速度
            float strafe = keyLeft ? 0.5f : (keyRight ? -0.5f : 0);
            float vertical = keyForward ? -(player.getXRot() - 10) / 22.5f : 0;
            float forward = keyForward ? 3 : (keyBack ? -0.5f : 0);

            this.moveRelative(0.02f, new Vec3(strafe, vertical, forward));
            this.move(MoverType.SELF, this.getDeltaMovement());
        } else if (!this.onGround()) {
            // 玩家没有坐在扫帚上，那就让它掉下来
            super.travel(new Vec3(0, -0.3f, 0));
        } else {
            super.travel(vec3);
        }
    }

    @Override
    protected void pushEntities() {
        if (this.getPassengers().size() > 1) {
            return;
        }
        if (!level.isClientSide) {
            List<EntityMaid> list = level.getEntitiesOfClass(EntityMaid.class,
                    getBoundingBox().expandTowards(0.5, 0.1, 0.5),
                    e -> e.canBrainMoving() && e.getPassengers().isEmpty() && EntitySelector.pushableBy(this).test(e));
            list.stream().findFirst().ifPresent(entity -> entity.startRiding(this));
        }
    }

    @Override
    protected void tickRidden(Player player, Vec3 pTravelVector) {
        // 记得将 fall distance 设置为 0，否则会摔死
        this.fallDistance = 0;

        // 与旋转有关系的一堆东西，用来控制扫帚朝向
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        this.setRot(player.getYRot(), player.getXRot());
        super.tickRidden(player, pTravelVector);
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            double xOffset = passenger instanceof EntityMaid ? -0.5 : 0;
            double yOffset = this.isRemoved() ? 0.01 : this.getPassengersRidingOffset() + passenger.getMyRidingOffset();
            if (this.getPassengers().size() > 1) {
                int passengerIndex = this.getPassengers().indexOf(passenger);
                if (passengerIndex == 0) {
                    xOffset = 0.35;
                } else {
                    xOffset = -0.35;
                }
            }
            Vec3 offset = new Vec3(xOffset, yOffset, 0).yRot((float) (-this.getYRot() * Math.PI / 180 - Math.PI / 2));
            moveFunction.accept(passenger, this.getX() + offset.x, this.getY() + offset.y, this.getZ() + offset.z);
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand pHand) {
        if (!player.isDiscrete() && !this.isPassenger() && !(this.getControllingPassenger() instanceof Player)) {
            if (this.getPassengers().size() < 2) {
                player.startRiding(this);
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.interact(player, pHand);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Player player) {
            return player;
        }
        return null;
    }

    @Override
    protected boolean canAddPassenger(Entity entity) {
        return this.getPassengers().size() < 2;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0;
    }

    @Override
    protected boolean canKillEntity(Player player) {
        return false;
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.WOOL_BREAK;
    }

    @Override
    protected Item getWithItem() {
        return InitItems.BROOM.get();
    }

    @Override
    protected ItemStack getKilledStack() {
        return new ItemStack(this.getWithItem());
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
        this.resetFallDistance();
    }

    /**
     * 不允许被挤走，所以此处留空
     */
    @Override
    public void push(Entity entityIn) {
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}
