package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.*;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.api.util.BaubleItemHandler;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGarageKit;
import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumSerializer;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.*;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySuitcase;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityRinnosuke;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import com.github.tartaricacid.touhoulittlemaid.internal.task.TaskIdle;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidHandsItemHandler;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidInventoryItemHandler;
import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.github.tartaricacid.touhoulittlemaid.util.ItemDropUtil;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EntityArmorInvWrapper;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class EntityMaid extends AbstractEntityMaid {
    private static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PICKUP = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> TASK = EntityDataManager.createKey(EntityMaid.class, DataSerializers.STRING);
    private static final DataParameter<Integer> EXP = EntityDataManager.createKey(EntityMaid.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> HOME = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ARM_RISE = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> MODEL_ID = EntityDataManager.createKey(EntityMaid.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> STRUCK_BY_LIGHTNING = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> SASIMONO_CRC32 = EntityDataManager.createKey(EntityMaid.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> SHOW_SASIMONO = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    /**
     * 无敌状态不会主动同步至客户端
     */
    private static final DataParameter<Boolean> INVULNERABLE = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> BACKPACK_LEVEL = EntityDataManager.createKey(EntityMaid.class, DataSerializers.VARINT);
    /**
     * 要在客户端显示女仆当前所处的模式，所以需要同步客户端
     */
    private static final DataParameter<Integer> COMPASS_MODE = EntityDataManager.createKey(EntityMaid.class, DataSerializers.VARINT);
    /**
     * 模式所应用的 AI 的优先级
     */
    private static final int TASK_PRIORITY = 5;
    /**
     * 拾起物品声音的延时计数器
     */
    private static int PICKUP_SOUND_COUNT = GeneralConfig.MAID_CONFIG.maidPickupSoundInterval;
    /**
     * 玩家伤害女仆后的声音延时计数器
     */
    private static int PLAYER_HURT_SOUND_COUNT = GeneralConfig.MAID_CONFIG.maidHurtSoundInterval;
    /**
     * 冷却时间计数器
     */
    private final CooldownTracker cooldownTracker = new CooldownTracker();
    /**
     * 各种物品栏
     */
    private final EntityArmorInvWrapper armorInvWrapper = new EntityArmorInvWrapper(this);
    private final EntityHandsInvWrapper handsInvWrapper = new MaidHandsItemHandler(this);
    private final ItemStackHandler mainInv = new MaidInventoryItemHandler(15);
    private final ItemStackHandler smallBackpackInv = new MaidInventoryItemHandler(10);
    private final ItemStackHandler middleBackpackInv = new MaidInventoryItemHandler(10);
    private final ItemStackHandler bigBackpackInv = new MaidInventoryItemHandler(10);
    private final BaubleItemHandler baubleInv = new BaubleItemHandler(8);
    /**
     * 依据此变量，在打开 GUI 时暂时中断实体的 AI 执行
     */
    public boolean guiOpening;
    /**
     * 是否开启 debug 模式下的地面显示，仅在客户端调用
     */
    public boolean isDebugFloorOpen = false;
    /**
     * 是否开启 debug 模式下的扫帚显示，仅在客户端调用
     */
    public boolean isDebugBroomShow = false;
    /**
     * 用来暂存当前实体所调用的 IMaidTask 对象
     */
    @Nonnull
    private IMaidTask task = LittleMaidAPI.getIdleTask();
    /**
     * 当前 IMaidTask 对象对应的 AI
     */
    @Nullable
    private EntityAIBase taskAI;
    /**
     * 罗盘存储的坐标
     */
    private List<BlockPos> compassPosList = Lists.newArrayList();
    private int currentIndex = 0;
    private boolean descending = false;
    /**
     * 部分功能开启或者关闭
     */
    private boolean canHoldTrolley = true;
    private boolean canHoldVehicle = true;
    private boolean canRidingBroom = true;
    private boolean canRiding = true;

    public EntityMaid(World worldIn) {
        super(worldIn);
        setSize(0.6f, 1.5f);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityMaidPanic(this, 1.0f));
        this.tasks.addTask(3, new EntityMaidAvoidEntity(this, EntityRinnosuke.class, 3.0f, 0.8d, 0.9d));
        this.tasks.addTask(3, new EntityMaidAvoidEntity(this, EntityFairy.class, 3.0f, 0.8d, 0.9d));
        this.tasks.addTask(3, new EntityMaidAvoidEntity(this, EntityCreeper.class, 6.0F, 0.8d, 0.9d));
        this.tasks.addTask(3, new EntityMaidCompassSetting(this, 0.6f));
        this.tasks.addTask(4, new EntityMaidBeg(this, 8.0f));
        this.tasks.addTask(4, new EntityMaidOpenDoor(this, true));

        this.tasks.addTask(6, new EntityMaidPickup(this, 0.8f));
        this.tasks.addTask(6, new EntityMaidFollowOwner(this, 0.8f, 5.0f, 2.0f));

        this.tasks.addTask(9, new EntityMaidWatchClosest2(this, EntityPlayer.class, 4.0F, 0.1f));
        this.tasks.addTask(9, new EntityMaidWatchClosest(this, EntityMaid.class, 4.0F, 0.2f));
        this.tasks.addTask(9, new EntityMaidWatchClosest(this, EntityWolf.class, 4.0F, 0.1f));
        this.tasks.addTask(9, new EntityMaidWatchClosest(this, EntityOcelot.class, 4.0F, 0.1f));
        this.tasks.addTask(9, new EntityMaidWatchClosest(this, EntityParrot.class, 4.0F, 0.1f));
        this.tasks.addTask(10, new EntityAILookIdle(this));
        this.tasks.addTask(11, new EntityMaidWanderAvoidWater(this, 0.5f));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityMob.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntitySlime.class, true));
        this.targetTasks.addTask(3, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(4, new EntityAIOwnerHurtTarget(this));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(BEGGING, Boolean.FALSE);
        this.dataManager.register(PICKUP, Boolean.TRUE);
        this.dataManager.register(TASK, TaskIdle.UID.toString());
        this.dataManager.register(EXP, 0);
        this.dataManager.register(HOME, Boolean.FALSE);
        this.dataManager.register(ARM_RISE, Boolean.FALSE);
        this.dataManager.register(MODEL_ID, "touhou_little_maid:hakurei_reimu");
        this.dataManager.register(STRUCK_BY_LIGHTNING, false);
        this.dataManager.register(SASIMONO_CRC32, String.valueOf(0L));
        this.dataManager.register(SHOW_SASIMONO, false);
        this.dataManager.register(INVULNERABLE, false);
        this.dataManager.register(BACKPACK_LEVEL, EnumBackPackLevel.EMPTY.getLevel());
        this.dataManager.register(COMPASS_MODE, ItemKappaCompass.Mode.NONE.ordinal());
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();

        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

        this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.5d);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4d);
    }

    @Override
    public void onLivingUpdate() {
        baubleInv.fireEvent((b, s) -> {
            b.onTick(this, s);
            return false;
        });
        // 更新手部使用
        this.updateArmSwingProgress();
        // 计数器自减
        if (PLAYER_HURT_SOUND_COUNT > 0) {
            PLAYER_HURT_SOUND_COUNT--;
        }
        spawnPortalParticle();
        // 随机回血
        this.randomRestoreHealth();
        super.onLivingUpdate();
        applyEntityRiding();
        applyNavigatorAndMoveHelper();
        // 计数器 tick
        cooldownTracker.tick();
    }

    private void spawnPortalParticle() {
        if (this.world.isRemote && this.getIsInvulnerable() && this.getOwnerId() != null) {
            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(EnumParticleTypes.PORTAL,
                        this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                        this.posY + this.rand.nextDouble() * (double) this.height - 0.25D,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                        (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(),
                        (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }
    }

    /**
     * 随机拥有小概率回血
     * <p>
     * 每 tick 有 0.25% 概率回血，也就是平均 20 秒尝试回血一次
     */
    private void randomRestoreHealth() {
        if (this.getHealth() < this.getMaxHealth() && rand.nextFloat() < 0.0025) {
            this.heal(1);
            this.spawnRestoreHealthParticle(rand.nextInt(3) + 7);
        }
    }

    /**
     * 来自原版爆炸粒子效果的修改
     */
    private void spawnRestoreHealthParticle(int particleCount) {
        if (this.world.isRemote) {
            for (int i = 0; i < particleCount; ++i) {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.world.spawnParticle(EnumParticleTypes.SPELL,
                        this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width - d0 * 10.0D,
                        this.posY + (double) (this.rand.nextFloat() * this.height) - d1 * 10.0D,
                        this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width - d2 * 10.0D,
                        d0, d1, d2);
            }
        }
    }

    /**
     * 时时刻刻检查寻路算法
     */
    private void applyNavigatorAndMoveHelper() {
        Entity passenger = getControllingPassenger();
        boolean isRidingBroom = passenger instanceof EntityMarisaBroom;
        boolean isFlyingNavigator = navigator instanceof PathNavigateFlying;
        boolean isFlyHelper = moveHelper instanceof EntityFlyHelper;
        // 非骑扫帚状态，对应寻路算法也对劲
        if (!isRidingBroom && !isFlyingNavigator && !isFlyHelper) {
            return;
        }
        // 骑扫帚状态，对应寻路算法也对劲
        if (isRidingBroom && isFlyingNavigator && isFlyHelper) {
            return;
        }
        // 否则，重置
        if (isRidingBroom) {
            navigator = createNavigatorFlying(world);
            moveHelper = new EntityFlyHelper(this);
        } else {
            navigator = createNavigatorGround(world);
            moveHelper = new EntityMoveHelper(this);
            // 以防万一设置重力
            this.setNoGravity(false);
        }
    }

    /**
     * 将贴近的实体附加到女仆身上去
     */
    private void applyEntityRiding() {
        // 取自原版船部分逻辑
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(),
                entity -> entity instanceof IEntityRidingMaid);

        if (list.isEmpty()) {
            return;
        }

        // 遍历进行乘坐判定
        for (Entity entity : list) {
            // 如果选择的实体不是已经坐上去的乘客
            if (!entity.isPassenger(this)) {
                // 没有实体坐在女仆上，女仆也没有坐在别的实体上
                boolean maidNotRiddenAndRiding = !this.isBeingRidden() && !this.isRiding();
                boolean passengerNotRiddenAndRiding = !entity.isBeingRidden() && !entity.isRiding();
                // 女仆处于待命或设置了不骑乘状态
                boolean stateIsForbid = this.isSitting() || !this.isCanRiding();
                // 服务端，而且尝试坐上去的实体是 IEntityRidingMaid
                if (!world.isRemote && !stateIsForbid && maidNotRiddenAndRiding && passengerNotRiddenAndRiding &&
                        entity instanceof IEntityRidingMaid && ((IEntityRidingMaid) entity).canRiding(this)) {
                    entity.startRiding(this);
                }
            }
        }
    }

    /**
     * 与旋转有关系的一堆东西，用来控制 IEntityRidingMaid 朝向
     */
    @Override
    public void updatePassenger(@Nonnull Entity passenger) {
        if (passenger instanceof IEntityRidingMaid) {
            ((IEntityRidingMaid) passenger).updatePassenger(this);
        } else {
            super.updatePassenger(passenger);
        }
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    protected void collideWithNearbyEntities() {
        super.collideWithNearbyEntities();
        // 只有拾物模式开启，驯服状态下才可以捡起物品
        if (this.isPickup() && this.isTamed()) {
            List<Entity> entityList = this.world.getEntitiesInAABBexcluding(this,
                    this.getEntityBoundingBox().expand(0.5, 0, 0.5).expand(-0.5, 0, -0.5), EntityMaidPredicate.IS_PICKUP);
            if (!entityList.isEmpty() && this.isEntityAlive()) {
                for (Entity entityPickup : entityList) {
                    // 如果是物品
                    if (entityPickup instanceof EntityItem) {
                        pickupItem((EntityItem) entityPickup, false);
                    }
                    // 如果是经验
                    if (entityPickup instanceof EntityXPOrb) {
                        pickupXPOrb((EntityXPOrb) entityPickup);
                    }
                    // 如果是 P 点
                    if (entityPickup instanceof EntityPowerPoint) {
                        pickupPowerPoint((EntityPowerPoint) entityPickup);
                    }
                    // 如果是箭
                    if (entityPickup instanceof EntityArrow) {
                        pickupArrow((EntityArrow) entityPickup, false);
                    }
                }
            }
        }
    }

    /**
     * 捡起物品部分的逻辑
     *
     * @param simulate 是否是模拟塞入，可用于检测此物品是否能拾起
     */
    public boolean pickupItem(EntityItem entityItem, boolean simulate) {
        // TODO: 当物品 pickupDelay 较小时等待
        if (!world.isRemote && entityItem.isEntityAlive() && !entityItem.cannotPickup()) {
            // 获取实体的物品堆
            ItemStack itemstack = entityItem.getItem();
            // 检查物品是否合法
            if (MaidInventoryItemHandler.isIllegalItem(itemstack)) {
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
                this.onItemPickup(entityItem, count - itemstack.getCount());
                PICKUP_SOUND_COUNT--;
                if (PICKUP_SOUND_COUNT == 0) {
                    this.playSound(MaidSoundEvent.MAID_ITEM_GET, 1, 1);
                    PICKUP_SOUND_COUNT = GeneralConfig.MAID_CONFIG.maidPickupSoundInterval;
                }
                // 如果遍历塞完后发现为空了
                if (itemstack.isEmpty()) {
                    // 清除这个实体
                    entityItem.setDead();
                } else {
                    // 将物品数量同步到客户端
                    entityItem.setItem(itemstack);
                }
            }

            return true;
        }
        return false;
    }

    /**
     * 捡起经验球部分的逻辑
     */
    private void pickupXPOrb(EntityXPOrb entityXPOrb) {
        if (!this.world.isRemote && entityXPOrb.isEntityAlive() && entityXPOrb.delayBeforeCanPickup == 0) {
            // 这是向客户端同步数据用的，如果加了这个方法，会有短暂的拾取动画和音效
            this.onItemPickup(entityXPOrb, 1);
            PICKUP_SOUND_COUNT--;
            if (PICKUP_SOUND_COUNT == 0) {
                this.playSound(MaidSoundEvent.MAID_ITEM_GET, 1, 1);
                PICKUP_SOUND_COUNT = GeneralConfig.MAID_CONFIG.maidPickupSoundInterval;
            }

            // 对经验修补的应用，因为全部来自于原版，所以效果也是相同的
            ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, this);
            if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
                int i = Math.min(entityXPOrb.xpValue * 2, itemstack.getItemDamage());
                entityXPOrb.xpValue -= (i / 2);
                itemstack.setItemDamage(itemstack.getItemDamage() - i);
            }
            if (entityXPOrb.xpValue > 0) {
                this.addExp(entityXPOrb.xpValue);
            }
            entityXPOrb.setDead();
        }
    }

    /**
     * 捡起 P 点部分的逻辑
     */
    private void pickupPowerPoint(EntityPowerPoint powerPoint) {
        if (!this.world.isRemote && powerPoint.isEntityAlive() && powerPoint.delayBeforeCanPickup == 0) {
            // 这是向客户端同步数据用的，如果加了这个方法，会有短暂的拾取动画和音效
            powerPoint.onPickup(this, 1);
            PICKUP_SOUND_COUNT--;
            if (PICKUP_SOUND_COUNT == 0) {
                this.playSound(MaidSoundEvent.MAID_ITEM_GET, 1, 1);
                PICKUP_SOUND_COUNT = GeneralConfig.MAID_CONFIG.maidPickupSoundInterval;
            }

            // 对经验修补的应用，因为全部来自于原版，所以效果也是相同的
            int xpValue = EntityPowerPoint.transPowerValueToXpValue(powerPoint.powerValue);
            ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, this);
            if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
                int i = Math.min(xpValue * 2, itemstack.getItemDamage());
                xpValue -= (i / 2);
                itemstack.setItemDamage(itemstack.getItemDamage() - i);
            }
            if (xpValue > 0) {
                this.addExp(xpValue);
            }
            powerPoint.setDead();
        }
    }

    /**
     * 捡起箭部分的逻辑
     *
     * @param simulate 是否是模拟塞入，可用于检测此物品是否能拾起
     */
    public boolean pickupArrow(EntityArrow arrow, boolean simulate) {
        if (!this.world.isRemote && arrow.isEntityAlive() && arrow.inGround && arrow.arrowShake <= 0) {
            // 先判断箭是否处于可以拾起的状态
            if (arrow.pickupStatus != EntityArrow.PickupStatus.ALLOWED) {
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
                this.onItemPickup(arrow, 1);
                PICKUP_SOUND_COUNT--;
                if (PICKUP_SOUND_COUNT == 0) {
                    this.playSound(MaidSoundEvent.MAID_ITEM_GET, 1, 1);
                    PICKUP_SOUND_COUNT = GeneralConfig.MAID_CONFIG.maidPickupSoundInterval;
                }
                arrow.setDead();
            }
            return true;
        }
        return false;
    }

    /**
     * 无法对抽象类使用 AT，所以通过反射获取箭
     */
    private ItemStack getArrowFromEntity(EntityArrow entity) {
        try {
            Method method = ReflectionHelper.findMethod(entity.getClass(), "getArrowStack", "func_184550_j");
            method.setAccessible(true);
            return (ItemStack) method.invoke(ItemStack.class);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return new ItemStack(Items.ARROW);
        } catch (ReflectionHelper.UnableToFindMethodException e) {
            // 临时解决办法：直接清空此掉落物
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        // 拥有旗指物时，玩家对自己女仆的伤害数值为 1/5，最大为 2
        if (source.getTrueSource() instanceof EntityPlayer && this.isOwner((EntityPlayer) source.getTrueSource()) && this.hasSasimono()) {
            amount = MathHelper.clamp(amount / 5, 0, 2);
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
        return cls != EntitySuitcase.class && cls != EntityChair.class && cls != EntityMarisaBroom.class && cls != EntityArmorStand.class && super.canAttackClass(cls);
    }

    @Override
    public void attackEntityWithRangedAttack(@Nonnull EntityLivingBase target, float distanceFactor) {
        task.onRangedAttack(this, target, distanceFactor);
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entityIn) {
        // 先获取实体基本的攻击数据
        float damage = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        // 用来获取击退相关数据
        int knockBack = 0;
        // 火焰附加
        int fireAspect = 0;

        if (entityIn instanceof EntityLivingBase) {
            // 附加上主手武器的攻击（含附魔）数据
            damage += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) entityIn).getCreatureAttribute());
            // 附加上击退附魔数据
            knockBack += EnchantmentHelper.getKnockbackModifier(this);
            // 附加上火焰附加带来的数据
            fireAspect += EnchantmentHelper.getFireAspectModifier(this);
        }

        // 检查攻击对象是否是无敌的
        boolean isInvulnerable = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), damage);

        // 如果不是无敌的
        if (isInvulnerable) {
            // 应用击退效果
            if (knockBack > 0) {
                ((EntityLivingBase) entityIn).knockBack(this, knockBack * 0.5F,
                        MathHelper.sin(this.rotationYaw * 0.017453292F),
                        (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            // 应用火焰附加效果
            if (fireAspect > 0) {
                entityIn.setFire(fireAspect * 4);
            }

            // 如果攻击对象是玩家
            if (entityIn instanceof EntityPlayer) {
                attackEntityAsPlayer((EntityPlayer) entityIn);
            }

            // 应用其他附魔
            this.applyEnchantments(this, entityIn);
            // 别忘了扣除耐久
            this.getHeldItemMainhand().damageItem(1, this);
        }

        return isInvulnerable;
    }

    /**
     * 攻击玩家
     */
    private void attackEntityAsPlayer(EntityPlayer entityplayer) {
        // 攻击方手持的物品
        ItemStack itemMaidHand = this.getHeldItemMainhand();
        // 玩家手持物品
        ItemStack itemPlayerHand = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

        // 如果玩家手持盾牌而且还处于持盾状态，并且所持物品能够破盾
        if (!itemMaidHand.isEmpty() && !itemPlayerHand.isEmpty() && itemMaidHand.getItem().canDisableShield(itemMaidHand, itemPlayerHand, entityplayer, this)
                && itemPlayerHand.getItem().isShield(itemPlayerHand, entityplayer)) {
            float efficiencyModifier = 0.25F + EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

            if (this.rand.nextFloat() < efficiencyModifier) {
                entityplayer.getCooldownTracker().setCooldown(itemPlayerHand.getItem(), 100);
                this.world.setEntityState(entityplayer, (byte) 30);
            }
        }
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        super.onStruckByLightning(lightningBolt);
        if (!isStruckByLightning()) {
            double beforeMaxHealth = this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue();
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(beforeMaxHealth * 2);
            setStruckByLightning(true);
        }
    }

    @Override
    protected void damageArmor(float damage) {
        // 依据原版玩家护甲耐久掉落机制书写而成
        damage = damage / 4.0F;

        // 最小伤害必须为 1.0
        if (damage < 1.0F) {
            damage = 1.0F;
        }

        for (int i = 0; i < this.armorInvWrapper.getSlots(); ++i) {
            ItemStack itemstack = this.armorInvWrapper.getStackInSlot(i);
            if (itemstack.getItem() instanceof ItemArmor) {
                itemstack.damageItem((int) damage, this);
            }
        }
    }

    @Nullable
    @Override
    public EntityAgeable createChild(@Nonnull EntityAgeable ageable) {
        return null;
    }

    @Override
    public boolean processInteract(EntityPlayer player, @Nullable EnumHand hand) {
        // 必须是主手
        if (hand == EnumHand.MAIN_HAND) {
            boolean isYourMaid = this.isTamed() && this.getOwnerId() != null && this.getOwnerId().equals(player.getUniqueID());
            ItemStack itemstack = player.getHeldItem(hand);
            if (isYourMaid) {
                // 事件系统实现更清晰的交互
                // 利用短路原理，逐个触发对应的交互事件
                return MinecraftForge.EVENT_BUS.post(new InteractMaidEvent(player, this, itemstack))
                        || itemstack.interactWithEntity(player, this, EnumHand.MAIN_HAND)
                        || openMaidGui(player);
            } else {
                return tamedMaid(itemstack, player);
            }
        }
        return false;
    }

    /**
     * 驯服女仆
     *
     * @return 该逻辑是否成功应用
     */
    private boolean tamedMaid(ItemStack itemstack, EntityPlayer player) {
        Item tamedItem = Item.getByNameOrId(GeneralConfig.MAID_CONFIG.maidTamedItem) == null ? Items.CAKE : Item.getByNameOrId(GeneralConfig.MAID_CONFIG.maidTamedItem);
        boolean isReloadTamedCondition = itemstack.getItem() == Item.getItemFromBlock(Blocks.STRUCTURE_VOID);
        boolean isNormalTamedCondition = !this.isTamed() && itemstack.getItem() == tamedItem;
        boolean isTamedCondition = isReloadTamedCondition || isNormalTamedCondition;
        MaidNumHandler num = player.getCapability(MaidNumSerializer.MAID_NUM_CAP, null);
        if (!world.isRemote && isTamedCondition && num != null) {
            if (num.canAdd()) {
                consumeItemFromStack(player, itemstack);
                this.setTamedBy(player);
                this.playTameEffect(true);
                this.getNavigator().clearPath();
                this.world.setEntityState(this, (byte) 7);
                this.playSound(MaidSoundEvent.MAID_TAMED, 1, 1);
                num.add();
                return true;
            } else {
                player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.owner_maid_num.can_not_add", num.get(), num.getMaxNum()));
            }
        }
        return false;
    }

    /**
     * 打开 GUI
     *
     * @return 该逻辑是否成功应用
     */
    private boolean openMaidGui(EntityPlayer player) {
        // 否则打开 GUI
        if (!world.isRemote) {
            player.openGui(TouhouLittleMaid.INSTANCE, MaidGuiHandler.MAIN_GUI.MAIN.getId(), world, this.getEntityId(), LittleMaidAPI.getTasks().indexOf(task), 0);
        }
        return true;
    }

    /**
     * 女仆可不能繁殖哦
     */
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    /**
     * 女仆可不能杂交哦
     */
    @Override
    public boolean canMateWith(@Nonnull EntityAnimal otherAnimal) {
        return false;
    }

    @Override
    public void onDeath(@Nonnull DamageSource cause) {
        super.onDeath(cause);

        // 防止 Forge 的事件系统的取消，导致后面掉落物的触发，故加此判定
        if (!dead) {
            return;
        }

        // 因为原版的死亡事件触发位置不对，故在此设计一个
        if (baubleInv.fireEvent((b, s) -> b.onDropsPre(this, s))) {
            return;
        }

        // 将女仆身上的物品进行掉落
        if (!world.isRemote) {
            IItemHandler itemHandler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            ItemDropUtil.dropItemHandlerItems(itemHandler, world, this.getPositionVector());
            // 最后掉落手办
            dropGarageKit();
        }
    }

    /**
     * 掉落手办
     */
    @SuppressWarnings("all")
    private void dropGarageKit() {
        // 先在死亡前获取女仆的 NBT 数据
        hurtResistantTime = 0;
        hurtTime = 0;
        deathTime = 0;
        NBTTagCompound entityTag = new NBTTagCompound();
        this.writeEntityToNBT(entityTag);
        // 剔除物品部分
        entityTag.removeTag("ArmorItems");
        entityTag.removeTag("HandItems");
        entityTag.removeTag(NBT.MAID_INVENTORY.getName());
        entityTag.removeTag(NBT.BAUBLE_INVENTORY.getName());
        // 掉落女仆手办
        ItemStack stack = BlockGarageKit.getItemStackWithData("touhou_little_maid:entity.passive.maid",
                this.getModelId(), entityTag);
        // 生成物品实体
        entityDropItem(stack, 0).setEntityInvulnerable(true);
    }

    @Override
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
        // 不要调用父类的掉落方法，很坑爹的会掉落耐久损失很多的东西
    }

    @Nonnull
    @Override
    @Deprecated
    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        } else {
            String modelId = getModelId();
            if (world.isRemote) {
                if (CustomResourcesLoader.MAID_MODEL.getInfo(modelId).isPresent()) {
                    return ParseI18n.parse(CustomResourcesLoader.MAID_MODEL.getInfo(modelId).get().getName());
                }
            } else {
                if (CommonProxy.VANILLA_ID_NAME_MAP.containsKey(modelId)) {
                    return CommonProxy.VANILLA_ID_NAME_MAP.get(modelId);
                }
            }
            return super.getName();
        }
    }

    @Override
    public double getYOffset() {
        if (this.getRidingEntity() instanceof EntityPlayer) {
            return this.getRidingEntity().isSneaking() ? 0.7d : 0.9d;
        } else {
            return super.getYOffset();
        }
    }

    /**
     * 用于刷怪蛋、刷怪笼、自然生成的初始化
     */
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingData) {
        if (!CommonProxy.VANILLA_ID_NAME_MAP.isEmpty()) {
            // 随机获取某个模型对象
            String key = CommonProxy.VANILLA_ID_NAME_MAP.keySet().stream().skip(rand.nextInt(CommonProxy.VANILLA_ID_NAME_MAP.size())).findFirst().get();
            // 应用各种数据
            this.setModelId(key);
        }
        return livingData;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey(NBT.MAID_INVENTORY.getName())) {
            mainInv.deserializeNBT((NBTTagCompound) compound.getTag(NBT.MAID_INVENTORY.getName()));
        }
        if (compound.hasKey(NBT.BAUBLE_INVENTORY.getName())) {
            baubleInv.deserializeNBT((NBTTagCompound) compound.getTag(NBT.BAUBLE_INVENTORY.getName()));
        }
        if (compound.hasKey(NBT.IS_PICKUP.getName())) {
            setPickup(compound.getBoolean(NBT.IS_PICKUP.getName()));
        }
        if (compound.hasKey(NBT.MAID_TASK.getName())) {
            setTask(LittleMaidAPI.findTask(new ResourceLocation(compound.getString(NBT.MAID_TASK.getName())))
                    .or(LittleMaidAPI.getIdleTask()));
        }
        if (compound.hasKey(NBT.MAID_EXP.getName())) {
            setExp(compound.getInteger(NBT.MAID_EXP.getName()));
        }
        if (compound.hasKey(NBT.MAID_HOME.getName())) {
            setHomeModeEnable(compound.getBoolean(NBT.MAID_HOME.getName()));
        }
        if (compound.hasKey(NBT.MODEL_ID.getName())) {
            setModelId(compound.getString(NBT.MODEL_ID.getName()));
        }
        if (compound.hasKey(NBT.STRUCK_BY_LIGHTNING.getName())) {
            setStruckByLightning(compound.getBoolean(NBT.STRUCK_BY_LIGHTNING.getName()));
        }
        if (compound.hasKey(NBT.SASIMONO_CRC32.getName())) {
            setSasimonoCRC32(compound.getLong(NBT.SASIMONO_CRC32.getName()));
        }
        if (compound.hasKey(NBT.SHOW_SASIMONO.getName())) {
            setShowSasimono(compound.getBoolean(NBT.SHOW_SASIMONO.getName()));
        }
        if (compound.hasKey(NBT.INVULNERABLE.getName())) {
            setEntityInvulnerable(compound.getBoolean(NBT.INVULNERABLE.getName()));
        }
        if (compound.hasKey(NBT.BACKPACK_LEVEL.getName())) {
            setBackpackLevel(EnumBackPackLevel.getEnumLevelByNum(compound.getInteger(NBT.BACKPACK_LEVEL.getName())));
        }
        if (compound.hasKey(NBT.MAID_SMALL_BACKPACK.getName())) {
            smallBackpackInv.deserializeNBT((NBTTagCompound) compound.getTag(NBT.MAID_SMALL_BACKPACK.getName()));
        }
        if (compound.hasKey(NBT.MAID_MIDDLE_BACKPACK.getName())) {
            middleBackpackInv.deserializeNBT((NBTTagCompound) compound.getTag(NBT.MAID_MIDDLE_BACKPACK.getName()));
        }
        if (compound.hasKey(NBT.MAID_BIG_BACKPACK.getName())) {
            bigBackpackInv.deserializeNBT((NBTTagCompound) compound.getTag(NBT.MAID_BIG_BACKPACK.getName()));
        }
        if (compound.hasKey(NBT.COMPASS_MODE.getName())) {
            setCompassMode(ItemKappaCompass.Mode.getModeByIndex(compound.getInteger(NBT.COMPASS_MODE.getName())));
        }
        if (compound.hasKey(NBT.COMPASS_POS_LIST.getName())) {
            NBTTagList tagList = compound.getTagList(NBT.COMPASS_POS_LIST.getName(), Constants.NBT.TAG_COMPOUND);
            List<BlockPos> posList = Lists.newArrayList();
            for (int i = 0; i < tagList.tagCount(); i++) {
                BlockPos pos = NBTUtil.getPosFromTag(tagList.getCompoundTagAt(i));
                posList.add(pos);
            }
            compassPosList = posList;
        }
        if (compound.hasKey(NBT.CURRENT_INDEX.getName())) {
            currentIndex = compound.getInteger(NBT.CURRENT_INDEX.getName());
        }
        if (compound.hasKey(NBT.DESCENDING.getName())) {
            descending = compound.getBoolean(NBT.DESCENDING.getName());
        }
        if (compound.hasKey(NBT.CAN_HOLD_TROLLEY.getName())) {
            canHoldTrolley = compound.getBoolean(NBT.CAN_HOLD_TROLLEY.getName());
        }
        if (compound.hasKey(NBT.CAN_HOLD_VEHICLE.getName())) {
            canHoldVehicle = compound.getBoolean(NBT.CAN_HOLD_VEHICLE.getName());
        }
        if (compound.hasKey(NBT.CAN_RIDING_BROOM.getName())) {
            canRidingBroom = compound.getBoolean(NBT.CAN_RIDING_BROOM.getName());
        }
        if (compound.hasKey(NBT.CAN_RIDING.getName())) {
            canRiding = compound.getBoolean(NBT.CAN_RIDING.getName());
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag(NBT.MAID_INVENTORY.getName(), mainInv.serializeNBT());
        compound.setTag(NBT.BAUBLE_INVENTORY.getName(), baubleInv.serializeNBT());
        compound.setBoolean(NBT.IS_PICKUP.getName(), isPickup());
        compound.setString(NBT.MAID_TASK.getName(), task.getUid().toString());
        compound.setInteger(NBT.MAID_EXP.getName(), getExp());
        compound.setBoolean(NBT.MAID_HOME.getName(), isHomeModeEnable());
        compound.setString(NBT.MODEL_ID.getName(), getModelId());
        compound.setBoolean(NBT.STRUCK_BY_LIGHTNING.getName(), isStruckByLightning());
        compound.setLong(NBT.SASIMONO_CRC32.getName(), getSasimonoCRC32());
        compound.setBoolean(NBT.SHOW_SASIMONO.getName(), isShowSasimono());
        compound.setBoolean(NBT.INVULNERABLE.getName(), getIsInvulnerable());
        compound.setInteger(NBT.BACKPACK_LEVEL.getName(), getBackLevel().getLevel());
        compound.setTag(NBT.MAID_SMALL_BACKPACK.getName(), smallBackpackInv.serializeNBT());
        compound.setTag(NBT.MAID_MIDDLE_BACKPACK.getName(), middleBackpackInv.serializeNBT());
        compound.setTag(NBT.MAID_BIG_BACKPACK.getName(), bigBackpackInv.serializeNBT());
        compound.setInteger(NBT.COMPASS_MODE.getName(), getCompassMode().ordinal());

        NBTTagList tagList = new NBTTagList();
        for (BlockPos pos : compassPosList) {
            tagList.appendTag(NBTUtil.createPosTag(pos));
        }
        compound.setTag(NBT.COMPASS_POS_LIST.getName(), tagList);

        compound.setInteger(NBT.CURRENT_INDEX.getName(), currentIndex);
        compound.setBoolean(NBT.DESCENDING.getName(), descending);
        compound.setBoolean(NBT.CAN_HOLD_TROLLEY.getName(), canHoldTrolley);
        compound.setBoolean(NBT.CAN_HOLD_VEHICLE.getName(), canHoldVehicle);
        compound.setBoolean(NBT.CAN_RIDING_BROOM.getName(), canRidingBroom);
        compound.setBoolean(NBT.CAN_RIDING.getName(), canRiding);
    }

    @Override
    public boolean getIsInvulnerable() {
        return this.dataManager.get(INVULNERABLE);
    }

    @Override
    public void setEntityInvulnerable(boolean isInvulnerable) {
        super.setEntityInvulnerable(isInvulnerable);
        this.dataManager.set(INVULNERABLE, isInvulnerable);
    }

    @Override
    public int getTalkInterval() {
        return GeneralConfig.MAID_CONFIG.maidTalkInterval;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return task.getAmbientSound(this);
    }

    @Override
    protected float getSoundPitch() {
        return 1 + rand.nextFloat() * 0.1F;
    }

    @Override
    public boolean isChild() {
        return false;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        if (damageSourceIn.isFireDamage()) {
            return MaidSoundEvent.MAID_HURT_FIRE;
        } else if (damageSourceIn.getTrueSource() instanceof EntityPlayer) {
            if (PLAYER_HURT_SOUND_COUNT == 0) {
                PLAYER_HURT_SOUND_COUNT = GeneralConfig.MAID_CONFIG.maidHurtSoundInterval;
                return MaidSoundEvent.MAID_PLAYER;
            } else {
                return null;
            }
        } else {
            return MaidSoundEvent.MAID_HURT;
        }
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return MaidSoundEvent.MAID_DEATH;
    }

    @Override
    public float getEyeHeight() {
        return this.height * (isSitting() ? 0.65F : 0.85F);
    }

    @Nonnull
    @Override
    protected PathNavigate createNavigator(@Nonnull World worldIn) {
        if (this.getControllingPassenger() instanceof EntityMarisaBroom) {
            return createNavigatorFlying(worldIn);
        }
        return createNavigatorGround(worldIn);
    }

    private PathNavigate createNavigatorFlying(World worldIn) {
        PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
        pathnavigateflying.setCanFloat(true);
        pathnavigateflying.setCanEnterDoors(true);
        pathnavigateflying.setCanOpenDoors(true);
        pathnavigateflying.setSpeed(0.8f);
        return pathnavigateflying;
    }

    private PathNavigate createNavigatorGround(World worldIn) {
        PathNavigateGround pathNavigate = new PathNavigateGround(this, worldIn) {
            @Override
            protected PathFinder getPathFinder() {
                this.nodeProcessor = new MaidNodeProcessor();
                this.nodeProcessor.setCanEnterDoors(true);
                return new PathFinder(this.nodeProcessor);
            }
        };
        pathNavigate.setBreakDoors(true);
        pathNavigate.setEnterDoors(true);
        pathNavigate.setCanSwim(true);
        pathNavigate.setAvoidSun(false);
        return pathNavigate;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            switch (this.getBackLevel()) {
                default:
                case EMPTY:
                    return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(
                            new CombinedInvWrapper(armorInvWrapper, handsInvWrapper, mainInv, baubleInv));
                case SMALL:
                    return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(
                            new CombinedInvWrapper(armorInvWrapper, handsInvWrapper, mainInv, smallBackpackInv, baubleInv));
                case MIDDLE:
                    return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(
                            new CombinedInvWrapper(armorInvWrapper, handsInvWrapper, mainInv, smallBackpackInv, middleBackpackInv, baubleInv));
                case BIG:
                    return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(
                            new CombinedInvWrapper(armorInvWrapper, handsInvWrapper, mainInv, smallBackpackInv, middleBackpackInv, bigBackpackInv, baubleInv));
            }
        } else {
            return super.getCapability(capability, facing);
        }
    }

    @Override
    public IItemHandlerModifiable getInv(MaidInventory type) {
        switch (type) {
            case HAND:
                return handsInvWrapper;
            case BAUBLE:
                return baubleInv;
            case ARMOR:
                return armorInvWrapper;
            case SMALL_BACKPACK:
                return smallBackpackInv;
            case MIDDLE_BACKPACK:
                return middleBackpackInv;
            case BIG_BACKPACK:
                return bigBackpackInv;
            case MAIN:
            default:
                return mainInv;
        }
    }

    @Override
    public BaubleItemHandler getBaubleInv() {
        return baubleInv;
    }

    @Override
    public CombinedInvWrapper getAvailableInv(boolean handsFirst) {
        CombinedInvWrapper combinedInvWrapper;
        switch (this.getBackLevel()) {
            default:
            case EMPTY:
                combinedInvWrapper = new CombinedInvWrapper(mainInv);
                break;
            case SMALL:
                combinedInvWrapper = new CombinedInvWrapper(mainInv, smallBackpackInv);
                break;
            case MIDDLE:
                combinedInvWrapper = new CombinedInvWrapper(mainInv, smallBackpackInv, middleBackpackInv);
                break;
            case BIG:
                combinedInvWrapper = new CombinedInvWrapper(mainInv, smallBackpackInv, middleBackpackInv, bigBackpackInv);
                break;
        }
        return handsFirst ? new CombinedInvWrapper(handsInvWrapper, combinedInvWrapper) : new CombinedInvWrapper(combinedInvWrapper, handsInvWrapper);
    }

    public IItemHandlerModifiable getAllBackpackInv() {
        return new CombinedInvWrapper(smallBackpackInv, middleBackpackInv, bigBackpackInv);
    }

    private static final float INFINITY_LEASHED_DISTANCE = -1.0f;
    private BlockPos leashedPosition = BlockPos.ORIGIN;
    private float maximumLeashedDistance = INFINITY_LEASHED_DISTANCE;

    @Override
    public boolean isWithinHomeDistanceCurrentPosition() {
        return this.isWithinHomeDistanceFromPosition(new BlockPos(this));
    }

    @Override
    public boolean isWithinHomeDistanceFromPosition(@Nonnull BlockPos pos) {
        if (!isHomeModeEnable()) {
            return true;
        }
        float maxDistance = getMaximumHomeDistance();
        BlockPos homePos = getHomePosition();
        if (getCompassMode() == ItemKappaCompass.Mode.SET_RANGE && maxDistance != INFINITY_LEASHED_DISTANCE) {
            List<BlockPos> posList = getCompassPosList(getCompassMode());
            int index = posList.size() - 1;
            BlockPos pos1 = posList.get(index);
            BlockPos pos2 = posList.get(--index);
            boolean xIsOkay = Math.min(pos1.getX(), pos2.getX()) <= pos.getX() && pos.getX() <= Math.max(pos1.getX(), pos2.getX());
            boolean yIsOkay = Math.min(pos1.getY(), pos2.getY()) <= pos.getY() && pos.getY() <= Math.max(pos1.getY(), pos2.getY());
            boolean zIsOkay = Math.min(pos1.getZ(), pos2.getZ()) <= pos.getZ() && pos.getZ() <= Math.max(pos1.getZ(), pos2.getZ());
            return xIsOkay && yIsOkay && zIsOkay && this.isHomeModeEnable();
        }
        if (maxDistance == INFINITY_LEASHED_DISTANCE) {
            return true;
        } else {
            return homePos.distanceSq(pos) < (double) (maxDistance * maxDistance);
        }
    }

    /**
     * 对于女仆来说，此方法只会被栓绳调用
     */
    @Override
    public void setHomePosAndDistance(@Nonnull BlockPos pos, int distance) {
        this.leashedPosition = pos;
        this.maximumLeashedDistance = (float) distance;
    }

    @Nonnull
    @Override
    public BlockPos getHomePosition() {
        if (isLeashedAndInSameWorld()) {
            return leashedPosition;
        }
        if (!isHomeModeEnable()) {
            return BlockPos.ORIGIN;
        }
        List<BlockPos> posList = getCompassPosList(getCompassMode());
        if (posList.size() > 0) {
            switch (getCompassMode()) {
                case SINGLE_POINT:
                    return posList.get(0);
                case MULTI_POINT_CLOSURE:
                case MULTI_POINT_REENTRY:
                    return posList.get(getCurrentIndex());
                case SET_RANGE:
                    if (posList.size() > 1) {
                        int index = posList.size() - 1;
                        BlockPos tmpPos = posList.get(index).add(posList.get(--index));
                        return new BlockPos(tmpPos.getX() / 2, tmpPos.getY() / 2, tmpPos.getZ() / 2);
                    }
                case NONE:
                    return BlockPos.ORIGIN;
            }
        }
        return BlockPos.ORIGIN;
    }

    @Override
    public float getMaximumHomeDistance() {
        if (isLeashedAndInSameWorld()) {
            return maximumLeashedDistance;
        }
        if (!isHomeModeEnable()) {
            return INFINITY_LEASHED_DISTANCE;
        }
        List<BlockPos> posList = getCompassPosList(getCompassMode());
        if (posList.size() > 0) {
            switch (getCompassMode()) {
                case SINGLE_POINT:
                case MULTI_POINT_CLOSURE:
                case MULTI_POINT_REENTRY:
                    return ItemKappaCompass.MAX_DISTANCE / 2.0f;
                case SET_RANGE:
                    if (posList.size() > 1) {
                        int index = posList.size() - 1;
                        return MathHelper.sqrt(posList.get(index).distanceSq(posList.get(--index)));
                    }
                case NONE:
                    return INFINITY_LEASHED_DISTANCE;
            }
        }
        return INFINITY_LEASHED_DISTANCE;
    }

    @Override
    public boolean hasHome() {
        if (isLeashedAndInSameWorld()) {
            return maximumLeashedDistance == INFINITY_LEASHED_DISTANCE;
        }
        return (getCompassMode() != ItemKappaCompass.Mode.NONE) && isHomeModeEnable();
    }

    private boolean isLeashedAndInSameWorld() {
        return getLeashed() && getLeashHolder() != null && getLeashHolder().world == world;
    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer player) {
        return player.equals(this.getOwner()) && super.canBeLeashedTo(player);
    }

    @Nullable
    @Override
    public EntityLivingBase getAttackTarget() {
        boolean hasSasimonoAndTargetIsPlayer = this.hasSasimono() && super.getAttackTarget() instanceof EntityPlayer;
        boolean hasSasimonoAndTargetIsTameable = this.hasSasimono() && super.getAttackTarget() instanceof EntityTameable;
        if (hasSasimonoAndTargetIsPlayer || hasSasimonoAndTargetIsTameable) {
            return null;
        }
        return super.getAttackTarget();
    }

    @Override
    public void setAttackTarget(@Nullable EntityLivingBase entityLivingBase) {
        boolean hasSasimonoAndTargetIsPlayer = this.hasSasimono() && entityLivingBase instanceof EntityPlayer;
        boolean hasSasimonoAndTargetIsTameable = this.hasSasimono() && entityLivingBase instanceof EntityTameable;
        if (hasSasimonoAndTargetIsPlayer || hasSasimonoAndTargetIsTameable) {
            super.setAttackTarget(null);
        } else {
            super.setAttackTarget(entityLivingBase);
        }
    }

    @Nullable
    @Override
    public EntityLivingBase getRevengeTarget() {
        boolean hasSasimonoAndTargetIsPlayer = this.hasSasimono() && super.getRevengeTarget() instanceof EntityPlayer;
        boolean hasSasimonoAndTargetIsTameable = this.hasSasimono() && super.getRevengeTarget() instanceof EntityTameable;
        if (hasSasimonoAndTargetIsPlayer || hasSasimonoAndTargetIsTameable) {
            return null;
        }
        return super.getRevengeTarget();
    }

    @Override
    public void setRevengeTarget(@Nullable EntityLivingBase entityLivingBase) {
        boolean hasSasimonoAndTargetIsPlayer = this.hasSasimono() && entityLivingBase instanceof EntityPlayer;
        boolean hasSasimonoAndTargetIsTameable = this.hasSasimono() && entityLivingBase instanceof EntityTameable;
        if (hasSasimonoAndTargetIsPlayer || hasSasimonoAndTargetIsTameable) {
            super.setRevengeTarget(null);
        } else {
            super.setRevengeTarget(entityLivingBase);
        }
    }

    public boolean isBegging() {
        return this.dataManager.get(BEGGING);
    }

    public void setBegging(boolean beg) {
        this.dataManager.set(BEGGING, beg);
    }

    @Override
    public boolean isPickup() {
        return this.dataManager.get(PICKUP);
    }

    public void setPickup(boolean pickup) {
        this.dataManager.set(PICKUP, pickup);
    }

    public IMaidTask getTask() {
        return LittleMaidAPI.findTask(new ResourceLocation(this.dataManager.get(TASK)))
                .or(LittleMaidAPI.getIdleTask());
    }

    public void setTask(IMaidTask task) {
        if (task == this.task) {
            return;
        }
        // 先应用 IMaidTask 对象对应的 AI
        if (!world.isRemote) {
            // 如果 taskAI 不为空，先将其移除
            if (this.taskAI != null) {
                tasks.removeTask(taskAI);
            }
            // 然后通过 IMaidTask 对象创建指定的 AI
            taskAI = task.createAI(this);
            // 再次检查此 AI 是否为空，加入 AI 列表中
            if (taskAI != null) {
                tasks.addTask(TASK_PRIORITY, taskAI);
            }
        }
        // 将实体的 IMaidTask 对象指向传入的 IMaidTask
        this.task = task;
        // 往实体数据中存入此对象
        this.dataManager.set(TASK, task.getUid().toString());
    }

    @Override
    public int getExp() {
        return this.dataManager.get(EXP);
    }

    @Override
    public void setExp(int expIn) {
        this.dataManager.set(EXP, expIn);
    }

    @Override
    public void addExp(int expIn) {
        setExp(getExp() + expIn);
    }

    @Override
    public boolean isHomeModeEnable() {
        return this.dataManager.get(HOME);
    }

    public void setHomeModeEnable(boolean enable) {
        this.dataManager.set(HOME, enable);
    }

    public boolean isSwingingArms() {
        return this.dataManager.get(ARM_RISE);
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
        this.dataManager.set(ARM_RISE, swingingArms);
    }

    @Override
    public String getModelId() {
        return this.dataManager.get(MODEL_ID);
    }

    @Override
    public void setModelId(String modelId) {
        this.dataManager.set(MODEL_ID, modelId);
    }

    public boolean isStruckByLightning() {
        return this.getDataManager().get(STRUCK_BY_LIGHTNING);
    }

    public void setStruckByLightning(boolean isStruck) {
        this.getDataManager().set(STRUCK_BY_LIGHTNING, isStruck);
    }

    public Long getSasimonoCRC32() {
        return Long.valueOf(this.dataManager.get(SASIMONO_CRC32));
    }

    public void setSasimonoCRC32(Long crc32) {
        this.dataManager.set(SASIMONO_CRC32, String.valueOf(crc32));
    }

    @Override
    public boolean hasSasimono() {
        return !this.dataManager.get(SASIMONO_CRC32).equals(String.valueOf(0L));
    }

    public boolean isShowSasimono() {
        return this.dataManager.get(SHOW_SASIMONO);
    }

    public void setShowSasimono(boolean isShow) {
        this.dataManager.set(SHOW_SASIMONO, isShow);
    }

    public void setBackpackLevel(EnumBackPackLevel level) {
        this.dataManager.set(BACKPACK_LEVEL,
                MathHelper.clamp(level.getLevel(), EnumBackPackLevel.EMPTY.getLevel(), EnumBackPackLevel.BIG.getLevel()));
    }

    public EnumBackPackLevel getBackLevel() {
        return EnumBackPackLevel.getEnumLevelByNum(this.dataManager.get(BACKPACK_LEVEL));
    }

    @Override
    public CooldownTracker getCooldownTracker() {
        return cooldownTracker;
    }

    @Override
    public boolean canDestroyBlock(BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().canEntityDestroy(state, world, pos, this) && ForgeEventFactory.onEntityDestroyBlock(this, pos, state);
    }

    @Override
    public boolean canPlaceBlock(BlockPos pos, IBlockState state) {
        // https://github.com/MinecraftForge/MinecraftForge/pull/5057
        IBlockState oldState = world.getBlockState(pos);
        return oldState.getBlock().isReplaceable(world, pos);
    }

    @Override
    public boolean destroyBlock(BlockPos pos) {
        return destroyBlock(pos, true);
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropBlock) {
        return canDestroyBlock(pos) && world.destroyBlock(pos, dropBlock);
    }

    @Override
    public boolean placeBlock(BlockPos pos, IBlockState state) {
        return canPlaceBlock(pos, state) && world.setBlockState(pos, state);
    }

    public ItemKappaCompass.Mode getCompassMode() {
        return ItemKappaCompass.Mode.getModeByIndex(this.dataManager.get(COMPASS_MODE));
    }

    public void setCompassMode(ItemKappaCompass.Mode mode) {
        this.dataManager.set(COMPASS_MODE, mode.ordinal());
    }

    public boolean setCompassPosList(List<BlockPos> posList, ItemKappaCompass.Mode mode) {
        int size = posList.size();
        if (size <= 0) {
            return false;
        }
        switch (mode) {
            case SINGLE_POINT:
                compassPosList = Collections.singletonList(posList.get(size - 1));
                return true;
            case MULTI_POINT_CLOSURE:
                if (size > 1) {
                    BlockPos pos1 = posList.get(size - 1);
                    BlockPos pos2 = posList.get(0);
                    if (pos1.distanceSq(pos2) <= Math.pow(ItemKappaCompass.MAX_DISTANCE, 2)) {
                        compassPosList = posList;
                        return true;
                    }
                }
                return false;
            case MULTI_POINT_REENTRY:
                if (size > 1) {
                    compassPosList = posList;
                    return true;
                }
                return false;
            case SET_RANGE:
                if (size > 1) {
                    compassPosList = Lists.newArrayList(posList.get(size - 1), posList.get(size - 2));
                    return true;
                }
                return false;
            case NONE:
            default:
                return false;
        }
    }

    public List<BlockPos> getCompassPosList(ItemKappaCompass.Mode mode) {
        int size = compassPosList.size();
        switch (mode) {
            case SINGLE_POINT:
                if (size > 0) {
                    return Collections.singletonList(compassPosList.get(size - 1));
                }
            case MULTI_POINT_REENTRY:
            case MULTI_POINT_CLOSURE:
                return compassPosList;
            case SET_RANGE:
                if (size > 1) {
                    return Lists.newArrayList(compassPosList.get(size - 1), compassPosList.get(size - 2));
                }
            case NONE:
            default:
                return Lists.newArrayList();
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = MathHelper.clamp(currentIndex, 0, compassPosList.size());
    }

    public boolean isDescending() {
        return descending;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
    }

    @Override
    public boolean isCanHoldTrolley() {
        return canHoldTrolley;
    }

    public void setCanHoldTrolley(boolean canHoldTrolley) {
        this.canHoldTrolley = canHoldTrolley;
    }

    @Override
    public boolean isCanHoldVehicle() {
        return canHoldVehicle;
    }

    public void setCanHoldVehicle(boolean canHoldVehicle) {
        this.canHoldVehicle = canHoldVehicle;
    }

    @Override
    public boolean isCanRidingBroom() {
        return canRidingBroom;
    }

    public void setCanRidingBroom(boolean canRidingBroom) {
        this.canRidingBroom = canRidingBroom;
    }

    public boolean isCanRiding() {
        return canRiding;
    }

    public void setCanRiding(boolean canRiding) {
        this.canRiding = canRiding;
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        EntityModelJson modelJson = CustomResourcesLoader.MAID_MODEL.getModel(getModelId()).orElse(null);
        if (modelJson == null) {
            return super.getRenderBoundingBox();
        }
        return modelJson.renderBoundingBox.offset(getPositionVector());
    }

    public enum NBT {
        // 女仆的物品栏
        MAID_INVENTORY("MaidInventory"),
        // 女仆饰品栏
        BAUBLE_INVENTORY("BaubleInventory"),
        // 能否捡起物品
        IS_PICKUP("IsPickup"),
        // 女仆模式
        MAID_TASK("MaidTask"),
        // 女仆经验
        MAID_EXP("MaidExp"),
        // 是否开启 Home 模式
        MAID_HOME("MaidHome"),
        // 模型
        MODEL_ID("ModelId"),
        // 是否被雷击过
        STRUCK_BY_LIGHTNING("StruckByLightning"),
        // 指物旗的 CRC32
        SASIMONO_CRC32("SasimonoCRC32"),
        // 是否显示指物旗
        SHOW_SASIMONO("ShowSasimono"),
        // 无敌状态
        INVULNERABLE("Invulnerable"),
        // 背包等级
        BACKPACK_LEVEL("BackpackLevel"),
        // 女仆小背包
        MAID_SMALL_BACKPACK("MaidSmallBackpack"),
        // 女仆中背包
        MAID_MIDDLE_BACKPACK("MaidMiddleBackpack"),
        // 女仆大背包
        MAID_BIG_BACKPACK("MaidBigBackpack"),
        // 女仆罗盘模式
        COMPASS_MODE("MaidCompassMode"),
        // 女仆罗盘坐标列表
        COMPASS_POS_LIST("MaidCompassPosList"),
        // 女仆当前寻路的坐标索引
        CURRENT_INDEX("MaidCurrentIndex"),
        // 当前寻路顺序是升序还是降序
        DESCENDING("MaidCurrentDescending"),
        // 能否持有拉杆箱
        CAN_HOLD_TROLLEY("MaidCanHoldTrolley"),
        // 能够持有载具
        CAN_HOLD_VEHICLE("MaidCanHoldVehicle"),
        // 能够使用扫帚
        CAN_RIDING_BROOM("MaidCanRidingBroom"),
        // 能够主动坐上坐垫之类的
        CAN_RIDING("MaidCanRidingEntity");

        private String name;

        /**
         * 在女仆存储时所使用的 NBT 标签名枚举
         *
         * @param name 存储的标签名
         */
        NBT(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum EnumBackPackLevel {
        // 背包大小
        EMPTY(0),
        SMALL(1),
        MIDDLE(2),
        BIG(3);

        private int level;

        EnumBackPackLevel(int level) {
            this.level = level;
        }

        public static EnumBackPackLevel getEnumLevelByNum(int num) {
            int numClamp = MathHelper.clamp(num, EMPTY.getLevel(), BIG.getLevel());
            return EnumBackPackLevel.values()[numClamp];
        }

        public int getLevel() {
            return level;
        }
    }
}
