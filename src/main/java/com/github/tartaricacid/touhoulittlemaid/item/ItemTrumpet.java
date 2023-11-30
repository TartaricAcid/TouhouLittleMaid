package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTrumpet extends Item {
    private static final int MIN_USE_DURATION = 20;

    public ItemTrumpet() {
        super((new Properties()).stacksTo(1));
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player && timeLeft >= MIN_USE_DURATION) {
            Player player = (Player) entityLiving;
            if (worldIn instanceof ServerLevel) {
                ((ServerLevel) worldIn).getEntities(EntityMaid.TYPE, Entity::isAlive).stream()
                        .filter(maid -> maid.isOwnedBy(player))
                        .forEach(maid -> teleportToOwner(maid, player));
            }
            player.getCooldowns().addCooldown(this, 200);
        }
    }

    private void teleportToOwner(EntityMaid maid, Player player) {
        maid.setHomeModeEnable(false);
        maid.teleportTo(player.getX() + player.getRandom().nextInt(3) - 1, player.getY(), player.getZ() + player.getRandom().nextInt(3) - 1);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 100;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.trumpet.desc.usage").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.trumpet.desc.note").withStyle(ChatFormatting.DARK_RED));
    }
}
