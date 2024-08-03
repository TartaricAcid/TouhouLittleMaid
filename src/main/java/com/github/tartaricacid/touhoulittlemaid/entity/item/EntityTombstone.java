package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Arrays;
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
        ItemStack itemInHand = player.getItemInHand(hand);
        Ingredient ntrItem = EntityMaid.getNtrItem();
        // NTR 工具可以收回墓碑
        if (player.getUUID().equals(this.ownerId) || ntrItem.test(itemInHand)) {
            for (int i = 0; i < this.items.getSlots(); i++) {
                int size = this.items.getSlotLimit(i);
                ItemStack extractItem = this.items.extractItem(i, size, false);
                ItemHandlerHelper.giveItemToPlayer(player, extractItem);
            }
            this.discard();
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!player.level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            ItemStack stack = Arrays.stream(ntrItem.getItems()).findFirst().orElse(ItemStack.EMPTY);
            Component displayName = stack.getDisplayName();
            player.sendSystemMessage(Component.translatable("message.touhou_little_maid.tombstone.not_yours.1"));
            player.sendSystemMessage(Component.translatable("message.touhou_little_maid.tombstone.not_yours.2").append(displayName));
        }
        return super.interact(player, hand);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(MAID_NAME, Component.empty());
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
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setMaidName(@Nullable Component name) {
        if (name != null) {
            this.entityData.set(MAID_NAME, name);
        }
    }

    public Component getMaidName() {
        return this.entityData.get(MAID_NAME);
    }

    public ItemStackHandler getItems() {
        return items;
    }
}
