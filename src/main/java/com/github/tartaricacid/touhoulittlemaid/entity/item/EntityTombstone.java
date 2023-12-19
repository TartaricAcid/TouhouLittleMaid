package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityTombstone extends Entity {
    public static final EntityType<EntityTombstone> TYPE = EntityType.Builder.<EntityTombstone>of(EntityTombstone::new, MobCategory.MISC)
            .sized(0.8f, 1.2f).clientTrackingRange(10).build("tombstone");
    private static final String OWNER_ID_TAG = "OwnerId";
    private static final String TOMBSTONE_ITEMS_TAG = "TombstoneItems";
    private static final String MAID_NAME_TAG = "MaidName";
    private static final EntityDataAccessor<Component> MAID_NAME = SynchedEntityData.defineId(EntityTombstone.class, EntityDataSerializers.COMPONENT);
    private final ItemStackHandler items = new ItemStackHandler(64);
    private UUID ownerId = Util.NIL_UUID;

    public EntityTombstone(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EntityTombstone(Level worldIn, UUID ownerId, Vec3 pos) {
        this(TYPE, worldIn);
        this.ownerId = ownerId;
        this.setPos(pos);
    }

    public void insertItem(ItemStack item) {
        ItemHandlerHelper.insertItemStacked(this.items, item, false);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.getUUID().equals(this.ownerId)) {
            for (int i = 0; i < this.items.getSlots(); i++) {
                int size = this.items.getSlotLimit(i);
                ItemStack extractItem = this.items.extractItem(i, size, false);
                ItemHandlerHelper.giveItemToPlayer(player, extractItem);
            }
            this.discard();
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.interact(player, hand);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(MAID_NAME, TextComponent.EMPTY);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains(OWNER_ID_TAG)) {
            this.ownerId = tag.getUUID(OWNER_ID_TAG);
        }
        if (tag.contains(TOMBSTONE_ITEMS_TAG)) {
            items.deserializeNBT(tag.getCompound(TOMBSTONE_ITEMS_TAG));
        }
        if (tag.contains(MAID_NAME_TAG)) {
            String nameJson = tag.getString(MAID_NAME_TAG);
            setMaidName(Component.Serializer.fromJson(nameJson));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putUUID(OWNER_ID_TAG, this.ownerId);
        tag.put(TOMBSTONE_ITEMS_TAG, this.items.serializeNBT());
        tag.putString(MAID_NAME_TAG, Component.Serializer.toJson(this.getMaidName()));
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            this.checkOutOfWorld();
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
    public void remove(RemovalReason reason) {
        if (reason.shouldDestroy()) {
            MaidWorldData maidWorldData = MaidWorldData.get(level);
            if (maidWorldData != null) {
                maidWorldData.removeTombstones(this);
            }
        }
        super.remove(reason);
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
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public Component getMaidName() {
        return this.entityData.get(MAID_NAME);
    }

    public void setMaidName(@Nullable Component name) {
        if (name != null) {
            this.entityData.set(MAID_NAME, name);
        }
    }
}
