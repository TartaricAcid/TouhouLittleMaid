package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IBackpackData;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.*;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidBrain;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.*;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManger;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatText;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.MaidChatBubbles;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.MaidScriptBookManager;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.FavorabilityManager;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.info.ServerCustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskIdle;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.MaidBackpackHandler;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.MaidHandsInvWrapper;
import com.github.tartaricacid.touhoulittlemaid.item.ItemFilm;
import com.github.tartaricacid.touhoulittlemaid.mixin.MixinArrowEntity;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.ItemBreakMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.PlayMaidSoundMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SendEffectMessage;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.github.tartaricacid.touhoulittlemaid.util.TeleportHelper;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import com.google.common.collect.Lists;
import com.mojang.serialization.Dynamic;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.neoforged.neoforge.items.wrapper.EntityArmorInvWrapper;
import net.neoforged.neoforge.items.wrapper.EntityHandsInvWrapper;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class EntityMaid extends TamableAnimal implements CrossbowAttackMob, IMaid {
    public static final EntityType<EntityMaid> TYPE = EntityType.Builder.<EntityMaid>of(EntityMaid::new, MobCategory.CREATURE)
            .sized(0.6f, 1.5f).clientTrackingRange(10).build("maid");
    public static final String MODEL_ID_TAG = "ModelId";
    public static final String SOUND_PACK_ID_TAG = "SoundPackId";
    public static final String MAID_BACKPACK_TYPE = "MaidBackpackType";
    public static final String MAID_INVENTORY_TAG = "MaidInventory";
    public static final String MAID_BAUBLE_INVENTORY_TAG = "MaidBaubleInventory";
    public static final String EXPERIENCE_TAG = "MaidExperience";
    private static final EntityDataAccessor<String> DATA_MODEL_ID = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_SOUND_PACK_ID = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_TASK = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_BEGGING = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_PICKUP = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_HOME_MODE = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_RIDEABLE = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_INVULNERABLE = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_HUNGER = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_FAVORABILITY = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_EXPERIENCE = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_STRUCK_BY_LIGHTNING = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ARM_RISE = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<MaidSchedule> SCHEDULE_MODE = SynchedEntityData.defineId(EntityMaid.class, MaidSchedule.DATA);
    private static final EntityDataAccessor<BlockPos> RESTRICT_CENTER = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Float> RESTRICT_RADIUS = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<MaidChatBubbles> CHAT_BUBBLE = SynchedEntityData.defineId(EntityMaid.class, MaidChatBubbles.DATA);
    private static final EntityDataAccessor<String> BACKPACK_TYPE = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<ItemStack> BACKPACK_ITEM_SHOW = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<String> BACKPACK_FLUID = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<CompoundTag> GAME_SKILL = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.COMPOUND_TAG);
    private static final String TASK_TAG = "MaidTask";
    private static final String PICKUP_TAG = "MaidIsPickup";
    private static final String HOME_TAG = "MaidIsHome";
    private static final String RIDEABLE_TAG = "MaidIsRideable";
    private static final String STRUCK_BY_LIGHTNING_TAG = "StruckByLightning";
    private static final String INVULNERABLE_TAG = "Invulnerable";
    private static final String HUNGER_TAG = "MaidHunger";
    private static final String FAVORABILITY_TAG = "MaidFavorability";
    private static final String SCHEDULE_MODE_TAG = "MaidScheduleMode";
    private static final String BACKPACK_DATA_TAG = "MaidBackpackData";
    private static final String GAME_SKILL_TAG = "MaidGameSkillData";
    @Deprecated
    private static final String BACKPACK_LEVEL_TAG = "MaidBackpackLevel";
    @Deprecated
    private static final String RESTRICT_CENTER_TAG = "MaidRestrictCenter";

    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
    public final ItemStack[] handItemsForAnimation = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
    private final EntityArmorInvWrapper armorInvWrapper = new EntityArmorInvWrapper(this);
    private final EntityHandsInvWrapper handsInvWrapper = new MaidHandsInvWrapper(this);
    private final ItemStackHandler maidInv = new MaidBackpackHandler(36, this);
    private final BaubleItemHandler maidBauble = new BaubleItemHandler(9);
    private final FavorabilityManager favorabilityManager;
    private final MaidScriptBookManager scriptBookManager;
    private final SchedulePos schedulePos;
    public boolean guiOpening = false;

    private List<SendEffectMessage.EffectData> effects = Lists.newArrayList();
    private IMaidTask task = TaskManager.getIdleTask();
    private IMaidBackpack backpack = BackpackManager.getEmptyBackpack();
    private int playerHurtSoundCount = 120;
    private int pickupSoundCount = 5;
    private int backpackDelay = 0;
    private IBackpackData backpackData = null;

    protected EntityMaid(EntityType<EntityMaid> type, Level world) {
        super(type, world);
        ((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
        this.getNavigation().setCanFloat(true);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.favorabilityManager = new FavorabilityManager(this);
        this.scriptBookManager = new MaidScriptBookManager();
        this.schedulePos = new SchedulePos(BlockPos.ZERO, world.dimension().location());
    }

    public EntityMaid(Level worldIn) {
        this(TYPE, worldIn);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 64).add(Attributes.ATTACK_KNOCKBACK).add(Attributes.ATTACK_DAMAGE);
    }

    public static boolean canInsertItem(ItemStack stack) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (key != null && MaidConfig.MAID_BACKPACK_BLACKLIST.get().contains(key.toString())) {
            return false;
        }
        return stack.getItem().canFitInsideContainerItems();
    }

    @SuppressWarnings("all")
    public static Ingredient getNtrItem() {
        return getConfigIngredient(MaidConfig.MAID_NTR_ITEM.get(), Items.STRUCTURE_VOID);
    }

    private static Ingredient getConfigIngredient(String config, Item defaultItem) {
        if (config.startsWith(MaidConfig.TAG_PREFIX)) {
            ITagManager<Item> tags = ForgeRegistries.ITEMS.tags();
            if (tags != null) {
                ResourceLocation key = new ResourceLocation(config.substring(1));
                TagKey<Item> tagKey = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), key);
                if (tags.isKnownTagName(tagKey)) {
                    return Ingredient.of(tagKey);
                }
            }
        } else {
            ResourceLocation key = new ResourceLocation(config);
            if (ForgeRegistries.ITEMS.containsKey(key)) {
                return Ingredient.of(ForgeRegistries.ITEMS.getValue(key));
            }
        }
        return Ingredient.of(defaultItem);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_MODEL_ID, DEFAULT_MODEL_ID);
        this.entityData.define(DATA_SOUND_PACK_ID, DefaultMaidSoundPack.getInitSoundPackId());
        this.entityData.define(DATA_TASK, TaskIdle.UID.toString());
        this.entityData.define(DATA_BEGGING, false);
        this.entityData.define(DATA_PICKUP, true);
        this.entityData.define(DATA_HOME_MODE, false);
        this.entityData.define(DATA_RIDEABLE, true);
        this.entityData.define(DATA_INVULNERABLE, false);
        this.entityData.define(DATA_HUNGER, 0);
        this.entityData.define(DATA_FAVORABILITY, 0);
        this.entityData.define(DATA_EXPERIENCE, 0);
        this.entityData.define(DATA_STRUCK_BY_LIGHTNING, false);
        this.entityData.define(DATA_IS_CHARGING_CROSSBOW, false);
        this.entityData.define(DATA_ARM_RISE, false);
        this.entityData.define(SCHEDULE_MODE, MaidSchedule.DAY);
        this.entityData.define(RESTRICT_CENTER, BlockPos.ZERO);
        this.entityData.define(RESTRICT_RADIUS, MaidConfig.MAID_NON_HOME_RANGE.get().floatValue());
        this.entityData.define(CHAT_BUBBLE, MaidChatBubbles.DEFAULT);
        this.entityData.define(BACKPACK_TYPE, EmptyBackpack.ID.toString());
        this.entityData.define(BACKPACK_ITEM_SHOW, ItemStack.EMPTY);
        this.entityData.define(BACKPACK_FLUID, StringUtils.EMPTY);
        this.entityData.define(GAME_SKILL, new CompoundTag());
    }

    @Override
    @SuppressWarnings("all")
    public Brain<EntityMaid> getBrain() {
        return (Brain<EntityMaid>) super.getBrain();
    }

    @Override
    protected Brain.Provider<EntityMaid> brainProvider() {
        return Brain.provider(MaidBrain.getMemoryTypes(), MaidBrain.getSensorTypes());
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamicIn) {
        Brain<EntityMaid> brain = this.brainProvider().makeBrain(dynamicIn);
        MaidBrain.registerBrainGoals(brain, this);
        return brain;
    }

    public void refreshBrain(ServerLevel serverWorldIn) {
        Brain<EntityMaid> brain = this.getBrain();
        brain.stopAll(serverWorldIn, this);
        this.brain = brain.copyWithoutBehaviors();
        MaidBrain.registerBrainGoals(this.getBrain(), this);
    }

    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("maidBrain");
        if (!guiOpening) {
            this.getBrain().tick((ServerLevel) this.level, this);
        }
        this.level.getProfiler().pop();
        super.customServerAiStep();
    }

    @Override
    public void tick() {
        if (!NeoForge.EVENT_BUS.post(new MaidTickEvent(this)).isCanceled()) {
            super.tick();
            maidBauble.fireEvent((b, s) -> {
                b.onTick(this, s);
                return false;
            });
        }
    }

    @Override
    public void rideTick() {
        super.rideTick();
        if (this.getVehicle() != null) {
            Entity vehicle = this.getVehicle();
            this.setYHeadRot(vehicle.getYRot());
            this.setYBodyRot(vehicle.getYRot());
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (backpackDelay > 0) {
            backpackDelay--;
        }
        if (playerHurtSoundCount > 0) {
            playerHurtSoundCount--;
        }
        this.spawnPortalParticle();
        this.randomRestoreHealth();
        this.onMaidSleep();
    }

    private void onMaidSleep() {
        if (isSleeping()) {
            getSleepingPos().ifPresent(pos -> setPos(pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5));
            setDeltaMovement(Vec3.ZERO);
            if (!isSilent()) {
                this.setSilent(true);
            }
        } else {
            if (isSilent()) {
                this.setSilent(false);
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();
        if (!level.isClientSide) {
            ChatBubbleManger.tick(this);
            if (this.backpackData != null) {
                this.level.getProfiler().push("maidBackpackData");
                this.backpackData.serverTick(this);
                this.level.getProfiler().pop();
            }

            this.level.getProfiler().push("maidFavorability");
            this.favorabilityManager.tick();
            this.level.getProfiler().pop();

            this.level.getProfiler().push("maidSchedulePos");
            this.schedulePos.tick(this);
            this.level.getProfiler().pop();
        }
    }

    @Override
    public InteractionResult mobInteract(Player playerIn, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND && isOwnedBy(playerIn)) {
            ItemStack stack = playerIn.getMainHandItem();
            InteractMaidEvent event = new InteractMaidEvent(playerIn, this, stack);
            // 利用短路原理，逐个触发对应的交互事件
            if (NeoForge.EVENT_BUS.post(event).isCanceled()
                    || stack.interactLivingEntity(playerIn, this, hand).consumesAction()
                    || openMaidGui(playerIn)) {
                return InteractionResult.SUCCESS;
            }
        } else {
            return tameMaid(playerIn.getItemInHand(hand), playerIn);
        }
        return InteractionResult.PASS;
    }

    private InteractionResult tameMaid(ItemStack stack, Player player) {
        return player.getCapability(MaidNumCapabilityProvider.MAID_NUM_CAP).map(cap -> {
            if (cap.canAdd() || player.isCreative()) {
                boolean isNormal = !isTame() && getTamedItem().test(stack);
                boolean isNtr = getNtrItem().test(stack);
                if (isNormal || isNtr) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                        cap.add();
                    }
                    this.tame(player);
                    // 清掉寻路，清掉敌对记忆
                    this.navigation.stop();
                    this.setTarget(null);
                    this.brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                    this.playSound(InitSounds.MAID_TAMED.get(), 1, 1);
                    return InteractionResult.SUCCESS;
                }
            } else {
                if (player instanceof ServerPlayer) {
                    MutableComponent msg = Component.translatable("message.touhou_little_maid.owner_maid_num.can_not_add",
                            cap.get(), cap.getMaxNum());
                    player.sendSystemMessage(msg);
                }
            }
            return InteractionResult.PASS;
        }).orElse(InteractionResult.PASS);
    }

    @Override
    protected void pushEntities() {
        super.pushEntities();
        // 只有拾物模式开启，驯服状态下才可以捡起物品
        if (this.isPickup() && this.isTame()) {
            List<Entity> entityList = this.level.getEntities(this,
                    this.getBoundingBox().inflate(0.5, 0, 0.5), this::canPickup);
            if (!entityList.isEmpty() && this.isAlive()) {
                for (Entity entityPickup : entityList) {
                    // 如果是物品
                    if (entityPickup instanceof ItemEntity) {
                        pickupItem((ItemEntity) entityPickup, false);
                    }
                    // 如果是经验
                    if (entityPickup instanceof ExperienceOrb) {
                        pickupXPOrb((ExperienceOrb) entityPickup);
                    }
                    // 如果是 P 点
                    if (entityPickup instanceof EntityPowerPoint) {
                        pickupPowerPoint((EntityPowerPoint) entityPickup);
                    }
                    // 如果是箭
                    if (entityPickup instanceof AbstractArrow) {
                        pickupArrow((AbstractArrow) entityPickup, false);
                    }
                }
            }
        }
    }

    public boolean pickupItem(ItemEntity entityItem, boolean simulate) {
        if (!level.isClientSide && entityItem.isAlive() && !entityItem.hasPickUpDelay()) {
            // 获取实体的物品堆
            ItemStack itemstack = entityItem.getItem();
            // 检查物品是否合法
            if (!canInsertItem(itemstack)) {
                return false;
            }
            // 获取数量，为后面方面用
            int count = itemstack.getCount();
            itemstack = ItemHandlerHelper.insertItemStacked(getAvailableInv(false), itemstack, simulate);
            if (count == itemstack.getCount()) {
                return false;
            }
            if (!simulate) {
                // 这是向客户端同步数据用的，如果加了这个方法，会有短暂的拾取动画和音效
                this.take(entityItem, count - itemstack.getCount());
                if (!NeoForge.EVENT_BUS.post(new MaidPlaySoundEvent(this)).isCanceled()) {
                    pickupSoundCount--;
                    if (pickupSoundCount == 0) {
                        this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
                        pickupSoundCount = 5;
                    }
                }
                // 如果遍历塞完后发现为空了
                if (itemstack.isEmpty()) {
                    // 清除这个实体
                    entityItem.discard();
                } else {
                    // 将物品数量同步到客户端
                    entityItem.setItem(itemstack);
                }
            }
            return true;
        }
        return false;
    }

    public void pickupXPOrb(ExperienceOrb entityXPOrb) {
        if (!this.level.isClientSide && entityXPOrb.isAlive() && entityXPOrb.tickCount > 2) {
            // 这是向客户端同步数据用的，如果加了这个方法，会有短暂的拾取动画和音效
            this.take(entityXPOrb, 1);
            if (!NeoForge.EVENT_BUS.post(new MaidPlaySoundEvent(this)).isCanceled()) {
                pickupSoundCount--;
                if (pickupSoundCount == 0) {
                    this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
                    pickupSoundCount = 5;
                }
            }

            // 对经验修补的应用，因为全部来自于原版，所以效果也是相同的
            Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, this, ItemStack::isDamaged);
            if (entry != null) {
                ItemStack itemstack = entry.getValue();
                if (!itemstack.isEmpty() && itemstack.isDamaged()) {
                    int i = Math.min((int) (entityXPOrb.value * itemstack.getXpRepairRatio()), itemstack.getDamageValue());
                    entityXPOrb.value -= (i / 2);
                    itemstack.setDamageValue(itemstack.getDamageValue() - i);
                }
            }
            if (entityXPOrb.value > 0) {
                this.setExperience(getExperience() + entityXPOrb.value);
            }
            entityXPOrb.discard();
        }
    }

    public void pickupPowerPoint(EntityPowerPoint powerPoint) {
        if (!this.level.isClientSide && powerPoint.isAlive() && powerPoint.throwTime == 0) {
            // 这是向客户端同步数据用的，如果加了这个方法，会有短暂的拾取动画和音效
            powerPoint.take(this, 1);
            if (!NeoForge.EVENT_BUS.post(new MaidPlaySoundEvent(this)).isCanceled()) {
                pickupSoundCount--;
                if (pickupSoundCount == 0) {
                    this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
                    pickupSoundCount = 5;
                }
            }

            // 对经验修补的应用，因为全部来自于原版，所以效果也是相同的
            ItemStack itemstack = this.getRandomItemWithMendingEnchantments();
            int xpValue = EntityPowerPoint.transPowerValueToXpValue(powerPoint.getValue());
            if (!itemstack.isEmpty()) {
                if (!itemstack.isEmpty() && itemstack.isDamaged()) {
                    int i = Math.min((int) (xpValue * itemstack.getXpRepairRatio()), itemstack.getDamageValue());
                    xpValue -= (i / 2);
                    itemstack.setDamageValue(itemstack.getDamageValue() - i);
                }
            }
            if (xpValue > 0) {
                this.setExperience(getExperience() + xpValue);
            }
            powerPoint.discard();
        }
    }

    private ItemStack getRandomItemWithMendingEnchantments() {
        List<ItemStack> stacks = Lists.newArrayList();
        this.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(cap -> {
            for (int i = 0; i < cap.getSlots(); i++) {
                ItemStack itemstack = cap.getStackInSlot(i);
                if (!itemstack.isEmpty() && itemstack.getEnchantmentLevel(Enchantments.MENDING) > 0 && itemstack.isDamaged()) {
                    stacks.add(itemstack);
                }
            }
        });
        return stacks.isEmpty() ? ItemStack.EMPTY : stacks.get(this.getRandom().nextInt(stacks.size()));
    }

    public boolean pickupArrow(AbstractArrow arrow, boolean simulate) {
        if (!this.level.isClientSide && arrow.isAlive() && arrow.shakeTime <= 0) {
            // 先判断箭是否处于可以拾起的状态
            if (arrow.pickup != AbstractArrow.Pickup.ALLOWED) {
                return false;
            }
            // 能够塞入
            ItemStack stack = getArrowFromEntity(arrow);
            if (stack.isEmpty()) {
                return false;
            }
            if (!ItemHandlerHelper.insertItemStacked(getAvailableInv(false), stack, simulate).isEmpty()) {
                return false;
            }
            // 非模拟状态下，清除实体箭
            if (!simulate) {
                // 这是向客户端同步数据用的，如果加了这个方法，会有短暂的拾取动画和音效
                this.take(arrow, 1);
                if (!NeoForge.EVENT_BUS.post(new MaidPlaySoundEvent(this)).isCanceled()) {
                    pickupSoundCount--;
                    if (pickupSoundCount == 0) {
                        this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
                        pickupSoundCount = 5;
                    }
                }
                arrow.discard();
            }
            return true;
        }
        return false;
    }

    private ItemStack getArrowFromEntity(AbstractArrow entity) {
        if (entity instanceof MixinArrowEntity mixinArrow) {
            if (mixinArrow.tlmInGround() || entity.isNoPhysics()) {
                return mixinArrow.getTlmPickupItem();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity entity) {
        int attackDistance = this.favorabilityManager.getAttackDistanceByPoint(this.getFavorability());
        return attackDistance * attackDistance;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean result = super.doHurtTarget(target);
        if (result) {
            doSweepHurt(target);
        }
        this.getMainHandItem().hurtAndBreak(1, this, EquipmentSlot.MAINHAND);
        if (this.getTask() instanceof IAttackTask attackTask && attackTask.hasExtraAttack(this, target)) {
            boolean extraResult = attackTask.doExtraAttack(this, target);
            return result && extraResult;
        }
        return result;
    }

    private void doSweepHurt(Entity target) {
        ItemStack mainhandItem = this.getItemInHand(InteractionHand.MAIN_HAND);
        boolean canSweep = mainhandItem.canPerformAction(net.neoforged.neoforge.common.ItemAbilities.SWORD_SWEEP);
        float sweepingDamageRatio = (float) this.getAttributes().getValue(Attributes.SWEEPING_DAMAGE_RATIO);
        //TODO : 需要设置横扫范围
        if (canSweep && sweepingDamageRatio > 0) {
            float baseDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float sweepDamage = 1.0f + sweepingDamageRatio * baseDamage;
            AABB sweepRange = this.getFavorabilityManager().getSweepRange(target, this.getFavorability());
            List<LivingEntity> hurtEntities = this.level.getEntitiesOfClass(LivingEntity.class, sweepRange);
            for (LivingEntity entity : hurtEntities) {
                if (entity != this && entity != target && !this.isAlliedTo(entity) && canAttack(entity) && canAttackType(entity.getType())) {
                    float posX = Mth.sin(this.getYRot() * ((float) Math.PI / 180F));
                    float posY = -Mth.cos(this.getYRot() * ((float) Math.PI / 180F));
                    entity.knockback(0.4, posX, posY);
                    entity.hurt(this.damageSources().mobAttack(this), sweepDamage);
                }
            }
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, this.getSoundSource(), 1, 1);
            this.spawnSweepAttackParticle();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (NeoForge.EVENT_BUS.post(new MaidAttackEvent(this, source, amount)).isCanceled()) {
            return false;
        }
        if (source.getEntity() instanceof Player && this.isOwnedBy((Player) source.getEntity())) {
            // 玩家对自己女仆的伤害数值为 1/5，最大为 2
            amount = Mth.clamp(amount / 5, 0, 2);
        }
        return super.hurt(source, amount);
    }

    /**
     * 重新复写父类方法，添加上自己的 Event
     */
    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        if (!this.isInvulnerableTo(damageSrc)) {
            MaidHurtEvent maidHurtEvent = new MaidHurtEvent(this, damageSrc, damageAmount);
            damageAmount = NeoForge.EVENT_BUS.post(maidHurtEvent).isCanceled() ? 0 : maidHurtEvent.getAmount();
            damageAmount = ForgeHooks.onLivingHurt(this, damageSrc, damageAmount);
            if (damageAmount > 0) {
                damageAmount = this.getDamageAfterArmorAbsorb(damageSrc, damageAmount);
                damageAmount = this.getDamageAfterMagicAbsorb(damageSrc, damageAmount);
                float damageAfterAbsorption = Math.max(damageAmount - this.getAbsorptionAmount(), 0);
                this.setAbsorptionAmount(this.getAbsorptionAmount() - (damageAmount - damageAfterAbsorption));
                float damageDealtAbsorbed = damageAmount - damageAfterAbsorption;
                if (0 < damageDealtAbsorbed && damageDealtAbsorbed < (Float.MAX_VALUE / 10) && damageSrc.getEntity() instanceof ServerPlayer) {
                    ((ServerPlayer) damageSrc.getEntity()).awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(damageDealtAbsorbed * 10));
                }
                MaidDamageEvent maidDamageEvent = new MaidDamageEvent(this, damageSrc, damageAfterAbsorption);
                damageAfterAbsorption = NeoForge.EVENT_BUS.post(maidDamageEvent) ? 0 : maidDamageEvent.getAmount();
                damageAfterAbsorption = ForgeHooks.onLivingDamage(this, damageSrc, damageAfterAbsorption);
                if (damageAfterAbsorption != 0) {
                    float health = this.getHealth();
                    this.getCombatTracker().recordDamage(damageSrc, damageAfterAbsorption);
                    this.setHealth(health - damageAfterAbsorption);
                    this.setAbsorptionAmount(this.getAbsorptionAmount() - damageAfterAbsorption);
                }
            }
        }
    }

    @Nullable
    @Override
    public Entity changeDimension(ServerLevel serverLevel, ITeleporter teleporter) {
        if (this.level instanceof ServerLevel && !this.isRemoved()) {
            final int MAX_RETRY = 16;
            for (int i = 0; i < MAX_RETRY; ++i) {
                if (TeleportHelper.teleport(this)) {
                    this.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 1, true, false));
                }
            }
        }
        return null;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.getOwnerUUID() != null) {
            MaidWorldData data = MaidWorldData.get(this.level);
            if (data != null) {
                data.removeInfo(this);
            }
        }
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (!this.level.isClientSide && this.isAlive() && this.getOwnerUUID() != null) {
            MaidWorldData data = MaidWorldData.get(this.level);
            if (data != null) {
                data.addInfo(this);
            }
        }
    }

    @Override
    public void die(DamageSource cause) {
        if (!NeoForge.EVENT_BUS.post(new MaidDeathEvent(this, cause))) {
            super.die(cause);
        }
    }

    public boolean canPickup(Entity pickupEntity, boolean checkInWater) {
        if (isPickup()) {
            if (checkInWater && pickupEntity.isInWater()) {
                return false;
            }
            if (pickupEntity instanceof ItemEntity) {
                return pickupItem((ItemEntity) pickupEntity, true);
            }
            if (pickupEntity instanceof AbstractArrow) {
                return pickupArrow((AbstractArrow) pickupEntity, true);
            }
            if (pickupEntity instanceof ExperienceOrb) {
                return true;
            }
            return pickupEntity instanceof EntityPowerPoint;
        }
        return false;
    }

    public boolean canPickup(Entity pickupEntity) {
        return canPickup(pickupEntity, false);
    }

    @Override
    public void setChargingCrossbow(boolean isCharging) {
        this.entityData.set(DATA_IS_CHARGING_CROSSBOW, isCharging);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity target, ItemStack crossbow, Projectile projectileEntity, float projectileAngle) {
        // 弩箭伤害也和好感度挂钩
        // 但是烟花火箭的伤害是很特殊的，就不应用了
        if (projectileEntity instanceof AbstractArrow arrow) {
            AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
            double attackValue = 2.0;
            if (attackDamage != null) {
                attackValue = attackDamage.getBaseValue();
            }
            float multiplier = (float) (attackValue / 2.0f);
            arrow.setBaseDamage(arrow.getBaseDamage() * multiplier);
        }
        this.shootCrossbowProjectile(this, target, projectileEntity, projectileAngle, 1.6F);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    @Nullable
    public LivingEntity getTarget() {
        // 实现 CrossbowAttackMob 接口中拿到目标实体的方法
        return this.brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    // 弩在装载时的 tryLoadProjectiles 方法会从这里拿到需要装填的物品
    @Override
    public ItemStack getProjectile(ItemStack weaponStack) {
        // 烟花只检查副手：优先检查副手有没有烟花
        if (this.getOffhandItem().getItem() instanceof FireworkRocketItem) {
            return this.getOffhandItem();
        }
        CombinedInvWrapper handler = this.getAvailableInv(true);
        int slot = ItemsUtil.findStackSlot(handler, ((CrossbowItem) this.getMainHandItem().getItem()).getAllSupportedProjectiles());
        if (slot < 0) {
            // 不存在时，返回空
            return ItemStack.EMPTY;
        } else {
            // 拿到弹药物品
            return handler.getStackInSlot(slot);
        }
    }

    @Override
    public void thunderHit(ServerLevel world, LightningBolt lightning) {
        super.thunderHit(world, lightning);
        if (!isStruckByLightning()) {
            double beforeMaxHealth = this.getAttributeBaseValue(Attributes.MAX_HEALTH);
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(beforeMaxHealth + 20);
            setStruckByLightning(true);
        }
    }

    @Override
    protected void hurtArmor(DamageSource damageSource, float damage) {
        // 依据原版玩家护甲耐久掉落机制书写而成
        // net.minecraft.entity.player.PlayerInventory#hurtArmor
        if (damage <= 0.0F) {
            return;
        }

        damage = damage / 4.0F;

        // 最小伤害必须为 1.0
        if (damage < 1.0F) {
            damage = 1.0F;
        }

        for (int i = 0; i < this.armorInvWrapper.getSlots(); ++i) {
            ItemStack stack = this.armorInvWrapper.getStackInSlot(i);
            boolean fireResistant = damageSource.is(DamageTypeTags.IS_FIRE) && stack.getItem().isFireResistant();
            if (!fireResistant && stack.getItem() instanceof ArmorItem) {
                final int index = i;
                stack.hurtAndBreak((int) damage, this,
                        (maid) -> maid.broadcastBreakEvent(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, index)));
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        IMaidTask maidTask = this.getTask();
        if (maidTask instanceof IRangedAttackTask) {
            ((IRangedAttackTask) maidTask).performRangedAttack(this, target, distanceFactor);
        }
    }

    @Override
    public boolean canAttackType(EntityType<?> typeIn) {
        return typeIn != EntityType.ARMOR_STAND && super.canAttackType(typeIn);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (this.getTask() instanceof IAttackTask attackTask) {
            return attackTask.canAttack(this, target);
        }
        return super.canAttack(target);
    }

    public void sendItemBreakMessage(ItemStack stack) {
        if (!this.level.isClientSide) {
            NetworkHandler.sendToNearby(this, new ItemBreakMessage(this.getId(), stack));
        }
    }

    private void randomRestoreHealth() {
        if (this.getHealth() < this.getMaxHealth() && random.nextFloat() < 0.0025) {
            this.heal(1);
            this.spawnRestoreHealthParticle(random.nextInt(3) + 7);
        }
    }

    private void spawnPortalParticle() {
        if (this.level.isClientSide && this.getIsInvulnerable() && this.getOwner() != null) {
            this.level.addParticle(ParticleTypes.PORTAL,
                    this.getX() + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth(),
                    this.getY() + this.random.nextDouble() * (double) this.getBbHeight() - 0.25D,
                    this.getZ() + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth(),
                    (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(),
                    (this.random.nextDouble() - 0.5D) * 2.0D);
        }
    }

    public void spawnRestoreHealthParticle(int particleCount) {
        if (this.level.isClientSide) {
            for (int i = 0; i < particleCount; ++i) {
                double xRandom = this.random.nextGaussian() * 0.02D;
                double yRandom = this.random.nextGaussian() * 0.02D;
                double zRandom = this.random.nextGaussian() * 0.02D;

                this.level.addParticle(ParticleTypes.ENTITY_EFFECT,
                        this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth() - xRandom * 10.0D,
                        this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()) - yRandom * 10.0D,
                        this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth() - zRandom * 10.0D,
                        0.9, 0.1, 0.1);
            }
        }
    }

    public void spawnExplosionParticle() {
        if (this.level.isClientSide) {
            for (int i = 0; i < 20; ++i) {
                float mx = (random.nextFloat() - 0.5F) * 0.02F;
                float my = (random.nextFloat() - 0.5F) * 0.02F;
                float mz = (random.nextFloat() - 0.5F) * 0.02F;
                level.addParticle(ParticleTypes.CLOUD,
                        getX() + random.nextFloat() - 0.5F,
                        getY() + random.nextFloat() - 0.5F,
                        getZ() + random.nextFloat() - 0.5F,
                        mx, my, mz);
            }
        }
    }

    public void spawnBubbleParticle() {
        if (this.level.isClientSide) {
            for (int i = 0; i < 8; ++i) {
                double offsetX = 2 * random.nextDouble() - 1;
                double offsetY = random.nextDouble() / 2;
                double offsetZ = 2 * random.nextDouble() - 1;
                level.addParticle(ParticleTypes.BUBBLE, getX() + offsetX, getY() + offsetY, getZ() + offsetZ,
                        0, 0.1, 0);
            }
        }
    }

    public void spawnHeartParticle() {
        if (this.level.isClientSide) {
            for (int i = 0; i < 8; ++i) {
                double offsetX = this.random.nextGaussian() * 0.02;
                double offsetY = this.random.nextGaussian() * 0.02;
                double offsetZ = this.random.nextGaussian() * 0.02;
                level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), offsetX, offsetY, offsetZ);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void spawnRankUpParticle() {
        if (this.level.isClientSide) {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.particleEngine.createTrackingEmitter(this, ParticleTypes.TOTEM_OF_UNDYING, 30);
            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.BELL_BLOCK, this.getSoundSource(), 1.0F, 1.0F, false);
            minecraft.gui.setTitle(Component.translatable("message.touhou_little_maid.gomoku.rank_up.title"));
            minecraft.gui.setSubtitle(Component.translatable("message.touhou_little_maid.gomoku.rank_up.subtitle"));
        }
    }

    private void spawnSweepAttackParticle() {
        double xOffset = -Mth.sin(this.getYRot() * ((float) Math.PI / 180F));
        double zOffset = Mth.cos(this.getYRot() * ((float) Math.PI / 180F));
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK,
                    this.getX() + xOffset, this.getY(0.5),
                    this.getZ() + zOffset, 0, xOffset, 0, zOffset, 0);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString(MODEL_ID_TAG, getModelId());
        compound.putString(SOUND_PACK_ID_TAG, getSoundPackId());
        compound.putString(TASK_TAG, getTask().getUid().toString());
        compound.putBoolean(PICKUP_TAG, isPickup());
        compound.putBoolean(HOME_TAG, isHomeModeEnable());
        compound.putBoolean(RIDEABLE_TAG, isRideable());
        compound.put(MAID_INVENTORY_TAG, maidInv.serializeNBT());
        compound.put(MAID_BAUBLE_INVENTORY_TAG, maidBauble.serializeNBT());
        compound.putBoolean(STRUCK_BY_LIGHTNING_TAG, isStruckByLightning());
        compound.putBoolean(INVULNERABLE_TAG, getIsInvulnerable());
        compound.putInt(HUNGER_TAG, getHunger());
        compound.putInt(FAVORABILITY_TAG, getFavorability());
        compound.putInt(EXPERIENCE_TAG, getExperience());
        compound.putString(SCHEDULE_MODE_TAG, getSchedule().name());
        compound.putString(MAID_BACKPACK_TYPE, getMaidBackpackType().getId().toString());
        compound.put(GAME_SKILL_TAG, getGameSkill());
        this.favorabilityManager.addAdditionalSaveData(compound);
        this.scriptBookManager.addAdditionalSaveData(compound);
        this.schedulePos.save(compound);
        if (this.backpackData != null) {
            CompoundTag tag = new CompoundTag();
            this.backpackData.save(tag, this);
            compound.put(BACKPACK_DATA_TAG, tag);
        } else {
            compound.put(BACKPACK_DATA_TAG, new CompoundTag());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains(MODEL_ID_TAG, Tag.TAG_STRING)) {
            setModelId(compound.getString(MODEL_ID_TAG));
        }
        if (compound.contains(SOUND_PACK_ID_TAG, Tag.TAG_STRING)) {
            setSoundPackId(compound.getString(SOUND_PACK_ID_TAG));
        }
        if (compound.contains(SCHEDULE_MODE_TAG, Tag.TAG_STRING)) {
            setSchedule(MaidSchedule.valueOf(compound.getString(SCHEDULE_MODE_TAG)));
        }
        if (compound.contains(TASK_TAG, Tag.TAG_STRING)) {
            ResourceLocation uid = new ResourceLocation(compound.getString(TASK_TAG));
            IMaidTask task = TaskManager.findTask(uid).orElse(TaskManager.getIdleTask());
            setTask(task);
        }
        if (compound.contains(PICKUP_TAG, Tag.TAG_BYTE)) {
            setPickup(compound.getBoolean(PICKUP_TAG));
        }
        if (compound.contains(HOME_TAG, Tag.TAG_BYTE)) {
            setHomeModeEnable(compound.getBoolean(HOME_TAG));
        }
        if (compound.contains(RIDEABLE_TAG, Tag.TAG_BYTE)) {
            setRideable(compound.getBoolean(RIDEABLE_TAG));
        }
        if (compound.contains(BACKPACK_LEVEL_TAG, Tag.TAG_INT)) {
            // 存档迁移
            int backpackLevel = compound.getInt(BACKPACK_LEVEL_TAG);
            if (backpackLevel == 1) {
                BackpackManager.findBackpack(SmallBackpack.ID).ifPresent(this::setMaidBackpackType);
            }
            if (backpackLevel == 2) {
                BackpackManager.findBackpack(MiddleBackpack.ID).ifPresent(this::setMaidBackpackType);
            }
            if (backpackLevel == 3) {
                BackpackManager.findBackpack(BigBackpack.ID).ifPresent(this::setMaidBackpackType);
            }
            compound.remove(BACKPACK_LEVEL_TAG);
        }
        if (compound.contains(MAID_INVENTORY_TAG, Tag.TAG_COMPOUND)) {
            maidInv.deserializeNBT(compound.getCompound(MAID_INVENTORY_TAG));
        }
        if (compound.contains(MAID_BAUBLE_INVENTORY_TAG, Tag.TAG_COMPOUND)) {
            maidBauble.deserializeNBT(compound.getCompound(MAID_BAUBLE_INVENTORY_TAG));
        }
        if (compound.contains(STRUCK_BY_LIGHTNING_TAG, Tag.TAG_BYTE)) {
            setStruckByLightning(compound.getBoolean(STRUCK_BY_LIGHTNING_TAG));
        }
        if (compound.contains(INVULNERABLE_TAG, Tag.TAG_BYTE)) {
            setEntityInvulnerable(compound.getBoolean(INVULNERABLE_TAG));
        }
        if (compound.contains(HUNGER_TAG, Tag.TAG_INT)) {
            setHunger(compound.getInt(HUNGER_TAG));
        }
        if (compound.contains(FAVORABILITY_TAG, Tag.TAG_INT)) {
            setFavorability(compound.getInt(FAVORABILITY_TAG));
        }
        if (compound.contains(EXPERIENCE_TAG, Tag.TAG_INT)) {
            setExperience(compound.getInt(EXPERIENCE_TAG));
        }
        if (compound.contains(GAME_SKILL_TAG, Tag.TAG_COMPOUND)) {
            setGameSkill(compound.getCompound(GAME_SKILL_TAG));
        }
        if (compound.contains(RESTRICT_CENTER_TAG, Tag.TAG_COMPOUND)) {
            // 存档迁移
            BlockPos blockPos = NbtUtils.readBlockPos(compound.getCompound(RESTRICT_CENTER_TAG));
            this.schedulePos.setHomeModeEnable(this, blockPos);
            compound.remove(RESTRICT_CENTER_TAG);
        }
        if (compound.contains(MAID_BACKPACK_TYPE, Tag.TAG_STRING)) {
            ResourceLocation id = new ResourceLocation(compound.getString(MAID_BACKPACK_TYPE));
            IMaidBackpack backpack = BackpackManager.findBackpack(id).orElse(BackpackManager.getEmptyBackpack());
            setMaidBackpackType(backpack);
            if (this.backpackData != null && compound.contains(BACKPACK_DATA_TAG, Tag.TAG_COMPOUND)) {
                this.backpackData.load(compound.getCompound(BACKPACK_DATA_TAG), this);
            }
        }
        this.favorabilityManager.readAdditionalSaveData(compound);
        this.scriptBookManager.readAdditionalSaveData(compound);
        this.schedulePos.load(compound, this);
        this.setBackpackShowItem(maidInv.getStackInSlot(MaidBackpackHandler.BACKPACK_ITEM_SLOT));
    }

    public boolean openMaidGui(Player player) {
        return openMaidGui(player, TabIndex.MAIN);
    }

    public boolean openMaidGui(Player player, int tabIndex) {
        if (player instanceof ServerPlayer && !this.isSleeping()) {
            this.navigation.stop();
            NetworkHooks.openScreen((ServerPlayer) player, getGuiProvider(tabIndex), (buffer) -> buffer.writeInt(getId()));
        }
        return true;
    }

    private MenuProvider getGuiProvider(int tabIndex) {
        switch (tabIndex) {
            case TabIndex.CONFIG:
                return MaidConfigContainer.create(getId());
            case TabIndex.MAIN:
            default:
                return this.getMaidBackpackType().getGuiProvider(getId());
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (this.isAlive() && capability == ForgeCapabilities.ITEM_HANDLER) {
            if (facing == null) {
                return LazyOptional.of(() -> new CombinedInvWrapper(armorInvWrapper, handsInvWrapper, maidInv, maidBauble)).cast();
            }
            if (facing.getAxis().isVertical()) {
                return LazyOptional.of(() -> handsInvWrapper).cast();
            }
            if (facing.getAxis().isHorizontal()) {
                return LazyOptional.of(() -> armorInvWrapper).cast();
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    protected void dropEquipment() {
        if (this.getOwnerUUID() != null && !level.isClientSide && !PetBedDrop.hasPetBedPos(this)) {
            // 掉出世界的判断
            Vec3 position = Vec3.atBottomCenterOf(blockPosition());
            // 防止卡在基岩里？
            if (this.getY() < this.level.getMinBuildHeight() + 5) {
                position = new Vec3(position.x, this.level.getMinBuildHeight() + 5, position.z);
            }
            if (this.getY() > this.level.getMaxBuildHeight()) {
                position = new Vec3(position.x, this.level.getMaxBuildHeight(), position.z);
            }
            EntityTombstone tombstone = new EntityTombstone(level, this.getOwnerUUID(), position);
            tombstone.setMaidName(this.getDisplayName());

            // 女仆物品栏
            CombinedInvWrapper invWrapper = new CombinedInvWrapper(armorInvWrapper, handsInvWrapper, maidInv, maidBauble);
            for (int i = 0; i < invWrapper.getSlots(); i++) {
                int size = invWrapper.getSlotLimit(i);
                tombstone.insertItem(invWrapper.extractItem(i, size, false));
            }
            // 背包额外数据
            IMaidBackpack maidBackpack = this.getMaidBackpackType();
            tombstone.insertItem(maidBackpack.getTakeOffItemStack(ItemStack.EMPTY, null, this));
            maidBackpack.onSpawnTombstone(this, tombstone);
            // 胶片
            ItemStack filmItem = ItemFilm.maidToFilm(this);
            tombstone.insertItem(filmItem);
            // 全局记录
            MaidWorldData maidWorldData = MaidWorldData.get(level);
            if (maidWorldData != null) {
                maidWorldData.addTombstones(this, tombstone);
            }

            level.addFreshEntity(tombstone);
        }
    }

    @Override
    public ItemStack eat(Level level, ItemStack food) {
        ItemStack foodAfterEat = super.eat(level, food);
        NeoForge.EVENT_BUS.post(new MaidAfterEatEvent(this, foodAfterEat));
        return foodAfterEat;
    }

    @Override
    protected boolean isAlwaysExperienceDropper() {
        return true;
    }

    @Override
    public int getExperienceReward() {
        return this.getExperience();
    }

    @Override
    protected Component getTypeName() {
        Optional<MaidModelInfo> info = ServerCustomPackLoader.SERVER_MAID_MODELS.getInfo(getModelId());
        return info.map(maidModelInfo -> ParseI18n.parse(maidModelInfo.getName())).orElseGet(() -> Component.literal(getType().getDescriptionId()));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        int modelSize = ServerCustomPackLoader.SERVER_MAID_MODELS.getModelSize();
        // 这里居然可能为 0
        if (modelSize > 0) {
            int skipRandom = random.nextInt(modelSize);
            Optional<String> modelId = ServerCustomPackLoader.SERVER_MAID_MODELS.getModelIdSet().stream().skip(skipRandom).findFirst();
            return modelId.map(id -> {
                this.setModelId(id);
                return spawnDataIn;
            }).orElse(spawnDataIn);
        }
        return spawnDataIn;
    }

    @Override
    public void playSound(SoundEvent soundEvent, float volume, float pitch) {
        if (soundEvent.getLocation().getPath().startsWith("maid") && !level.isClientSide) {
            NetworkHandler.sendToNearby(this, new PlayMaidSoundMessage(soundEvent.getLocation(), this.getSoundPackId(), this.getId()), 16);
        } else {
            super.playSound(soundEvent, volume, pitch);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (NeoForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
            return null;
        }
        return task.getAmbientSound(this);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        if (NeoForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
            return null;
        }
        if (damageSourceIn.is(DamageTypeTags.IS_FIRE)) {
            return InitSounds.MAID_HURT_FIRE.get();
        } else if (damageSourceIn.getEntity() instanceof Player) {
            if (playerHurtSoundCount == 0) {
                playerHurtSoundCount = 120;
                return InitSounds.MAID_PLAYER.get();
            } else {
                return null;
            }
        } else {
            return InitSounds.MAID_HURT.get();
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (NeoForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
            return null;
        }
        return InitSounds.MAID_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return 1 + random.nextFloat() * 0.1F;
    }

    @Override
    public float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * (isMaidInSittingPose() ? 0.65F : 0.85F);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return null;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return this.isOwnedBy(player) && super.canBeLeashed(player);
    }

    public boolean canPathReach(BlockPos pos) {
        Path path = this.getNavigation().createPath(pos, 0);
        return path != null && path.canReach();
    }

    public boolean canPathReach(Entity entity) {
        Path path = this.getNavigation().createPath(entity, 0);
        return path != null && path.canReach();
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem shootableItem) {
        return getTask() instanceof IRangedAttackTask;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getBoundingBoxForCulling() {
        BedrockModel<Mob> model = CustomPackLoader.MAID_MODELS.getModel(getModelId()).orElse(null);
        if (model == null) {
            return super.getBoundingBoxForCulling();
        }
        return model.getRenderBoundingBox().move(position());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Vec3 getLeashOffset() {
        Optional<BedrockModel<Mob>> modelOptional = CustomPackLoader.MAID_MODELS.getModel(this.getModelId());
        Optional<MaidModelInfo> infoOptional = CustomPackLoader.MAID_MODELS.getInfo(this.getModelId());
        if (modelOptional.isPresent() && infoOptional.isPresent()) {
            BedrockModel<Mob> model = modelOptional.get();
            float renderEntityScale = infoOptional.get().getRenderEntityScale();
            if (model.hasHead()) {
                BedrockPart head = model.getHead();
                return new Vec3(head.x * renderEntityScale, (1.5 - head.y / 16) * renderEntityScale, head.z * renderEntityScale);
            }
        }
        return super.getLeashOffset();
    }

    @Override
    public void swing(InteractionHand pHand) {
        SlashBladeCompat.swingSlashBlade(this, getItemInHand(pHand));
        super.swing(pHand);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        // 修正睡觉时渲染问题，默认 64 格内渲染
        double range = 64.0 * getViewScale();
        return distance < range * range;
    }

    @Override
    public void startSleeping(BlockPos pPos) {
        super.startSleeping(pPos);
        this.setHealth(this.getMaxHealth());
        this.favorabilityManager.apply(Type.SLEEP);
    }

    public void setBackpackDelay() {
        backpackDelay = 20;
    }

    public boolean backpackHasDelay() {
        return backpackDelay > 0;
    }

    @Override
    public String getModelId() {
        return this.entityData.get(DATA_MODEL_ID);
    }

    public void setModelId(String modelId) {
        this.entityData.set(DATA_MODEL_ID, modelId);
    }

    public String getSoundPackId() {
        return this.entityData.get(DATA_SOUND_PACK_ID);
    }

    public void setSoundPackId(String soundPackId) {
        this.entityData.set(DATA_SOUND_PACK_ID, soundPackId);
    }

    @Override
    public boolean isMaidInSittingPose() {
        return super.isInSittingPose();
    }

    @Override
    public boolean isBegging() {
        return this.entityData.get(DATA_BEGGING);
    }

    public void setBegging(boolean begging) {
        this.entityData.set(DATA_BEGGING, begging);
    }

    public boolean isHomeModeEnable() {
        return this.entityData.get(DATA_HOME_MODE);
    }

    public void setHomeModeEnable(boolean enable) {
        this.entityData.set(DATA_HOME_MODE, enable);
    }

    @Override
    public boolean isWithinRestriction() {
        return this.isWithinRestriction(this.blockPosition());
    }

    @Override
    public boolean isWithinRestriction(BlockPos pos) {
        if (hasRestriction()) {
            return this.getRestrictCenter().distSqr(pos) < (double) (this.getRestrictRadius() * this.getRestrictRadius());
        }
        return true;
    }

    @Override
    public void restrictTo(BlockPos pos, int distance) {
        this.entityData.set(RESTRICT_CENTER, pos);
        this.entityData.set(RESTRICT_RADIUS, (float) distance);
    }

    @Override
    public BlockPos getRestrictCenter() {
        return this.entityData.get(RESTRICT_CENTER);
    }

    @Override
    public float getRestrictRadius() {
        return this.entityData.get(RESTRICT_RADIUS);
    }

    @Override
    public void clearRestriction() {
        this.schedulePos.clear(this);
    }

    @Override
    public boolean hasRestriction() {
        return this.isHomeModeEnable();
    }

    public BlockPos getBrainSearchPos() {
        if (this.hasRestriction()) {
            return this.getRestrictCenter();
        } else {
            return this.blockPosition();
        }
    }

    public boolean canBrainMoving() {
        return !this.isMaidInSittingPose() && !this.isPassenger() && !this.isSleeping() && !this.isLeashed();
    }

    public MaidChatBubbles getChatBubble() {
        return this.entityData.get(CHAT_BUBBLE);
    }

    public void setChatBubble(MaidChatBubbles bubbles) {
        this.entityData.set(CHAT_BUBBLE, bubbles);
    }

    public void addChatBubble(long endTime, ChatText text) {
        ChatBubbleManger.addChatBubble(endTime, text, this);
    }

    public int getChatBubbleCount() {
        return ChatBubbleManger.getChatBubbleCount(this);
    }

    public MaidScriptBookManager getScriptBookManager() {
        return scriptBookManager;
    }

    public boolean isPickup() {
        return this.entityData.get(DATA_PICKUP);
    }

    public void setPickup(boolean isPickup) {
        this.entityData.set(DATA_PICKUP, isPickup);
    }

    public boolean isRideable() {
        return this.entityData.get(DATA_RIDEABLE);
    }

    public void setRideable(boolean rideable) {
        this.entityData.set(DATA_RIDEABLE, rideable);
    }

    public int getHunger() {
        return this.entityData.get(DATA_HUNGER);
    }

    public void setHunger(int hunger) {
        this.entityData.set(DATA_HUNGER, hunger);
    }

    @Override
    public int getFavorability() {
        return this.entityData.get(DATA_FAVORABILITY);
    }

    public void setFavorability(int favorability) {
        this.entityData.set(DATA_FAVORABILITY, favorability);
    }

    @Override
    public int getExperience() {
        return this.entityData.get(DATA_EXPERIENCE);
    }

    public void setExperience(int experience) {
        this.entityData.set(DATA_EXPERIENCE, experience);
    }

    public boolean isStruckByLightning() {
        return this.entityData.get(DATA_STRUCK_BY_LIGHTNING);
    }

    public void setStruckByLightning(boolean isStruck) {
        this.entityData.set(DATA_STRUCK_BY_LIGHTNING, isStruck);
    }

    @Override
    public boolean isSwingingArms() {
        return this.entityData.get(DATA_ARM_RISE);
    }

    public void setSwingingArms(boolean swingingArms) {
        this.entityData.set(DATA_ARM_RISE, swingingArms);
    }

    public String getBackpackFluid() {
        return this.entityData.get(BACKPACK_FLUID);
    }

    public void setBackpackFluid(String fluidName) {
        this.entityData.set(BACKPACK_FLUID, fluidName);
    }

    public MaidSchedule getSchedule() {
        return this.entityData.get(SCHEDULE_MODE);
    }

    public void setSchedule(MaidSchedule schedule) {
        this.entityData.set(SCHEDULE_MODE, schedule);
        if (this.level instanceof ServerLevel) {
            this.refreshBrain((ServerLevel) this.level);
        }
    }

    public Activity getScheduleDetail() {
        MaidSchedule schedule = this.getSchedule();
        int time = (int) (this.level.getDayTime() % 24000L);
        switch (schedule) {
            case ALL -> {
                return Activity.WORK;
            }
            case NIGHT -> {
                return InitEntities.MAID_NIGHT_SHIFT_SCHEDULES.get().getActivityAt(time);
            }
            default -> {
                return InitEntities.MAID_DAY_SHIFT_SCHEDULES.get().getActivityAt(time);
            }
        }
    }

    public SchedulePos getSchedulePos() {
        return schedulePos;
    }

    @Override
    public ItemStack getBackpackShowItem() {
        return this.entityData.get(BACKPACK_ITEM_SHOW);
    }

    public void setBackpackShowItem(ItemStack stack) {
        this.entityData.set(BACKPACK_ITEM_SHOW, stack);
    }

    @Override
    public IMaidBackpack getMaidBackpackType() {
        ResourceLocation id = new ResourceLocation(entityData.get(BACKPACK_TYPE));
        return BackpackManager.findBackpack(id).orElse(BackpackManager.getEmptyBackpack());
    }

    public void setMaidBackpackType(IMaidBackpack backpack) {
        if (backpack == this.backpack) {
            return;
        }
        this.backpack = backpack;
        if (this.backpack.hasBackpackData()) {
            this.backpackData = this.backpack.getBackpackData(this);
        } else {
            this.backpackData = null;
        }
        this.entityData.set(BACKPACK_TYPE, backpack.getId().toString());
    }

    public IBackpackData getBackpackData() {
        return backpackData;
    }

    public ItemStackHandler getMaidInv() {
        return maidInv;
    }

    public CombinedInvWrapper getAvailableInv(boolean handsFirst) {
        RangedWrapper combinedInvWrapper = this.getAvailableBackpackInv();
        return handsFirst ? new CombinedInvWrapper(handsInvWrapper, combinedInvWrapper) : new CombinedInvWrapper(combinedInvWrapper, handsInvWrapper);
    }

    public RangedWrapper getAvailableBackpackInv() {
        return new RangedWrapper(maidInv, 0, getMaidBackpackType().getAvailableMaxContainerIndex());
    }

    public EntityHandsInvWrapper getHandsInvWrapper() {
        return handsInvWrapper;
    }

    public BaubleItemHandler getMaidBauble() {
        return maidBauble;
    }

    public boolean getIsInvulnerable() {
        return this.entityData.get(DATA_INVULNERABLE);
    }

    public void setEntityInvulnerable(boolean isInvulnerable) {
        super.setInvulnerable(isInvulnerable);
        this.entityData.set(DATA_INVULNERABLE, isInvulnerable);
    }

    public IMaidTask getTask() {
        ResourceLocation uid = new ResourceLocation(entityData.get(DATA_TASK));
        return TaskManager.findTask(uid).orElse(TaskManager.getIdleTask());
    }

    public void setTask(IMaidTask task) {
        if (task == this.task) {
            return;
        }
        this.task = task;
        this.entityData.set(DATA_TASK, task.getUid().toString());
        if (level instanceof ServerLevel) {
            refreshBrain((ServerLevel) level);
        }
    }

    @Override
    public void setInSittingPose(boolean inSittingPose) {
        super.setInSittingPose(inSittingPose);
        setOrderedToSit(inSittingPose);
    }

    public CompoundTag getGameSkill() {
        return this.entityData.get(GAME_SKILL);
    }

    public void setGameSkill(CompoundTag gameSkill) {
        this.entityData.set(GAME_SKILL, gameSkill, true);
    }

    public List<SendEffectMessage.EffectData> getEffects() {
        return effects;
    }

    public void setEffects(List<SendEffectMessage.EffectData> effects) {
        this.effects = effects;
    }

    public boolean canDestroyBlock(BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock().canEntityDestroy(state, level, pos, this) && ForgeEventFactory.onEntityDestroyBlock(this, pos, state);
    }

    public boolean canPlaceBlock(BlockPos pos) {
        BlockState oldState = level.getBlockState(pos);
        return oldState.canBeReplaced();
    }

    public boolean destroyBlock(BlockPos pos) {
        return destroyBlock(pos, true);
    }

    public boolean destroyBlock(BlockPos pos, boolean dropBlock) {
        return canDestroyBlock(pos) && destroyBlock(level, pos, dropBlock, this);
    }

    public boolean destroyBlock(Level level, BlockPos blockPos, boolean dropBlock, @Nullable Entity entity) {
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.isAir()) {
            return false;
        } else {
            FluidState fluidState = level.getFluidState(blockPos);
            if (!(blockState.getBlock() instanceof BaseFireBlock)) {
                level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState));
            }
            if (dropBlock) {
                BlockEntity blockEntity = blockState.hasBlockEntity() ? level.getBlockEntity(blockPos) : null;
                dropResourcesToMaidInv(blockState, level, blockPos, blockEntity, this, ItemStack.EMPTY);
            }
            boolean setResult = level.setBlock(blockPos, fluidState.createLegacyBlock(), Block.UPDATE_ALL);
            if (setResult) {
                level.gameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Context.of(entity, blockState));
            }
            return setResult;
        }
    }

    public void dropResourcesToMaidInv(BlockState state, Level level, BlockPos pos, @Nullable BlockEntity blockEntity, EntityMaid maid, ItemStack tool) {
        if (level instanceof ServerLevel serverLevel) {
            CombinedInvWrapper availableInv = this.getAvailableInv(false);
            Block.getDrops(state, serverLevel, pos, blockEntity, maid, tool).forEach(stack -> {
                ItemStack remindItemStack = ItemHandlerHelper.insertItemStacked(availableInv, stack, false);
                if (!remindItemStack.isEmpty()) {
                    Block.popResource(level, pos, remindItemStack);
                }
            });
            state.spawnAfterBreak(serverLevel, pos, tool, true);
        }
    }

    public boolean placeItemBlock(InteractionHand hand, BlockPos placePos, Direction direction, ItemStack stack) {
        if (stack.getItem() instanceof BlockItem) {
            return ((BlockItem) stack.getItem()).place(new BlockPlaceContext(level, null, hand, stack,
                    getBlockRayTraceResult(placePos, direction))).consumesAction();
        }
        return false;
    }

    public boolean placeItemBlock(BlockPos placePos, Direction direction, ItemStack stack) {
        return placeItemBlock(InteractionHand.MAIN_HAND, placePos, direction, stack);
    }

    public boolean placeItemBlock(BlockPos placePos, ItemStack stack) {
        return placeItemBlock(placePos, Direction.UP, stack);
    }

    private BlockHitResult getBlockRayTraceResult(BlockPos pos, Direction direction) {
        return new BlockHitResult(
                new Vec3((double) pos.getX() + 0.5D + (double) direction.getStepX() * 0.5D,
                        (double) pos.getY() + 0.5D + (double) direction.getStepY() * 0.5D,
                        (double) pos.getZ() + 0.5D + (double) direction.getStepZ() * 0.5D),
                direction, pos, false);
    }

    public FavorabilityManager getFavorabilityManager() {
        return favorabilityManager;
    }

    @SuppressWarnings("all")
    public Ingredient getTamedItem() {
        return getConfigIngredient(MaidConfig.MAID_TAMED_ITEM.get(), Items.CAKE);
    }

    @SuppressWarnings("all")
    public Ingredient getTemptationItem() {
        return getConfigIngredient(MaidConfig.MAID_TEMPTATION_ITEM.get(), Items.CAKE);
    }

    @Override
    public EntityMaid asStrictMaid() {
        return this;
    }

    @Override
    public Mob asEntity() {
        return this;
    }

    @Override
    public ItemStack[] getHandItemsForAnimation() {
        return handItemsForAnimation;
    }

}
