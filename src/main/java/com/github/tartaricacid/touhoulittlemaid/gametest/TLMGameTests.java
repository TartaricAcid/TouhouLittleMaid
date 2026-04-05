package com.github.tartaricacid.touhoulittlemaid.gametest;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.List;


@GameTestHolder(TouhouLittleMaid.MOD_ID)
@PrefixGameTestTemplate(value = false)
public class TLMGameTests {
    @GameTest(template = "game_test")
    public static void tlmMaidSpawn(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        ItemStack smartSlab = InitItems.SMART_SLAB_INIT.get().getDefaultInstance();
        player.setItemInHand(InteractionHand.MAIN_HAND, smartSlab);

        // 在中心位置放置女仆
        BlockPos groundPos = new BlockPos(7, 1, 7);
        useItemOn(helper, player, smartSlab, groundPos, Direction.UP);

        // 女仆生成在 groundPos 上方，延迟 2 tick 后验证
        helper.runAfterDelay(2, () -> {
            List<EntityMaid> entities = helper.getEntities(InitEntities.MAID.get(), groundPos.above(), 1);
            if (!entities.isEmpty()) {
                EntityMaid maid = entities.get(0);
                maid.getSchedulePos().setHomeModeEnable(maid, maid.blockPosition());
                maid.setHomeModeEnable(true);
            }
            helper.succeed();
        });
    }

    private static void useItemOn(GameTestHelper helper, Player player, ItemStack stack, BlockPos relativePos, Direction face) {
        BlockPos absolutePos = helper.absolutePos(relativePos);
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(absolutePos), face, absolutePos, false);
        UseOnContext context = new UseOnContext(helper.getLevel(), player, InteractionHand.MAIN_HAND, stack, hit);
        stack.useOn(context);
    }
}
