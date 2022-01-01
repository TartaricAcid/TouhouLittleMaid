package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class BlockGarageKit extends Block {
    public static final VoxelShape BLOCK_AABB = Block.box(4, 0, 4, 12, 16, 12);
    private static final String ENTITY_INFO = "EntityInfo";

    public BlockGarageKit() {
        super(AbstractBlock.Properties.of(Material.CLAY).strength(1, 2).noOcclusion());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityGarageKit();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        for (String modelId : CustomPackLoader.MAID_MODELS.getModelIdSet()) {
            ItemStack stack = new ItemStack(InitBlocks.GARAGE_KIT.get());
            CompoundNBT data = stack.getOrCreateTagElement(ENTITY_INFO);
            data.putString("id", Objects.requireNonNull(InitEntities.MAID.get().getRegistryName()).toString());
            data.putString(EntityMaid.MODEL_ID_TAG, modelId);
            items.add(stack);
        }
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        popResource(worldIn, pos, getGarageKitFromWorld(worldIn, pos));
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        this.getGarageKit(worldIn, pos).ifPresent(te -> {
            Direction facing = Direction.SOUTH;
            if (placer != null) {
                facing = placer.getDirection().getOpposite();
            }
            te.setData(facing, ItemGarageKit.getMaidData(stack));
        });
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return getGarageKitFromWorld(world, pos);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        ItemStack stack = playerIn.getItemInHand(hand);
        if (!(worldIn instanceof ServerWorld) || !(stack.getItem() instanceof SpawnEggItem)) {
            return ActionResultType.PASS;
        }
        TileEntity tile = worldIn.getBlockEntity(pos);
        if (!(tile instanceof TileEntityGarageKit)) {
            return ActionResultType.PASS;
        }
        EntityType<?> type = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
        if (type.getRegistryName() == null) {
            return ActionResultType.PASS;
        }

        String id = type.getRegistryName().toString();
        CompoundNBT data = new CompoundNBT();
        data.putString("id", id);

        Entity entity = type.create(worldIn);
        if (entity instanceof MobEntity) {
            MobEntity mobEntity = (MobEntity) entity;
            mobEntity.finalizeSpawn((ServerWorld) worldIn, worldIn.getCurrentDifficultyAt(pos), SpawnReason.SPAWN_EGG, null, data);
            mobEntity.addAdditionalSaveData(data);
        }

        TileEntityGarageKit garageKit = (TileEntityGarageKit) tile;
        garageKit.setData(garageKit.getFacing(), data);
        return ActionResultType.SUCCESS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) {
        Minecraft.getInstance().particleEngine.destroy(pos, Blocks.CLAY.defaultBlockState());
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addHitEffects(BlockState state, World world, RayTraceResult target, ParticleManager manager) {
        if (target instanceof BlockRayTraceResult && world instanceof ClientWorld) {
            BlockRayTraceResult blockTarget = (BlockRayTraceResult) target;
            BlockPos pos = blockTarget.getBlockPos();
            ClientWorld clientWorld = (ClientWorld) world;
            this.crack(clientWorld, pos, Blocks.CLAY.defaultBlockState(), blockTarget.getDirection());
        }
        return true;
    }

    private ItemStack getGarageKitFromWorld(IBlockReader world, BlockPos pos) {
        ItemStack stack = new ItemStack(InitBlocks.GARAGE_KIT.get());
        getGarageKit(world, pos).ifPresent(te -> stack.getOrCreateTag().put(ENTITY_INFO, te.getExtraData()));
        return stack;
    }

    private Optional<TileEntityGarageKit> getGarageKit(IBlockReader world, BlockPos pos) {
        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityGarageKit) {
            return Optional.of((TileEntityGarageKit) te);
        }
        return Optional.empty();
    }

    @Nullable
    public EntityType<?> getType(@Nullable CompoundNBT nbt) {
        if (nbt != null && nbt.contains("EntityTag", 10)) {
            CompoundNBT compoundnbt = nbt.getCompound("EntityTag");
            if (compoundnbt.contains("id", 8)) {
                return EntityType.byString(compoundnbt.getString("id")).orElse(null);
            }
        }
        return null;
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

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BLOCK_AABB;
    }
}
