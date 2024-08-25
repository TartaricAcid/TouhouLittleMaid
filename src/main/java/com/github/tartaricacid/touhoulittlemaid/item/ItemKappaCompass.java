package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.KAPPA_COMPASS_ACTIVITY_POS;
import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.KAPPA_COMPASS_DIMENSION;

public class ItemKappaCompass extends Item {
    public ItemKappaCompass() {
        super((new Item.Properties()).stacksTo(1));
    }

    public static void addPoint(Activity activity, BlockPos pos, ItemStack compass) {
        Map<String, BlockPos> activityPos = Objects.requireNonNullElse(compass.get(KAPPA_COMPASS_ACTIVITY_POS), new HashMap<>());
        activityPos.put(activity.getName(), pos);
        compass.set(KAPPA_COMPASS_ACTIVITY_POS, activityPos);
    }

    public static void addDimension(ResourceLocation dimension, ItemStack compass) {
        compass.set(KAPPA_COMPASS_DIMENSION, dimension.toString());
    }

    @Nullable
    public static BlockPos getPoint(Activity activity, ItemStack compass) {
        Map<String, BlockPos> activityPos = compass.get(KAPPA_COMPASS_ACTIVITY_POS);
        if (activityPos != null) {
            String name = activity.getName();
            if (activityPos.containsKey(name)) {
                return activityPos.get(name);
            }
            name = Activity.IDLE.getName();
            if (activityPos.containsKey(name)) {
                return activityPos.get(name);
            }
            name = Activity.WORK.getName();
            if (activityPos.containsKey(name)) {
                return activityPos.get(name);
            }
        }
        return null;
    }

    @Nullable
    public static ResourceLocation getDimension(ItemStack compass) {
        String dim = compass.get(KAPPA_COMPASS_DIMENSION);
        if (dim != null) {
            return ResourceLocation.parse(dim);
        }
        return null;
    }

    public static int getRecordCount(ItemStack compass) {
        Map<String, BlockPos> activityPos = compass.get(KAPPA_COMPASS_ACTIVITY_POS);
        if (activityPos != null) {
            return activityPos.size();
        }
        return 0;
    }

