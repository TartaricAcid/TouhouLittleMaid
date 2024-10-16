package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.github.tartaricacid.touhoulittlemaid.inventory.AltarRecipeInventory;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.github.tartaricacid.touhoulittlemaid.util.PosListData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble.RANDOM;


public class BlockAltar extends Block implements EntityBlock {
    public BlockAltar() {
        super(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(2, 2).noOcclusion());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityAltar(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        return this.getAltar(worldIn, pos).filter(altar -> handIn == InteractionHand.MAIN_HAND).map(altar -> {
            if (player.isShiftKeyDown() || player.getMainHandItem().isEmpty()) {
                takeOutItem(worldIn, altar, player);
            } else {
                takeInOrCraft(worldIn, altar, player);
            }
            altar.refresh();
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }).orElse(super.use(state, worldIn, pos, player, handIn, hit));
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
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
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        if (!world.isClientSide) {
            this.getAltar(world, pos).ifPresent(altar -> this.restoreStorageBlock(world, pos, altar.getBlockPosList()));
        }
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
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
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return this.getAltar(world, pos)
                .map(altar -> new ItemStack(altar.getStorageState().getBlock()))
                .orElse(super.getCloneItemStack(state, target, world, pos, player));
    }

    @Override
    public void initializeClient(Consumer<IClientBlockExtensions> consumer) {
        consumer.accept(new IClientBlockExtensions() {
            @Override
            public boolean addHitEffects(BlockState state, Level world, HitResult target, ParticleEngine manager) {
                if (target instanceof BlockHitResult && world instanceof ClientLevel) {
                    BlockHitResult blockTarget = (BlockHitResult) target;
                    BlockPos pos = blockTarget.getBlockPos();
                    ClientLevel clientLevel = (ClientLevel) world;
                    this.getAltar(world, pos).ifPresent(altar -> this.crack(clientLevel, pos, altar.getStorageState(), blockTarget.getDirection()));
                }
                return true;
            }

            @Override
            public boolean addDestroyEffects(BlockState state, Level world, BlockPos pos, ParticleEngine manager) {
                this.getAltar(world, pos).ifPresent(altar -> Minecraft.getInstance().particleEngine.destroy(pos, altar.getStorageState()));
                return true;
            }

            private Optional<TileEntityAltar> getAltar(BlockGetter world, BlockPos pos) {
                BlockEntity te = world.getBlockEntity(pos);
                if (te instanceof TileEntityAltar) {
                    return Optional.of((TileEntityAltar) te);
                }
                return Optional.empty();
            }

            @OnlyIn(Dist.CLIENT)
            private void crack(ClientLevel world, BlockPos pos, BlockState state, Direction side) {
                if (state.getRenderShape() != RenderShape.INVISIBLE) {
                    int posX = pos.getX();
                    int posY = pos.getY();
                    int posZ = pos.getZ();
                    AABB aabb = state.getShape(world, pos).bounds();
                    double x = posX + world.random.nextDouble() * (aabb.maxX - aabb.minX - 0.2) + 0.1 + aabb.minX;
                    double y = posY + world.random.nextDouble() * (aabb.maxY - aabb.minY - 0.2) + 0.1 + aabb.minY;
                    double z = posZ + world.random.nextDouble() * (aabb.maxZ - aabb.minZ - 0.2) + 0.1 + aabb.minZ;
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
                    TerrainParticle diggingParticle = new TerrainParticle(world, x, y, z, 0, 0, 0, state);
                    Minecraft.getInstance().particleEngine.add(diggingParticle.updateSprite(state, pos).setPower(0.2f).scale(0.6f));
                }
            }
        });
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, @Nullable Entity entity) {
        return this.getAltar(world, pos)
                .map(altar -> altar.getStorageState().getSoundType())
                .orElse(super.getSoundType(state, world, pos, entity));
    }


    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    private void restoreStorageBlock(Level worldIn, BlockPos currentPos, PosListData posList) {
        for (BlockPos storagePos : posList.getData()) {
            if (storagePos.equals(currentPos)) {
                continue;
            }
            this.getAltar(worldIn, storagePos).ifPresent(altar -> worldIn.setBlock(storagePos, altar.getStorageState(), Block.UPDATE_ALL));
        }
    }

    private void takeOutItem(Level world, TileEntityAltar altar, Player player) {
        if (altar.isCanPlaceItem()) {
            if (!altar.handler.getStackInSlot(0).isEmpty()) {
                ItemStack extractItem = altar.handler.extractItem(0, 1, false);
                ItemHandlerHelper.giveItemToPlayer(player, extractItem);
                altarCraft(world, altar, player);
            }
        }
    }

    private void takeInOrCraft(Level world, TileEntityAltar altar, Player playerIn) {
        if (altar.isCanPlaceItem() && altar.handler.getStackInSlot(0).isEmpty()) {
            altar.handler.setStackInSlot(0, ItemHandlerHelper.copyStackWithSize(playerIn.getMainHandItem(), 1));
            if (!playerIn.isCreative()) {
                playerIn.getMainHandItem().shrink(1);
            }
            altarCraft(world, altar, playerIn);
        }
    }

    private void altarCraft(Level world, TileEntityAltar altar, Player playerIn) {
        final AltarRecipeInventory inv = new AltarRecipeInventory();
        List<BlockPos> posList = altar.getCanPlaceItemPosList().getData();
        for (int i = 0; i < posList.size(); i++) {
            BlockEntity te = world.getBlockEntity(posList.get(i));
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

    private Optional<TileEntityAltar> getAltar(BlockGetter world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityAltar) {
            return Optional.of((TileEntityAltar) te);
        }
        return Optional.empty();
    }

    private void spawnResultEntity(Level world, Player playerIn, PowerCapability power,
                                   AltarRecipe altarRecipe, AltarRecipeInventory inventory, TileEntityAltar altar) {
        if (power.get() >= altarRecipe.getPowerCost()) {
            power.min(altarRecipe.getPowerCost());
            BlockPos centrePos = getCentrePos(altar.getBlockPosList(), altar.getBlockPos());
            if (world instanceof ServerLevel) {
                altarRecipe.spawnOutputEntity((ServerLevel) world, centrePos.above(2), inventory);
            }
            removeAllAltarItem(world, altar);
            spawnParticleInCentre(world, centrePos);
            world.playSound(null, centrePos, InitSounds.ALTAR_CRAFT.get(), SoundSource.VOICE, 1.0f, 1.0f);
            if (playerIn instanceof ServerPlayer serverPlayer) {
                InitTrigger.ALTAR_CRAFT.trigger(serverPlayer, altarRecipe.getId());
            }
        } else {
            if (!world.isClientSide) {
                playerIn.sendSystemMessage(Component.translatable("message.touhou_little_maid.altar.not_enough_power"));
            }
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

    private void removeAllAltarItem(Level world, TileEntityAltar altar) {
        for (BlockPos pos : altar.getCanPlaceItemPosList().getData()) {
            this.getAltar(world, pos).ifPresent(te -> {
                te.handler.setStackInSlot(0, ItemStack.EMPTY);
                te.refresh();
                spawnParticleInCentre(world, te.getBlockPos());
            });
        }
    }

    private void spawnParticleInCentre(Level world, BlockPos centrePos) {
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
