package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityMaidBeacon extends TileEntity implements ITickableTileEntity {
    public static final TileEntityType<TileEntityMaidBeacon> TYPE = TileEntityType.Builder.of(TileEntityMaidBeacon::new, InitBlocks.MAID_BEACON.get()).build(null);
    public static final String POTION_INDEX_TAG = "PotionIndex";
    public static final String STORAGE_POWER_TAG = "StoragePower";
    public static final String OVERFLOW_DELETE_TAG = "OverflowDelete";

    private int potionIndex = -1;
    private float storagePower;
    private boolean overflowDelete = false;

    public TileEntityMaidBeacon() {
        super(TYPE);
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide && this.level.dayTime() % 80L == 0L) {
            if (potionIndex != -1 && storagePower >= this.getEffectCost()) {
                storagePower = storagePower - this.getEffectCost();
                updateBeaconEffect(this.level, BeaconEffect.getEffectByIndex(potionIndex).getEffect());
            }
            updateAbsorbPower(this.level);
        }
    }

    private void updateBeaconEffect(World world, Effect potion) {
        List<EntityMaid> list = world.getEntitiesOfClass(EntityMaid.class, new AxisAlignedBB(getBlockPos()).inflate(8, 8, 8), LivingEntity::isAlive);
        for (EntityMaid maid : list) {
            maid.addEffect(new EffectInstance(potion, 100, 1, true, true));
        }
    }

    private void updateAbsorbPower(World world) {
        int range = MiscConfig.SHRINE_LAMP_MAX_RANGE.get();
        List<EntityPowerPoint> list = world.getEntitiesOfClass(EntityPowerPoint.class, new AxisAlignedBB(getBlockPos()).inflate(range, range, range), Entity::isAlive);
        for (EntityPowerPoint powerPoint : list) {
            float addNum = this.getStoragePower() + powerPoint.value / 100.0f;
            if (addNum <= this.getMaxStorage()) {
                this.setStoragePower(addNum);
                powerPoint.spawnExplosionParticle();
                powerPoint.remove();
            } else {
                if (overflowDelete) {
                    powerPoint.spawnExplosionParticle();
                    powerPoint.remove();
                }
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        getTileData().putInt(POTION_INDEX_TAG, potionIndex);
        getTileData().putFloat(STORAGE_POWER_TAG, storagePower);
        getTileData().putBoolean(OVERFLOW_DELETE_TAG, overflowDelete);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        potionIndex = getTileData().getInt(POTION_INDEX_TAG);
        storagePower = getTileData().getFloat(STORAGE_POWER_TAG);
        overflowDelete = getTileData().getBoolean(OVERFLOW_DELETE_TAG);
    }

    public void loadData(CompoundNBT data) {
        potionIndex = data.getInt(POTION_INDEX_TAG);
        storagePower = data.getFloat(STORAGE_POWER_TAG);
        overflowDelete = data.getBoolean(OVERFLOW_DELETE_TAG);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getBlockPos(), -1, this.save(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (level != null) {
            this.load(level.getBlockState(pkt.getPos()), pkt.getTag());
        }
    }

    public int getPotionIndex() {
        return potionIndex;
    }

    public void setPotionIndex(int potionIndex) {
        this.potionIndex = potionIndex;
        this.refresh();
    }

    public float getStoragePower() {
        return storagePower;
    }

    public void setStoragePower(float storagePower) {
        this.storagePower = storagePower;
        this.refresh();
    }

    public boolean isOverflowDelete() {
        return overflowDelete;
    }

    public void setOverflowDelete(boolean overflowDelete) {
        this.overflowDelete = overflowDelete;
        this.refresh();
    }

    public float getEffectCost() {
        return (float) (MiscConfig.SHRINE_LAMP_EFFECT_COST.get() / 900);
    }

    public float getMaxStorage() {
        return MiscConfig.SHRINE_LAMP_MAX_STORAGE.get().floatValue();
    }

    public void refresh() {
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, Constants.BlockFlags.DEFAULT);
        }
    }

    public enum BeaconEffect {
        // Effects
        SPEED(Effects.MOVEMENT_SPEED),
        FIRE_RESISTANCE(Effects.FIRE_RESISTANCE),
        STRENGTH(Effects.DAMAGE_BOOST),
        RESISTANCE(Effects.DAMAGE_RESISTANCE),
        REGENERATION(Effects.REGENERATION);

        private final Effect effect;

        BeaconEffect(Effect effect) {
            this.effect = effect;
        }

        public static BeaconEffect getEffectByIndex(int index) {
            return values()[MathHelper.clamp(0, index, values().length - 1)];
        }

        public Effect getEffect() {
            return effect;
        }
    }
}
