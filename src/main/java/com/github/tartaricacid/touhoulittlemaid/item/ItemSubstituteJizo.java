package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

@Mod.EventBusSubscriber
public class ItemSubstituteJizo extends Item {
    public ItemSubstituteJizo() {
        super((new Properties()).tab(MAIN_TAB).stacksTo(1));
    }

    @SubscribeEvent
    public static void onEntityInteract(InteractMaidEvent event) {
        EntityMaid maid = event.getMaid();
        ItemStack stack = event.getStack();
        PlayerEntity player = event.getPlayer();
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
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.substitute_jizo.desc").withStyle(TextFormatting.GRAY));
    }
}
