package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/11/14 14:44
 **/
public class EntityBox extends Entity {
    public static final int FIRST_STAGE = 64;
    public static final int SECOND_STAGE = 60;
    public static final int THIRD_STAGE = 0;
    public static final int MAX_TEXTURE_SIZE = 7;
    private static final String STAGE_TAG = "OpenStage";
    private static final String TEXTURE_TAG = "TextureIndex";
    private static final DataParameter<Integer> OPEN_STAGE = EntityDataManager.createKey(EntityBox.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TEXTURE_INDEX = EntityDataManager.createKey(EntityBox.class, DataSerializers.VARINT);

    public EntityBox(World worldIn) {
        super(worldIn);
        setSize(2.0f, 2.0f);
    }

    @Override
    public void onEntityUpdate() {
        if (!this.hasNoGravity() && !this.onGround) {
            this.motionY = -0.04;
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        }
        super.onEntityUpdate();
        int stage = getOpenStage();
        stageChange(stage);
        for (Entity riding : this.getPassengers()) {
            if (riding instanceof EntityTameable) {
                applyInvisibilityEffect((EntityTameable) riding, stage);
            }
        }
        this.collideWithNearbyEntities();
    }

    private void stageChange(int stage) {
        if (stage != FIRST_STAGE && stage != SECOND_STAGE && stage != THIRD_STAGE) {
            this.setOpenStage(stage - 1);
        }
        if (stage < THIRD_STAGE) {
            this.setDead();
            this.onDeath();
        }
    }

    private void applyInvisibilityEffect(EntityTameable tameable, int stage) {
        if (stage > FIRST_STAGE) {
            tameable.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 2, 1, false, false));
        }
    }

    private void collideWithNearbyEntities() {
        List<Entity> list = this.world.getEntitiesWithinAABB(EntityTameable.class, this.getEntityBoundingBox().shrink(1));
        if (list.isEmpty()) {
            return;
        }
        for (Entity entity : list) {
            if (!entity.isPassenger(this)) {
                boolean notRiddenAndRiding = !entity.isBeingRidden() && !entity.isRiding();
                if (!world.isRemote && notRiddenAndRiding && entity instanceof EntityTameable) {
                    if (((EntityTameable) entity).isSitting()) {
                        return;
                    }
                    entity.startRiding(this);
                }
            }
        }
    }

    private void onDeath() {
        if (!this.world.isRemote) {
            this.dropItem(Items.PAPER, 2 + rand.nextInt(3));
        }
    }

    @Override
    public double getMountedYOffset() {
        return 0;
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(OPEN_STAGE, FIRST_STAGE);
        this.dataManager.register(TEXTURE_INDEX, 1);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (this.getOpenStage() == FIRST_STAGE) {
            setOpenStage(FIRST_STAGE - 1);
            this.playSound(MaidSoundEvent.BOX_OPEN, 3.0f, 1.0f);
            return true;
        }
        if (this.getOpenStage() == SECOND_STAGE) {
            setOpenStage(SECOND_STAGE - 1);
            this.playSound(MaidSoundEvent.BOX_OPEN, 3.0f, 1.0f);
            return true;
        }
        if (this.getOpenStage() == THIRD_STAGE) {
            setOpenStage(THIRD_STAGE - 1);
            this.playSound(MaidSoundEvent.BOX_OPEN, 3.0f, 2.0f);
            return true;
        }
        return super.processInitialInteract(player, hand);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return getEntityBoundingBox();
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public int getOpenStage() {
        return this.dataManager.get(OPEN_STAGE);
    }

    public void setOpenStage(int stage) {
        this.dataManager.set(OPEN_STAGE, MathHelper.clamp(stage, -1, FIRST_STAGE));
    }

    public int getTextureIndex() {
        return this.dataManager.get(TEXTURE_INDEX);
    }

    public void setTextureIndex(int index) {
        this.dataManager.set(TEXTURE_INDEX, MathHelper.clamp(index, 1, MAX_TEXTURE_SIZE));
    }

    public void setRandomTexture() {
        setTextureIndex(1 + rand.nextInt(MAX_TEXTURE_SIZE));
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        if (compound.hasKey(STAGE_TAG)) {
            setOpenStage(compound.getInteger(STAGE_TAG));
        }
        if (compound.hasKey(TEXTURE_TAG)) {
            setTextureIndex(compound.getInteger(TEXTURE_TAG));
        }
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        compound.setInteger(STAGE_TAG, getOpenStage());
        compound.setInteger(TEXTURE_TAG, getTextureIndex());
    }
}
