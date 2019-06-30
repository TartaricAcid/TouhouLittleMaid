package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.*;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuShoot;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuType;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.google.common.base.Predicate;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EntityArmorInvWrapper;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;

import javax.annotation.Nullable;
import java.util.List;

public class EntityMaid extends EntityTameable implements IRangedAttackMob {
    public static final Predicate<Entity> IS_PICKUP = entity -> (entity instanceof EntityItem || entity instanceof EntityXPOrb);
    public static final Predicate<Entity> IS_MOB = entity -> entity instanceof EntityMob;
    public static final Predicate<Entity> CAN_SHEAR = entity -> entity instanceof IShearable;
    private static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PICKUP = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> MODE = EntityDataManager.createKey(EntityMaid.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> EXP = EntityDataManager.createKey(EntityMaid.class, DataSerializers.VARINT);
    private static final DataParameter<BlockPos> HOME_POS = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Boolean> HOME = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ARM_RISE = EntityDataManager.createKey(EntityMaid.class, DataSerializers.BOOLEAN);
    private final EntityArmorInvWrapper armorInvWrapper = new EntityArmorInvWrapper(this);
    private final EntityHandsInvWrapper handsInvWrapper = new EntityHandsInvWrapper(this);
    private final ItemStackHandler mainInv = new ItemStackHandler(15);
    private final ItemStackHandler baubleInv = new ItemStackHandler(8);

    public EntityMaid(World worldIn) {
        super(worldIn);
        setSize(0.6f, 1.5f);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityMaidSit(this));
        this.tasks.addTask(3, new EntityMaidPanic(this, 1.0f));
        this.tasks.addTask(3, new EntityMaidReturnHome(this, 0.6f, 200));
        this.tasks.addTask(4, new EntityMaidBeg(this, 8.0f));

        this.tasks.addTask(5, new EntityMaidAttack(this, 0.6f, false));
        this.tasks.addTask(5, new EntityMaidFarm(this, 0.6f));
        this.tasks.addTask(5, new EntityMaidAttackRanged(this, 0.6f, 2, 16));
        this.tasks.addTask(5, new EntityMaidFeedOwner(this, 8));
        this.tasks.addTask(5, new EntityMaidShear(this, 0.6f));

        this.tasks.addTask(6, new EntityMaidPickup(this, 0.8f));
        this.tasks.addTask(6, new EntityMaidFollowOwner(this, 0.8f, 6.0F, 2.0F));

        this.tasks.addTask(8, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 6.0F, 0.2f));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityWolf.class, 6.0F, 0.2f));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityOcelot.class, 6.0F, 0.2f));
        this.tasks.addTask(10, new EntityAILookIdle(this));
        this.tasks.addTask(11, new EntityMaidWanderAvoidWater(this, 0.4f));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityMob.class, true));
        this.targetTasks.addTask(3, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(4, new EntityAIOwnerHurtTarget(this));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(BEGGING, Boolean.FALSE);
        this.dataManager.register(PICKUP, Boolean.TRUE);
        this.dataManager.register(MODE, MaidMode.IDLE.getModeIndex());
        this.dataManager.register(EXP, 0);
        this.dataManager.register(HOME_POS, BlockPos.ORIGIN);
        this.dataManager.register(HOME, Boolean.FALSE);
        this.dataManager.register(ARM_RISE, Boolean.FALSE);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4d);
    }

    @Override
    protected void collideWithNearbyEntities() {
        super.collideWithNearbyEntities();

        List<Entity> entityList = this.world.getEntitiesInAABBexcluding(this,
                this.getEntityBoundingBox().expand(0.5, 0, 0.5).expand(-0.5, 0, -0.5), IS_PICKUP);
        if (!entityList.isEmpty() && this.isEntityAlive()) {
            for (Entity entityPickup : entityList) {
                // 如果是物品
                if (entityPickup instanceof EntityItem) {
                    ItemStack itemstack = ((EntityItem) entityPickup).getItem();
                    for (int i = 0; i < mainInv.getSlots(); i++) {
                        itemstack = mainInv.insertItem(i, itemstack, false);
                    }
                    if (itemstack.isEmpty()) {
                        entityPickup.setDead();
                        this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F,
                                ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    } else {
                        ((EntityItem) entityPickup).getItem().setCount(itemstack.getCount());
                    }
                }
                // 如果是经验
                if (entityPickup instanceof EntityXPOrb) {
                    EntityXPOrb entityXp = (EntityXPOrb) entityPickup;
                    if (!this.world.isRemote && entityXp.delayBeforeCanPickup == 0) {
                        // 对经验修补的应用，因为全部来自于原版，所以效果也是相同的
                        ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, this);
                        if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
                            int i = Math.min(entityXp.xpValue * 2, itemstack.getItemDamage());
                            entityXp.xpValue -= (i / 2);
                            itemstack.setItemDamage(itemstack.getItemDamage() - i);
                        }
                        if (entityXp.xpValue > 0) {
                            this.addExp(entityXp.xpValue);
                        }
                        entityXp.setDead();
                        this.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.2F,
                                (world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F);
                    }
                }
            }
        }
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        if (this.getMode() == MaidMode.RANGE_ATTACK) {
            EntityArrow entityarrow = this.getArrow(distanceFactor);

            // 如果获取得到的箭为 null，不执行攻击
            if (entityarrow == null) {
                return;
            }

            double x = target.posX - this.posX;
            double y = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - entityarrow.posY;
            double z = target.posZ - this.posZ;
            double pitch = MathHelper.sqrt(x * x + z * z) * 0.15D;

            entityarrow.shoot(x, y + pitch, z, 1.6F, 1);
            this.getHeldItemMainhand().damageItem(1, this);
            this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
            this.world.spawnEntity(entityarrow);
            return;
        }

        if (this.getMode() == MaidMode.DANMAKU_ATTACK) {
            // 获取周围 -10~10 范围内怪物数量
            List<Entity> entityList = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox()
                    .expand(8, 3, 8)
                    .expand(-8, -3, -8), IS_MOB);

            // 分为三档
            // 1 自机狙
            // <=5 60 度扇形
            // >5 120 度扇形
            if (entityList.size() <= 1) {
                DanmakuShoot.aimedShot(world, this, target, 2 * (distanceFactor + 1), 0, 0.3f * (distanceFactor + 1),
                        0.2f, DanmakuType.getType(rand.nextInt(2)), DanmakuColor.getColor(rand.nextInt(7)));
            } else if (entityList.size() <= 5) {
                DanmakuShoot.fanShapedShot(world, this, target, 2 * (distanceFactor + 1.2f), 0, 0.3f * (distanceFactor + 1),
                        0.2f, DanmakuType.getType(rand.nextInt(2)), DanmakuColor.getColor(rand.nextInt(7)), Math.PI / 3, 8);
            } else {
                DanmakuShoot.fanShapedShot(world, this, target, 2 * (distanceFactor + 1.5f), 0, 0.3f * (distanceFactor + 1),
                        0.2f, DanmakuType.getType(rand.nextInt(2)), DanmakuColor.getColor(rand.nextInt(7)), Math.PI * 2 / 3, 32);
            }
        }
    }

    /**
     * 依据背包里面的箭获取对应实体箭
     *
     * @return 如果没有箭，会返回一个 null 对象
     */
    @Nullable
    private EntityArrow getArrow(float chargeTime) {
        ItemStack itemstack = ItemStack.EMPTY;

        // 遍历女仆背包，找到第一个属于 arrow 的物品
        for (int i = 0; i < mainInv.getSlots(); ++i) {
            itemstack = mainInv.getStackInSlot(i);
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemArrow) {
                break;
            }
        }

        // 如果是光灵箭
        if (itemstack.getItem() == Items.SPECTRAL_ARROW) {
            EntitySpectralArrow entityspectralarrow = new EntitySpectralArrow(this.world, this);
            entityspectralarrow.setEnchantmentEffectsFromEntity(this, chargeTime);
            shrinkArrow(itemstack, entityspectralarrow);
            return entityspectralarrow;
        }

        // 如果是药水箭或者普通的箭
        if (itemstack.getItem() == Items.ARROW || itemstack.getItem() == Items.TIPPED_ARROW) {
            EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);
            entitytippedarrow.setEnchantmentEffectsFromEntity(this, chargeTime);
            entitytippedarrow.setPotionEffect(itemstack);
            shrinkArrow(itemstack, entitytippedarrow);
            return entitytippedarrow;
        }

        return null;
    }

    /**
     * 依据主手持有物品是否有无限附魔来决定消耗
     */
    private void shrinkArrow(ItemStack arrow, EntityArrow entityArrow) {
        // 无限附魔不存在或者小于 0 时
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, this.getHeldItemMainhand()) <= 0) {
            arrow.shrink(1);
            // 记得把箭设置为可以拾起状态
            entityArrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
        }
    }

    /**
     * 检查女仆背包内是否有箭
     */
    public boolean hasArrow() {
        for (int i = 0; i < mainInv.getSlots(); ++i) {
            ItemStack itemstack = mainInv.getStackInSlot(i);
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemArrow) {
                return true;
            }
        }
        return false;
    }

    /**
     * 指定物品类型能否插入女仆背包
     */
    public boolean canInsertSlot(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        for (int i = 0; i < mainInv.getSlots(); ++i) {
            ItemStack slot = mainInv.getStackInSlot(i);
            if (slot.isEmpty()) {
                return true;
            }
            if (slot.getItem() == stack.getItem() && slot.getCount() < slot.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        // 先获取实体基本的攻击数据
        float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        // 用来获取击退相关数据
        int i = 0;
        // 火焰附加
        int j = 0;

        if (entityIn instanceof EntityLivingBase) {
            // 附加上主手武器的攻击（含附魔）数据
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) entityIn).getCreatureAttribute());
            // 附加上击退附魔数据
            i += EnchantmentHelper.getKnockbackModifier(this);
            // 附加上火焰附加带来的数据
            j += EnchantmentHelper.getFireAspectModifier(this);
        }

        // 检查攻击对象是否是无敌的
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        // 如果不是无敌的
        if (flag) {
            // 应用击退效果
            if (i > 0) {
                ((EntityLivingBase) entityIn).knockBack(this, (float) i * 0.5F,
                        (double) MathHelper.sin(this.rotationYaw * 0.017453292F),
                        (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            // 应用火焰附加效果
            if (j > 0) {
                entityIn.setFire(j * 4);
            }

            // 如果攻击对象是玩家
            if (entityIn instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) entityIn;
                ItemStack itemstack = this.getHeldItemMainhand(); // 攻击方手持的物品
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY; // 玩家手持物品

                // 如果玩家手持盾牌而且还处于持盾状态，并且所持物品能够破盾
                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this)
                        && itemstack1.getItem().isShield(itemstack1, entityplayer)) {
                    float f1 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1) {
                        entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
                        this.world.setEntityState(entityplayer, (byte) 30);
                    }
                }
            }

            // 应用其他附魔
            this.applyEnchantments(this, entityIn);
            // 别忘了扣除耐久
            this.getHeldItemMainhand().damageItem(1, this);
        }

        return flag;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);

        // 打开 GUI 和切换待命状态
        if (this.isTamed() && hand == EnumHand.MAIN_HAND && this.getOwnerId().equals(player.getUniqueID()) && itemstack.isEmpty()) {
            // 先清除寻路逻辑
            this.getNavigator().clearPath();
            // 如果玩家为潜行状态，那么切换待命
            if (player.isSneaking()) {
                if (this.isSitting()) {
                    this.setSitting(false);
                } else {
                    this.setSitting(true);
                }
                this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F,
                        ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            } else {
                // 否则打开 GUI
                player.openGui(TouhouLittleMaid.INSTANCE, 1, world, this.getEntityId(), 0, 0);
            }
            return true;
        }

        // 驯服
        if (!this.isTamed() && hand == EnumHand.MAIN_HAND && itemstack.getItem() == Items.CAKE) {
            if (!world.isRemote) {
                itemstack.shrink(1);
                this.setTamedBy(player);
                this.playTameEffect(true);
                this.getNavigator().clearPath();
                this.world.setEntityState(this, (byte) 7);
                this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F,
                        ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                return true;
            }
        }

        // 写入坐标
        if (this.isTamed() && hand == EnumHand.MAIN_HAND && this.getOwnerId().equals(player.getUniqueID()) && itemstack.getItem() == MaidItems.KAPPA_COMPASS) {
            if (!itemstack.isEmpty() && itemstack.getItem() == MaidItems.KAPPA_COMPASS && itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("Pos")) {
                int[] i = itemstack.getTagCompound().getIntArray("Pos");
                this.setHomePos(new BlockPos(i[0], i[1], i[2]));
                if (!world.isRemote) {
                    // 尝试移动到这里，距离超过 16 就传送
                    // 没办法，路径系统最大只允许寻路 16
                    if (this.getPosition().getDistance(i[0], i[1], i[2]) < 16) {
                        this.getNavigator().tryMoveToXYZ(i[0], i[1], i[2], 0.6f);
                    } else {
                        this.attemptTeleport(i[0], i[1], i[2]);
                    }
                    player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.kappa_compass.write_success"));
                }
                return true;
            }
            if (!world.isRemote) {
                player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.kappa_compass.write_fail"));
            }
        }
        return super.processInteract(player, hand);
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (!world.isRemote) {
            CombinedInvWrapper combinedInvWrapper = new CombinedInvWrapper(armorInvWrapper, handsInvWrapper, mainInv, baubleInv);
            for (int i = 0; i < combinedInvWrapper.getSlots(); ++i) {
                ItemStack itemstack = combinedInvWrapper.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    InventoryHelper.spawnItemStack(world, this.posX, this.posY, this.posZ, itemstack);
                }
            }
        }
    }

    @Override
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
        // 不要调用父类的掉落方法，很坑爹的会掉落耐久损失很多的东西
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("MaidInventory")) {
            mainInv.deserializeNBT((NBTTagCompound) compound.getTag("MaidInventory"));
        }
        if (compound.hasKey("BaubleInventory")) {
            baubleInv.deserializeNBT((NBTTagCompound) compound.getTag("BaubleInventory"));
        }
        if (compound.hasKey("IsPickup")) {
            setPickup(compound.getBoolean("IsPickup"));
        }
        if (compound.hasKey("MaidMode")) {
            setMode(MaidMode.getMode(compound.getInteger("MaidMode")));
        }
        if (compound.hasKey("MaidExp")) {
            setExp(compound.getInteger("MaidExp"));
        }
        if (compound.hasKey("HomePos")) {
            int[] pos = compound.getIntArray("HomePos");
            setHomePos(new BlockPos(pos[0], pos[1], pos[2]));
        }
        if (compound.hasKey("MaidHome")) {
            setHome(compound.getBoolean("MaidHome"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("MaidInventory", mainInv.serializeNBT());
        compound.setTag("BaubleInventory", baubleInv.serializeNBT());
        compound.setBoolean("IsPickup", isPickup());
        compound.setInteger("MaidMode", getMode().getModeIndex());
        compound.setInteger("MaidExp", getExp());
        compound.setIntArray("HomePos", new int[]{getHomePos().getX(), getHomePos().getY(), getHomePos().getZ()});
        compound.setBoolean("MaidHome", isHome());
        return compound;
    }

    public boolean isFarmItemInInventory() {
        CombinedInvWrapper combinedInvWrapper = new CombinedInvWrapper(handsInvWrapper, mainInv);
        for (int i = 0; i < combinedInvWrapper.getSlots(); ++i) {
            ItemStack itemstack = combinedInvWrapper.getStackInSlot(i);
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof IPlantable) {
                return true;
            }
        }
        return false;
    }

    /*----------------------------------- 相关实体数据的获取与设置 -------------------------------------------*/
    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return (T) new CombinedInvWrapper(armorInvWrapper, handsInvWrapper, mainInv, baubleInv);
    }

    public ItemStackHandler getMainInv() {
        return mainInv;
    }

    public ItemStackHandler getBaubleInv() {
        return baubleInv;
    }

    public boolean isBegging() {
        return this.dataManager.get(BEGGING);
    }

    public void setBegging(boolean beg) {
        this.dataManager.set(BEGGING, beg);
    }

    public boolean isPickup() {
        return this.dataManager.get(PICKUP);
    }

    public void setPickup(boolean pickup) {
        this.dataManager.set(PICKUP, pickup);
    }

    public MaidMode getMode() {
        return MaidMode.getMode(this.dataManager.get(MODE));
    }

    public void setMode(MaidMode maidMode) {
        this.dataManager.set(MODE, maidMode.getModeIndex());
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
}
