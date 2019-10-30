package com.github.tartaricacid.touhoulittlemaid.inventory;

import javax.annotation.Nonnull;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

/**
 * @author TartaricAcid
 * @date 2019/10/9 19:50
 **/
public class MaidBeaconContainer extends Container {
    private TileEntityMaidBeacon tileEntityMaidBeacon;

    public MaidBeaconContainer(TileEntityMaidBeacon tileEntityMaidBeacon) {
        this.tileEntityMaidBeacon = tileEntityMaidBeacon;
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return true;
    }

    public TileEntityMaidBeacon getTileEntityMaidBeacon() {
        return tileEntityMaidBeacon;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener : this.listeners) {
            if (listener instanceof EntityPlayerMP) {
                SPacketUpdateTileEntity packet = tileEntityMaidBeacon.getUpdatePacket();
                if (packet != null) {
                    ((EntityPlayerMP) listener).connection.sendPacket(packet);
                }
            }
        }
    }
}
