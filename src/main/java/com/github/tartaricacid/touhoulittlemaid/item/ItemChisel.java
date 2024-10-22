package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.block.BlockStatue;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityStatue;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemChisel extends Item {
    public ItemChisel() {
        super((new Properties()).stacksTo(1).durability(64));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (context.getHand() == InteractionHand.MAIN_HAND && player != null) {
            if (worldIn.getBlockState(pos).getBlock() != Blocks.CLAY) {
                if (!worldIn.isClientSide) {
                    player.sendSystemMessage(Component.translatable("message.touhou_little_maid.chisel.hit_block_error"));
                }
                return InteractionResult.PASS;
            }
            if (player.getOffhandItem().getItem() != InitItems.PHOTO.get()) {
                if (!worldIn.isClientSide) {
                    player.sendSystemMessage(Component.translatable("message.touhou_little_maid.chisel.offhand_not_photo"));
                }
                return InteractionResult.PASS;
            }
            genStatueBlocks(player, worldIn, pos, context.getClickedFace());
            if (player instanceof ServerPlayer serverPlayer) {
                InitTrigger.MAID_EVENT.get().trigger(serverPlayer, TriggerType.CHISEL_STATUE);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    private void genStatueBlocks(@Nonnull Player player, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull Direction facing) {
        CustomData compoundData = player.getOffhandItem().get(InitDataComponent.MAID_INFO);
        if (compoundData != null) {
            TileEntityStatue.Size[] sizes = TileEntityStatue.Size.values();
            for (int i = sizes.length - 1; i >= 0; i--) {
                TileEntityStatue.Size size = sizes[i];
                Vec3i dimension = size.getDimension();
                BlockPos[] posList = checkBlocks(worldIn, pos, dimension, facing);
                if (posList != null) {
                    boolean isTiny = posList.length == 1;
                    for (BlockPos posIn : posList) {
                        worldIn.setBlock(posIn, InitBlocks.STATUE.get().defaultBlockState().setValue(BlockStatue.IS_TINY, isTiny), Block.UPDATE_ALL);
                        BlockEntity te = worldIn.getBlockEntity(posIn);
                        if (te instanceof TileEntityStatue statue) {
                            if (posIn.equals(pos)) {
                                statue.setForgeData(size, true, pos, facing,
                                        Lists.newArrayList(posList), compoundData.copyTag());
                            } else {
                                statue.setForgeData(size, false, pos, facing,
                                        Lists.newArrayList(posList), null);
                            }
                        }
                    }

                    player.getMainHandItem().hurtAndBreak(size.ordinal() + 1, player, EquipmentSlot.MAINHAND);
                    player.playSound(SoundEvents.ANVIL_LAND, 0.5f, 1.5f);
                    return;
                }
            }
        }
    }


    @Nullable
    private BlockPos[] checkBlocks(@Nonnull Level worldIn, BlockPos origin, Vec3i dimension, Direction facing) {
        BlockPos[] posList = new BlockPos[dimension.getX() * dimension.getY() * dimension.getZ()];
        int index = 0;
        for (int x = 0; x < dimension.getX(); x++) {
            for (int y = 0; y < dimension.getY(); y++) {
                for (int z = 0; z < dimension.getZ(); z++) {
                    BlockPos pos = switch (facing) {
                        case WEST -> origin.offset(new Vec3i(x, y, z));
                        case SOUTH -> origin.offset(new Vec3i(x, y, -z));
                        default -> origin.offset(new Vec3i(-x, y, z));
                        case EAST -> origin.offset(new Vec3i(-x, y, -z));
                    };
                    posList[index] = pos;
                    index++;
                    if (worldIn.getBlockState(pos).getBlock() != Blocks.CLAY) {
                        return null;
                    }
                }
            }
        }
        return posList;
    }
}
