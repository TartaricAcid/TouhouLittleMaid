package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityTombstone extends Entity {
    public static final EntityType<EntityTombstone> TYPE = EntityType.Builder.<EntityTombstone>of(EntityTombstone::new, EntityClassification.MISC)
            .sized(0.8f, 1.2f).clientTrackingRange(10).build("tombstone");
    private static final String OWNER_ID_TAG = "OwnerId";
    private static final String TOMBSTONE_ITEMS_TAG = "TombstoneItems";
    private static final String MAID_NAME_TAG = "MaidName";
    private static final DataParameter<ITextComponent> MAID_NAME = EntityDataManager.defineId(EntityTombstone.class, DataSerializers.COMPONENT);
    private final ItemStackHandler items = new ItemStackHandler(64);
    private UUID ownerId = Util.NIL_UUID;

    public EntityTombstone(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EntityTombstone(World worldIn, UUID ownerId, Vector3d pos) {
        this(TYPE, worldIn);
        this.ownerId = ownerId;
        this.setPos(pos.x, pos.y, pos.z);
    }

    public void insertItem(ItemStack item) {
        ItemHandlerHelper.insertItemStacked(this.items, item, false);
    }

    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        if (player.getUUID().equals(this.ownerId)) {
            for (int i = 0; i < this.items.getSlots(); i++) {
                int size = this.items.getSlotLimit(i);
                ItemStack extractItem = this.items.extractItem(i, size, false);
                ItemHandlerHelper.giveItemToPlayer(player, extractItem);
            }
            this.removeFromData();
            return ActionResultType.sidedSuccess(level.isClientSide);
        }
        return super.interact(player, hand);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(MAID_NAME, StringTextComponent.EMPTY);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT tag) {
        if (tag.contains(OWNER_ID_TAG)) {
            this.ownerId = tag.getUUID(OWNER_ID_TAG);
        }
        if (tag.contains(TOMBSTONE_ITEMS_TAG)) {
            items.deserializeNBT(tag.getCompound(TOMBSTONE_ITEMS_TAG));
        }
        if (tag.contains(MAID_NAME_TAG)) {
            String nameJson = tag.getString(MAID_NAME_TAG);
            setMaidName(ITextComponent.Serializer.fromJson(nameJson));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT tag) {
        tag.putUUID(OWNER_ID_TAG, this.ownerId);
        tag.put(TOMBSTONE_ITEMS_TAG, this.items.serializeNBT());
        tag.putString(MAID_NAME_TAG, ITextComponent.Serializer.toJson(this.getMaidName()));
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            this.checkBelowWorld();
        }
    }

    private void checkBelowWorld() {
        if (this.getY() < -64.0D) {
            this.outOfWorld();
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
    public void move(MoverType pType, Vector3d pPos) {
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
    public void thunderHit(ServerWorld pLevel, LightningBoltEntity pLightning) {
    }

    @Override
    public void refreshDimensions() {
    }

    private void removeFromData() {
        MaidWorldData maidWorldData = MaidWorldData.get(level);
        if (maidWorldData != null) {
            maidWorldData.removeTombstones(this);
        }
        this.remove();
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
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public ITextComponent getMaidName() {
        return this.entityData.get(MAID_NAME);
    }

    public void setMaidName(@Nullable ITextComponent name) {
        if (name != null) {
            this.entityData.set(MAID_NAME, name);
        }
    }
}
