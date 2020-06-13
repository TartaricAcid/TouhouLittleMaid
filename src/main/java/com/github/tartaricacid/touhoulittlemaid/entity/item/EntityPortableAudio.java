package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.item.PortableAudioGui;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPortableAudio extends AbstractEntityTrolley {
    private static final String PLAYING_TAG_NAME = "IsSongPlaying";
    private static final String SONG_ID_TAG_NAME = "SongId";
    private static final DataParameter<Boolean> PLAYING = EntityDataManager.createKey(EntityPortableAudio.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> SONG_ID = EntityDataManager.createKey(EntityPortableAudio.class, DataSerializers.STRING);

    public EntityPortableAudio(World worldIn) {
        super(worldIn);
        setSize(0.5f, 0.4f);
    }

    public EntityPortableAudio(World worldIn, double x, double y, double z, float yaw) {
        this(worldIn);
        this.setLocationAndAngles(x, y, z, yaw, 0);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(PLAYING, false);
        dataManager.register(SONG_ID, "0");
    }

    @Override
    protected boolean canKillEntity(EntityPlayer player) {
        return true;
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_CLOTH_HIT;
    }

    @Override
    protected Item getWithItem() {
        return MaidItems.PORTABLE_AUDIO;
    }

    @Override
    protected ItemStack getKilledStack() {
        return new ItemStack(MaidItems.PORTABLE_AUDIO);
    }

    @Override
    public void updatePassenger(AbstractEntityMaid maid) {
        if (maid.isPassenger(this)) {
            // 视线也必须同步，因为拉杆箱的朝向受视线限制
            // 只能以视线方向为中心左右各 90 度，不同步就会导致朝向错误
            this.rotationYawHead = maid.rotationYawHead;
            // 旋转方向同步，包括渲染的旋转方向
            this.rotationPitch = maid.rotationPitch;
            this.rotationYaw = maid.rotationYaw;
            this.renderYawOffset = maid.renderYawOffset;
            Vec3d vec3d = maid.getPositionVector().add(new Vec3d(0.3, 0.5625, -0.1).rotateYaw(maid.renderYawOffset * -0.01745329251f));
            this.setPosition(vec3d.x, vec3d.y, vec3d.z);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (world.isRemote) {
            Minecraft.getMinecraft().displayGuiScreen(new PortableAudioGui(this));
            return true;
        }
        return false;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setPlaying(compound.getBoolean(PLAYING_TAG_NAME));
        setSongId(compound.getInteger(SONG_ID_TAG_NAME));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean(PLAYING_TAG_NAME, isPlaying());
        compound.setLong(SONG_ID_TAG_NAME, getSongId());
    }

    public boolean isPlaying() {
        return this.dataManager.get(PLAYING);
    }

    public void setPlaying(boolean playing) {
        this.dataManager.set(PLAYING, playing);
    }

    public long getSongId() {
        return Long.parseUnsignedLong(this.dataManager.get(SONG_ID));
    }

    public void setSongId(long songId) {
        this.dataManager.set(SONG_ID, String.valueOf(songId));
    }
}
