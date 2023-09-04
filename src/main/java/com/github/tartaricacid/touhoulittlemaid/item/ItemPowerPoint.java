package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityThrowPowerPoint;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemPowerPoint extends Item {
    public ItemPowerPoint() {
        super((new Properties()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_BOTTLE_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!world.isClientSide) {
            EntityThrowPowerPoint powerPoint = new EntityThrowPowerPoint(world, player);
            powerPoint.setItem(stack);
            powerPoint.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.7F, 1.0F);
            world.addFreshEntity(powerPoint);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.isCreative()) {
            stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
    }
}
