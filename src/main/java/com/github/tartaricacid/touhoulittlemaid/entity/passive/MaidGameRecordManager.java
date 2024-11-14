package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import static com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid.GAME_SKILL;
import static com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid.GAME_STATUE;

public class MaidGameRecordManager {
    private static final String GAME_SKILL_TAG = "MaidGameSkillData";
    private static final String GOMOKU = "Gomoku";
    private static final byte NONE = 0, WIN = 1, LOSE = 2;

    private final EntityMaid maid;

    public MaidGameRecordManager(EntityMaid maid) {
        this.maid = maid;
    }

    void defineSynchedData() {
        maid.getEntityData().define(GAME_SKILL, new CompoundTag());
        maid.getEntityData().define(GAME_STATUE, (byte) 0);
    }

    void addAdditionalSaveData(CompoundTag compound) {
        compound.put(GAME_SKILL_TAG, getGameSkill());
    }

    void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains(GAME_SKILL_TAG, Tag.TAG_COMPOUND)) {
            setGameSkill(compound.getCompound(GAME_SKILL_TAG));
        }
    }

    void tick() {
        if (!(this.maid.getVehicle() instanceof EntitySit) && getGameStatue() != NONE) {
            resetStatue();
        }
    }

    private CompoundTag getGameSkill() {
        return maid.getEntityData().get(GAME_SKILL);
    }

    private void setGameSkill(CompoundTag gameSkill) {
        maid.getEntityData().set(GAME_SKILL, gameSkill, true);
    }

    private byte getGameStatue() {
        return maid.getEntityData().get(GAME_STATUE);
    }

    private void setGameStatue(byte gameStatue) {
        maid.getEntityData().set(GAME_STATUE, gameStatue);
    }

    public int getGomokuWinCount() {
        CompoundTag gameSkill = this.getGameSkill();
        if (gameSkill.contains(GOMOKU, Tag.TAG_INT)) {
            return gameSkill.getInt(GOMOKU);
        }
        return 0;
    }

    public void increaseGomokuWinCount() {
        CompoundTag gameSkill = this.getGameSkill();
        if (gameSkill.contains(GOMOKU, Tag.TAG_INT)) {
            gameSkill.putInt(GOMOKU, gameSkill.getInt(GOMOKU) + 1);
        } else {
            gameSkill.putInt(GOMOKU, 1);
        }
        this.setGameSkill(gameSkill);
    }

    public boolean isWin() {
        return this.getGameStatue() == WIN;
    }

    public boolean isLost() {
        return this.getGameStatue() == LOSE;
    }

    public void markStatue(boolean isWin) {
        this.setGameStatue(isWin ? WIN : LOSE);
        if (isWin) {
            maid.playSound(InitSounds.GAME_WIN.get(), 1, 1);
        } else {
            maid.playSound(InitSounds.GAME_LOST.get(), 1, 1);
        }
    }

    public void resetStatue() {
        this.setGameStatue(NONE);
    }
}
