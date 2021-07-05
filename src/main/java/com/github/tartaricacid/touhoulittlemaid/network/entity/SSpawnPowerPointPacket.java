package com.github.tartaricacid.touhoulittlemaid.network.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SSpawnPowerPointPacket implements IPacket<IClientPlayNetHandler> {
    private int id;
    private double x;
    private double y;
    private double z;
    private int value;

    public SSpawnPowerPointPacket(EntityPowerPoint powerPoint) {
        this.id = powerPoint.getId();
        this.x = powerPoint.getX();
        this.y = powerPoint.getY();
        this.z = powerPoint.getZ();
        this.value = powerPoint.getValue();
    }

    @Override
    public void read(PacketBuffer buf) {
        this.id = buf.readVarInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.value = buf.readVarInt();
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeVarInt(this.id);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeVarInt(this.value);
    }

    @Override
    public void handle(IClientPlayNetHandler handler) {
        this.handleAddEntityPowerPoint(this, handler);
    }

    @OnlyIn(Dist.CLIENT)
    private void handleAddEntityPowerPoint(SSpawnPowerPointPacket packetIn, IClientPlayNetHandler handler) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientWorld level = minecraft.level;

        PacketThreadUtil.ensureRunningOnSameThread(packetIn, handler, minecraft);
        if (level != null) {
            double x = packetIn.getX();
            double y = packetIn.getY();
            double z = packetIn.getZ();
            Entity entity = new EntityPowerPoint(level, x, y, z, packetIn.getValue());
            entity.setPacketCoordinates(x, y, z);
            entity.yRot = 0.0F;
            entity.xRot = 0.0F;
            entity.setId(packetIn.getId());
            level.putNonPlayerEntity(packetIn.getId(), entity);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getId() {
        return this.id;
    }

    @OnlyIn(Dist.CLIENT)
    public double getX() {
        return this.x;
    }

    @OnlyIn(Dist.CLIENT)
    public double getY() {
        return this.y;
    }

    @OnlyIn(Dist.CLIENT)
    public double getZ() {
        return this.z;
    }

    @OnlyIn(Dist.CLIENT)
    public int getValue() {
        return this.value;
    }
}
