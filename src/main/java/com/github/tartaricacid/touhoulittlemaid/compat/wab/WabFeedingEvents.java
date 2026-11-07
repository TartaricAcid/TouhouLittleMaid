package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidUtensil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * 餐具喂食的世界交互：
 * 潜行直喂（InteractMaidEvent，只有主人右键才触发）；
 * 左键食物方块盛一口（取消事件挡住破坏，只在 START 那一下处理，按住不连发）；
 * 右键食物方块也盛一口——只把方块自身的 use 置 FALSE（拦下蛋糕「喂玩家自己」），让餐具 useOn 接管，
 * 不取消事件，否则连物品 useOn 和网络包一起吞掉、服务端就收不到了。
 */
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class WabFeedingEvents {
    @SubscribeEvent
    public static void onInteractMaid(InteractMaidEvent event) {
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        ItemStack held = event.getStack();
        if (!WabFeeding.wantsDirectFeed(player, maid, held)) return;
        event.setCanceled(true);
        if (!player.level().isClientSide) {
            WabFeeding.feedFoodDirect(player, maid, held, InteractionHand.MAIN_HAND);
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) return;
        if (!scoopable(event.getItemStack(), event.getLevel(), event.getPos())) return;
        event.setCanceled(true);
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        UseOnContext ctx = new UseOnContext(player, event.getHand(),
                new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false));
        WabFeeding.takeBite(ctx);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!scoopable(event.getItemStack(), event.getLevel(), event.getPos())) return;
        event.setUseBlock(TriState.FALSE);
    }

    /** 拿着空餐具、系统开着、目标方块能盛一口，才接管这次点击；餐具已盛着或不是食物方块就照旧。 */
    private static boolean scoopable(ItemStack held, Level level, BlockPos pos) {
        return WabConfig.feedingEnabled()
                && held.getItem() instanceof ItemMaidUtensil
                && WabFeeding.read(held) == null
                && WabFeeding.canTakeBite(level, pos);
    }
}