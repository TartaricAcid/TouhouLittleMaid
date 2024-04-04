package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.ItemMaidTooltip;
import com.github.tartaricacid.touhoulittlemaid.util.PlaceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ItemPhoto extends Item {
    private static final String MAID_INFO = "MaidInfo";
    private static final String CUSTOM_NAME = "CustomName";

    public ItemPhoto() {
        super((new Properties()).stacksTo(1));
    }

    public static boolean hasMaidData(ItemStack stack) {
        return stack.hasTag() && !Objects.requireNonNull(stack.getTag()).getCompound(MAID_INFO).isEmpty();
    }

    public static CompoundTag getMaidData(ItemStack stack) {
        if (hasMaidData(stack)) {
            return Objects.requireNonNull(stack.getTag()).getCompound(MAID_INFO);
        }
        return new CompoundTag();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Direction facing = context.getClickedFace();
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack photo = context.getItemInHand();
        Vec3 clickLocation = context.getClickLocation();

        if (player == null) {
            return super.useOn(context);
        }

        // 方向不对或者位置不合适
        if (facing != Direction.UP || PlaceHelper.notSuitableForPlaceMaid(worldIn, pos)) {
            if (worldIn.isClientSide) {
                player.sendSystemMessage(Component.translatable("message.touhou_little_maid.photo.not_suitable_for_place_maid"));
            }
            return InteractionResult.FAIL;
        }

        // 检查照片的 NBT 数据
        if (!hasMaidData(photo)) {
            if (worldIn.isClientSide) {
                player.sendSystemMessage(Component.translatable("message.touhou_little_maid.photo.have_no_nbt_data"));
            }
            return InteractionResult.FAIL;
        }

        // 最后才应用生成实体的逻辑
        Optional<Entity> entityOptional = EntityType.create(getMaidData(photo), worldIn);
        if (entityOptional.isPresent() && entityOptional.get() instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) entityOptional.get();
            maid.setPos(clickLocation.x, clickLocation.y, clickLocation.z);
            // 实体生成必须在服务端应用
            if (!worldIn.isClientSide) {
                worldIn.addFreshEntity(maid);
            }
            maid.spawnExplosionParticle();
            photo.shrink(1);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (!entity.isInvulnerable()) {
            entity.setInvulnerable(true);
        }
        Vec3 position = entity.position();
        int minY = entity.level.getMinBuildHeight();
        if (position.y < minY) {
            entity.setNoGravity(true);
            entity.setDeltaMovement(Vec3.ZERO);
            entity.setPos(position.x, minY, position.z);
        }
        return super.onEntityItemUpdate(stack, entity);
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (!hasMaidData(stack)) {
            tooltip.add(Component.translatable("tooltips.touhou_little_maid.photo.no_data.desc").withStyle(ChatFormatting.DARK_RED));
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        CompoundTag maidData = getMaidData(stack);
        if (maidData.contains(EntityMaid.MODEL_ID_TAG, Tag.TAG_STRING)) {
            String modelId = maidData.getString(EntityMaid.MODEL_ID_TAG);
            String customName = "";
            if (maidData.contains(CUSTOM_NAME, Tag.TAG_STRING)) {
                customName = maidData.getString(CUSTOM_NAME);
            }
            return Optional.of(new ItemMaidTooltip(modelId, customName));
        }
        return Optional.empty();
    }
}
