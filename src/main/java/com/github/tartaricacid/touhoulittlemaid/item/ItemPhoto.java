package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.data.CompoundData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.util.PlaceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ItemPhoto extends AbstractStoreMaidItem {
    public ItemPhoto() {
        super((new Properties()).stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Direction facing = context.getClickedFace();
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack photo = context.getItemInHand();
        Vec3 clickLocation = context.getClickLocation();
        CompoundData compoundData = photo.get(InitDataComponent.MAID_INFO);

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
        if (compoundData == null) {
            if (worldIn.isClientSide) {
                player.sendSystemMessage(Component.translatable("message.touhou_little_maid.photo.have_no_nbt_data"));
            }
            return InteractionResult.FAIL;
        }

        // 最后才应用生成实体的逻辑
        Optional<Entity> entityOptional = EntityType.create(compoundData.nbt(), worldIn);
        if (entityOptional.isPresent() && entityOptional.get() instanceof EntityMaid maid) {
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
    public boolean canFitInsideContainerItems() {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.get(InitDataComponent.MAID_INFO) == null) {
            tooltip.add(Component.translatable("tooltips.touhou_little_maid.photo.no_data.desc").withStyle(ChatFormatting.DARK_RED));
        }
    }
}
