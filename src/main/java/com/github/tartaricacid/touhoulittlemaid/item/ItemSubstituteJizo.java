package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import javax.annotation.Nullable;
import java.util.List;

@EventBusSubscriber
public class ItemSubstituteJizo extends Item {
    public ItemSubstituteJizo() {
        super((new Properties()).stacksTo(1));
    }

    @SubscribeEvent
    public static void onEntityInteract(InteractMaidEvent event) {
        EntityMaid maid = event.getMaid();
        ItemStack stack = event.getStack();
        Player player = event.getPlayer();
        if (maid.isOwnedBy(player) && stack.getItem() == InitItems.SUBSTITUTE_JIZO.get() && !maid.getIsInvulnerable()) {
            maid.setEntityInvulnerable(true);
            stack.shrink(1);
            event.setCanceled(true);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.substitute_jizo.desc").withStyle(ChatFormatting.GRAY));
    }
}
