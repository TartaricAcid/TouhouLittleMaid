package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.MaidRayTraceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class ItemCamera extends Item {
    public ItemCamera() {
        super((new Properties()).stacksTo(1).durability(50));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (handIn == InteractionHand.MAIN_HAND) {
            int searchDistance = 8;
            ItemStack camera = playerIn.getItemInHand(handIn);
            Optional<EntityMaid> result = MaidRayTraceHelper.rayTraceMaid(playerIn, searchDistance);
            if (result.isPresent()) {
                EntityMaid maid = result.get();
                if (!worldIn.isClientSide && maid.isAlive() && maid.isOwnedBy(playerIn) && !maid.isSleeping()) {
                    spawnMaidPhoto(worldIn, maid, playerIn);
                    maid.discard();
                    playerIn.getCooldowns().addCooldown(this, 20);
                    camera.hurtAndBreak(1, playerIn, EquipmentSlot.MAINHAND);
                }
                maid.spawnExplosionParticle();
                playerIn.playSound(InitSounds.CAMERA_USE.get(), 1.0f, 1.0f);
                return InteractionResultHolder.sidedSuccess(camera, worldIn.isClientSide);
            }
        }
        return super.use(worldIn, playerIn, handIn);
    }

    private void spawnMaidPhoto(Level worldIn, EntityMaid maid, Player playerIn) {
        ItemStack photo = InitItems.PHOTO.get().getDefaultInstance();
        maid.setHomeModeEnable(false);
        AbstractStoreMaidItem.storeMaidData(photo, maid);
        Containers.dropItemStack(worldIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), photo);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        // 返回 true，阻止打开女仆 GUI
        if (stack.getItem() == this && target.isAlive() && target instanceof EntityMaid && ((EntityMaid) target).isOwnedBy(playerIn)) {
            this.use(playerIn.level, playerIn, hand);
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, playerIn, target, hand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.camera.desc").withStyle(ChatFormatting.DARK_GREEN));
    }
}
