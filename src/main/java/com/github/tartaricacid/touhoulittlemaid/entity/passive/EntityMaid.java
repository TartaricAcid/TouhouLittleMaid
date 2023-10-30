package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.api.event.*;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidBrain;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManger;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatText;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.MaidChatBubbles;
import com.github.tartaricacid.touhoulittlemaid.entity.info.ServerCustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskIdle;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.MaidBackpackHandler;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.MaidHandsInvWrapper;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.github.tartaricacid.touhoulittlemaid.item.ItemFilm;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.mixin.MixinArrowEntity;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.ItemBreakMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SendEffectMessage;
import com.github.tartaricacid.touhoulittlemaid.util.BiomeCacheUtil;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.google.common.collect.Lists;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EntityArmorInvWrapper;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class EntityMaid extends TameableEntity implements ICrossbowUser {
    public static final EntityType<EntityMaid> TYPE = EntityType.Builder.<EntityMaid>of(EntityMaid::new, EntityClassification.CREATURE)
            .sized(0.6f, 1.5f).clientTrackingRange(10).build("maid");
    public static final String MODEL_ID_TAG = "ModelId";
    public static final String BACKPACK_LEVEL_TAG = "MaidBackpackLevel";
    public static final String MAID_INVENTORY_TAG = "MaidInventory";
    public static final String MAID_BAUBLE_INVENTORY_TAG = "MaidBaubleInventory";
    public static final String EXPERIENCE_TAG = "MaidExperience";
    private static final DataParameter<String> DATA_MODEL_ID = EntityDataManager.defineId(EntityMaid.class, DataSerializers.STRING);
    private static final DataParameter<String> DATA_TASK = EntityDataManager.defineId(EntityMaid.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> DATA_BEGGING = EntityDataManager.defineId(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_PICKUP = EntityDataManager.defineId(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_HOME_MODE = EntityDataManager.defineId(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_RIDEABLE = EntityDataManager.defineId(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_INVULNERABLE = EntityDataManager.defineId(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DATA_BACKPACK_LEVEL = EntityDataManager.defineId(EntityMaid.class, DataSerializers.INT);
    private static final DataParameter<Integer> DATA_HUNGER = EntityDataManager.defineId(EntityMaid.class, DataSerializers.INT);
    private static final DataParameter<Integer> DATA_FAVORABILITY = EntityDataManager.defineId(EntityMaid.class, DataSerializers.INT);
    private static final DataParameter<Integer> DATA_EXPERIENCE = EntityDataManager.defineId(EntityMaid.class, DataSerializers.INT);
    private static final DataParameter<Boolean> DATA_STRUCK_BY_LIGHTNING = EntityDataManager.defineId(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_IS_CHARGING_CROSSBOW = EntityDataManager.defineId(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_ARM_RISE = EntityDataManager.defineId(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<MaidSchedule> SCHEDULE_MODE = EntityDataManager.defineId(EntityMaid.class, MaidSchedule.DATA);
    private static final DataParameter<BlockPos> RESTRICT_CENTER = EntityDataManager.defineId(EntityMaid.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Float> RESTRICT_RADIUS = EntityDataManager.defineId(EntityMaid.class, DataSerializers.FLOAT);
    private static final DataParameter<MaidChatBubbles> CHAT_BUBBLE = EntityDataManager.defineId(EntityMaid.class, MaidChatBubbles.DATA);
    private static final String TASK_TAG = "MaidTask";
    private static final String PICKUP_TAG = "MaidIsPickup";
    private static final String HOME_TAG = "MaidIsHome";
    private static final String RIDEABLE_TAG = "MaidIsRideable";
    private static final String STRUCK_BY_LIGHTNING_TAG = "StruckByLightning";
    private static final String INVULNERABLE_TAG = "Invulnerable";
    private static final String HUNGER_TAG = "MaidHunger";
    private static final String FAVORABILITY_TAG = "MaidFavorability";
    private static final String SCHEDULE_MODE_TAG = "MaidScheduleMode";
    private static final String RESTRICT_CENTER_TAG = "MaidRestrictCenter";

    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";

    private final EntityArmorInvWrapper armorInvWrapper = new EntityArmorInvWrapper(this);
    private final EntityHandsInvWrapper handsInvWrapper = new MaidHandsInvWrapper(this);
    private final ItemStackHandler maidInv = new MaidBackpackHandler(36);
    private final BaubleItemHandler maidBauble = new BaubleItemHandler(9);

    public boolean guiOpening = false;

    private List<SendEffectMessage.EffectData> effects = Lists.newArrayList();
    private IMaidTask task = TaskManager.getIdleTask();
    private int playerHurtSoundCount = 120;
    private int pickupSoundCount = 5;
    private int backpackDelay = 0;

    protected EntityMaid(EntityType<EntityMaid> type, World world) {
        super(type, world);
        ((GroundPathNavigator) this.getNavigation()).setCanOpenDoors(true);
        this.getNavigation().setCanFloat(true);
        this.setPathfindingMalus(PathNodeType.COCOA, -1.0F);
    }

    public EntityMaid(World worldIn) {
        this(TYPE, worldIn);
    }

    public static boolean canInsertItem(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem) {
            Block block = ((BlockItem) stack.getItem()).getBlock();
            return !(block instanceof ShulkerBoxBlock);
        }
        return stack.getItem() != InitItems.PHOTO.get();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_MODEL_ID, DEFAULT_MODEL_ID);
        this.entityData.define(DATA_TASK, TaskIdle.UID.toString());
        this.entityData.define(DATA_BEGGING, false);
        this.entityData.define(DATA_PICKUP, true);
        this.entityData.define(DATA_HOME_MODE, false);
        this.entityData.define(DATA_RIDEABLE, true);
        this.entityData.define(DATA_INVULNERABLE, false);
        this.entityData.define(DATA_BACKPACK_LEVEL, 0);
        this.entityData.define(DATA_HUNGER, 0);
        this.entityData.define(DATA_FAVORABILITY, 0);
        this.entityData.define(DATA_EXPERIENCE, 0);
        this.entityData.define(DATA_STRUCK_BY_LIGHTNING, false);
        this.entityData.define(DATA_IS_CHARGING_CROSSBOW, false);
        this.entityData.define(DATA_ARM_RISE, false);
        this.entityData.define(SCHEDULE_MODE, MaidSchedule.DAY);
        this.entityData.define(RESTRICT_CENTER, BlockPos.ZERO);
        this.entityData.define(RESTRICT_RADIUS, MaidConfig.MAID_HOME_RANGE.get().floatValue());
        this.entityData.define(CHAT_BUBBLE, MaidChatBubbles.DEFAULT);
    }

    @Override
    @SuppressWarnings("all")
    public Brain<EntityMaid> getBrain() {
        return (Brain<EntityMaid>) super.getBrain();
    }

    @Override
    protected Brain.BrainCodec<EntityMaid> brainProvider() {
        return Brain.provider(MaidBrain.getMemoryTypes(), MaidBrain.getSensorTypes());
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamicIn) {
        Brain<EntityMaid> brain = this.brainProvider().makeBrain(dynamicIn);
        MaidBrain.registerBrainGoals(brain, this);
        return brain;
    }

    public void refreshBrain(ServerWorld serverWorldIn) {
        Brain<EntityMaid> brain = this.getBrain();
        brain.stopAll(serverWorldIn, this);
        this.brain = brain.copyWithoutBehaviors();
        MaidBrain.registerBrainGoals(this.getBrain(), this);
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
        if (!MinecraftForge.EVENT_BUS.post(new MaidTickEvent(this))) {
            super.tick();
            maidBauble.fireEvent((b, s) -> {
                b.onTick(this, s);
                return false;
            });
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
            setDeltaMovement(Vector3d.ZERO);
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
        }
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
                    this.navigation.stop();
                    this.setTarget(null);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                    this.playSound(InitSounds.MAID_TAMED.get(), 1, 1);
                    return ActionResultType.SUCCESS;
                }
            } else {
                if (player instanceof ServerPlayerEntity) {
                    TranslationTextComponent msg = new TranslationTextComponent("message.touhou_little_maid.owner_maid_num.can_not_add",
                            cap.get(), cap.getMaxNum());
                    ((ServerPlayerEntity) player).sendMessage(msg, ChatType.GAME_INFO, Util.NIL_UUID);
                }
            }
            return ActionResultType.PASS;
        }).orElse(ActionResultType.PASS);
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
                    if (entityPickup instanceof ExperienceOrbEntity) {
                        pickupXPOrb((ExperienceOrbEntity) entityPickup);
                    }
                    // 如果是 P 点
                    if (entityPickup instanceof EntityPowerPoint) {
                        pickupPowerPoint((EntityPowerPoint) entityPickup);
                    }
                    // 如果是箭
                    if (entityPickup instanceof AbstractArrowEntity) {
                        pickupArrow((AbstractArrowEntity) entityPickup, false);
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
                if (!MinecraftForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
                    pickupSoundCount--;
                    if (pickupSoundCount == 0) {
                        this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
                        pickupSoundCount = 5;
                    }
                }
                // 如果遍历塞完后发现为空了
                if (itemstack.isEmpty()) {
                    // 清除这个实体
                    entityItem.remove();
                } else {
                    // 将物品数量同步到客户端
                    entityItem.setItem(itemstack);
                }
            }
            return true;
        }
        return false;
    }

    public void pickupXPOrb(ExperienceOrbEntity entityXPOrb) {
        if (!this.level.isClientSide && entityXPOrb.isAlive() && entityXPOrb.tickCount > 2) {
            // 这是向客户端同步数据用的，如果加了这个方法，会有短暂的拾取动画和音效
            this.take(entityXPOrb, 1);
            if (!MinecraftForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
                pickupSoundCount--;
                if (pickupSoundCount == 0) {
                    this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
                    pickupSoundCount = 5;
                }
            }

            // 对经验修补的应用，因为全部来自于原版，所以效果也是相同的
            Map.Entry<EquipmentSlotType, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, this, ItemStack::isDamaged);
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
            entityXPOrb.remove();
        }
    }

    public void pickupPowerPoint(EntityPowerPoint powerPoint) {
        if (!this.level.isClientSide && powerPoint.isAlive() && powerPoint.throwTime == 0) {
            // 这是向客户端同步数据用的，如果加了这个方法，会有短暂的拾取动画和音效
            powerPoint.take(this, 1);
            if (!MinecraftForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
                pickupSoundCount--;
                if (pickupSoundCount == 0) {
                    this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
                    pickupSoundCount = 5;
                }
            }

            // 对经验修补的应用，因为全部来自于原版，所以效果也是相同的
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
            powerPoint.remove();
        }
    }

    private ItemStack getRandomItemWithMendingEnchantments() {
        List<ItemStack> stacks = Lists.newArrayList();
        this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(cap -> {
            for (int i = 0; i < cap.getSlots(); i++) {
                ItemStack itemstack = cap.getStackInSlot(i);
                if (!itemstack.isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING, itemstack) > 0 && itemstack.isDamaged()) {
                    stacks.add(itemstack);
                }
            }
        });
        return stacks.isEmpty() ? ItemStack.EMPTY : stacks.get(this.getRandom().nextInt(stacks.size()));
    }

    public boolean pickupArrow(AbstractArrowEntity arrow, boolean simulate) {
        if (!this.level.isClientSide && arrow.isAlive() && arrow.shakeTime <= 0) {
            // 先判断箭是否处于可以拾起的状态
            if (arrow.pickup != AbstractArrowEntity.PickupStatus.ALLOWED) {
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
                if (!MinecraftForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
                    pickupSoundCount--;
                    if (pickupSoundCount == 0) {
                        this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
                        pickupSoundCount = 5;
                    }
                }
                arrow.remove();
            }
            return true;
        }
        return false;
    }

    private ItemStack getArrowFromEntity(AbstractArrowEntity entity) {
        if (entity instanceof MixinArrowEntity) {
            MixinArrowEntity mixinArrow = (MixinArrowEntity) entity;
            if (mixinArrow.tlmInGround() || entity.isNoPhysics()) {
                return mixinArrow.getTlmPickupItem();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean result = super.doHurtTarget(entityIn);
        this.getMainHandItem().hurtAndBreak(1, this, (maid) -> maid.broadcastBreakEvent(Hand.MAIN_HAND));
        return result;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (MinecraftForge.EVENT_BUS.post(new MaidAttackEvent(this, source, amount))) {
            return false;
        }
        if (source.getEntity() instanceof PlayerEntity && this.isOwnedBy((PlayerEntity) source.getEntity())) {
            // 玩家对自己女仆的伤害数值为 1/5，最大为 2
            amount = MathHelper.clamp(amount / 5, 0, 2);
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
            damageAmount = MinecraftForge.EVENT_BUS.post(maidHurtEvent) ? 0 : maidHurtEvent.getAmount();
            damageAmount = ForgeHooks.onLivingHurt(this, damageSrc, damageAmount);
            if (damageAmount > 0) {
                damageAmount = this.getDamageAfterArmorAbsorb(damageSrc, damageAmount);
                damageAmount = this.getDamageAfterMagicAbsorb(damageSrc, damageAmount);
                float damageAfterAbsorption = Math.max(damageAmount - this.getAbsorptionAmount(), 0);
                this.setAbsorptionAmount(this.getAbsorptionAmount() - (damageAmount - damageAfterAbsorption));
                float damageDealtAbsorbed = damageAmount - damageAfterAbsorption;
                if (0 < damageDealtAbsorbed && damageDealtAbsorbed < (Float.MAX_VALUE / 10) && damageSrc.getEntity() instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) damageSrc.getEntity()).awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(damageDealtAbsorbed * 10));
                }
                MaidDamageEvent maidDamageEvent = new MaidDamageEvent(this, damageSrc, damageAfterAbsorption);
                damageAfterAbsorption = MinecraftForge.EVENT_BUS.post(maidDamageEvent) ? 0 : maidDamageEvent.getAmount();
                damageAfterAbsorption = ForgeHooks.onLivingDamage(this, damageSrc, damageAfterAbsorption);
                if (damageAfterAbsorption != 0) {
                    float health = this.getHealth();
                    this.getCombatTracker().recordDamage(damageSrc, health, damageAfterAbsorption);
                    this.setHealth(health - damageAfterAbsorption);
                    this.setAbsorptionAmount(this.getAbsorptionAmount() - damageAfterAbsorption);
                }
            }
        }
    }

    @Override
    public void die(DamageSource cause) {
        if (!MinecraftForge.EVENT_BUS.post(new MaidDeathEvent(this, cause))) {
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
            if (pickupEntity instanceof AbstractArrowEntity) {
                return pickupArrow((AbstractArrowEntity) pickupEntity, true);
            }
            if (pickupEntity instanceof ExperienceOrbEntity) {
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
    public void shootCrossbowProjectile(LivingEntity target, ItemStack crossbow, ProjectileEntity projectileEntity, float projectileAngle) {
        this.shootCrossbowProjectile(this, target, projectileEntity, projectileAngle, 1.6F);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public void thunderHit(ServerWorld world, LightningBoltEntity lightning) {
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
            boolean fireResistant = damageSource.isFire() && stack.getItem().isFireResistant();
            if (!fireResistant && stack.getItem() instanceof ArmorItem) {
                final int index = i;
                stack.hurtAndBreak((int) damage, this,
                        (maid) -> maid.broadcastBreakEvent(EquipmentSlotType.byTypeAndIndex(EquipmentSlotType.Group.ARMOR, index)));
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
        if (this.getOwner() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) this.getOwner();
            LivingEntity lastHurtByMob = player.getLastHurtByMob();
            if (target.equals(lastHurtByMob) && checkCanAttackEntity(lastHurtByMob)) {
                return true;
            }
            LivingEntity lastHurtMob = player.getLastHurtMob();
            if (target.equals(lastHurtMob) && checkCanAttackEntity(lastHurtMob)) {
                return true;
            }
        }
        LivingEntity maidLastHurtByMob = this.getLastHurtByMob();
        if (target.equals(maidLastHurtByMob) && checkCanAttackEntity(maidLastHurtByMob)) {
            return true;
        }
        if (target instanceof IMob) {
            return super.canAttack(target);
        }
        return false;
    }

    private boolean checkCanAttackEntity(LivingEntity target) {
        // 不能攻击玩家
        if (target instanceof PlayerEntity) {
            return false;
        }
        // 有主的宠物也不攻击
        if (target instanceof TameableEntity) {
            TameableEntity tamableAnimal = (TameableEntity) target;
            return tamableAnimal.getOwnerUUID() == null;
        }
        return true;
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

    private void spawnRestoreHealthParticle(int particleCount) {
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

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putString(MODEL_ID_TAG, getModelId());
        compound.putString(TASK_TAG, getTask().getUid().toString());
        compound.putBoolean(PICKUP_TAG, isPickup());
        compound.putBoolean(HOME_TAG, isHomeModeEnable());
        compound.putBoolean(RIDEABLE_TAG, isRideable());
        compound.putInt(BACKPACK_LEVEL_TAG, getBackpackLevel());
        compound.put(MAID_INVENTORY_TAG, maidInv.serializeNBT());
        compound.put(MAID_BAUBLE_INVENTORY_TAG, maidBauble.serializeNBT());
        compound.putBoolean(STRUCK_BY_LIGHTNING_TAG, isStruckByLightning());
        compound.putBoolean(INVULNERABLE_TAG, getIsInvulnerable());
        compound.putInt(HUNGER_TAG, getHunger());
        compound.putInt(FAVORABILITY_TAG, getFavorability());
        compound.putInt(EXPERIENCE_TAG, getExperience());
        compound.putString(SCHEDULE_MODE_TAG, getSchedule().name());
        compound.put(RESTRICT_CENTER_TAG, NBTUtil.writeBlockPos(getRestrictCenter()));
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
        if (compound.contains(RIDEABLE_TAG, Constants.NBT.TAG_BYTE)) {
            setRideable(compound.getBoolean(RIDEABLE_TAG));
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
            setStruckByLightning(compound.getBoolean(STRUCK_BY_LIGHTNING_TAG));
        }
        if (compound.contains(INVULNERABLE_TAG, Constants.NBT.TAG_BYTE)) {
            setEntityInvulnerable(compound.getBoolean(INVULNERABLE_TAG));
        }
        if (compound.contains(HUNGER_TAG, Constants.NBT.TAG_INT)) {
            setHunger(compound.getInt(HUNGER_TAG));
        }
        if (compound.contains(FAVORABILITY_TAG, Constants.NBT.TAG_INT)) {
            setFavorability(compound.getInt(FAVORABILITY_TAG));
        }
        if (compound.contains(EXPERIENCE_TAG, Constants.NBT.TAG_INT)) {
            setExperience(compound.getInt(EXPERIENCE_TAG));
        }
        if (compound.contains(SCHEDULE_MODE_TAG, Constants.NBT.TAG_STRING)) {
            setSchedule(MaidSchedule.valueOf(compound.getString(SCHEDULE_MODE_TAG)));
        }
        if (compound.contains(RESTRICT_CENTER_TAG, Constants.NBT.TAG_COMPOUND)) {
            setRestrictCenter(NBTUtil.readBlockPos(compound.getCompound(RESTRICT_CENTER_TAG)));
        }
    }

    public boolean openMaidGui(PlayerEntity player) {
        return openMaidGui(player, TabIndex.MAIN);
    }

    public boolean openMaidGui(PlayerEntity player, int tabIndex) {
        if (player instanceof ServerPlayerEntity && !this.isSleeping()) {
            this.navigation.stop();
            NetworkHooks.openGui((ServerPlayerEntity) player, getGuiProvider(tabIndex), (buffer) -> buffer.writeInt(getId()));
        }
        return true;
    }

    private INamedContainerProvider getGuiProvider(int tabIndex) {
        switch (tabIndex) {
            case TabIndex.CONFIG:
                return MaidConfigContainer.create(getId());
            case TabIndex.MAIN:
            default:
                return MaidMainContainer.create(getId());
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (this.isAlive() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
        ItemsUtil.dropEntityItems(this, new CombinedInvWrapper(armorInvWrapper, handsInvWrapper, maidInv, maidBauble));
        ItemMaidBackpack.getInstance(getBackpackLevel()).ifPresent(backpack ->
                InventoryHelper.dropItemStack(level, getX(), getY(), getZ(), backpack.getDefaultInstance()));
        spawnAtLocation(ItemFilm.maidToFilm(this), 0.2f);
    }

    @Override
    protected boolean isAlwaysExperienceDropper() {
        return true;
    }

    @Override
    protected int getExperienceReward(PlayerEntity player) {
        return this.getExperience();
    }

    @Override
    protected ITextComponent getTypeName() {
        Optional<MaidModelInfo> info = ServerCustomPackLoader.SERVER_MAID_MODELS.getInfo(getModelId());
        return info.map(maidModelInfo -> ParseI18n.parse(maidModelInfo.getName())).orElseGet(() -> getType().getDescription());
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        int skipRandom = random.nextInt(ServerCustomPackLoader.SERVER_MAID_MODELS.getModelSize());
        Optional<String> modelId = ServerCustomPackLoader.SERVER_MAID_MODELS.getModelIdSet().stream().skip(skipRandom).findFirst();
        return modelId.map(id -> {
            this.setModelId(id);
            return spawnDataIn;
        }).orElse(spawnDataIn);
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
    public float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * (isInSittingPose() ? 0.65F : 0.85F);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return null;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canBeLeashed(PlayerEntity player) {
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
    public boolean canFireProjectileWeapon(ShootableItem shootableItem) {
        return getTask() instanceof IRangedAttackTask;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getBoundingBoxForCulling() {
        BedrockModel<EntityMaid> model = CustomPackLoader.MAID_MODELS.getModel(getModelId()).orElse(null);
        if (model == null) {
            return super.getBoundingBoxForCulling();
        }
        return model.getRenderBoundingBox().move(position());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Vector3d getLeashOffset() {
        Optional<BedrockModel<EntityMaid>> modelOptional = CustomPackLoader.MAID_MODELS.getModel(this.getModelId());
        Optional<MaidModelInfo> infoOptional = CustomPackLoader.MAID_MODELS.getInfo(this.getModelId());
        if (modelOptional.isPresent() && infoOptional.isPresent()) {
            BedrockModel<EntityMaid> model = modelOptional.get();
            float renderEntityScale = infoOptional.get().getRenderEntityScale();
            if (model.hasHead()) {
                ModelRenderer head = model.getHead();
                return new Vector3d(head.x * renderEntityScale, (1.5 - head.y / 16) * renderEntityScale, head.z * renderEntityScale);
            }
        }
        return super.getLeashOffset();
    }

    public void setBackpackDelay() {
        backpackDelay = 20;
    }

    public boolean backpackHasDelay() {
        return backpackDelay > 0;
    }

    public String getModelId() {
        return this.entityData.get(DATA_MODEL_ID);
    }

    public void setModelId(String modelId) {
        this.entityData.set(DATA_MODEL_ID, modelId);
    }

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
        if (enable) {
            setRestrictCenter(this.blockPosition());
        }
    }

    @Override
    public BlockPos getRestrictCenter() {
        return this.entityData.get(RESTRICT_CENTER);
    }

    public void setRestrictCenter(BlockPos center) {
        this.entityData.set(RESTRICT_CENTER, center);
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
    public boolean hasRestriction() {
        return this.isHomeModeEnable();
    }

    @Override
    public float getRestrictRadius() {
        return this.entityData.get(RESTRICT_RADIUS);
    }

    public MaidChatBubbles getChatBubble() {
        return this.entityData.get(CHAT_BUBBLE);
    }

    public void addChatBubble(long endTime, ChatText text) {
        ChatBubbleManger.addChatBubble(endTime, text, this);
    }

    public void setChatBubble(MaidChatBubbles bubbles) {
        this.entityData.set(CHAT_BUBBLE, bubbles);
    }

    public int getChatBubbleCount() {
        return ChatBubbleManger.getChatBubbleCount(this);
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

    public int getBackpackLevel() {
        return this.entityData.get(DATA_BACKPACK_LEVEL);
    }

    public void setBackpackLevel(int level) {
        this.entityData.set(DATA_BACKPACK_LEVEL, level);
    }

    public boolean hasBackpack() {
        return getBackpackLevel() > 0;
    }

    public int getHunger() {
        return this.entityData.get(DATA_HUNGER);
    }

    public void setHunger(int hunger) {
        this.entityData.set(DATA_HUNGER, hunger);
    }

    public int getFavorability() {
        return this.entityData.get(DATA_FAVORABILITY);
    }

    public void setFavorability(int favorability) {
        this.entityData.set(DATA_FAVORABILITY, favorability);
    }

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

    public boolean isSwingingArms() {
        return this.entityData.get(DATA_ARM_RISE);
    }

    public void setSwingingArms(boolean swingingArms) {
        this.entityData.set(DATA_ARM_RISE, swingingArms);
    }

    public MaidSchedule getSchedule() {
        return this.entityData.get(SCHEDULE_MODE);
    }

    public void setSchedule(MaidSchedule schedule) {
        this.entityData.set(SCHEDULE_MODE, schedule);
        if (this.level instanceof ServerWorld) {
            this.refreshBrain((ServerWorld) this.level);
        }
    }

    public ItemStackHandler getMaidInv() {
        return maidInv;
    }

    public CombinedInvWrapper getAvailableInv(boolean handsFirst) {
        RangedWrapper combinedInvWrapper = new RangedWrapper(maidInv, 0, BackpackLevel.BACKPACK_CAPACITY_MAP.get(getBackpackLevel()));
        return handsFirst ? new CombinedInvWrapper(handsInvWrapper, combinedInvWrapper) : new CombinedInvWrapper(combinedInvWrapper, handsInvWrapper);
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
        if (level instanceof ServerWorld) {
            refreshBrain((ServerWorld) level);
        }
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

    public List<SendEffectMessage.EffectData> getEffects() {
        return effects;
    }

    public void setEffects(List<SendEffectMessage.EffectData> effects) {
        this.effects = effects;
    }

    @Deprecated
    public boolean hasSasimono() {
        return false;
    }

    public boolean canDestroyBlock(BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock().canEntityDestroy(state, level, pos, this) && ForgeEventFactory.onEntityDestroyBlock(this, pos, state);
    }

    public boolean canPlaceBlock(BlockPos pos) {
        BlockState oldState = level.getBlockState(pos);
        return oldState.getMaterial().isReplaceable();
    }

    public boolean destroyBlock(BlockPos pos) {
        return destroyBlock(pos, true);
    }

    public boolean destroyBlock(BlockPos pos, boolean dropBlock) {
        return canDestroyBlock(pos) && level.destroyBlock(pos, dropBlock, this);
    }

    public boolean placeItemBlock(Hand hand, BlockPos placePos, Direction direction, ItemStack stack) {
        if (stack.getItem() instanceof BlockItem) {
            return ((BlockItem) stack.getItem()).place(new BlockItemUseContext(level, null, hand, stack,
                    getBlockRayTraceResult(placePos, direction))).consumesAction();
        }
        return false;
    }

    public boolean placeItemBlock(BlockPos placePos, Direction direction, ItemStack stack) {
        return placeItemBlock(Hand.MAIN_HAND, placePos, direction, stack);
    }

    public boolean placeItemBlock(BlockPos placePos, ItemStack stack) {
        return placeItemBlock(placePos, Direction.UP, stack);
    }

    private BlockRayTraceResult getBlockRayTraceResult(BlockPos pos, Direction direction) {
        return new BlockRayTraceResult(
                new Vector3d((double) pos.getX() + 0.5D + (double) direction.getStepX() * 0.5D,
                        (double) pos.getY() + 0.5D + (double) direction.getStepY() * 0.5D,
                        (double) pos.getZ() + 0.5D + (double) direction.getStepZ() * 0.5D),
                direction, pos, false);
    }

    @Deprecated
    public String getAtBiomeTemp() {
        float temp = BiomeCacheUtil.getCacheBiome(this).getBaseTemperature();
        if (temp < 0.15) {
            return "COLD";
        } else if (temp < 0.55) {
            return "OCEAN";
        } else if (temp < 0.95) {
            return "MEDIUM";
        } else {
            return "WARM";
        }
    }

    public boolean isSitInJoyBlock() {
        // TODO：待完成
        return false;
    }

    @Deprecated
    public int getDim() {
        RegistryKey<World> dim = this.level.dimension();
        if (dim.equals(World.OVERWORLD)) {
            return 0;
        }
        if (dim.equals(World.NETHER)) {
            return -1;
        }
        if (dim.equals(World.END)) {
            return 1;
        }
        return 0;
    }


    @SuppressWarnings("all")
    public Ingredient getTamedItem() {
        return getConfigIngredient(MaidConfig.MAID_TAMED_ITEM.get(), Items.CAKE);
    }

    @SuppressWarnings("all")
    public Ingredient getTemptationItem() {
        return getConfigIngredient(MaidConfig.MAID_TEMPTATION_ITEM.get(), Items.CAKE);
    }

    @SuppressWarnings("all")
    public Ingredient getNtrItem() {
        return getConfigIngredient(MaidConfig.MAID_NTR_ITEM.get(), Items.STRUCTURE_VOID);
    }

    private Ingredient getConfigIngredient(String config, Item defaultItem) {
        if (config.startsWith(MaidConfig.TAG_PREFIX)) {
            ResourceLocation key = new ResourceLocation(config.substring(1));
            ITag<Item> tag = ItemTags.getAllTags().getTag(key);
            if (tag != null) {
                return Ingredient.of(tag);
            }
        } else {
            ResourceLocation key = new ResourceLocation(config);
            if (ForgeRegistries.ITEMS.containsKey(key)) {
                return Ingredient.of(ForgeRegistries.ITEMS.getValue(key));
            }
        }
        return Ingredient.of(defaultItem);
    }
}
