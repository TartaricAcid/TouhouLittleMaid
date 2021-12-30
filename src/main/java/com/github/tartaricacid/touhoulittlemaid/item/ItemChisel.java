package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityStatue;
import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;
import static net.minecraftforge.common.util.Constants.BlockFlags.DEFAULT;

public class ItemChisel extends Item {
    public ItemChisel() {
        super((new Properties()).tab(MAIN_TAB).stacksTo(1).durability(64));
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        PlayerEntity player = context.getPlayer();

        if (context.getHand() == Hand.MAIN_HAND && player != null) {
            if (worldIn.getBlockState(pos).getBlock() != Blocks.CLAY) {
                if (!worldIn.isClientSide) {
                    player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.chisel.hit_block_error"), Util.NIL_UUID);
                }
                return ActionResultType.PASS;
            }
            if (player.getOffhandItem().getItem() != InitItems.PHOTO.get()) {
                if (!worldIn.isClientSide) {
                    player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.chisel.offhand_not_photo"), Util.NIL_UUID);
                }
                return ActionResultType.PASS;
            }
            genStatueBlocks(player, worldIn, pos, context.getClickedFace());
            return ActionResultType.SUCCESS;
        }
        return super.useOn(context);
    }

    private void genStatueBlocks(@Nonnull PlayerEntity player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Direction facing) {
        CompoundNBT data = ItemPhoto.getMaidData(player.getOffhandItem());
        TileEntityStatue.Size[] sizes = TileEntityStatue.Size.values();
        for (int i = sizes.length - 1; i >= 0; i--) {
            TileEntityStatue.Size size = sizes[i];
            Vector3i dimension = size.getDimension();
            BlockPos[] posList = checkBlocks(worldIn, pos, dimension, facing);
            if (posList != null) {
                for (BlockPos posIn : posList) {
                    worldIn.setBlock(posIn, InitBlocks.STATUE.get().defaultBlockState(), DEFAULT);
                    TileEntity te = worldIn.getBlockEntity(posIn);
                    if (te instanceof TileEntityStatue) {
                        TileEntityStatue statue = (TileEntityStatue) te;
                        if (posIn.equals(pos)) {
                            statue.setForgeData(size, true, pos, facing,
                                    Lists.newArrayList(posList), data);
                        } else {
                            statue.setForgeData(size, false, pos, facing,
                                    Lists.newArrayList(posList), null);
                        }
                    }
                }

                player.getMainHandItem().hurtAndBreak(size.ordinal() + 1, player, (e) -> e.broadcastBreakEvent(Hand.MAIN_HAND));
                player.playSound(SoundEvents.ANVIL_LAND, 0.5f, 1.5f);
                return;
            }
        }
    }


    @Nullable
    private BlockPos[] checkBlocks(@Nonnull World worldIn, BlockPos origin, Vector3i dimension, Direction facing) {
        BlockPos[] posList = new BlockPos[dimension.getX() * dimension.getY() * dimension.getZ()];
        int index = 0;
        for (int x = 0; x < dimension.getX(); x++) {
            for (int y = 0; y < dimension.getY(); y++) {
                for (int z = 0; z < dimension.getZ(); z++) {
                    BlockPos pos;
                    switch (facing) {
                        case WEST:
                            pos = origin.offset(new Vector3i(x, y, z));
                            break;
                        case SOUTH:
                            pos = origin.offset(new Vector3i(x, y, -z));
                            break;
                        default:
                        case UP:
                        case DOWN:
                        case NORTH:
                            pos = origin.offset(new Vector3i(-x, y, z));
                            break;
                        case EAST:
                            pos = origin.offset(new Vector3i(-x, y, -z));
                            break;
                    }
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
