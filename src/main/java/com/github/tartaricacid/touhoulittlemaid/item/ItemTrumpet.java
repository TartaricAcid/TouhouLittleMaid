package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemTrumpet extends Item {
    private static final int MIN_USE_DURATION = 20;

    public ItemTrumpet() {
        super((new Properties()).tab(MAIN_TAB).stacksTo(1));
    }

    @Override
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity && timeLeft >= MIN_USE_DURATION) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (worldIn instanceof ServerWorld) {
                ((ServerWorld) worldIn).getEntities()
                        .filter(Entity::isAlive)
                        .filter(e -> e instanceof EntityMaid)
                        .filter(e -> ((EntityMaid) e).isOwnedBy(player))
                        .forEach(e -> teleportToOwner((EntityMaid) e, player));
                MaidWorldData data = MaidWorldData.get(worldIn);
                if (data != null) {
                    List<MaidInfo> infos = data.getPlayerMaidInfos(player);
                    if (infos != null && !infos.isEmpty()) {
                        player.sendSystemMessage(Component.translatable("message.touhou_little_maid.trumpet.unloaded_maid", infos.size()).withStyle(ChatFormatting.DARK_RED));
                    }
                }
            }
            player.getCooldowns().addCooldown(this, 200);
        }
    }

    private void teleportToOwner(EntityMaid maid, PlayerEntity player) {
        maid.setHomeModeEnable(false);
        maid.teleportTo(player.getX() + random.nextInt(3) - 1, player.getY(), player.getZ() + random.nextInt(3) - 1);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 100;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        return ActionResult.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.trumpet.desc.usage").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.trumpet.desc.note").withStyle(TextFormatting.DARK_RED));
    }
}
