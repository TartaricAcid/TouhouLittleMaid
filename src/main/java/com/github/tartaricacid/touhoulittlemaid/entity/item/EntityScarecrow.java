package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.client.gui.item.ScarecrowGui;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityScarecrow extends AbstractEntityFromItem {
    public static final String CUSTOM_NAME_TAG_NAME = "ScarecrowCustomName";
    public static final String TEXT_TAG_NAME = "ScarecrowText";
    public static final String SPECIAL_TAG_NAME = "ScarecrowSpecial";
    public static final DataParameter<String> TEXT = EntityDataManager.createKey(EntityScarecrow.class, DataSerializers.STRING);
    public static final DataParameter<Boolean> SPECIAL = EntityDataManager.createKey(EntityScarecrow.class, DataSerializers.BOOLEAN);

    public EntityScarecrow(World worldIn) {
        super(worldIn);
    }

    public EntityScarecrow(World worldIn, double x, double y, double z, float yaw) {
        this(worldIn);
        this.setLocationAndAngles(x, y, z, yaw, 0);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(TEXT, "");
        this.dataManager.register(SPECIAL, false);
    }

    @Override
    protected boolean canKillEntity(EntityPlayer player) {
        return true;
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ENTITY_ARMORSTAND_BREAK;
    }

    @Override
    protected Item getWithItem() {
        return MaidItems.SCARECROW;
    }

    @Override
    protected ItemStack getKilledStack() {
        ItemStack stack = new ItemStack(MaidItems.SCARECROW);
        NBTTagCompound tagCompound = new NBTTagCompound();
        if (this.hasCustomName()) {
            tagCompound.setString(CUSTOM_NAME_TAG_NAME, this.getCustomNameTag());
        }
        if (StringUtils.isNotBlank(this.getText())) {
            tagCompound.setString(TEXT_TAG_NAME, getText());
        }
        if (isSpecial()) {
            tagCompound.setBoolean(SPECIAL_TAG_NAME, isSpecial());
        }
        if (!tagCompound.isEmpty()) {
            stack.setTagCompound(tagCompound);
        }
        return stack;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (world.isRemote) {
            Minecraft.getMinecraft().displayGuiScreen(new ScarecrowGui(this));
            return true;
        }
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getRidingEntity() instanceof EntityMaidVehicle) {
            EntityMaidVehicle vehicle = (EntityMaidVehicle) this.getRidingEntity();
            // 视线也必须同步，因为拉杆箱的朝向受视线限制
            // 只能以视线方向为中心左右各 90 度，不同步就会导致朝向错误
            this.rotationYawHead = vehicle.rotationYawHead;
            // 旋转方向同步，包括渲染的旋转方向
            this.rotationPitch = vehicle.rotationPitch;
            this.rotationYaw = vehicle.rotationYaw;
            this.renderYawOffset = vehicle.renderYawOffset;
        }
    }

    @Override
    protected void collideWithEntity(@Nonnull Entity entityIn) {
    }

    @Override
    protected void collideWithNearbyEntities() {
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(),
                entity -> entity instanceof EntityMaidVehicle && ((EntityMaidVehicle) entity).canBeRidden(this));
        for (Entity entity : list) {
            this.startRiding(entity);
        }
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString(TEXT_TAG_NAME, getText());
        compound.setBoolean(SPECIAL_TAG_NAME, isSpecial());
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setText(compound.getString(TEXT_TAG_NAME));
        setSpecial(compound.getBoolean(SPECIAL_TAG_NAME));
    }

    public String getText() {
        return dataManager.get(TEXT);
    }

    public void setText(String text) {
        dataManager.set(TEXT, text);
    }

    public boolean isSpecial() {
        return dataManager.get(SPECIAL);
    }

    public void setSpecial(boolean isSpecial) {
        dataManager.set(SPECIAL, isSpecial);
    }

    @Override
    public void onStruckByLightning(@Nonnull EntityLightningBolt lightningBolt) {
        if (!this.isSpecial()) {
            this.setSpecial(true);
        }
    }
}
