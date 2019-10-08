package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBeacon;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/10/8 15:09
 **/
public class TileEntityMaidBeacon extends TileEntity implements ITickable {
    @Override
    public void update() {
        if (this.world.getTotalWorldTime() % 80L == 0L) {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() == MaidBlocks.MAID_BEACON && state.getValue(BlockMaidBeacon.POSITION) != BlockMaidBeacon.Position.DOWN) {
                updateBeacon();
            }
        }
    }

    private void updateBeacon() {
        if (this.world != null && !this.world.isRemote) {
            // TODO: 2019/10/8 临时设计，后续会增加更复杂功能
            List<EntityMaid> list = this.world.getEntitiesWithinAABB(EntityMaid.class, new AxisAlignedBB(pos).grow(8, 8, 8));
            for (EntityMaid maid : list) {
                maid.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 90, 1, true, true));
            }
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }
}
