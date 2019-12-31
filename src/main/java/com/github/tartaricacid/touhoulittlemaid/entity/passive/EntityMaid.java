package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.api.MaidInventory;
import com.github.tartaricacid.touhoulittlemaid.api.util.BaubleItemHandler;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGarageKit;
import com.github.tartaricacid.touhoulittlemaid.capability.CapabilityOwnerMaidNumHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.OwnerMaidNumHandler;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.*;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityRinnosuke;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import com.github.tartaricacid.touhoulittlemaid.internal.task.TaskIdle;
import com.github.tartaricacid.touhoulittlemaid.item.ItemAlbum;
import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import com.github.tartaricacid.touhoulittlemaid.item.ItemPhoto;
import com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.google.common.base.Predicate;
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
import net.minecraft.init.*;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.capabilities.Capability;
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
import java.util.List;

public class EntityMaid extends AbstractEntityMaid {
    public static final Predicate<Entity> IS_PICKUP = entity -> ((entity instanceof EntityItem && !isIllegalItem(((EntityItem) entity).getItem()))
            || entity instanceof EntityXPOrb || entity instanceof EntityPowerPoint || entity instanceof EntityArrow) && !entity.isInWater();
    public static final Predicate<Entity> IS_MOB = entity -> entity instanceof EntityMob && entity.isEntityAlive();
    public static final Predicate<Entity> CAN_SHEAR = entity -> entity instanceof IShearable && ((IShearable) entity).isShearable(new ItemStack(Items.SHEARS), entity.world, entity.getPosition())
            && entity.isEntityAlive();
    public static final Predicate<Entity> IS_COW = entity -> entity instanceof EntityCow && !((EntityCow) entity).isChild() && entity.isEntityAlive();

    private static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PICKUP = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> TASK = EntityDataManager.createKey(EntityMaid.class, DataSerializers.STRING);
    private static final DataParameter<Integer> EXP = EntityDataManager.createKey(EntityMaid.class, DataSerializers.VARINT);
    private static final DataParameter<BlockPos> HOME_POS = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Boolean> HOME = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ARM_RISE = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> MODEL_ID = EntityDataManager.createKey(EntityMaid.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> STRUCK_BY_LIGHTNING = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> SASIMONO_CRC32 = EntityDataManager.createKey(EntityMaid.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> SHOW_SASIMONO = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    // 无敌状态不会主动同步至客户端
    private static final DataParameter<Boolean> INVULNERABLE = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);

    /**
     * 模式所应用的 AI 的优先级
     */
    private static final int TASK_PRIORITY = 5;
    /**
     * 拾起物品声音的延时计数器
     */
    private static int pickupSoundCount = GeneralConfig.MAID_CONFIG.maidPickupSoundInterval;
    /**
     * 玩家伤害女仆后的声音延时计数器
     */
    private static int playerHurtSoundCount = GeneralConfig.MAID_CONFIG.maidHurtSoundInterval;
    private final EntityArmorInvWrapper armorInvWrapper = new EntityArmorInvWrapper(this);
    private final EntityHandsInvWrapper handsInvWrapper = new EntityHandsInvWrapper(this) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return !isIllegalItem(stack);
        }

