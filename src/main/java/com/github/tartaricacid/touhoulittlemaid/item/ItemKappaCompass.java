package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class ItemKappaCompass extends Item {
    public ItemKappaCompass() {
        super((new Item.Properties()).stacksTo(1).tab(MaidGroup.MAIN_TAB));
    }

    public static void addPoint(Activity activity, BlockPos pos, ItemStack compass) {
        CompoundNBT tag = compass.getOrCreateTagElement("KappaCompassData");
        tag.put(activity.getName(), NBTUtil.writeBlockPos(pos));
    }

    public static void addDimension(ResourceLocation dimension, ItemStack compass) {
        CompoundNBT tag = compass.getOrCreateTagElement("KappaCompassData");
        tag.putString("Dimension", dimension.toString());
    }

    @Nullable
    public static BlockPos getPoint(Activity activity, ItemStack compass) {
        CompoundNBT tag = compass.getTagElement("KappaCompassData");
        if (tag != null) {
            String name = activity.getName();
            if (tag.contains(name, Constants.NBT.TAG_COMPOUND)) {
                return NBTUtil.readBlockPos(tag.getCompound(name));
            }
            name = Activity.IDLE.getName();
            if (tag.contains(name, Constants.NBT.TAG_COMPOUND)) {
                return NBTUtil.readBlockPos(tag.getCompound(name));
            }
            name = Activity.WORK.getName();
            if (tag.contains(name, Constants.NBT.TAG_COMPOUND)) {
                return NBTUtil.readBlockPos(tag.getCompound(name));
            }
        }
        return null;
    }

    @Nullable
    public static ResourceLocation getDimension(ItemStack compass) {
        CompoundNBT tag = compass.getTagElement("KappaCompassData");
        if (tag != null) {
            return new ResourceLocation(tag.getString("Dimension"));
        }
        return null;
    }

    public static int getRecordCount(ItemStack compass) {
        CompoundNBT tag = compass.getTagElement("KappaCompassData");
        int count = 0;
        if (tag != null) {
            if (tag.contains(Activity.WORK.getName(), Constants.NBT.TAG_COMPOUND)) {
                count++;
            }
            if (tag.contains(Activity.IDLE.getName(), Constants.NBT.TAG_COMPOUND)) {
                count++;
            }
            if (tag.contains(Activity.REST.getName(), Constants.NBT.TAG_COMPOUND)) {
                count++;
            }
        }
        return count;
    }

    public static boolean hasKappaCompassData(ItemStack compass) {
        return compass.getTagElement("KappaCompassData") != null;
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack compass, PlayerEntity player, LivingEntity livingEntity, Hand hand) {
        if (livingEntity instanceof EntityMaid && !livingEntity.level.isClientSide) {
            EntityMaid maid = (EntityMaid) livingEntity;
            if (player.isDiscrete()) {
                maid.getSchedulePos().clear(maid);
                player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.maid_clear"), Util.NIL_UUID);
                player.level.playSound(null, player.blockPosition(), InitSounds.COMPASS_POINT.get(), SoundCategory.PLAYERS, 0.8f, 1.5f);
                return ActionResultType.SUCCESS;
            }
            CompoundNBT tag = compass.getTagElement("KappaCompassData");
            ResourceLocation dimension = getDimension(compass);
            if (tag != null || dimension != null) {
                if (!maid.level.dimension().location().equals(dimension)) {
                    player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.maid_dimension_check"), Util.NIL_UUID);
                    return ActionResultType.CONSUME;
                }
                maid.getSchedulePos().setDimension(dimension);
                BlockPos point = getPoint(Activity.WORK, compass);
                if (point != null) {
                    maid.getSchedulePos().setWorkPos(point);
                }
                point = getPoint(Activity.IDLE, compass);
                if (point != null) {
                    maid.getSchedulePos().setIdlePos(point);
                }
                point = getPoint(Activity.REST, compass);
                if (point != null) {
                    maid.getSchedulePos().setSleepPos(point);
                }
                maid.getSchedulePos().setConfigured(true);
                maid.getSchedulePos().restrictTo(maid);
                player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.maid_write"), Util.NIL_UUID);
                player.level.playSound(null, player.blockPosition(), InitSounds.COMPASS_POINT.get(), SoundCategory.PLAYERS, 0.8f, 1.5f);
                return ActionResultType.SUCCESS;
            }
            player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.no_data"), Util.NIL_UUID);
            return ActionResultType.CONSUME;
        }
        return super.interactLivingEntity(compass, player, livingEntity, hand);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        ItemStack compass = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();
        if (player == null || context.getLevel().isClientSide) {
            return super.useOn(context);
        }
        if (player.isDiscrete()) {
            compass.removeTagKey("KappaCompassData");
            player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.clear"), Util.NIL_UUID);
        } else {
            int recordCount = getRecordCount(compass);
            if (recordCount >= 3) {
                player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.full"), Util.NIL_UUID);
            } else if (recordCount == 2) {
                BlockPos idlePos = getPoint(Activity.IDLE, compass);
                if (idlePos != null && idlePos.distSqr(clickedPos) > 64 * 64) {
                    player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.far_away"), Util.NIL_UUID);
                    return super.useOn(context);
                }
                addPoint(Activity.REST, clickedPos, compass);
                player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.sleep", clickedPos.getX(), clickedPos.getY(), clickedPos.getZ()), Util.NIL_UUID);
            } else if (recordCount == 1) {
                BlockPos workPos = getPoint(Activity.WORK, compass);
                if (workPos != null && workPos.distSqr(clickedPos) > 64 * 64) {
                    player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.far_away"), Util.NIL_UUID);
                    return super.useOn(context);
                }
                addPoint(Activity.IDLE, clickedPos, compass);
                player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.idle", clickedPos.getX(), clickedPos.getY(), clickedPos.getZ()), Util.NIL_UUID);
            } else {
                addPoint(Activity.WORK, clickedPos, compass);
                player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.work", clickedPos.getX(), clickedPos.getY(), clickedPos.getZ()), Util.NIL_UUID);
            }
            addDimension(player.level.dimension().location(), compass);
        }
        player.level.playSound(null, player.blockPosition(), InitSounds.COMPASS_POINT.get(), SoundCategory.PLAYERS, 0.8f, 1.5f);
        return ActionResultType.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World pLevel, List<ITextComponent> components, ITooltipFlag pIsAdvanced) {
        if (hasKappaCompassData(stack)) {
            ResourceLocation dimension = getDimension(stack);
            BlockPos workPos = getPoint(Activity.WORK, stack);
            BlockPos idlePos = getPoint(Activity.IDLE, stack);
            BlockPos sleepPos = getPoint(Activity.REST, stack);
            if (dimension != null) {
                components.add(new TranslationTextComponent("tooltips.touhou_little_maid.fox_scroll.dimension", dimension.toString()).withStyle(TextFormatting.GOLD));
            }
            if (workPos != null) {
                components.add(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.work", workPos.getX(), workPos.getY(), workPos.getZ()).withStyle(TextFormatting.RED));
            }
            if (idlePos != null) {
                components.add(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.idle", idlePos.getX(), idlePos.getY(), idlePos.getZ()).withStyle(TextFormatting.GREEN));
            }
            if (sleepPos != null) {
                components.add(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.sleep", sleepPos.getX(), sleepPos.getY(), sleepPos.getZ()).withStyle(TextFormatting.BLUE));
            }
            components.add(StringTextComponent.EMPTY);
        }
        components.add(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.usage.set_pos"));
        components.add(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.usage.clear_pos"));
        components.add(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.usage.write_pos_to_maid"));
        components.add(new TranslationTextComponent("message.touhou_little_maid.kappa_compass.usage.clear_maid_pos"));
    }
}