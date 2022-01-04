package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.util.PlaceHelper;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemSmartSlab extends Item {
    private static final String MAID_INFO = "MaidInfo";
    private final Type type;

    public ItemSmartSlab(Type type) {
        super((new Properties()).tab(MAIN_TAB).stacksTo(1));
        this.type = type;
    }

    public static void storeMaidData(ItemStack stack, EntityMaid maid) {
        maid.addAdditionalSaveData(stack.getOrCreateTagElement(MAID_INFO));
    }

    public static boolean hasMaidData(ItemStack stack) {
        return stack.hasTag() && !Objects.requireNonNull(stack.getTag()).getCompound(MAID_INFO).isEmpty();
    }

    public static CompoundNBT getMaidData(ItemStack stack) {
        if (hasMaidData(stack)) {
            return Objects.requireNonNull(stack.getTag()).getCompound(MAID_INFO);
        }
        return new CompoundNBT();
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group) && this.type == Type.INIT) {
            items.add(new ItemStack(this));
        }
    }

    @Override
    public String getDescriptionId() {
        return "item.touhou_little_maid.smart_slab";
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        Direction clickedFace = context.getClickedFace();
        PlayerEntity player = context.getPlayer();
        World worldIn = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        if (clickedFace == Direction.UP && player != null && !PlaceHelper.notSuitableForPlaceMaid(worldIn, clickedPos)) {
            EntityMaid maid = InitEntities.MAID.get().create(worldIn);
            if (maid == null) {
                return super.useOn(context);
            }
            maid.moveTo(clickedPos.above(), 0, 0);
            if (this.type == Type.INIT) {
                return spawnNewMaid(context, player, worldIn, maid);
            }
            if (this.type == Type.HAS_MAID) {
                return spawnFromStore(context, player, worldIn, maid);
            }
        }
        return super.useOn(context);
    }

    private ActionResultType spawnFromStore(ItemUseContext context, PlayerEntity player, World worldIn, EntityMaid maid) {
        ItemStack stack = context.getItemInHand();
        if (hasMaidData(stack)) {
            maid.readAdditionalSaveData(getMaidData(stack));
            if (stack.hasCustomHoverName()) {
                maid.setCustomName(stack.getHoverName());
            }
            if (worldIn instanceof ServerWorld) {
                worldIn.addFreshEntity(maid);
            }
            maid.spawnExplosionParticle();
            maid.playSound(SoundEvents.PLAYER_SPLASH, 1.0F, worldIn.random.nextFloat() * 0.1F + 0.9F);
            player.setItemInHand(context.getHand(), InitItems.SMART_SLAB_EMPTY.get().getDefaultInstance());
            return ActionResultType.sidedSuccess(worldIn.isClientSide);
        }
        return super.useOn(context);
    }

    private ActionResultType spawnNewMaid(ItemUseContext context, PlayerEntity player, World worldIn, EntityMaid maid) {
        return player.getCapability(MaidNumCapabilityProvider.MAID_NUM_CAP).map(cap -> {
            if (cap.canAdd()) {
                cap.add();
                maid.tame(player);
                if (worldIn instanceof ServerWorld) {
                    maid.finalizeSpawn((ServerWorld) worldIn, worldIn.getCurrentDifficultyAt(context.getClickedPos()),
                            SpawnReason.SPAWN_EGG, null, null);
                    worldIn.addFreshEntity(maid);
                }
                maid.spawnExplosionParticle();
                maid.playSound(SoundEvents.PLAYER_SPLASH, 1.0F, worldIn.random.nextFloat() * 0.1F + 0.9F);
                player.setItemInHand(context.getHand(), InitItems.SMART_SLAB_EMPTY.get().getDefaultInstance());
                return ActionResultType.sidedSuccess(worldIn.isClientSide);
            }
            return super.useOn(context);
        }).orElse(super.useOn(context));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.type != Type.EMPTY;
    }

    public enum Type {
        /**
         * Slab Type
         */
        INIT,
        EMPTY,
        HAS_MAID,
    }
}