        @Override
        @Nonnull
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            // 物品合法才允许插入，否则原样返回
            if (isItemValid(slot, stack)) {
                return super.insertItem(slot, stack, simulate);
            } else {
                return stack;
            }
        }
    };
    private final ItemStackHandler mainInv = new ItemStackHandler(15) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return !isIllegalItem(stack);
        }

        @Override
        @Nonnull
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            // 物品合法才允许插入，否则原样返回
            if (isItemValid(slot, stack)) {
                return super.insertItem(slot, stack, simulate);
            } else {
                return stack;
            }
        }
    };
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

    public EntityMaid(World worldIn) {
        super(worldIn);
        setSize(0.6f, 1.5f);
    }

    /**
     * 检查输入的物品是否是非法的
     */
    private static boolean isIllegalItem(ItemStack stack) {
        return stack.getItem() instanceof ItemShulkerBox || stack.getItem() instanceof ItemPhoto || stack.getItem() instanceof ItemAlbum;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityMaidSit(this));
        this.tasks.addTask(3, new EntityMaidPanic(this, 1.0f));
        this.tasks.addTask(3, new EntityMaidAvoidEntity(this, EntityRinnosuke.class, 3.0f, 0.8d, 0.9d));
        this.tasks.addTask(3, new EntityMaidAvoidEntity(this, EntityFairy.class, 3.0f, 0.8d, 0.9d));
        this.tasks.addTask(3, new EntityMaidAvoidEntity(this, EntityCreeper.class, 6.0F, 0.8d, 0.9d));
        this.tasks.addTask(3, new EntityMaidReturnHome(this, 0.6f, 200));
        this.tasks.addTask(4, new EntityMaidBeg(this, 8.0f));
        this.tasks.addTask(4, new EntityMaidGridInteract(this, 0.6f));
        this.tasks.addTask(4, new EntityMaidOpenDoor(this, true));

        this.tasks.addTask(6, new EntityMaidPickup(this, 0.8f));
        this.tasks.addTask(6, new EntityMaidFollowOwner(this, 0.8f, 5.0f, 2.0f));

        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 4.0F, 0.1f));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityMaid.class, 4.0F, 0.2f));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityWolf.class, 4.0F, 0.1f));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityOcelot.class, 4.0F, 0.1f));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityParrot.class, 4.0F, 0.1f));
        this.tasks.addTask(10, new EntityAILookIdle(this));
        this.tasks.addTask(11, new EntityMaidWanderAvoidWater(this, 0.4f));

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
        this.dataManager.register(HOME_POS, BlockPos.ORIGIN);
        this.dataManager.register(HOME, Boolean.FALSE);
        this.dataManager.register(ARM_RISE, Boolean.FALSE);
        this.dataManager.register(MODEL_ID, "touhou_little_maid:hakurei_reimu");
        this.dataManager.register(STRUCK_BY_LIGHTNING, false);
        this.dataManager.register(SASIMONO_CRC32, String.valueOf(0L));
        this.dataManager.register(SHOW_SASIMONO, false);
        this.dataManager.register(INVULNERABLE, false);
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
        if (playerHurtSoundCount > 0) {
            playerHurtSoundCount--;
        }
        spawnPortalParticle();
        // 随机回血
        this.randomRestoreHealth();
        super.onLivingUpdate();
        applyBroomRiding();
        applyNavigatorAndMoveHelper();
    }

    private void spawnPortalParticle() {
        if (this.world.isRemote && this.getIsInvulnerable()) {
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
     * 将贴近的扫把附加到女仆身上去
     */
    private void applyBroomRiding() {
        // 取自原版船部分逻辑
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(0, 0, 0),
                entity -> entity instanceof EntityMarisaBroom);

        if (list.isEmpty()) {
            return;
        }

        // 遍历进行乘坐判定
        for (Entity entity : list) {
            // 如果选择的实体不是已经坐上去的乘客
            if (!entity.isPassenger(this)) {
                // 没有实体坐在女仆上，女仆也没有坐在别的实体上
                boolean maidNotRiddenAndRiding = !entity.isBeingRidden() && !entity.isRiding();
                // 服务端，女仆没有处于待命状态，而且尝试坐上去的实体是扫把
                if (!world.isRemote && !this.isSitting() && maidNotRiddenAndRiding && entity instanceof EntityMarisaBroom) {
                    entity.startRiding(this);
                }
            }
        }
    }

    /**
     * 与旋转有关系的一堆东西，用来控制扫帚朝向
     */
    @Override
    public void updatePassenger(@Nonnull Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            if (passenger instanceof EntityMarisaBroom) {
                EntityMarisaBroom broom = (EntityMarisaBroom) passenger;
                // 视线也必须同步，因为扫把的朝向受视线限制
                // 只能以视线方向为中心左右各 90 度，不同步就会导致朝向错误
                broom.rotationYawHead = this.rotationYawHead;
                // 旋转方向同步，包括渲染的旋转方向
                broom.rotationPitch = this.rotationPitch;
                broom.rotationYaw = this.rotationYaw;
                broom.renderYawOffset = this.renderYawOffset;
                // fallDistance 永远为 0
                this.fallDistance = 0;
                broom.fallDistance = 0;
            }
        }
    }

    /**
     * 只有扫帚能骑上女仆，其他一概不允许
     */
    @Override
    protected boolean canBeRidden(Entity entityIn) {
        if (entityIn instanceof EntityMarisaBroom) {
            return true;
        }
        return super.canBeRidden(entityIn);
    }

    @Override
    public double getMountedYOffset() {
        return 0.15;
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
                    this.getEntityBoundingBox().expand(0.5, 0, 0.5).expand(-0.5, 0, -0.5), IS_PICKUP);
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
            if (isIllegalItem(itemstack)) {
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
                pickupSoundCount--;
                if (pickupSoundCount == 0) {
                    this.playSound(MaidSoundEvent.MAID_ITEM_GET, 1, 1);
                    pickupSoundCount = GeneralConfig.MAID_CONFIG.maidPickupSoundInterval;
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
            pickupSoundCount--;
            if (pickupSoundCount == 0) {
                this.playSound(MaidSoundEvent.MAID_ITEM_GET, 1, 1);
                pickupSoundCount = GeneralConfig.MAID_CONFIG.maidPickupSoundInterval;
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
            pickupSoundCount--;
            if (pickupSoundCount == 0) {
                this.playSound(MaidSoundEvent.MAID_ITEM_GET, 1, 1);
                pickupSoundCount = GeneralConfig.MAID_CONFIG.maidPickupSoundInterval;
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
                pickupSoundCount--;
                if (pickupSoundCount == 0) {
                    this.playSound(MaidSoundEvent.MAID_ITEM_GET, 1, 1);
                    pickupSoundCount = GeneralConfig.MAID_CONFIG.maidPickupSoundInterval;
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
        return cls != EntityChair.class && cls != EntityMarisaBroom.class && cls != EntityArmorStand.class && super.canAttackClass(cls);
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
                // 利用短路原理，逐个触发对应的交互事件
                return writeHomePos(itemstack, player) || applyPotionEffect(itemstack, player) || applyGoldenApple(itemstack, player)
                        || getExpBottle(itemstack, player) || dismountMaid(player) || switchSitting(player)
                        || itemstack.interactWithEntity(player, this, EnumHand.MAIN_HAND) || openMaidGui(player);
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
        OwnerMaidNumHandler num = player.getCapability(CapabilityOwnerMaidNumHandler.OWNER_MAID_NUM_CAP, null);
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
     * 对女仆应用坐标
     *
     * @return 该逻辑是否成功应用
     */
    private boolean writeHomePos(ItemStack itemstack, EntityPlayer player) {
        if (itemstack.getItem() == MaidItems.KAPPA_COMPASS) {
            BlockPos pos = ItemKappaCompass.getPos(itemstack);
            if (pos != null) {
                if (player.isSneaking()) {
                    setHomePos(BlockPos.ORIGIN);
                } else {
                    this.setHomePos(pos);
                    if (!world.isRemote) {
                        // 如果尝试移动失败，那就尝试传送
                        if (!getNavigator().tryMoveToXYZ(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.6f)) {
                            attemptTeleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                        }
                        player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.kappa_compass.write_success"));
                    }
                }
                return true;
            }
            if (!world.isRemote) {
                player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.kappa_compass.write_fail"));
            }
        }
        return false;
    }

    private boolean applyPotionEffect(ItemStack itemstack, EntityPlayer player) {
        if (itemstack.getItem() == Items.POTIONITEM) {
            if (player.capabilities.isCreativeMode) {
                itemstack.getItem().onItemUseFinish(itemstack.copy(), world, this);
            } else {
                player.setHeldItem(EnumHand.MAIN_HAND, itemstack.getItem().onItemUseFinish(itemstack, world, this));
            }
            this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.6f, 0.8F + rand.nextFloat() * 0.4F);
            return true;
        }
        return false;
    }

    private boolean applyGoldenApple(ItemStack itemstack, EntityPlayer player) {
        if (itemstack.getItem() == Items.GOLDEN_APPLE) {
            if (!world.isRemote) {
                // 作用效果
                if (itemstack.getMetadata() > 0) {
                    this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));
                    this.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, 0));
                    this.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, 0));
                    this.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 3));
                } else {
                    this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1));
                    this.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 0));
                }
            }

            // 物品消耗判定
            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }

            // 播放音效
            this.playSound(SoundEvents.ENTITY_PLAYER_BURP, 0.5F, rand.nextFloat() * 0.1F + 0.9F);
            return true;
        }
        return false;
    }

    private boolean getExpBottle(ItemStack itemstack, EntityPlayer player) {
        // WIKI 上说附魔之瓶会掉落 3-11 的经验
        // 那么我们就让其消耗 12 点经验获得一个附魔之瓶吧
        int costNum = 12;
        if (itemstack.getItem() == Items.GLASS_BOTTLE && this.getExp() / costNum > 0) {
            this.setExp(this.getExp() - costNum);
            itemstack.shrink(1);
            if (!world.isRemote) {
                InventoryHelper.spawnItemStack(world, player.posX, player.posY, player.posZ, new ItemStack(Items.EXPERIENCE_BOTTLE));
            }
            this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            return true;
        }
        return false;
    }

    /**
     * 解除女仆的所有骑乘，被骑乘状态
     */
    private boolean dismountMaid(EntityPlayer player) {
        if (player.isSneaking()) {
            // 满足其一，返回 true
            if (this.getRidingEntity() != null || this.getControllingPassenger() != null) {
                // 取消骑乘状态
                if (this.getRidingEntity() != null) {
                    this.dismountRidingEntity();
                }
                // 取消被骑乘状态
                if (this.getControllingPassenger() != null) {
                    this.getControllingPassenger().dismountRidingEntity();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 切换待命状态
     */
    private boolean switchSitting(EntityPlayer player) {
        if (player.isSneaking()) {
            this.setSitting(!this.isSitting());
            if (this.isSitting()) {
                // 清除寻路逻辑
                this.getNavigator().clearPath();
                // 清除所有的攻击目标
                this.setAttackTarget(null);
                this.setRevengeTarget(null);
            }
            this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F,
                    ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            return true;
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
            if (itemHandler != null) {
                for (int i = 0; i < itemHandler.getSlots(); ++i) {
                    ItemStack itemstack = itemHandler.getStackInSlot(i);
                    if (!itemstack.isEmpty()) {
                        InventoryHelper.spawnItemStack(world, this.posX, this.posY, this.posZ, itemstack);
                    }
                }
            }
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
                if (ClientProxy.MAID_MODEL.getInfo(modelId).isPresent()) {
                    return ParseI18n.parse(ClientProxy.MAID_MODEL.getInfo(modelId).get().getName());
                }
            } else {
                if (CommonProxy.VANILLA_ID_NAME_MAP.containsKey(modelId)) {
                    return CommonProxy.VANILLA_ID_NAME_MAP.get(modelId);
                }
            }
            return super.getName();
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
        if (compound.hasKey(NBT.HOME_POS.getName())) {
            int[] pos = compound.getIntArray(NBT.HOME_POS.getName());
            setHomePos(new BlockPos(pos[0], pos[1], pos[2]));
        }
        if (compound.hasKey(NBT.MAID_HOME.getName())) {
            setHome(compound.getBoolean(NBT.MAID_HOME.getName()));
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
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag(NBT.MAID_INVENTORY.getName(), mainInv.serializeNBT());
        compound.setTag(NBT.BAUBLE_INVENTORY.getName(), baubleInv.serializeNBT());
        compound.setBoolean(NBT.IS_PICKUP.getName(), isPickup());
        compound.setString(NBT.MAID_TASK.getName(), task.getUid().toString());
        compound.setInteger(NBT.MAID_EXP.getName(), getExp());
        compound.setIntArray(NBT.HOME_POS.getName(), new int[]{getHomePos().getX(), getHomePos().getY(), getHomePos().getZ()});
        compound.setBoolean(NBT.MAID_HOME.getName(), isHome());
        compound.setString(NBT.MODEL_ID.getName(), getModelId());
        compound.setBoolean(NBT.STRUCK_BY_LIGHTNING.getName(), isStruckByLightning());
        compound.setLong(NBT.SASIMONO_CRC32.getName(), getSasimonoCRC32());
        compound.setBoolean(NBT.SHOW_SASIMONO.getName(), isShowSasimono());
        compound.setBoolean(NBT.INVULNERABLE.getName(), getIsInvulnerable());
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
            if (playerHurtSoundCount == 0) {
                playerHurtSoundCount = GeneralConfig.MAID_CONFIG.maidHurtSoundInterval;
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
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new CombinedInvWrapper(armorInvWrapper, handsInvWrapper, mainInv, baubleInv));
        } else {
            return super.getCapability(capability, facing);
        }
    }

    @Override
    public IItemHandlerModifiable getInv(MaidInventory type) {
        switch (type) {
            case MAIN:
                return mainInv;
            case HAND:
                return handsInvWrapper;
            case BAUBLE:
                return baubleInv;
            case ARMOR:
                return armorInvWrapper;
            default:
                return mainInv;
        }
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

    @Override
    public BaubleItemHandler getBaubleInv() {
        return baubleInv;
    }

    @Override
    public CombinedInvWrapper getAvailableInv(boolean handsFirst) {
        return handsFirst ? new CombinedInvWrapper(handsInvWrapper, mainInv) : new CombinedInvWrapper(mainInv, handsInvWrapper);
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

    public int getExp() {
        return this.dataManager.get(EXP);
    }

    public void setExp(int expIn) {
        this.dataManager.set(EXP, expIn);
    }

    public void addExp(int expIn) {
        setExp(getExp() + expIn);
    }

    public BlockPos getHomePos() {
        return this.dataManager.get(HOME_POS);
    }

    public void setHomePos(BlockPos home) {
        this.dataManager.set(HOME_POS, home);
    }

    @Override
    public boolean isHome() {
        return this.dataManager.get(HOME);
    }

    public void setHome(boolean isHome) {
        this.dataManager.set(HOME, isHome);
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
        // TODO 破坏进度
        return canDestroyBlock(pos) && world.destroyBlock(pos, true);
    }

    @Override
    public boolean placeBlock(BlockPos pos, IBlockState state) {
        return canPlaceBlock(pos, state) && world.setBlockState(pos, state);
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        EntityModelJson modelJson = ClientProxy.MAID_MODEL.getModel(getModelId()).orElse(null);
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
        // Home 的坐标
        HOME_POS("HomePos"),
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
        INVULNERABLE("Invulnerable");

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
}
