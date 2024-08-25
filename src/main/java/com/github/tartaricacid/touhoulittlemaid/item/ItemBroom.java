package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ItemBroom extends Item {
    public ItemBroom() {
        super((new Properties()).stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() != Direction.DOWN) {
            Level world = context.getLevel();
            BlockPos clickedPos = new BlockPlaceContext(context).getClickedPos();
            AABB boundingBox = EntityBroom.TYPE.getDimensions().makeBoundingBox(Vec3.atBottomCenterOf(clickedPos));
            if (world.noCollision(boundingBox) && world.getEntities(null, boundingBox).isEmpty()) {
                ItemStack stack = context.getItemInHand();
                if (world instanceof ServerLevel serverWorld) {
                    EntityBroom broom = EntityBroom.TYPE.create(serverWorld, (e) -> {
                        if (stack.get(DataComponents.CUSTOM_NAME) != null) {
                            e.setCustomName(stack.get(DataComponents.CUSTOM_NAME));
                        }
                    }, context.getClickedPos(), MobSpawnType.SPAWN_EGG, true, true);
                    if (broom == null) {
                        return InteractionResult.FAIL;
                    }
                    if (context.getPlayer() != null) {
                        broom.setOwnerUUID(context.getPlayer().getUUID());
                    }
                    world.addFreshEntity(broom);
                    world.playSound(null, broom.getX(), broom.getY(), broom.getZ(), SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
                }
                stack.shrink(1);
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.broom.desc").withStyle(ChatFormatting.GRAY));
    }
}
