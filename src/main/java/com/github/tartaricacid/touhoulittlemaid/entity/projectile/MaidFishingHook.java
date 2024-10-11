package com.github.tartaricacid.touhoulittlemaid.entity.projectile;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidFishedEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskFishing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class MaidFishingHook extends Projectile {
    public static final EntityType<MaidFishingHook> TYPE = EntityType.Builder.<MaidFishingHook>of(MaidFishingHook::new, MobCategory.MISC)
            .noSave().noSummon().sized(0.25F, 0.25F)
            .clientTrackingRange(4).updateInterval(5)
            .build("fishing_hook");
    protected static final EntityDataAccessor<Boolean> DATA_BITING = SynchedEntityData.defineId(MaidFishingHook.class, EntityDataSerializers.BOOLEAN);
    protected static final int MAX_OUT_OF_WATER_TIME = 10;
    protected final RandomSource syncronizedRandom = RandomSource.create();
    protected final int luck;
    protected final int lureSpeed;
    protected boolean biting;
    protected int nibble;
    protected int timeUntilLured;
    protected int timeUntilHooked;
    protected int outOfWaterTime;
    protected int life;
    protected float fishAngle;
    protected boolean openWater = true;
    protected MaidFishingHook.FishHookState currentState = MaidFishingHook.FishHookState.FLYING;

    protected MaidFishingHook(EntityType<? extends MaidFishingHook> entityType, Level level, int luck, int lureSpeed) {
        super(entityType, level);
        this.noCulling = true;
        this.luck = Math.max(0, luck);
        this.lureSpeed = Math.max(0, lureSpeed);
    }

    public MaidFishingHook(EntityType<MaidFishingHook> entityType, Level level) {
        this(entityType, level, 0, 0);
    }

    public MaidFishingHook(EntityMaid maid, Level level, int luck, int lureSpeed, Vec3 pos) {
        this(TYPE, level, luck, lureSpeed);
        this.setOwner(maid);
        this.moveTo(pos);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_BITING, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_BITING.equals(key)) {
            this.biting = this.getEntityData().get(DATA_BITING);
            if (this.biting) {
                this.setDeltaMovement(this.getDeltaMovement().x, -0.4 * Mth.nextFloat(this.syncronizedRandom, 0.6F, 1.0F), this.getDeltaMovement().z);
            }
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 64 * 64;
    }

    @Override
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
    }

    @Override
    public void tick() {
        // 每个 tick 给予不同的 seed，保证随机不一致
        this.syncronizedRandom.setSeed(this.getUUID().getLeastSignificantBits() ^ this.level.getGameTime());
        // 父类调用
        super.tick();
        // 获取当前钓钩的女仆
        EntityMaid maid = this.getMaidOwner();
        // 女仆为空，那么吊钩也不应当存在
        if (maid == null) {
            this.discard();
        } else if (this.level.isClientSide || !this.shouldStopFishing(maid)) {
            if (this.lifeTick()) {
                return;
            }
            maid.getLookControl().setLookAt(this);
            this.fishingTick();
        }
    }

    protected boolean lifeTick() {
        if (this.onGround()) {
            // 如果钓钩在地面，最多存在 100 tick 就消失
            ++this.life;
            if (this.life >= 100) {
                this.discard();
                return true;
            }
        } else {
            this.life = 0;
        }
        return false;
    }

    protected void fishingTick() {
        // 获取水面高度
        BlockPos blockPos = this.blockPosition();
        FluidState fluidState = this.level.getFluidState(blockPos);
        float fluidHeight = this.getFluidHeight(fluidState, blockPos);
        boolean onWaterSurface = fluidHeight > 0;

        if (this.currentState == FishHookState.FLYING) {
            // 如果在水面，那么上下飘动即可
            if (onWaterSurface) {
                this.waterSurfaceTick();
                return;
            }
        } else {
            // 如果已经处于上下飘动状态
            if (this.currentState == FishHookState.BOBBING) {
                // 继续上下飘动
                this.bobbingTick(blockPos, fluidHeight);
                // 开放水域检测，开放水域能够钓到宝藏
                this.checkOpenWater(blockPos);
                // 计算咬钩时的运动和其他逻辑
                if (onWaterSurface) {
                    this.bitingTick(blockPos);
                } else {
                    this.outOfWaterTime = Math.min(MAX_OUT_OF_WATER_TIME, this.outOfWaterTime + 1);
                }
            }
        }

        // 不在水面，那就下坠
        this.fallTick(fluidState);

        // 运动相关更新
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.updateRotation();
        if (this.currentState == FishHookState.FLYING && (this.onGround() || this.horizontalCollision)) {
            this.setDeltaMovement(Vec3.ZERO);
        }
        this.setDeltaMovement(this.getDeltaMovement().scale(0.92));
        this.reapplyPosition();
    }

    protected void fallTick(FluidState fluidState) {
        if (!fluidState.is(FluidTags.WATER)) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
        }
    }

    protected void bitingTick(BlockPos blockPos) {
        this.outOfWaterTime = Math.max(0, this.outOfWaterTime - 1);
        if (this.biting) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.1D * (double) this.syncronizedRandom.nextFloat() * (double) this.syncronizedRandom.nextFloat(), 0.0D));
        }
        // 咬钩！
        if (!this.level.isClientSide) {
            this.catchingFish(blockPos, (ServerLevel) this.level);
        }
    }

    protected void checkOpenWater(BlockPos blockPos) {
        if (this.nibble <= 0 && this.timeUntilHooked <= 0) {
            this.openWater = true;
        } else {
            this.openWater = this.openWater && this.outOfWaterTime < 10 && this.calculateOpenWater(blockPos);
        }
    }

    protected float getFluidHeight(FluidState fluidState, BlockPos blockPos) {
        float fluidHeight = 0;
        if (fluidState.is(FluidTags.WATER)) {
            fluidHeight = fluidState.getHeight(this.level(), blockPos);
        }
        return fluidHeight;
    }

    protected void waterSurfaceTick() {
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.3D, 0.2D, 0.3D));
        this.currentState = FishHookState.BOBBING;
    }

    protected void bobbingTick(BlockPos blockPos, float fluidHeight) {
        Vec3 movement = this.getDeltaMovement();
        double bobbingY = this.getY() + movement.y - (double) blockPos.getY() - fluidHeight;
        if (Math.abs(bobbingY) < 0.01D) {
            bobbingY += Math.signum(bobbingY) * 0.1;
        }
        this.setDeltaMovement(movement.x * 0.9, movement.y - bobbingY * (double) this.random.nextFloat() * 0.2, movement.z * 0.9);
    }

    protected void catchingFish(BlockPos pos, ServerLevel level) {
        int time = 1;
        BlockPos abovePos = pos.above();
        // 如果下雨，随机加快钓鱼等待时间
        if (this.random.nextFloat() < 0.25F && level.isRainingAt(abovePos)) {
            ++time;
        }
        // 如果没有露天，减少钓鱼等待时间
        if (this.random.nextFloat() < 0.5F && !level.canSeeSky(abovePos)) {
            --time;
        }
        // 咬钩时间
        if (this.nibble > 0) {
            --this.nibble;
            this.onNibble(level);
        }
        // 如果等待时间
        else if (this.timeUntilHooked > 0) {
            this.timeUntilHooked -= time;
            // 如果等待时间没结束，那么随机加一点粒子效果
            if (this.timeUntilHooked > 0) {
                // 随机给予运动角度
                this.fishAngle += (float) this.random.triangle(0.0D, 9.188D);
                float fishAngleRad = this.fishAngle * ((float) Math.PI / 180F);
                float sin = Mth.sin(fishAngleRad);
                float cos = Mth.cos(fishAngleRad);
                double x = this.getX() + sin * this.timeUntilHooked * 0.1;
                double y = Mth.floor(this.getY()) + 1.0;
                double z = this.getZ() + cos * this.timeUntilHooked * 0.1;
                BlockState blockState = level.getBlockState(BlockPos.containing(x, y - 1.0, z));
                // 随机给予钓鱼时出现的水花粒子
                this.spawnFishingParticle(level, blockState, x, y, z, sin, cos);
            } else {
                // 给予咬钩时的粒子效果和音效
                this.spawnNibbleParticle(level);
                // 添加随机的咬钩时间
                this.nibble = Mth.nextInt(this.random, 20, 40);
                this.getEntityData().set(DATA_BITING, true);
            }
        }
        // 计算下一次饵钓时间
        else if (this.timeUntilLured > 0) {
            this.timeUntilLured -= time;
            float probability = 0.15F;
            if (this.timeUntilLured < 20) {
                probability += (float) (20 - this.timeUntilLured) * 0.05F;
            } else if (this.timeUntilLured < 40) {
                probability += (float) (40 - this.timeUntilLured) * 0.02F;
            } else if (this.timeUntilLured < 60) {
                probability += (float) (60 - this.timeUntilLured) * 0.01F;
            }
            // 随机给予粒子效果
            if (this.random.nextFloat() < probability) {
                float randomRot = Mth.nextFloat(this.random, 0.0F, 360.0F) * ((float) Math.PI / 180F);
                float randomNum = Mth.nextFloat(this.random, 25.0F, 60.0F);
                double x = this.getX() + Mth.sin(randomRot) * randomNum * 0.1;
                double y = Mth.floor(this.getY()) + 1.0;
                double z = this.getZ() + Mth.cos(randomRot) * randomNum * 0.1;
                BlockState blockState = level.getBlockState(BlockPos.containing(x, y - 1.0, z));
                this.spawnSplashParticle(level, blockState, x, y, z);
            }
            // 饵钓时间到，开始随机赋予等待时间
            if (this.timeUntilLured <= 0) {
                this.fishAngle = Mth.nextFloat(this.random, 0.0F, 360.0F);
                this.timeUntilHooked = Mth.nextInt(this.random, 20, 80);
            }
        } else {
            this.timeUntilLured = Mth.nextInt(this.random, 100, 600);
            this.timeUntilLured -= this.lureSpeed * 20 * 5;
        }
    }

    protected void spawnSplashParticle(ServerLevel level, BlockState blockState, double x, double y, double z) {
        if (blockState.is(Blocks.WATER)) {
            level.sendParticles(ParticleTypes.SPLASH, x, y, z, 2 + this.random.nextInt(2), 0.1F, 0.0D, 0.1F, 0.0D);
        }
    }

    protected void spawnNibbleParticle(ServerLevel level) {
        double yOffset = this.getY() + 0.5D;
        float bbWidth = this.getBbWidth();
        this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
        level.sendParticles(ParticleTypes.BUBBLE, this.getX(), yOffset, this.getZ(), (int) (1.0F + bbWidth * 20.0F), bbWidth, 0.0D, bbWidth, 0.2F);
        level.sendParticles(ParticleTypes.FISHING, this.getX(), yOffset, this.getZ(), (int) (1.0F + bbWidth * 20.0F), bbWidth, 0.0D, bbWidth, 0.2F);
    }

    protected void spawnFishingParticle(ServerLevel level, BlockState blockState, double x, double y, double z, float sin, float cos) {
        if (blockState.is(Blocks.WATER)) {
            if (this.random.nextFloat() < 0.15F) {
                level.sendParticles(ParticleTypes.BUBBLE, x, y - 0.1, z, 1, sin, 0.1D, cos, 0.0D);
            }
            float sinOffset = sin * 0.04F;
            float cosOffset = cos * 0.04F;
            level.sendParticles(ParticleTypes.FISHING, x, y, z, 0, cosOffset, 0.01D, -sinOffset, 1.0D);
            level.sendParticles(ParticleTypes.FISHING, x, y, z, 0, -cosOffset, 0.01D, sinOffset, 1.0D);
        }
    }

    protected void onNibble(ServerLevel level) {
        // 咬钩时间到了，收杆
        EntityMaid maid = getMaidOwner();
        int retrieveTime = Mth.nextInt(this.random, 2, 10);
        // TODO：收杆应该有成功率，应该和好感度挂钩
        if (this.nibble <= retrieveTime && maid != null) {
            ItemStack rodItem = maid.getMainHandItem();
            int rodDamage = this.retrieve(rodItem);
            this.hurtRod(maid, rodItem, rodDamage);
            maid.swing(InteractionHand.MAIN_HAND);
            level.playSound(null, maid.getX(), maid.getY(), maid.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        }
    }

    public int retrieve(ItemStack stack) {
        EntityMaid maid = this.getMaidOwner();
        if (!this.level.isClientSide && maid != null && !this.shouldStopFishing(maid)) {
            MaidFishedEvent event = null;
            MinecraftServer server = this.level.getServer();
            int rodDamage = 0;

            // 如果是咬钩时间
            if (this.nibble > 0 && server != null) {
                ServerLevel serverLevel = (ServerLevel) this.level;
                LootParams lootParams = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.ORIGIN, this.position())
                        .withParameter(LootContextParams.TOOL, stack)
                        .withParameter(LootContextParams.THIS_ENTITY, this)
                        .withParameter(LootContextParams.KILLER_ENTITY, maid)
                        .withLuck(this.luck + maid.getLuck())
                        .create(LootContextParamSets.FISHING);

                List<ItemStack> randomItems = this.getLoot(server, lootParams);
                // 添加额外的物品
                this.addExtraLoot(randomItems);

                event = new MaidFishedEvent(randomItems, this.onGround() ? 2 : 1, this);
                MinecraftForge.EVENT_BUS.post(event);
                if (event.isCanceled()) {
                    this.discard();
                    return event.getRodDamage();
                }

                this.spawnLoot(randomItems, maid);
                // 用于在钓到鱼后施加特殊功能
                this.afterFishing();

                rodDamage = 1;
            }
            if (this.onGround()) {
                rodDamage = 2;
            }
            this.discard();
            return event == null ? rodDamage : event.getRodDamage();
        } else {
            return 0;
        }
    }

    @NotNull
    protected List<ItemStack> getLoot(MinecraftServer server, LootParams lootParams) {
        LootTable lootTable = server.getLootData().getLootTable(BuiltInLootTables.FISHING);
        return lootTable.getRandomItems(lootParams);
    }

    protected void spawnLoot(List<ItemStack> randomItems, EntityMaid maid) {
        for (ItemStack result : randomItems) {
            ItemEntity itemEntity = new ItemEntity(this.level(), maid.getX(), maid.getY() + 0.5, maid.getZ(), result);
            itemEntity.setDeltaMovement(0, 0.1, 0);
            this.level.addFreshEntity(itemEntity);
            this.level.addFreshEntity(new ExperienceOrb(maid.level(), maid.getX(), maid.getY() + 0.5, maid.getZ(), this.random.nextInt(6) + 1));
        }
    }

    protected void addExtraLoot(List<ItemStack> randomItems) {
    }

    protected void afterFishing() {
    }

    protected void hurtRod(EntityMaid maid, ItemStack rodItem, int rodDamage) {
        rodItem.hurtAndBreak(rodDamage, maid, m -> maid.sendItemBreakMessage(rodItem));
    }

    private boolean calculateOpenWater(BlockPos pos) {
        MaidFishingHook.OpenWaterType openWaterType = MaidFishingHook.OpenWaterType.INVALID;
        for (int y = -1; y <= 2; ++y) {
            MaidFishingHook.OpenWaterType openWaterTypeForArea = this.getOpenWaterTypeForArea(pos.offset(-2, y, -2), pos.offset(2, y, 2));
            switch (openWaterTypeForArea) {
                case INVALID:
                    return false;
                case ABOVE_WATER:
                    if (openWaterType == MaidFishingHook.OpenWaterType.INVALID) {
                        return false;
                    }
                    break;
                case INSIDE_WATER:
                    if (openWaterType == MaidFishingHook.OpenWaterType.ABOVE_WATER) {
                        return false;
                    }
            }
            openWaterType = openWaterTypeForArea;
        }
        return true;
    }

    private MaidFishingHook.OpenWaterType getOpenWaterTypeForArea(BlockPos firstPos, BlockPos secondPos) {
        return BlockPos.betweenClosedStream(firstPos, secondPos)
                .map(this::getOpenWaterTypeForBlock)
                .reduce((firstType, secondType) -> firstType == secondType ? firstType : OpenWaterType.INVALID)
                .orElse(MaidFishingHook.OpenWaterType.INVALID);
    }

    private MaidFishingHook.OpenWaterType getOpenWaterTypeForBlock(BlockPos blockPos) {
        BlockState blockState = this.level.getBlockState(blockPos);
        if (!blockState.isAir() && !blockState.is(Blocks.LILY_PAD)) {
            FluidState fluidState = blockState.getFluidState();
            return fluidState.is(FluidTags.WATER) && fluidState.isSource()
                   && blockState.getCollisionShape(this.level(), blockPos).isEmpty() ? MaidFishingHook.OpenWaterType.INSIDE_WATER : MaidFishingHook.OpenWaterType.INVALID;
        } else {
            return MaidFishingHook.OpenWaterType.ABOVE_WATER;
        }
    }

    public boolean isOpenWaterFishing() {
        return this.openWater;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        this.updateOwnerInfo(null);
        super.remove(reason);
    }

    @Override
    public void onClientRemoval() {
        this.updateOwnerInfo(null);
    }

    @Override
    public void setOwner(@Nullable Entity owner) {
        super.setOwner(owner);
        this.updateOwnerInfo(this);
    }

    private void updateOwnerInfo(@Nullable MaidFishingHook fishingHook) {
        EntityMaid maid = this.getMaidOwner();
        if (maid != null) {
            maid.fishing = fishingHook;
        }
    }

    @Nullable
    public EntityMaid getMaidOwner() {
        Entity entity = this.getOwner();
        return entity instanceof EntityMaid ? (EntityMaid) entity : null;
    }

    private boolean shouldStopFishing(EntityMaid maid) {
        ItemStack mainHandItem = maid.getMainHandItem();
        boolean hasFishingRod = mainHandItem.canPerformAction(ToolActions.FISHING_ROD_CAST);
        boolean isFishingTask = maid.getTask() instanceof TaskFishing;
        boolean hasVehicle = maid.getVehicle() != null;
        if (!maid.isRemoved() && maid.isAlive() && hasVehicle && isFishingTask && hasFishingRod && this.distanceToSqr(maid) < 256) {
            return false;
        } else {
            this.discard();
            return true;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity entity = this.getOwner();
        return new ClientboundAddEntityPacket(this, entity == null ? this.getId() : entity.getId());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        if (this.getMaidOwner() == null) {
            int dataId = packet.getData();
            TouhouLittleMaid.LOGGER.error("Failed to recreate fishing hook on client. {} (id: {}) is not a valid owner.", this.level.getEntity(dataId), dataId);
            this.kill();
        }
    }

    protected enum FishHookState {
        FLYING,
        BOBBING;
    }

    protected enum OpenWaterType {
        ABOVE_WATER,
        INSIDE_WATER,
        INVALID;
    }
}
