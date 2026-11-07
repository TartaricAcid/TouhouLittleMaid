package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabConfig;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabFeeding;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 餐具（勺 / 刀叉 / 筷子）：放置类食物的「间接中转站」。
 * 对着世界里的食物方块右键装一口（这一口的信息存进物品 NBT），再对准女仆右键把这一口喂出去。
 * 餐具不挑食物，三种只在种类与样式上有差别；喂完这一口餐具就空了，但餐具本身 reusable。
 */
public class ItemMaidUtensil extends Item {
    public static final String PORTION_TAG = "WabPortion";

    private final String kind;

    public ItemMaidUtensil(String kind) {
        super(new Properties().stacksTo(1));
        this.kind = kind;
    }

    public String kind() {
        return kind;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return WabFeeding.takeBite(context);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!WabConfig.feedingEnabled()) {
            return InteractionResult.PASS;
        }
        if (target instanceof EntityMaid maid) {
            return WabFeeding.feedMaidFromUtensil(stack, player, maid, hand);
        }
        if (target instanceof Player eater && WabConfig.multiplayerFeeding()) {
            return WabFeeding.feedPlayerFromUtensil(stack, player, eater, hand);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.utensil.usage.take").withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.utensil.usage.feed").withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.utensil.usage.direct").withStyle(ChatFormatting.DARK_GRAY));
        WabFeeding.Portion portion = WabFeeding.read(stack);
        if (portion == null) {
            tooltip.add(Component.translatable("tooltips.touhou_little_maid.utensil.empty").withStyle(ChatFormatting.GRAY));
            return;
        }
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.utensil.portion", portion.foodName())
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.utensil.source", sourceText(portion))
                .withStyle(ChatFormatting.DARK_GRAY));
    }

    /** 「取自」的可读化：手持食物就是它自己，方块给注册名，其余原样。 */
    private static String sourceText(WabFeeding.Portion portion) {
        String source = portion.source();
        if ("item".equals(source)) return portion.foodName();
        if (source.startsWith("block:")) return source.substring("block:".length());
        return source;
    }
}
