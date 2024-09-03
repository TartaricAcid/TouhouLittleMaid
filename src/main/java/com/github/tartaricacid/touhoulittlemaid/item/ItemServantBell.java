package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.gui.item.ServantBellSetScreen;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidInfo;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemServantBell extends Item {
    private static final String UUID_TAG = "ServantBellUuid";
    private static final String TIP_TAG = "ServantBellTip";
    private static final String SHOW_TAG = "ServantBellShow";
    private static final String SHOW_DIMENSION_TAG = "Dimension";
    private static final String SHOW_POS_TAG = "Pos";
    private static final int MIN_USE_DURATION = 20;

    public ItemServantBell() {
        super((new Properties().tab(MAIN_TAB).stacksTo(1)));
    }

    public static void recordMaidInfo(ItemStack stack, UUID uuid, String tip) {
        if (stack.is(InitItems.SERVANT_BELL.get())) {
            CompoundTag tag = stack.getOrCreateTag();
            tag.putUUID(UUID_TAG, uuid);
            tag.putString(TIP_TAG, tip);
        }
    }

    @Nullable
    public static Pair<String, BlockPos> getMaidShow(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(SHOW_TAG, Tag.TAG_COMPOUND)) {
            CompoundTag showTag = tag.getCompound(SHOW_TAG);
            if (showTag.contains(SHOW_DIMENSION_TAG, Tag.TAG_STRING) && showTag.contains(SHOW_POS_TAG, Tag.TAG_COMPOUND)) {
                String dimension = showTag.getString(SHOW_DIMENSION_TAG);
                BlockPos blockPos = NbtUtils.readBlockPos(showTag.getCompound(SHOW_POS_TAG));
                return Pair.of(dimension, blockPos);
            }
        }
        return null;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand usedHand) {
        if (usedHand == InteractionHand.MAIN_HAND && target instanceof EntityMaid maid && maid.isOwnedBy(player)) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> openServantBellSetScreen(maid));
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, target, usedHand);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        UUID searchUuid = getMaidUuid(stack);
        if (searchUuid != null) {
            playerIn.startUsingItem(handIn);
            return InteractionResultHolder.consume(stack);
        }
        if (!worldIn.isClientSide) {
            playerIn.sendMessage(new TranslatableComponent("message.touhou_little_maid.servant_bell.data_is_empty"), Util.NIL_UUID);
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof Player player) || timeLeft < MIN_USE_DURATION) {
            return;
        }
        UUID searchUuid = getMaidUuid(stack);
        if (searchUuid == null) {
            player.sendMessage(new TranslatableComponent("message.touhou_little_maid.servant_bell.data_is_empty"), Util.NIL_UUID);
            return;
        }
        if (worldIn instanceof ServerLevel serverLevel) {
            List<? extends EntityMaid> maids = serverLevel.getEntities(EntityMaid.TYPE, maid -> checkMaidUuid(player, maid, searchUuid));
            if (maids.isEmpty()) {
                showMaidInfo(worldIn, player, stack, searchUuid);
            } else {
                stack.getOrCreateTag().remove(SHOW_TAG);
                teleportMaid(player, maids);
            }
        }
        player.getCooldowns().addCooldown(this, 20);
    }

    @Nullable
    private UUID getMaidUuid(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.hasUUID(UUID_TAG)) {
            return tag.getUUID(UUID_TAG);
        }
        return null;
    }

    private void teleportMaid(Player player, List<? extends EntityMaid> maids) {
        maids.forEach(maid -> {
            maid.setHomeModeEnable(false);
            maid.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 1, true, false));
            maid.teleportTo(player.getX() + player.getRandom().nextInt(3) - 1, player.getY(), player.getZ() + player.getRandom().nextInt(3) - 1);
        });
    }

    private void showMaidInfo(Level worldIn, Player player, ItemStack stack, UUID searchUuid) {
        MaidWorldData data = MaidWorldData.get(worldIn);
        if (data == null) {
            player.sendMessage(new TranslatableComponent("message.touhou_little_maid.servant_bell.no_result"), Util.NIL_UUID);
            return;
        }
        List<MaidInfo> infos = data.getPlayerMaidInfos(player);
        if (infos == null || infos.isEmpty()) {
            player.sendMessage(new TranslatableComponent("message.touhou_little_maid.servant_bell.no_result"), Util.NIL_UUID);
            return;
        }
        infos.stream().filter(info -> info.getEntityId().equals(searchUuid)).findFirst().ifPresentOrElse(info -> {
            String dimension = info.getDimension();
            String playerDimension = player.level.dimension().location().toString();
            CompoundTag showTag = stack.getOrCreateTagElement(SHOW_TAG);
            showTag.putString(SHOW_DIMENSION_TAG, dimension);
            showTag.put(SHOW_POS_TAG, NbtUtils.writeBlockPos(info.getChunkPos()));
            if (dimension.equals(playerDimension)) {
                player.sendMessage(new TranslatableComponent("message.touhou_little_maid.servant_bell.show_pos"), Util.NIL_UUID);
            } else {
                player.sendMessage(new TranslatableComponent("message.touhou_little_maid.servant_bell.not_same_dimension", dimension), Util.NIL_UUID);
            }
        }, () -> {
            player.sendMessage(new TranslatableComponent("message.touhou_little_maid.servant_bell.no_result"), Util.NIL_UUID);
        });
    }

    private boolean checkMaidUuid(Player player, EntityMaid maid, UUID searchUuid) {
        return maid.isOwnedBy(player) && maid.getUUID().equals(searchUuid);
    }

    @OnlyIn(Dist.CLIENT)
    private void openServantBellSetScreen(EntityMaid maid) {
        if (maid.level.isClientSide) {
            Minecraft.getInstance().setScreen(new ServantBellSetScreen(maid));
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 100;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BLOCK;
    }

    @Override
    public Component getName(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(TIP_TAG, Tag.TAG_STRING)) {
            String tip = tag.getString(TIP_TAG);
            return new TranslatableComponent(tip).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.UNDERLINE);
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        UUID uuid = getMaidUuid(stack);
        if (uuid != null) {
            tooltip.add(new TranslatableComponent("tooltips.touhou_little_maid.servant_bell.uuid", uuid.toString()).withStyle(ChatFormatting.GRAY));
            tooltip.add(CommonComponents.NARRATION_SEPARATOR);
        }
        tooltip.add(new TranslatableComponent("tooltips.touhou_little_maid.servant_bell.desc.1").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("tooltips.touhou_little_maid.servant_bell.desc.2").withStyle(ChatFormatting.GRAY));
    }
}
