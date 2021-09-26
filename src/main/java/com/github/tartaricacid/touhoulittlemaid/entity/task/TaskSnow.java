package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import java.util.Collections;
import java.util.List;

public class TaskSnow implements IFarmTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "snow");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.SNOWBALL.getDefaultInstance();
    }

    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_REMOVE_SNOW.get(), 0.2f);
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return cropState.getBlock() instanceof SnowBlock;
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        ItemStack mainHandItem = maid.getMainHandItem();
        if (mainHandItem.getItem() instanceof ShovelItem) {
            if (maid.destroyBlock(cropPos)) {
                mainHandItem.hurtAndBreak(1, maid, (e) -> e.broadcastBreakEvent(Hand.MAIN_HAND));
            }
        } else {
            maid.destroyBlock(cropPos, false);
        }
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        return false;
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        return seed;
    }

    @Override
    public List<ITextComponent> getDescription(EntityMaid maid) {
        return Collections.emptyList();
    }
}
