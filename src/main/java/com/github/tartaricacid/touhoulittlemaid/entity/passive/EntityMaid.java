package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.config.Config;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.*;
import com.github.tartaricacid.touhoulittlemaid.entity.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.instance.TaskIdle;
import com.github.tartaricacid.touhoulittlemaid.event.api.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.event.api.MaidPlaySoundEvent;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.inventory.BaubleItemHandler;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidInventory;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.EntityArmorInvWrapper;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class EntityMaid extends TameableEntity implements INamedContainerProvider {
    public static final EntityType<EntityMaid> TYPE = EntityType.Builder.<EntityMaid>of(EntityMaid::new, EntityClassification.CREATURE)
            .sized(0.6f, 1.5f).clientTrackingRange(10).build("maid");

    private static final DataParameter<String> DATA_MODEL_ID = EntityDataManager.defineId(EntityMaid.class, DataSerializers.STRING);
    private static final DataParameter<String> DATA_TASK_ID = EntityDataManager.defineId(EntityMaid.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> DATA_BEGGING_ID = EntityDataManager.defineId(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_PICKUP_ID = EntityDataManager.defineId(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_HOME_ID = EntityDataManager.defineId(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_SHOW_HELMET_ID = EntityDataManager.defineId(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_INVULNERABLE_ID = EntityDataManager.defineId(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DATA_BACKPACK_LEVEL_ID = EntityDataManager.defineId(EntityMaid.class, DataSerializers.INT);

    private static final String MODEL_ID_TAG = "ModelId";
    private static final String TASK_TAG = "MaidTask";
    private static final String PICKUP_TAG = "ModelIsPickup";
    private static final String HOME_TAG = "ModelIsHome";
    private static final String SHOW_HELMET_TAG = "MaidShowHelmet";
    private static final String BACKPACK_LEVEL_TAG = "MaidBackpackLevel";
    private static final String MAID_INVENTORY_TAG = "MaidInventory";
    private static final String MAID_BAUBLE_INVENTORY_TAG = "MaidBaubleInventory";
    private static final String STRUCK_BY_LIGHTNING_TAG = "StruckByLightning";

    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
    private static final int TASK_PRIORITY = 5;
    private static final int HUNGER_VALUE_REFUSING_TASK = -256;
    /**
     * 玩家伤害女仆后的声音延时计数器
     */
    private static int PLAYER_HURT_SOUND_COUNT = 120;

    private final EntityArmorInvWrapper armorInvWrapper = new EntityArmorInvWrapper(this);
    private final EntityHandsInvWrapper handsInvWrapper = new EntityHandsInvWrapper(this) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return canInsertItem(stack);
        }
    };
    private final ItemStackHandler maidInv = new ItemStackHandler(36) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return canInsertItem(stack);
        }
    };
    private final BaubleItemHandler maidBauble = new BaubleItemHandler(9);

    public boolean guiOpening = false;
    /**
     * 用于替换背包延时的参数
     */
    private int backpackDelay = 0;
    private boolean struckByLightning = false;
    private boolean sleep = false;
    private IMaidTask task = TaskManager.getIdleTask();
    private Goal taskGoal;

    public EntityMaid(EntityType<EntityMaid> type, World world) {
        super(type, world);
        ((GroundPathNavigator) this.getNavigation()).setCanOpenDoors(true);
    }

    public EntityMaid(World worldIn) {
        this(TYPE, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute createMaidAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(InitEntities.HUNGER.get())
                .add(InitEntities.FAVORABILITY.get())
                .add(InitEntities.EXPERIENCE.get());
    }

    public static boolean canInsertItem(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem) {
            Block block = ((BlockItem) stack.getItem()).getBlock();
            return !(block instanceof ShulkerBoxBlock);
        }
        return true;
    }

    private static ImmutableList<MemoryModuleType<?>> getMemoryTypes() {
        return ImmutableList.of(
                MemoryModuleType.PATH,
                MemoryModuleType.DOORS_TO_CLOSE,
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.NEAREST_HOSTILE,
                MemoryModuleType.HURT_BY,
                MemoryModuleType.HURT_BY_ENTITY,
                MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleType.INTERACTION_TARGET
        );
    }

    private static ImmutableList<SensorType<? extends Sensor<? super EntityMaid>>> getSensorTypes() {
        return ImmutableList.of(
                SensorType.NEAREST_LIVING_ENTITIES,
                SensorType.HURT_BY,
                InitEntities.MAID_HOSTILES_SENSOR.get()
        );
    }

    public boolean canUseTaskGoal() {
        boolean guiNotOpening = !guiOpening;
        boolean isTamed = isTame();
        boolean notInSitting = !isInSittingPose();
        boolean notHunger = getAttributeValue(InitEntities.HUNGER.get()) > HUNGER_VALUE_REFUSING_TASK;
        boolean taskEnable = getTask().isEnable(this);
        return guiNotOpening && isTamed && notInSitting && notHunger && taskEnable;
    }

    @Override
    @SuppressWarnings("all")
    public Brain<EntityMaid> getBrain() {
        return (Brain<EntityMaid>) super.getBrain();
    }

    @Override
    protected Brain.BrainCodec<EntityMaid> brainProvider() {
        return Brain.provider(getMemoryTypes(), getSensorTypes());
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamicIn) {
        Brain<EntityMaid> brain = this.brainProvider().makeBrain(dynamicIn);

        Pair<Integer, SwimTask> swim = Pair.of(0, new SwimTask(0.5F));
        Pair<Integer, InteractWithDoorTask> interactWithDoor = Pair.of(0, new InteractWithDoorTask());
        Pair<Integer, LookTask> look = Pair.of(0, new LookTask(45, 90));
        Pair<Integer, MaidPanicTask> maidPanic = Pair.of(0, new MaidPanicTask());
        Pair<Integer, WalkToTargetTask> walkToTarget = Pair.of(1, new WalkToTargetTask());
        Pair<Integer, MaidFollowOwnerTask> followOwner = Pair.of(1, new MaidFollowOwnerTask(0.5f, 5, 2));

        brain.addActivity(Activity.CORE, ImmutableList.of(swim, interactWithDoor, look, maidPanic, walkToTarget, followOwner));

        Pair<Task<? super CreatureEntity>, Integer> lookToPlayer = Pair.of(new LookAtEntityTask(EntityType.PLAYER, 5), 1);
        Pair<Task<? super CreatureEntity>, Integer> lookToMaid = Pair.of(new LookAtEntityTask(EntityMaid.TYPE, 5), 1);
        Pair<Task<? super CreatureEntity>, Integer> lookToWolf = Pair.of(new LookAtEntityTask(EntityType.WOLF, 5), 1);
        Pair<Task<? super CreatureEntity>, Integer> lookToCat = Pair.of(new LookAtEntityTask(EntityType.CAT, 5), 1);
        Pair<Task<? super CreatureEntity>, Integer> lookToParrot = Pair.of(new LookAtEntityTask(EntityType.PARROT, 5), 1);
        Pair<Task<? super CreatureEntity>, Integer> walkRandomly = Pair.of(new MaidWalkRandomlyTask(0.3f, 3, 5), 1);
        Pair<Task<? super CreatureEntity>, Integer> noLook = Pair.of(new DummyTask(20, 40), 2);

        Pair<Integer, FirstShuffledTask<CreatureEntity>> shuffled = Pair.of(1, new FirstShuffledTask<>(
                ImmutableList.of(lookToPlayer, lookToMaid, lookToWolf, lookToCat, lookToParrot, walkRandomly, noLook)));

        Pair<Integer, MaidBegTask> beg = Pair.of(1, new MaidBegTask());
        Pair<Integer, FindInteractionAndLookTargetTask> getPlayer = Pair.of(1, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 6));
        brain.addActivity(Activity.IDLE, ImmutableList.of(shuffled, beg, getPlayer));

        Pair<Integer, MaidClearHurtTask> clearHurt = Pair.of(1, new MaidClearHurtTask());
        Pair<Integer, MaidRunAwayTask<? extends Entity>> runAway = Pair.of(1, MaidRunAwayTask.entity(MemoryModuleType.NEAREST_HOSTILE, 0.5f, 6, false));
        Pair<Integer, MaidRunAwayTask<? extends Entity>> runAwayHurt = Pair.of(1, MaidRunAwayTask.entity(MemoryModuleType.HURT_BY_ENTITY, 0.5f, 6, false));
        brain.addActivity(Activity.PANIC, ImmutableList.of(clearHurt, runAway, runAwayHurt));

        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("maidBrain");
        if (!guiOpening) {
            this.getBrain().tick((ServerWorld) this.level, this);
        }
        this.level.getProfiler().pop();
        super.customServerAiStep();
    }

    @Override
    public void tick() {
        super.tick();
        if (backpackDelay > 0) {
            backpackDelay--;
        }
        if (PLAYER_HURT_SOUND_COUNT > 0) {
            PLAYER_HURT_SOUND_COUNT--;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_MODEL_ID, DEFAULT_MODEL_ID);
        this.entityData.define(DATA_TASK_ID, TaskIdle.UID.toString());
        this.entityData.define(DATA_BEGGING_ID, false);
        this.entityData.define(DATA_PICKUP_ID, true);
        this.entityData.define(DATA_HOME_ID, false);
        this.entityData.define(DATA_SHOW_HELMET_ID, true);
        this.entityData.define(DATA_INVULNERABLE_ID, false);
        this.entityData.define(DATA_BACKPACK_LEVEL_ID, 0);
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity playerIn, Hand hand) {
        if (hand == Hand.MAIN_HAND && isOwnedBy(playerIn)) {
            ItemStack stack = playerIn.getMainHandItem();
            InteractMaidEvent event = new InteractMaidEvent(playerIn, this, stack);
            // 利用短路原理，逐个触发对应的交互事件
            if (MinecraftForge.EVENT_BUS.post(event)
                    || stack.interactLivingEntity(playerIn, this, hand).consumesAction()
                    || openMaidGui(playerIn)) {
                return ActionResultType.SUCCESS;
            }
        } else {
            return tameMaid(playerIn.getItemInHand(hand), playerIn);
        }
        return ActionResultType.PASS;
    }

    private ActionResultType tameMaid(ItemStack stack, PlayerEntity player) {
        boolean isNormal = !isTame() && stack.getItem() == getTamedItem();
        boolean isNtr = stack.getItem() == getNtrItem();
        if (isNormal || isNtr) {
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            this.tame(player);
            this.navigation.stop();
            this.setTarget(null);
            this.level.broadcastEntityEvent(this, (byte) 7);
            this.playSound(InitSounds.MAID_TAMED.get(), 1, 1);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    private boolean openMaidGui(PlayerEntity player) {
        // 否则打开 GUI
        if (player instanceof ServerPlayerEntity && !this.isSleep()) {
            this.navigation.stop();
            NetworkHooks.openGui((ServerPlayerEntity) player, this, (buffer) -> buffer.writeInt(getId()));
        }
        return true;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return null;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putString(MODEL_ID_TAG, getModelId());
        compound.putString(TASK_TAG, getTask().getUid().toString());
        compound.putBoolean(PICKUP_TAG, isPickup());
        compound.putBoolean(HOME_TAG, isHomeModeEnable());
        compound.putBoolean(SHOW_HELMET_TAG, isShowHelmet());
        compound.putInt(BACKPACK_LEVEL_TAG, getBackpackLevel());
        compound.put(MAID_INVENTORY_TAG, maidInv.serializeNBT());
        compound.put(MAID_BAUBLE_INVENTORY_TAG, maidBauble.serializeNBT());
        compound.putBoolean(STRUCK_BY_LIGHTNING_TAG, struckByLightning);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains(MODEL_ID_TAG, Constants.NBT.TAG_STRING)) {
            setModelId(compound.getString(MODEL_ID_TAG));
        }
        if (compound.contains(TASK_TAG, Constants.NBT.TAG_STRING)) {
            ResourceLocation uid = new ResourceLocation(compound.getString(TASK_TAG));
            IMaidTask task = TaskManager.findTask(uid).orElse(TaskManager.getIdleTask());
            setTask(task);
        }
        if (compound.contains(PICKUP_TAG, Constants.NBT.TAG_BYTE)) {
            setPickup(compound.getBoolean(PICKUP_TAG));
        }
        if (compound.contains(HOME_TAG, Constants.NBT.TAG_BYTE)) {
            setHomeModeEnable(compound.getBoolean(HOME_TAG));
        }
        if (compound.contains(SHOW_HELMET_TAG, Constants.NBT.TAG_BYTE)) {
            setShowHelmet(compound.getBoolean(SHOW_HELMET_TAG));
        }
        if (compound.contains(BACKPACK_LEVEL_TAG, Constants.NBT.TAG_INT)) {
            setBackpackLevel(compound.getInt(BACKPACK_LEVEL_TAG));
        }
        if (compound.contains(MAID_INVENTORY_TAG, Constants.NBT.TAG_COMPOUND)) {
            maidInv.deserializeNBT(compound.getCompound(MAID_INVENTORY_TAG));
        }
        if (compound.contains(MAID_BAUBLE_INVENTORY_TAG, Constants.NBT.TAG_COMPOUND)) {
            maidBauble.deserializeNBT(compound.getCompound(MAID_BAUBLE_INVENTORY_TAG));
        }
        if (compound.contains(STRUCK_BY_LIGHTNING_TAG, Constants.NBT.TAG_BYTE)) {
            struckByLightning = compound.getBoolean(STRUCK_BY_LIGHTNING_TAG);
        }
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity playerEntity) {
        return new MaidInventory(id, inventory, level, getId());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (this.isAlive() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != null) {
            if (facing.getAxis().isVertical()) {
                return LazyOptional.of(() -> handsInvWrapper).cast();
            }
            if (facing.getAxis().isHorizontal()) {
                return LazyOptional.of(() -> armorInvWrapper).cast();
            }
        }
        return super.getCapability(capability, facing);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (MinecraftForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
            return null;
        }
        return task.getAmbientSound(this);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        if (MinecraftForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
            return null;
        }
        if (damageSourceIn.isFire()) {
            return InitSounds.MAID_HURT_FIRE.get();
        } else if (damageSourceIn.getEntity() instanceof PlayerEntity) {
            if (PLAYER_HURT_SOUND_COUNT == 0) {
                PLAYER_HURT_SOUND_COUNT = 120;
                return InitSounds.MAID_PLAYER.get();
            } else {
                return null;
            }
        } else {
            return InitSounds.MAID_HURT.get();
        }
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        if (MinecraftForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
            return null;
        }
        return InitSounds.MAID_DEATH.get();
    }

    @Override
    protected float getVoicePitch() {
        return 1 + random.nextFloat() * 0.1F;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getBoundingBoxForCulling() {
        BedrockModel<EntityMaid> model = CustomPackLoader.MAID_MODEL.getModel(getModelId()).orElse(null);
        if (model == null) {
            return super.getBoundingBoxForCulling();
        }
        return model.getRenderBoundingBox().move(position());
    }

    public void setBackpackDelay() {
        backpackDelay = 20;
    }

    public boolean backpackNoDelay() {
        return backpackDelay <= 0;
    }

    public boolean isSleep() {
        return sleep;
    }

    public String getModelId() {
        return this.entityData.get(DATA_MODEL_ID);
    }

    public void setModelId(String modelId) {
        this.entityData.set(DATA_MODEL_ID, modelId);
    }

    public boolean isBegging() {
        return this.entityData.get(DATA_BEGGING_ID);
    }

    public void setBegging(boolean begging) {
        this.entityData.set(DATA_BEGGING_ID, begging);
    }

    public boolean isHomeModeEnable() {
        return this.entityData.get(DATA_HOME_ID);
    }

    public void setHomeModeEnable(boolean enable) {
        this.entityData.set(DATA_HOME_ID, enable);
    }

    public boolean isPickup() {
        return this.entityData.get(DATA_PICKUP_ID);
    }

    public void setPickup(boolean isPickup) {
        this.entityData.set(DATA_PICKUP_ID, isPickup);
    }

    public boolean isShowHelmet() {
        return this.entityData.get(DATA_SHOW_HELMET_ID);
    }

    public void setShowHelmet(boolean show) {
        this.entityData.set(DATA_SHOW_HELMET_ID, show);
    }

    public int getBackpackLevel() {
        return this.entityData.get(DATA_BACKPACK_LEVEL_ID);
    }

    public void setBackpackLevel(int level) {
        this.entityData.set(DATA_BACKPACK_LEVEL_ID, level);
    }

    public boolean hasBackpack() {
        return getBackpackLevel() > 0;
    }

    public ItemStackHandler getMaidInv() {
        return maidInv;
    }

    public BaubleItemHandler getMaidBauble() {
        return maidBauble;
    }

    public IMaidTask getTask() {
        ResourceLocation uid = new ResourceLocation(entityData.get(DATA_TASK_ID));
        return TaskManager.findTask(uid).orElse(TaskManager.getIdleTask());
    }

    public void setTask(IMaidTask task) {
        if (task == this.task) {
            return;
        }
        if (!level.isClientSide) {
            if (this.taskGoal != null) {
                goalSelector.removeGoal(taskGoal);
            }
            taskGoal = task.createGoal(this);
            if (taskGoal != null) {
                goalSelector.addGoal(TASK_PRIORITY, taskGoal);
            }
        }
        this.task = task;
        this.entityData.set(DATA_TASK_ID, task.getUid().toString());
    }

    @Override
    public void setInSittingPose(boolean inSittingPose) {
        super.setInSittingPose(inSittingPose);
        setOrderedToSit(inSittingPose);
    }

    public boolean hasHelmet() {
        return !getItemBySlot(EquipmentSlotType.HEAD).isEmpty();
    }

    public boolean hasChestPlate() {
        return !getItemBySlot(EquipmentSlotType.CHEST).isEmpty();
    }

    public boolean hasLeggings() {
        return !getItemBySlot(EquipmentSlotType.LEGS).isEmpty();
    }

    public boolean hasBoots() {
        return !getItemBySlot(EquipmentSlotType.FEET).isEmpty();
    }

    public boolean onHurt() {
        return hurtTime > 0;
    }

    @Deprecated
    public boolean hasSasimono() {
        return false;
    }

    public String getAtBiomeTemp() {
        // FIXME：待修正
        return "";
    }

    public boolean isSitInJoyBlock() {
        // TODO：待完成
        return false;
    }

    public int getDim() {
        // TODO：待完成
        return 0;
    }

    public Item getTamedItem() {
        ResourceLocation key = new ResourceLocation(Config.MAID_TAMED_ITEM.get());
        if (ForgeRegistries.ITEMS.containsKey(key)) {
            return ForgeRegistries.ITEMS.getValue(key);
        }
        return Items.CAKE;
    }

    public Item getTemptationItem() {
        ResourceLocation key = new ResourceLocation(Config.MAID_TEMPTATION_ITEM.get());
        if (ForgeRegistries.ITEMS.containsKey(key)) {
            return ForgeRegistries.ITEMS.getValue(key);
        }
        return Items.CAKE;
    }

    public Item getNtrItem() {
        ResourceLocation key = new ResourceLocation(Config.MAID_NTR_ITEM.get());
        if (ForgeRegistries.ITEMS.containsKey(key)) {
            return ForgeRegistries.ITEMS.getValue(key);
        }
        return Items.STRUCTURE_VOID;
    }
}