    public static boolean hasKappaCompassData(ItemStack compass) {
        return compass.has(KAPPA_COMPASS_ACTIVITY_POS) && compass.has(KAPPA_COMPASS_DIMENSION);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack compass, Player player, LivingEntity livingEntity, InteractionHand hand) {
        if (livingEntity instanceof EntityMaid maid && !maid.level.isClientSide) {
            if (player.isDiscrete()) {
                maid.getSchedulePos().clear(maid);
                player.sendSystemMessage(Component.translatable("message.touhou_little_maid.kappa_compass.maid_clear"));
                player.level.playSound(null, player.blockPosition(), InitSounds.COMPASS_POINT.get(), SoundSource.PLAYERS, 0.8f, 1.5f);
                return InteractionResult.SUCCESS;
            }
            ResourceLocation dimension = getDimension(compass);
            if (compass.has(KAPPA_COMPASS_ACTIVITY_POS) || dimension != null) {
                if (!maid.level.dimension().location().equals(dimension)) {
                    player.sendSystemMessage(Component.translatable("message.touhou_little_maid.kappa_compass.maid_dimension_check"));
                    return InteractionResult.CONSUME;
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
                player.sendSystemMessage(Component.translatable("message.touhou_little_maid.kappa_compass.maid_write"));
                player.level.playSound(null, player.blockPosition(), InitSounds.COMPASS_POINT.get(), SoundSource.PLAYERS, 0.8f, 1.5f);
                return InteractionResult.SUCCESS;
            }
            player.sendSystemMessage(Component.translatable("message.touhou_little_maid.kappa_compass.no_data"));
            return InteractionResult.CONSUME;
        }
        return super.interactLivingEntity(compass, player, livingEntity, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        ItemStack compass = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();
        if (player == null) {
            return super.useOn(context);
        }
        if (player.isDiscrete()) {
            compass.remove(KAPPA_COMPASS_ACTIVITY_POS);
            compass.remove(KAPPA_COMPASS_DIMENSION);
            sendMessage(player, Component.translatable("message.touhou_little_maid.kappa_compass.clear"));
        } else {
            int recordCount = getRecordCount(compass);
            if (recordCount >= 3) {
                sendMessage(player, Component.translatable("message.touhou_little_maid.kappa_compass.full"));
            } else if (recordCount == 2) {
                BlockPos idlePos = getPoint(Activity.IDLE, compass);
                if (idlePos != null && idlePos.distSqr(clickedPos) > 64 * 64) {
                    sendMessage(player, Component.translatable("message.touhou_little_maid.kappa_compass.far_away"));
                    return super.useOn(context);
                }
                addPoint(Activity.REST, clickedPos, compass);
                sendMessage(player, Component.translatable("message.touhou_little_maid.kappa_compass.sleep", clickedPos.getX(), clickedPos.getY(), clickedPos.getZ()));
            } else if (recordCount == 1) {
                BlockPos workPos = getPoint(Activity.WORK, compass);
                if (workPos != null && workPos.distSqr(clickedPos) > 64 * 64) {
                    sendMessage(player, Component.translatable("message.touhou_little_maid.kappa_compass.far_away"));
                    return super.useOn(context);
                }
                addPoint(Activity.IDLE, clickedPos, compass);
                sendMessage(player, Component.translatable("message.touhou_little_maid.kappa_compass.idle", clickedPos.getX(), clickedPos.getY(), clickedPos.getZ()));
            } else {
                addPoint(Activity.WORK, clickedPos, compass);
                sendMessage(player, Component.translatable("message.touhou_little_maid.kappa_compass.work", clickedPos.getX(), clickedPos.getY(), clickedPos.getZ()));
            }
            addDimension(player.level.dimension().location(), compass);
        }

        player.level.playSound(null, player.blockPosition(), InitSounds.COMPASS_POINT.get(), SoundSource.PLAYERS, 0.8f, 1.5f);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext worldIn, List<Component> components, TooltipFlag flagIn) {
        if (hasKappaCompassData(stack)) {
            ResourceLocation dimension = getDimension(stack);
            BlockPos workPos = getPoint(Activity.WORK, stack);
            BlockPos idlePos = getPoint(Activity.IDLE, stack);
            BlockPos sleepPos = getPoint(Activity.REST, stack);
            if (dimension != null) {
                components.add(Component.translatable("tooltips.touhou_little_maid.fox_scroll.dimension", dimension.toString()).withStyle(ChatFormatting.GOLD));
            }
            if (workPos != null) {
                components.add(Component.translatable("message.touhou_little_maid.kappa_compass.work", workPos.getX(), workPos.getY(), workPos.getZ()).withStyle(ChatFormatting.RED));
            }
            if (idlePos != null) {
                components.add(Component.translatable("message.touhou_little_maid.kappa_compass.idle", idlePos.getX(), idlePos.getY(), idlePos.getZ()).withStyle(ChatFormatting.GREEN));
            }
            if (sleepPos != null) {
                components.add(Component.translatable("message.touhou_little_maid.kappa_compass.sleep", sleepPos.getX(), sleepPos.getY(), sleepPos.getZ()).withStyle(ChatFormatting.BLUE));
            }
            components.add(Component.empty());
        }
        components.add(Component.translatable("message.touhou_little_maid.kappa_compass.usage.set_pos"));
        components.add(Component.translatable("message.touhou_little_maid.kappa_compass.usage.clear_pos"));
        components.add(Component.translatable("message.touhou_little_maid.kappa_compass.usage.write_pos_to_maid"));
        components.add(Component.translatable("message.touhou_little_maid.kappa_compass.usage.clear_maid_pos"));
    }

    private void sendMessage(Player player, Component component) {
        if (!player.level.isClientSide) {
            player.sendSystemMessage(component);
        }
    }
}