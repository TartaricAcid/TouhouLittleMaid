package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.data.MaidNumAttachment;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.util.PlaceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment.MAID_NUM;

public class ItemSmartSlab extends AbstractStoreMaidItem {
    private final Type type;

    public ItemSmartSlab(Type type) {
        super((new Properties()).stacksTo(1));
        this.type = type;
    }

    @Override
    public String getDescriptionId() {
        return "item.touhou_little_maid.smart_slab";
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Direction clickedFace = context.getClickedFace();
        Player player = context.getPlayer();
        Level worldIn = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        if (player == null) {
            return super.useOn(context);
        }
        if (clickedFace == Direction.UP && !PlaceHelper.notSuitableForPlaceMaid(worldIn, clickedPos)) {
            EntityMaid maid = InitEntities.MAID.get().create(worldIn);
            if (maid == null) {
                return super.useOn(context);
            }
            if (this.type == Type.INIT) {
                return spawnNewMaid(context, player, worldIn, maid);
            }
            if (this.type == Type.HAS_MAID) {
                return spawnFromStore(context, player, worldIn, maid, () -> {
                    player.setItemInHand(context.getHand(), InitItems.SMART_SLAB_EMPTY.get().getDefaultInstance());
                    player.getCooldowns().addCooldown(InitItems.SMART_SLAB_EMPTY.get(), 20);
                });
            }
        } else {
            if (this.type != Type.EMPTY && worldIn.isClientSide) {
                player.sendSystemMessage(Component.translatable("message.touhou_little_maid.photo.not_suitable_for_place_maid"));
            }
        }
        return super.useOn(context);
    }

    private InteractionResult spawnNewMaid(UseOnContext context, Player player, Level worldIn, EntityMaid maid) {
        MaidNumAttachment cap = player.getData(MAID_NUM);
        if (cap.canAdd() || player.isCreative()) {
            if (!player.isCreative()) {
                cap.add();
            }
            maid.tame(player);
            if (worldIn instanceof ServerLevel) {
                maid.finalizeSpawn((ServerLevel) worldIn, worldIn.getCurrentDifficultyAt(context.getClickedPos()), MobSpawnType.SPAWN_EGG, null);
                maid.moveTo(context.getClickedPos().above(), 0, 0);
                worldIn.addFreshEntity(maid);
            }
            maid.spawnExplosionParticle();
            maid.playSound(SoundEvents.PLAYER_SPLASH, 1.0F, worldIn.random.nextFloat() * 0.1F + 0.9F);
            player.setItemInHand(context.getHand(), InitItems.SMART_SLAB_EMPTY.get().getDefaultInstance());
            player.getCooldowns().addCooldown(InitItems.SMART_SLAB_EMPTY.get(), 20);
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        } else {
            if (worldIn.isClientSide) {
                player.sendSystemMessage(Component.translatable("message.touhou_little_maid.owner_maid_num.can_not_add", cap.get(), cap.getMaxNum()));
            }
            return super.useOn(context);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.type != Type.EMPTY;
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return this.type != Type.HAS_MAID;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (this.type == Type.INIT) {
            MutableComponent text = Component.translatable("tooltips.touhou_little_maid.smart_slab.maid_name", I18n.get("tooltips.touhou_little_maid.smart_slab.maid_name.unknown"));
            tooltip.add(text.withStyle(ChatFormatting.GRAY));
        }
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.smart_slab.desc").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (this.type == Type.HAS_MAID) {
            return super.getTooltipImage(stack);
        }
        return Optional.empty();
    }

    public enum Type {
        /**
         * Slab Type
         */
        INIT, EMPTY, HAS_MAID,
    }
}
