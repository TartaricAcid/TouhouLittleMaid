package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/12/18 15:36
 **/
public class EntityNPCMaid extends EntityMaid {
    private static final DataParameter<Byte> A = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> B = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> C = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> D = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> E = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> F = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> G = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> H = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> I = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> J = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> K = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> L = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> M = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> N = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> O = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> P = EntityDataManager.createKey(EntityNPCMaid.class, DataSerializers.BYTE);

    public EntityNPCMaid(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(A, (byte) 0);
        this.dataManager.register(B, (byte) 0);
        this.dataManager.register(C, (byte) 0);
        this.dataManager.register(D, (byte) 0);
        this.dataManager.register(E, (byte) 0);
        this.dataManager.register(F, (byte) 0);
        this.dataManager.register(G, (byte) 0);
        this.dataManager.register(H, (byte) 0);
        this.dataManager.register(I, (byte) 0);
        this.dataManager.register(J, (byte) 0);
        this.dataManager.register(K, (byte) 0);
        this.dataManager.register(L, (byte) 0);
        this.dataManager.register(M, (byte) 0);
        this.dataManager.register(N, (byte) 0);
        this.dataManager.register(O, (byte) 0);
        this.dataManager.register(P, (byte) 0);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setByte("A", getA());
        compound.setByte("B", getB());
        compound.setByte("C", getC());
        compound.setByte("D", getD());
        compound.setByte("E", getE());
        compound.setByte("F", getF());
        compound.setByte("G", getG());
        compound.setByte("H", getH());
        compound.setByte("I", getI());
        compound.setByte("J", getJ());
        compound.setByte("K", getK());
        compound.setByte("L", getL());
        compound.setByte("M", getM());
        compound.setByte("N", getN());
        compound.setByte("O", getO());
        compound.setByte("P", getP());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setA(compound.getByte("A"));
        this.setB(compound.getByte("B"));
        this.setC(compound.getByte("C"));
        this.setD(compound.getByte("D"));
        this.setE(compound.getByte("E"));
        this.setF(compound.getByte("F"));
        this.setG(compound.getByte("G"));
        this.setH(compound.getByte("H"));
        this.setI(compound.getByte("I"));
        this.setJ(compound.getByte("J"));
        this.setK(compound.getByte("K"));
        this.setL(compound.getByte("L"));
        this.setM(compound.getByte("M"));
        this.setN(compound.getByte("N"));
        this.setO(compound.getByte("O"));
        this.setP(compound.getByte("P"));
    }


    public byte getA() {
        return this.dataManager.get(A);
    }

    public void setA(byte in) {
        this.dataManager.set(A, in);
    }

    public byte getB() {
        return this.dataManager.get(B);
    }

    public void setB(byte in) {
        this.dataManager.set(B, in);
    }

    public byte getC() {
        return this.dataManager.get(C);
    }

    public void setC(byte in) {
        this.dataManager.set(C, in);
    }

    public byte getD() {
        return this.dataManager.get(D);
    }

    public void setD(byte in) {
        this.dataManager.set(D, in);
    }

    public byte getE() {
        return this.dataManager.get(E);
    }

    public void setE(byte in) {
        this.dataManager.set(E, in);
    }

    public byte getF() {
        return this.dataManager.get(F);
    }

    public void setF(byte in) {
        this.dataManager.set(F, in);
    }

    public byte getG() {
        return this.dataManager.get(G);
    }

    public void setG(byte in) {
        this.dataManager.set(G, in);
    }

    public byte getH() {
        return this.dataManager.get(H);
    }

    public void setH(byte in) {
        this.dataManager.set(H, in);
    }

    public byte getI() {
        return this.dataManager.get(I);
    }

    public void setI(byte in) {
        this.dataManager.set(I, in);
    }

    public byte getJ() {
        return this.dataManager.get(J);
    }

    public void setJ(byte in) {
        this.dataManager.set(J, in);
    }

    public byte getK() {
        return this.dataManager.get(K);
    }

    public void setK(byte in) {
        this.dataManager.set(K, in);
    }

    public byte getL() {
        return this.dataManager.get(L);
    }

    public void setL(byte in) {
        this.dataManager.set(L, in);
    }

    public byte getM() {
        return this.dataManager.get(M);
    }

    public void setM(byte in) {
        this.dataManager.set(M, in);
    }

    public byte getN() {
        return this.dataManager.get(N);
    }

    public void setN(byte in) {
        this.dataManager.set(N, in);
    }

    public byte getO() {
        return this.dataManager.get(O);
    }

    public void setO(byte in) {
        this.dataManager.set(O, in);
    }

    public byte getP() {
        return this.dataManager.get(P);
    }

    public void setP(byte in) {
        this.dataManager.set(P, in);
    }

    public int getModelIndex() {
        return getA() + (getB() << 1) + (getC() << 2) + (getD() << 3) + (getE() << 4) + (getF() << 5) +
                (getG() << 6) + (getH() << 7) + (getI() << 8) + (getJ() << 9) + (getK() << 10) +
                (getL() << 11) + (getM() << 12) + (getN() << 13) + (getO() << 14) + (getP() << 15);
    }
}
