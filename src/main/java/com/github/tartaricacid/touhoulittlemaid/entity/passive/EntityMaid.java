package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.config.Config;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidBrain;
import com.github.tartaricacid.touhoulittlemaid.entity.info.ServerCustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.entity.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.instance.TaskIdle;
import com.github.tartaricacid.touhoulittlemaid.event.api.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.event.api.MaidPlaySoundEvent;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.inventory.BaubleItemHandler;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidInventory;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EntityArmorInvWrapper;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class EntityMaid extends TameableEntity implements INamedContainerProvider, ICrossbowUser {
    public static final EntityType<EntityMaid> TYPE = EntityType.Builder.<EntityMaid>of(EntityMaid::new, EntityClassification.CREATURE)
            .sized(0.6f, 1.5f).clientTrackingRange(10).build("maid");

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

    private static final String MODEL_ID_TAG = "ModelId";
    private static final String TASK_TAG = "MaidTask";
    private static final String PICKUP_TAG = "MaidIsPickup";
    private static final String HOME_TAG = "MaidIsHome";
    private static final String RIDEABLE_TAG = "MaidIsRideable";
    private static final String BACKPACK_LEVEL_TAG = "MaidBackpackLevel";
    private static final String MAID_INVENTORY_TAG = "MaidInventory";
    private static final String MAID_BAUBLE_INVENTORY_TAG = "MaidBaubleInventory";
    private static final String STRUCK_BY_LIGHTNING_TAG = "StruckByLightning";
    private static final String HUNGER_TAG = "MaidHunger";
    private static final String FAVORABILITY_TAG = "MaidFavorability";
    private static final String EXPERIENCE_TAG = "MaidExperience";

    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
    private static final int HUNGER_VALUE_REFUSING_TASK = -256;
    private static int PLAYER_HURT_SOUND_COUNT = 120;
    private static int PICKUP_SOUND_COUNT = 5;

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
    public int takeXpDelay;
    /**
     * 用于替换背包延时的参数
     */
    private int backpackDelay = 0;
    private boolean sleep = false;
    private IMaidTask task = TaskManager.getIdleTask();

    protected EntityMaid(EntityType<EntityMaid> type, World world) {
        super(type, world);
        ((GroundPathNavigator) this.getNavigation()).setCanOpenDoors(true);
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
        return true;
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
    public void baseTick() {
        super.baseTick();
        if (backpackDelay > 0) {
            backpackDelay--;
        }
        if (takeXpDelay > 0) {
            takeXpDelay--;
        }
        if (PLAYER_HURT_SOUND_COUNT > 0) {
            PLAYER_HURT_SOUND_COUNT--;
        }
        this.spawnPortalParticle();
        this.randomRestoreHealth();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();
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

    @Override
    protected void pushEntities() {
        super.pushEntities();
        // 只有拾物模式开启，驯服状态下才可以捡起物品
        if (this.isPickup() && this.isTame()) {
            List<Entity> entityList = this.level.getEntities(this,
                    this.getBoundingBox().inflate(0.5, 0, 0.5), EntityMaidPredicates.IS_PICKUP);
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
                    PICKUP_SOUND_COUNT--;
                    if (PICKUP_SOUND_COUNT == 0) {
                        this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
                        PICKUP_SOUND_COUNT = 5;
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
        if (!this.level.isClientSide && entityXPOrb.isAlive() && entityXPOrb.tickCount > 2 && this.takeXpDelay == 0) {
            this.takeXpDelay = 2;
            // 这是向客户端同步数据用的，如果加了这个方法，会有短暂的拾取动画和音效
            this.take(entityXPOrb, 1);
            if (!MinecraftForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
                PICKUP_SOUND_COUNT--;
                if (PICKUP_SOUND_COUNT == 0) {
                    this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
                    PICKUP_SOUND_COUNT = 5;
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
                PICKUP_SOUND_COUNT--;
                if (PICKUP_SOUND_COUNT == 0) {
                    this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
                    PICKUP_SOUND_COUNT = 5;
                }
            }

            // 对经验修补的应用，因为全部来自于原版，所以效果也是相同的
            Map.Entry<EquipmentSlotType, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, this, ItemStack::isDamaged);
            int xpValue = EntityPowerPoint.transPowerValueToXpValue(powerPoint.getValue());
            if (entry != null) {
                ItemStack itemstack = entry.getValue();
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

    public boolean pickupArrow(AbstractArrowEntity arrow, boolean simulate) {
        if (!this.level.isClientSide && arrow.isAlive() && arrow.inGround && arrow.shakeTime <= 0) {
            // 先判断箭是否处于可以拾起的状态
            if (arrow.pickup != AbstractArrowEntity.PickupStatus.ALLOWED) {
                return false;
            }
            // 能够塞入
            ItemStack stack = getArrowFromEntity(arrow);
            if (!ItemHandlerHelper.insertItemStacked(getAvailableInv(false), stack, simulate).isEmpty()) {
                return false;
            }
            // 非模拟状态下，清除实体箭
            if (!simulate) {
                // 这是向客户端同步数据用的，如果加了这个方法，会有短暂的拾取动画和音效
                this.take(arrow, 1);
                if (!MinecraftForge.EVENT_BUS.post(new MaidPlaySoundEvent(this))) {
                    PICKUP_SOUND_COUNT--;
                    if (PICKUP_SOUND_COUNT == 0) {
                        this.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);
                        PICKUP_SOUND_COUNT = 5;
                    }
                }
                arrow.remove();
            }
            return true;
        }
        return false;
    }

    private ItemStack getArrowFromEntity(AbstractArrowEntity entity) {
        try {
            Method method = ObfuscationReflectionHelper.findMethod(entity.getClass(), "func_184550_j");
            return (ItemStack) method.invoke(entity);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return new ItemStack(Items.ARROW);
        } catch (ObfuscationReflectionHelper.UnableToFindMethodException e) {
            // 临时修复匠魂可能存在拾取箭的问题
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean result = super.doHurtTarget(entityIn);
        this.getMainHandItem().hurtAndBreak(1, this, (maid) -> maid.broadcastBreakEvent(Hand.MAIN_HAND));
        return result;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof PlayerEntity && this.isOwnedBy((PlayerEntity) source.getEntity())) {
            // 玩家对自己女仆的伤害数值为 1/5，最大为 2
            amount = MathHelper.clamp(amount / 5, 0, 2);
        }
        return super.hurt(source, amount);
    }

    public boolean canPickUp(Entity entity) {
        if (isPickup()) {
            if (entity.isInWater()) {
                return false;
            }
            if (entity instanceof ItemEntity) {
                return pickupItem((ItemEntity) entity, true);
            }
            if (entity instanceof AbstractArrowEntity) {
                return pickupArrow((AbstractArrowEntity) entity, true);
            }
            if (entity instanceof ExperienceOrbEntity) {
                return true;
            }
            return entity instanceof EntityPowerPoint;
        }
        return false;
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
        if (target instanceof IMob) {
            return super.canAttack(target);
        }
        return false;
    }

    private void spawnPortalParticle() {
        if (this.level.isClientSide && this.isInvulnerable() && this.getOwner() != null) {
            for (int i = 0; i < 2; ++i) {
                this.level.addParticle(ParticleTypes.PORTAL,
                        this.getX() + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth(),
                        this.getY() + this.random.nextDouble() * (double) this.getBbHeight() - 0.25D,
                        this.getZ() + (this.random.nextDouble() - 0.5D) * (double) this.getBbWidth(),
                        (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(),
                        (this.random.nextDouble() - 0.5D) * 2.0D);
            }
        }
    }

    private void randomRestoreHealth() {
        if (this.getHealth() < this.getMaxHealth() && random.nextFloat() < 0.0025) {
            this.heal(1);
            this.spawnRestoreHealthParticle(random.nextInt(3) + 7);
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
        compound.putInt(HUNGER_TAG, getHunger());
        compound.putInt(FAVORABILITY_TAG, getFavorability());
        compound.putInt(EXPERIENCE_TAG, getExperience());
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
        if (compound.contains(HUNGER_TAG, Constants.NBT.TAG_INT)) {
            setHunger(compound.getInt(HUNGER_TAG));
        }
        if (compound.contains(FAVORABILITY_TAG, Constants.NBT.TAG_INT)) {
            setFavorability(compound.getInt(FAVORABILITY_TAG));
        }
        if (compound.contains(EXPERIENCE_TAG, Constants.NBT.TAG_INT)) {
            setExperience(compound.getInt(EXPERIENCE_TAG));
        }
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity playerEntity) {
        return new MaidInventory(id, inventory, getId());
    }

    private boolean openMaidGui(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity && !this.isSleep()) {
            this.navigation.stop();
            NetworkHooks.openGui((ServerPlayerEntity) player, this, (buffer) -> buffer.writeInt(getId()));
        }
        return true;
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
        // TODO：背包掉落和记录女仆数据的物品
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
            // fixme：当前存在问题，呼喊声还是会叠加播放
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
        float temp = this.level.getBiome(blockPosition()).getBaseTemperature();
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
    public Item getTamedItem() {
        ResourceLocation key = new ResourceLocation(Config.MAID_TAMED_ITEM.get());
        if (ForgeRegistries.ITEMS.containsKey(key)) {
            return ForgeRegistries.ITEMS.getValue(key);
        }
        return Items.CAKE;
    }

    @SuppressWarnings("all")
    public Item getTemptationItem() {
        ResourceLocation key = new ResourceLocation(Config.MAID_TEMPTATION_ITEM.get());
        if (ForgeRegistries.ITEMS.containsKey(key)) {
            return ForgeRegistries.ITEMS.getValue(key);
        }
        return Items.CAKE;
    }

    @SuppressWarnings("all")
    public Item getNtrItem() {
        ResourceLocation key = new ResourceLocation(Config.MAID_NTR_ITEM.get());
        if (ForgeRegistries.ITEMS.containsKey(key)) {
            return ForgeRegistries.ITEMS.getValue(key);
        }
        return Items.STRUCTURE_VOID;
    }
}
