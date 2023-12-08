package com.github.tartaricacid.touhoulittlemaid.entity.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class EntitySit extends Entity {
    public static final EntityType<EntitySit> TYPE = EntityType.Builder.<EntitySit>of(EntitySit::new, MobCategory.MISC)
            .sized(0.5f, 0.1f).clientTrackingRange(10).build("sit");

    public EntitySit(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EntitySit(Level worldIn, Vec3 pos) {
        this(TYPE, worldIn);
        this.setPos(pos);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!player.isPassenger() && !player.isVehicle()) {
            player.startRiding(this);
        }
        return super.interact(player, hand);
    }

    @Override
    public double getPassengersRidingOffset() {
        return -0.3;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            this.checkBelowWorld();
        }
    }

    @Override
    public boolean skipAttackInteraction(Entity pEntity) {
        return true;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    public void move(MoverType pType, Vec3 pPos) {
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public void push(double pX, double pY, double pZ) {
    }

    @Override
    protected boolean repositionEntityAfterLoad() {
        return false;
    }

    @Override
    public void thunderHit(ServerLevel pLevel, LightningBolt pLightning) {
    }

    @Override
    public void refreshDimensions() {
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return this.isAlive();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
