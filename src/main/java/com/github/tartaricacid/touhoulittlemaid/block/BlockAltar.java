package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.inventory.AltarRecipeInventory;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.github.tartaricacid.touhoulittlemaid.util.PosListData;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static net.minecraftforge.common.util.Constants.BlockFlags.DEFAULT;

public class BlockAltar extends Block {
    public BlockAltar() {
        super(AbstractBlock.Properties.of(Material.STONE).strength(2, 2).noOcclusion());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityAltar();
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return this.getAltar(worldIn, pos).filter(altar -> handIn == Hand.MAIN_HAND).map(altar -> {
            if (player.isShiftKeyDown() || player.getMainHandItem().isEmpty()) {
                takeOutItem(worldIn, pos, altar, player);
            } else {
                takeInOrCraft(worldIn, altar, player);
            }
            altar.refresh();
            return ActionResultType.sidedSuccess(worldIn.isClientSide);
        }).orElse(super.use(state, worldIn, pos, player, handIn, hit));
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!worldIn.isClientSide) {
            this.getAltar(worldIn, pos).ifPresent(altar -> {
                ItemStack stack = altar.handler.getStackInSlot(0);
                if (!stack.isEmpty()) {
                    Block.popResource(worldIn, pos.offset(0, 1, 0), stack);
                }
            });
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        if (!world.isClientSide) {
            this.getAltar(world, pos).ifPresent(altar -> this.restoreStorageBlock(world, pos, altar.getBlockPosList()));
        }
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Override
    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isClientSide) {
            this.getAltar(worldIn, pos).ifPresent(altar -> {
                this.restoreStorageBlock(worldIn, pos, altar.getBlockPosList());
                if (!player.isCreative()) {
                    Block block = altar.getStorageState().getBlock();
                    Block.popResource(worldIn, pos, new ItemStack(block));
                }
            });
        }
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return this.getAltar(world, pos)
                .map(altar -> new ItemStack(altar.getStorageState().getBlock()))
                .orElse(super.getPickBlock(state, target, world, pos, player));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) {
        this.getAltar(world, pos).ifPresent(altar -> Minecraft.getInstance().particleEngine.destroy(pos, altar.getStorageState()));
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addHitEffects(BlockState state, World world, RayTraceResult target, ParticleManager manager) {
        if (target instanceof BlockRayTraceResult && world instanceof ClientWorld) {
            BlockRayTraceResult blockTarget = (BlockRayTraceResult) target;
            BlockPos pos = blockTarget.getBlockPos();
            ClientWorld clientWorld = (ClientWorld) world;
            this.getAltar(world, pos).ifPresent(altar -> this.crack(clientWorld, pos, altar.getStorageState(), blockTarget.getDirection()));
        }
        return true;
    }

    @Override
    public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity entity) {
        return this.getAltar(world, pos)
                .map(altar -> altar.getStorageState().getSoundType())
                .orElse(super.getSoundType(state, world, pos, entity));
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    private void restoreStorageBlock(World worldIn, BlockPos currentPos, PosListData posList) {
        for (BlockPos storagePos : posList.getData()) {
            if (storagePos.equals(currentPos)) {
                continue;
            }
            this.getAltar(worldIn, storagePos).ifPresent(altar -> worldIn.setBlock(storagePos, altar.getStorageState(), DEFAULT));
        }
    }

    private void takeOutItem(World world, BlockPos pos, TileEntityAltar altar, PlayerEntity player) {
        if (altar.isCanPlaceItem()) {
            if (!altar.handler.getStackInSlot(0).isEmpty()) {
                Block.popResource(world, pos.offset(0, 1, 0), altar.handler.extractItem(0, 1, false));
                altarCraft(world, altar, player);
            }
        }
    }

    private void takeInOrCraft(World world, TileEntityAltar altar, PlayerEntity playerIn) {
        if (altar.isCanPlaceItem() && altar.handler.getStackInSlot(0).isEmpty()) {
            altar.handler.setStackInSlot(0, ItemHandlerHelper.copyStackWithSize(playerIn.getMainHandItem(), 1));
            if (!playerIn.isCreative()) {
                playerIn.getMainHandItem().shrink(1);
            }
            altarCraft(world, altar, playerIn);
        }
    }

    private void altarCraft(World world, TileEntityAltar altar, PlayerEntity playerIn) {
        final AltarRecipeInventory inv = new AltarRecipeInventory();
        List<BlockPos> posList = altar.getCanPlaceItemPosList().getData();
        for (int i = 0; i < posList.size(); i++) {
            TileEntity te = world.getBlockEntity(posList.get(i));
            if (te instanceof TileEntityAltar) {
                inv.setItem(i, ((TileEntityAltar) te).getStorageItem());
            }
        }
        if (inv.isEmpty()) {
            return;
        }
        playerIn.getCapability(PowerCapabilityProvider.POWER_CAP, null)
                .ifPresent(power -> world.getRecipeManager().getRecipeFor(InitRecipes.ALTAR_CRAFTING, inv, world)
                        .ifPresent(recipe -> spawnResultEntity(world, playerIn, power, recipe, inv, altar)));
    }

    private Optional<TileEntityAltar> getAltar(IBlockReader world, BlockPos pos) {
        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityAltar) {
            return Optional.of((TileEntityAltar) te);
        }
        return Optional.empty();
    }

    private void spawnResultEntity(World world, PlayerEntity playerIn, PowerCapability power,
                                   AltarRecipe altarRecipe, AltarRecipeInventory inventory, TileEntityAltar altar) {
        if (power.get() >= altarRecipe.getPowerCost()) {
            power.min(altarRecipe.getPowerCost());
            BlockPos centrePos = getCentrePos(altar.getBlockPosList(), altar.getBlockPos());
            if (world instanceof ServerWorld) {
                altarRecipe.spawnOutputEntity((ServerWorld) world, centrePos.above(2), inventory);
            }
            removeAllAltarItem(world, altar);
            spawnParticleInCentre(world, centrePos);
            world.playSound(null, centrePos, InitSounds.ALTAR_CRAFT.get(), SoundCategory.VOICE, 1.0f, 1.0f);
        } else {
            if (!world.isClientSide) {
                playerIn.sendMessage(new TranslationTextComponent("message.touhou_little_maid.altar.not_enough_power"), Util.NIL_UUID);
            }
        }
    }


    @OnlyIn(Dist.CLIENT)
    private void crack(ClientWorld world, BlockPos pos, BlockState state, Direction side) {
        if (state.getRenderShape() != BlockRenderType.INVISIBLE) {
            int posX = pos.getX();
            int posY = pos.getY();
            int posZ = pos.getZ();
            AxisAlignedBB aabb = state.getShape(world, pos).bounds();
            double x = posX + this.RANDOM.nextDouble() * (aabb.maxX - aabb.minX - 0.2) + 0.1 + aabb.minX;
            double y = posY + this.RANDOM.nextDouble() * (aabb.maxY - aabb.minY - 0.2) + 0.1 + aabb.minY;
            double z = posZ + this.RANDOM.nextDouble() * (aabb.maxZ - aabb.minZ - 0.2) + 0.1 + aabb.minZ;
            if (side == Direction.DOWN) {
                y = posY + aabb.minY - 0.1;
            }
            if (side == Direction.UP) {
                y = posY + aabb.maxY + 0.1;
            }
            if (side == Direction.NORTH) {
                z = posZ + aabb.minZ - 0.1;
            }
            if (side == Direction.SOUTH) {
                z = posZ + aabb.maxZ + 0.1;
            }
            if (side == Direction.WEST) {
                x = posX + aabb.minX - 0.1;
            }
            if (side == Direction.EAST) {
                x = posX + aabb.maxX + 0.1;
            }
            DiggingParticle diggingParticle = new DiggingParticle(world, x, y, z, 0, 0, 0, state);
            Minecraft.getInstance().particleEngine.add(diggingParticle.init(pos).setPower(0.2f).scale(0.6f));
        }
    }

    private BlockPos getCentrePos(PosListData posList, BlockPos posClick) {
        int x = 0;
        int y = posClick.getY() - 2;
        int z = 0;
        for (BlockPos pos : posList.getData()) {
            if (pos.getY() == y) {
                x += pos.getX();
                z += pos.getZ();
            }
        }
        return new BlockPos(x / 8, y, z / 8);
    }

    private void removeAllAltarItem(World world, TileEntityAltar altar) {
        for (BlockPos pos : altar.getCanPlaceItemPosList().getData()) {
            this.getAltar(world, pos).ifPresent(te -> {
                te.handler.setStackInSlot(0, ItemStack.EMPTY);
                te.refresh();
                spawnParticleInCentre(world, te.getBlockPos());
            });
        }
    }

    private void spawnParticleInCentre(World world, BlockPos centrePos) {
        int width = 1;
        int height = 1;
        for (int i = 0; i < 5; ++i) {
            double xSpeed = RANDOM.nextGaussian() * 0.02;
            double ySpeed = RANDOM.nextGaussian() * 0.02;
            double zSpeed = RANDOM.nextGaussian() * 0.02;
            world.addParticle(ParticleTypes.CLOUD,
                    centrePos.getX() + RANDOM.nextFloat() * width * 2 - width - xSpeed * 10,
                    centrePos.getY() + RANDOM.nextFloat() * height - ySpeed * 10,
                    centrePos.getZ() + RANDOM.nextFloat() * width * 2 - width - zSpeed * 10,
                    xSpeed, ySpeed, zSpeed);
            world.addParticle(ParticleTypes.SMOKE,
                    centrePos.getX() + RANDOM.nextFloat() * width * 2 - width - xSpeed * 10,
                    centrePos.getY() + RANDOM.nextFloat() * height - ySpeed * 10,
                    centrePos.getZ() + RANDOM.nextFloat() * width * 2 - width - zSpeed * 10,
                    xSpeed, ySpeed, zSpeed);
        }
    }
}
