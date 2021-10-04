package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.api.block.IMultiBlock;
import com.github.tartaricacid.touhoulittlemaid.block.multiblock.MultiBlockManager;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.google.common.base.Predicates;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.function.Predicate;

public class ItemHakureiGohei extends ShootableItem {
    public ItemHakureiGohei() {
        super((new Properties()).tab(InitItems.MAIN_TAB).durability(300).setNoRepair());
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return Predicates.alwaysTrue();
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 500;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        if (context.getHand() == Hand.MAIN_HAND) {
            List<IMultiBlock> multiBlockList = MultiBlockManager.getMultiBlockList();
            BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
            World world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            Direction direction = context.getClickedFace();

            for (IMultiBlock multiBlock : multiBlockList) {
                if (multiBlock.isCoreBlock(blockState) && multiBlock.directionIsSuitable(direction)) {
                    if (world instanceof ServerWorld) {
                        BlockPos posStart = pos.offset(multiBlock.getCenterPos(direction));
                        Template template = multiBlock.getTemplate((ServerWorld) world, direction);
                        if (multiBlock.isMatch(world, posStart, direction, template)) {
                            multiBlock.build(world, posStart, direction, template);
                        }
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return super.useOn(context);
    }
}
