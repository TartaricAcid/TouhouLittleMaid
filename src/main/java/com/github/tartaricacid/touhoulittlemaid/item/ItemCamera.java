package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.MaidRayTraceHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemCamera extends Item {
    private static final String MAID_INFO = "MaidInfo";

    public ItemCamera() {
        super((new Properties()).tab(MAIN_TAB).stacksTo(1).durability(50));
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (handIn == Hand.MAIN_HAND) {
            int searchDistance = 8;
            ItemStack camera = playerIn.getItemInHand(handIn);
            Optional<EntityMaid> result = MaidRayTraceHelper.rayTraceMaid(playerIn, searchDistance);
            if (result.isPresent()) {
                EntityMaid maid = result.get();
                if (!worldIn.isClientSide && maid.isAlive() && maid.isOwnedBy(playerIn)) {
                    spawnMaidPhoto(worldIn, maid, playerIn);
                    maid.remove();
                    playerIn.getCooldowns().addCooldown(this, 20);
                    camera.hurtAndBreak(1, playerIn, (e) -> e.broadcastBreakEvent(Hand.MAIN_HAND));
                }
                maid.spawnExplosionParticle();
                playerIn.playSound(InitSounds.CAMERA_USE.get(), 1.0f, 1.0f);
                return ActionResult.sidedSuccess(camera, worldIn.isClientSide);
            }
        }
        return super.use(worldIn, playerIn, handIn);
    }

    private void spawnMaidPhoto(World worldIn, EntityMaid maid, PlayerEntity playerIn) {
        ItemStack photo = InitItems.PHOTO.get().getDefaultInstance();
        CompoundNBT photoTag = new CompoundNBT();
        CompoundNBT maidTag = new CompoundNBT();
        maid.addAdditionalSaveData(maidTag);
        maidTag.putString("id", Objects.requireNonNull(InitEntities.MAID.get().getRegistryName()).toString());
        photoTag.put(MAID_INFO, maidTag);
        photo.setTag(photoTag);
        if (maid.hasCustomName()) {
            photo.setHoverName(maid.getCustomName());
        }
        InventoryHelper.dropItemStack(worldIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), photo);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        // 返回 true，阻止打开女仆 GUI
        if (stack.getItem() == this && target.isAlive() && target instanceof EntityMaid && ((EntityMaid) target).isOwnedBy(playerIn)) {
            this.use(playerIn.level, playerIn, hand);
            return ActionResultType.SUCCESS;
        }
        return super.interactLivingEntity(stack, playerIn, target, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.camera.desc").withStyle(TextFormatting.DARK_GREEN));
    }
}
