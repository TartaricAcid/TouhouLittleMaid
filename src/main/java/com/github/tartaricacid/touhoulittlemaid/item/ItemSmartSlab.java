package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.github.tartaricacid.touhoulittlemaid.util.PlaceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItemSmartSlab extends Item {
    private static final String MAID_INFO = "MaidInfo";
    private static final String MAID_OWNER = "Owner";
    private final Type type;

    public ItemSmartSlab(Type type) {
        super((new Properties()).stacksTo(1));
        this.type = type;
    }

    public static void storeMaidData(ItemStack stack, EntityMaid maid) {
        maid.addAdditionalSaveData(stack.getOrCreateTagElement(MAID_INFO));
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
            maid.moveTo(clickedPos.above(), 0, 0);
            if (this.type == Type.INIT) {
                return spawnNewMaid(context, player, worldIn, maid);
            }
            if (this.type == Type.HAS_MAID) {
                return spawnFromStore(context, player, worldIn, maid);
            }
        } else {
            if (worldIn.isClientSide) {
                player.sendSystemMessage(Component.translatable("message.touhou_little_maid.photo.not_suitable_for_place_maid"));
            }
        }
        return super.useOn(context);
    }

    private InteractionResult spawnFromStore(UseOnContext context, Player player, Level worldIn, EntityMaid maid) {
        ItemStack stack = context.getItemInHand();
        if (hasMaidData(stack)) {
            CompoundTag maidData = getMaidData(stack);
            UUID ownerUid = maidData.getUUID(MAID_OWNER);
            if (!player.getUUID().equals(ownerUid)) {
                return InteractionResult.FAIL;
            }
            maid.readAdditionalSaveData(maidData);
            if (stack.hasCustomHoverName()) {
                maid.setCustomName(stack.getHoverName());
            }
            if (worldIn instanceof ServerLevel) {
                worldIn.addFreshEntity(maid);
            }
            maid.spawnExplosionParticle();
            maid.playSound(SoundEvents.PLAYER_SPLASH, 1.0F, worldIn.random.nextFloat() * 0.1F + 0.9F);
            player.setItemInHand(context.getHand(), InitItems.SMART_SLAB_EMPTY.get().getDefaultInstance());
            player.getCooldowns().addCooldown(InitItems.SMART_SLAB_EMPTY.get(), 20);
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }
        return super.useOn(context);
    }

    private InteractionResult spawnNewMaid(UseOnContext context, Player player, Level worldIn, EntityMaid maid) {
        return player.getCapability(MaidNumCapabilityProvider.MAID_NUM_CAP).map(cap -> {
            if (cap.canAdd() || player.isCreative()) {
                if (!player.isCreative()) {
                    cap.add();
                }
                maid.tame(player);
                if (worldIn instanceof ServerLevel) {
                    maid.finalizeSpawn((ServerLevel) worldIn, worldIn.getCurrentDifficultyAt(context.getClickedPos()),
                            MobSpawnType.SPAWN_EGG, null, null);
                    worldIn.addFreshEntity(maid);
                }
                maid.spawnExplosionParticle();
                maid.playSound(SoundEvents.PLAYER_SPLASH, 1.0F, worldIn.random.nextFloat() * 0.1F + 0.9F);
                player.setItemInHand(context.getHand(), InitItems.SMART_SLAB_EMPTY.get().getDefaultInstance());
                player.getCooldowns().addCooldown(InitItems.SMART_SLAB_EMPTY.get(), 20);
                return InteractionResult.sidedSuccess(worldIn.isClientSide);
            } else {
                if (worldIn.isClientSide) {
                    player.sendSystemMessage(Component.translatable("message.touhou_little_maid.owner_maid_num.can_not_add",
                            cap.get(), cap.getMaxNum()));
                }
                return super.useOn(context);
            }
        }).orElse(super.useOn(context));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.type != Type.EMPTY;
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
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (this.type == Type.INIT) {
            MutableComponent text = Component.translatable("tooltips.touhou_little_maid.smart_slab.maid_name",
                    I18n.get("tooltips.touhou_little_maid.smart_slab.maid_name.unknown"));
            tooltip.add(text.withStyle(ChatFormatting.GRAY));
        }
        if (this.type == Type.HAS_MAID) {
            CompoundTag maidData = getMaidData(stack);
            if (maidData.contains(EntityMaid.MODEL_ID_TAG, Tag.TAG_STRING)) {
                String modelId = getMaidData(stack).getString(EntityMaid.MODEL_ID_TAG);
                if (StringUtils.isNotBlank(modelId)) {
                    CustomPackLoader.MAID_MODELS.getInfo(modelId).ifPresent(info -> {
                        String modelNameKey = ParseI18n.getI18nKey(info.getName());
                        MutableComponent text = Component.translatable("tooltips.touhou_little_maid.smart_slab.maid_name",
                                I18n.get(modelNameKey));
                        tooltip.add(text.withStyle(ChatFormatting.GRAY));
                    });
                }
            }
        }
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.smart_slab.desc").withStyle(ChatFormatting.GRAY));
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
