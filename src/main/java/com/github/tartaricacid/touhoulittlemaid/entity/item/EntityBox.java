package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityBox extends Entity {
    public static final int FIRST_STAGE = 64;
    public static final int SECOND_STAGE = 60;
    public static final int THIRD_STAGE = 0;
    public static final int MAX_TEXTURE_SIZE = 7;
    private static final DataParameter<Integer> OPEN_STAGE = EntityDataManager.defineId(EntityBox.class, DataSerializers.INT);
    private static final DataParameter<Integer> TEXTURE_INDEX = EntityDataManager.defineId(EntityBox.class, DataSerializers.INT);
    public static final EntityType<EntityBox> TYPE = EntityType.Builder.<EntityBox>of(EntityBox::new, EntityClassification.MISC)
            .sized(2.0f, 2.0f).clientTrackingRange(10).build("box");
    private static final String STAGE_TAG = "OpenStage";
    private static final String TEXTURE_TAG = "TextureIndex";

    public EntityBox(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.setRandomTexture();
    }

    public EntityBox(World worldIn) {
        this(TYPE, worldIn);
    }

    @Override
    public void baseTick() {
        if (!this.isNoGravity() && !this.onGround) {
            this.move(MoverType.SELF, this.getDeltaMovement().add(0, -0.1, 0));
        }
        super.baseTick();
        int stage = getOpenStage();
        this.stageChange(stage);
        this.getPassengers().stream().filter(e -> e instanceof TameableEntity)
                .forEach(e -> applyInvisibilityEffect((TameableEntity) e, stage));
    }

    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        if (this.getOpenStage() == FIRST_STAGE) {
            setOpenStage(FIRST_STAGE - 1);
            this.playSound(InitSounds.BOX_OPEN.get(), 3.0f, 1.0f);
            return ActionResultType.sidedSuccess(level.isClientSide);
        }
        if (this.getOpenStage() == SECOND_STAGE) {
            setOpenStage(SECOND_STAGE - 1);
            this.playSound(InitSounds.BOX_OPEN.get(), 3.0f, 1.0f);
            return ActionResultType.sidedSuccess(level.isClientSide);
        }
        if (this.getOpenStage() == THIRD_STAGE) {
            setOpenStage(THIRD_STAGE - 1);
            this.playSound(InitSounds.BOX_OPEN.get(), 3.0f, 2.0f);
            return ActionResultType.sidedSuccess(level.isClientSide);
        }
        return super.interact(player, hand);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OPEN_STAGE, FIRST_STAGE);
        this.entityData.define(TEXTURE_INDEX, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        if (compound.contains(STAGE_TAG)) {
            setOpenStage(compound.getInt(STAGE_TAG));
        }
        if (compound.contains(TEXTURE_TAG)) {
            setTextureIndex(compound.getInt(TEXTURE_TAG));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        compound.putInt(STAGE_TAG, getOpenStage());
        compound.putInt(TEXTURE_TAG, getTextureIndex());
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0;
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
        this.entityData.set(OPEN_STAGE, MathHelper.clamp(stage, -1, FIRST_STAGE));
    }

    public int getTextureIndex() {
        return this.entityData.get(TEXTURE_INDEX);
    }

    public void setTextureIndex(int index) {
        this.entityData.set(TEXTURE_INDEX, MathHelper.clamp(index, 1, MAX_TEXTURE_SIZE - 1));
    }

    public void setRandomTexture() {
        setTextureIndex(random.nextInt(MAX_TEXTURE_SIZE));
    }

    private void stageChange(int stage) {
        if (stage != FIRST_STAGE && stage != SECOND_STAGE && stage != THIRD_STAGE) {
            this.setOpenStage(stage - 1);
        }
        if (stage < THIRD_STAGE) {
            this.remove();
            if (!this.level.isClientSide) {
                this.spawnAtLocation(Items.PAPER, 2 + random.nextInt(3));
            }
        }
    }

    private void applyInvisibilityEffect(TameableEntity tameable, int stage) {
        if (stage >= FIRST_STAGE) {
            tameable.addEffect(new EffectInstance(Effects.INVISIBILITY, 2, 1, false, false));
        }
    }
}
