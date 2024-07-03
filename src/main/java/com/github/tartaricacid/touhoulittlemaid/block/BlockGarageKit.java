package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class BlockGarageKit extends Block implements EntityBlock {
    public static final VoxelShape BLOCK_AABB = Block.box(4, 0, 4, 12, 16, 12);
    private static final String ENTITY_INFO = "EntityInfo";

    public BlockGarageKit() {
        super(BlockBehaviour.Properties.of(Material.CLAY).strength(1, 2).noOcclusion());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        for (String modelId : CustomPackLoader.MAID_MODELS.getModelIdSet()) {
            ItemStack stack = new ItemStack(InitBlocks.GARAGE_KIT.get());
            CompoundTag data = stack.getOrCreateTagElement(ENTITY_INFO);
            data.putString("id", Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(InitEntities.MAID.get())).toString());
            data.putString(EntityMaid.MODEL_ID_TAG, modelId);
            items.add(stack);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityGarageKit(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        popResource(worldIn, pos, getGarageKitFromWorld(worldIn, pos));
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        this.getGarageKit(worldIn, pos).ifPresent(te -> {
            Direction facing = Direction.SOUTH;
            if (placer != null) {
                facing = placer.getDirection().getOpposite();
            }
            te.setData(facing, ItemGarageKit.getMaidData(stack));
        });
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return getGarageKitFromWorld(world, pos);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = playerIn.getItemInHand(hand);
        if (!(worldIn instanceof ServerLevel) || !(stack.getItem() instanceof SpawnEggItem)) {
            return InteractionResult.PASS;
        }
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (!(tile instanceof TileEntityGarageKit)) {
            return InteractionResult.PASS;
        }
        TileEntityGarageKit garageKit = (TileEntityGarageKit) tile;
        EntityType<?> type = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
        ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(type);
        if (key == null) {
            return InteractionResult.PASS;
        }

        String id = key.toString();
        CompoundTag data = new CompoundTag();
        data.putString("id", id);

        Entity entity = type.create(worldIn);
        if (entity instanceof Mob) {
            Mob mobEntity = (Mob) entity;
            mobEntity.finalizeSpawn((ServerLevel) worldIn, worldIn.getCurrentDifficultyAt(pos), MobSpawnType.SPAWN_EGG, null, data);
            mobEntity.addAdditionalSaveData(data);
        }

        garageKit.setData(garageKit.getFacing(), data);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void initializeClient(Consumer<IClientBlockExtensions> consumer) {
        consumer.accept(new IClientBlockExtensions() {
            @Override
            public boolean addHitEffects(BlockState state, Level world, HitResult target, ParticleEngine manager) {
                if (target instanceof BlockHitResult && world instanceof ClientLevel) {
                    BlockHitResult blockTarget = (BlockHitResult) target;
                    BlockPos pos = blockTarget.getBlockPos();
                    ClientLevel clientWorld = (ClientLevel) world;
                    this.crack(clientWorld, pos, Blocks.CLAY.defaultBlockState(), blockTarget.getDirection());
                }
                return true;
            }

            @Override
            public boolean addDestroyEffects(BlockState state, Level world, BlockPos pos, ParticleEngine manager) {
                Minecraft.getInstance().particleEngine.destroy(pos, Blocks.CLAY.defaultBlockState());
                return true;
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

    private ItemStack getGarageKitFromWorld(BlockGetter world, BlockPos pos) {
        ItemStack stack = new ItemStack(InitBlocks.GARAGE_KIT.get());
        getGarageKit(world, pos).ifPresent(te -> stack.getOrCreateTag().put(ENTITY_INFO, te.getExtraData()));
        return stack;
    }

    private Optional<TileEntityGarageKit> getGarageKit(BlockGetter world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityGarageKit) {
            return Optional.of((TileEntityGarageKit) te);
        }
        return Optional.empty();
    }

    @Nullable
    public EntityType<?> getType(@Nullable CompoundTag nbt) {
        if (nbt != null && nbt.contains("EntityTag", Tag.TAG_COMPOUND)) {
            CompoundTag compound = nbt.getCompound("EntityTag");
            if (compound.contains("id", Tag.TAG_STRING)) {
                return EntityType.byString(compound.getString("id")).orElse(null);
            }
        }
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return BLOCK_AABB;
    }
}
