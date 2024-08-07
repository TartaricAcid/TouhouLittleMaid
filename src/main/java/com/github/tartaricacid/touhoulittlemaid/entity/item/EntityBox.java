package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class EntityBox extends Entity {
    public static final int FIRST_STAGE = 64;
    public static final int SECOND_STAGE = 60;
    public static final int THIRD_STAGE = 0;
    public static final int MAX_TEXTURE_SIZE = 8;
    private static final EntityDataAccessor<Integer> OPEN_STAGE = SynchedEntityData.defineId(EntityBox.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TEXTURE_INDEX = SynchedEntityData.defineId(EntityBox.class, EntityDataSerializers.INT);
    public static final EntityType<EntityBox> TYPE = EntityType.Builder.<EntityBox>of(EntityBox::new, MobCategory.MISC).ridingOffset(0)
            .sized(2.0f, 2.0f).clientTrackingRange(10).build("box");
    private static final String STAGE_TAG = "OpenStage";
    private static final String TEXTURE_TAG = "TextureIndex";

    public EntityBox(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.setRandomTexture();
    }

    public EntityBox(Level worldIn) {
        this(TYPE, worldIn);
    }

    @Override
    public void baseTick() {
        if (!this.isNoGravity() && !this.onGround()) {
            this.move(MoverType.SELF, this.getDeltaMovement().add(0, -0.1, 0));
        }
        super.baseTick();
        int stage = getOpenStage();
        this.stageChange(stage);
        this.getPassengers().stream().filter(e -> e instanceof TamableAnimal)
                .forEach(e -> applyInvisibilityEffect((TamableAnimal) e, stage));
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (this.getOpenStage() == FIRST_STAGE) {
            setOpenStage(FIRST_STAGE - 1);
            this.playSound(InitSounds.BOX_OPEN.get(), 3.0f, 1.0f);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (this.getOpenStage() == SECOND_STAGE) {
            setOpenStage(SECOND_STAGE - 1);
            this.playSound(InitSounds.BOX_OPEN.get(), 3.0f, 1.0f);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (this.getOpenStage() == THIRD_STAGE) {
            setOpenStage(THIRD_STAGE - 1);
            this.playSound(InitSounds.BOX_OPEN.get(), 3.0f, 2.0f);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.interact(player, hand);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(OPEN_STAGE, FIRST_STAGE);
        pBuilder.define(TEXTURE_INDEX, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains(STAGE_TAG)) {
            setOpenStage(compound.getInt(STAGE_TAG));
        }
        if (compound.contains(TEXTURE_TAG)) {
            setTextureIndex(compound.getInt(TEXTURE_TAG));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt(STAGE_TAG, getOpenStage());
        compound.putInt(TEXTURE_TAG, getTextureIndex());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity pEntity) {
        return new ClientboundAddEntityPacket(this, pEntity);
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive();
    }

    @Override
    public boolean isPickable() {
        return this.isAlive();
    }

    public int getOpenStage() {
        return this.entityData.get(OPEN_STAGE);
    }

    public void setOpenStage(int stage) {
        this.entityData.set(OPEN_STAGE, Mth.clamp(stage, -1, FIRST_STAGE));
    }

    public int getTextureIndex() {
        return this.entityData.get(TEXTURE_INDEX);
    }

    public void setTextureIndex(int index) {
        this.entityData.set(TEXTURE_INDEX, Mth.clamp(index, 1, MAX_TEXTURE_SIZE - 1));
    }

    public void setRandomTexture() {
        setTextureIndex(random.nextInt(MAX_TEXTURE_SIZE));
    }

    private void stageChange(int stage) {
        if (stage != FIRST_STAGE && stage != SECOND_STAGE && stage != THIRD_STAGE) {
            this.setOpenStage(stage - 1);
        }
        if (stage < THIRD_STAGE) {
            this.discard();
            if (!this.level.isClientSide) {
                this.spawnAtLocation(Items.PAPER, 2 + random.nextInt(3));
            }
        }
    }

    private void applyInvisibilityEffect(TamableAnimal tameable, int stage) {
        if (stage >= FIRST_STAGE) {
            tameable.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2, 1, false, false));
        }
    }
}
