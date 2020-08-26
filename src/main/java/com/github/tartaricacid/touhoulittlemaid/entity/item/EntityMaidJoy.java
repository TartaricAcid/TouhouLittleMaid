package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityMaidJoy extends Entity {
    private String type;

    public EntityMaidJoy(World worldIn) {
        super(worldIn);
        setSize(1f, 0.4f);
        //setSize(0, 0);
    }

    public EntityMaidJoy(World worldIn, String type, double x, double y, double z) {
        super(worldIn);
        this.type = type;
        this.setPosition(x, y, z);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getPassengers().isEmpty() || world.getBlockState(this.getPosition().down()).getBlock() != MaidBlocks.MAID_JOY) {
            this.setDead();
        }
    }

    @Override
    public double getMountedYOffset() {
        return -0.6;
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        type = compound.getString("JoyType");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setString("JoyType", type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
