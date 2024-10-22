package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.client.gui.item.ServantBellSetScreen;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidInfo;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.*;

public class ItemServantBell extends Item {
    private static final int MIN_USE_DURATION = 20;

    public ItemServantBell() {
        super((new Properties().stacksTo(1)));
    }

    public static void recordMaidInfo(ItemStack stack, UUID uuid, String tip) {
        if (stack.is(InitItems.SERVANT_BELL.get())) {
            stack.set(SAKUYA_BELL_UUID_TAG, uuid);
            stack.set(SAKUYA_BELL_TIP_TAG, tip);
        }
    }

    @Nullable
    public static ItemFoxScroll.TrackInfo getMaidShow(ItemStack stack) {
        return stack.get(SAKUYA_BELL_SHOW_TAG);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand usedHand) {
        if (usedHand == InteractionHand.MAIN_HAND && target instanceof EntityMaid maid && maid.isOwnedBy(player)) {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                openServantBellSetScreen(maid);
            }
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
            playerIn.sendSystemMessage(Component.translatable("message.touhou_little_maid.servant_bell.data_is_empty"));
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
            player.sendSystemMessage(Component.translatable("message.touhou_little_maid.servant_bell.data_is_empty"));
            return;
        }
        if (worldIn instanceof ServerLevel serverLevel) {
            List<? extends EntityMaid> maids = serverLevel.getEntities(EntityMaid.TYPE, maid -> checkMaidUuid(player, maid, searchUuid));
            if (maids.isEmpty()) {
                showMaidInfo(worldIn, player, stack, searchUuid);
            } else {
                stack.remove(SAKUYA_BELL_SHOW_TAG);
                teleportMaid(player, maids);
            }
        }
        worldIn.playSound(null, player.blockPosition(), SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0F, 1.0F);
        if (player instanceof ServerPlayer serverPlayer) {
            InitTrigger.MAID_EVENT.get().trigger(serverPlayer, TriggerType.USE_SERVANT_BELL);
        }
        player.getCooldowns().addCooldown(this, 20);
    }

    @Nullable
    private UUID getMaidUuid(ItemStack stack) {
        return stack.get(SAKUYA_BELL_UUID_TAG);
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
            player.sendSystemMessage(Component.translatable("message.touhou_little_maid.servant_bell.no_result"));
            return;
        }
        List<MaidInfo> infos = data.getPlayerMaidInfos(player);
        if (infos == null || infos.isEmpty()) {
            player.sendSystemMessage(Component.translatable("message.touhou_little_maid.servant_bell.no_result"));
            return;
        }
        infos.stream().filter(info -> info.getEntityId().equals(searchUuid)).findFirst().ifPresentOrElse(info -> {
            String dimension = info.getDimension();
            String playerDimension = player.level.dimension().location().toString();
            stack.set(SAKUYA_BELL_SHOW_TAG, new ItemFoxScroll.TrackInfo(dimension, info.getChunkPos()));
            if (dimension.equals(playerDimension)) {
                player.sendSystemMessage(Component.translatable("message.touhou_little_maid.servant_bell.show_pos"));
            } else {
                player.sendSystemMessage(Component.translatable("message.touhou_little_maid.servant_bell.not_same_dimension", dimension));
            }
        }, () -> player.sendSystemMessage(Component.translatable("message.touhou_little_maid.servant_bell.no_result")));
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
    public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
        return 100;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BLOCK;
    }

    @Override
    public Component getName(ItemStack stack) {
        String tip = stack.get(SAKUYA_BELL_TIP_TAG);
        if (tip != null) {
            return Component.literal(tip).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.UNDERLINE);
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        UUID uuid = getMaidUuid(stack);
        if (uuid != null) {
            tooltip.add(Component.translatable("tooltips.touhou_little_maid.servant_bell.uuid", uuid.toString()).withStyle(ChatFormatting.GRAY));
            tooltip.add(CommonComponents.space());
        }
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.servant_bell.desc.1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.servant_bell.desc.2").withStyle(ChatFormatting.GRAY));
    }
}
