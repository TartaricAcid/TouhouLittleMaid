package com.github.tartaricacid.touhoulittlemaid.network.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SSpawnDanmakuPacket implements IPacket<IClientPlayNetHandler> {
    private int id;
    private double x;
    private double y;
    private double z;

    public SSpawnDanmakuPacket(EntityDanmaku danmaku) {
        this.id = danmaku.getId();
        this.x = danmaku.getX();
        this.y = danmaku.getY();
        this.z = danmaku.getZ();
    }

    @Override
    public void read(PacketBuffer buf) {
        this.id = buf.readVarInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeVarInt(this.id);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
    }

    @Override
    public void handle(IClientPlayNetHandler handler) {
        this.handleAddEntityDanmaku(this, handler);
    }

    @OnlyIn(Dist.CLIENT)
    private void handleAddEntityDanmaku(SSpawnDanmakuPacket packetIn, IClientPlayNetHandler handler) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientWorld level = minecraft.level;

        PacketThreadUtil.ensureRunningOnSameThread(packetIn, handler, minecraft);
        if (level != null) {
            double x = packetIn.getX();
            double y = packetIn.getY();
            double z = packetIn.getZ();
            Entity entity = new EntityDanmaku(level, x, y, z);
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
}
