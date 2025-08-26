package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAndItemTransformEvent;
import com.github.tartaricacid.touhoulittlemaid.compat.ysm.YsmCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.ItemMaidTooltip;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.YsmMaidInfo;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.UUID;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.MODEL_ID_TAG_NAME;

public abstract class AbstractStoreMaidItem extends Item {
    static final String CUSTOM_NAME = "CustomName";
    private static final String MAID_OWNER = "Owner";

    public AbstractStoreMaidItem(Properties properties) {
        super(properties);
    }

    public static void storeMaidData(ItemStack stack, EntityMaid maid) {
        CustomData compoundData = stack.get(InitDataComponent.MAID_INFO);
        if (compoundData == null) {
            CompoundTag tag = new CompoundTag();
            maid.saveWithoutId(tag);

            var event = new MaidAndItemTransformEvent.ToItem(maid, stack, tag);
            NeoForge.EVENT_BUS.post(event);

            stack.set(InitDataComponent.MAID_INFO, CustomData.of(tag));
        }
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (!entity.isCurrentlyGlowing()) {
            entity.setGlowingTag(true);
        }
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
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        CustomData maidInfo = stack.get(InitDataComponent.MAID_INFO);
        if (maidInfo == null) {
            return Optional.empty();
        }
        Optional<String> modelId = maidInfo.read(Codec.STRING.fieldOf(MODEL_ID_TAG_NAME)).result();
        if (modelId.isEmpty()) {
            return Optional.empty();
        }
        String customName = maidInfo.read(Codec.STRING.fieldOf(CUSTOM_NAME)).result().orElse(StringUtils.EMPTY);
        // YSM 渲染相关数据
        YsmMaidInfo ysmMaidInfo = YsmCompat.getYsmMaidInfo(maidInfo.copyTag());
        return Optional.of(new ItemMaidTooltip(modelId.get(), customName, ysmMaidInfo));
    }

    public InteractionResult spawnFromStore(UseOnContext context, Player player, Level worldIn, EntityMaid maid, Runnable runnable) {
        ItemStack stack = context.getItemInHand();
        CustomData compoundData = stack.get(InitDataComponent.MAID_INFO);
        if (compoundData != null) {
            CompoundTag maidCompound = compoundData.copyTag();
            UUID ownerUid = maidCompound.getUUID(MAID_OWNER);
            if (!player.getUUID().equals(ownerUid)) {
                return InteractionResult.FAIL;
            }

            var event = new MaidAndItemTransformEvent.ToMaid(maid, stack, maidCompound);
            NeoForge.EVENT_BUS.post(event);

            maid.load(maidCompound);
            maid.moveTo(context.getClickedPos().above(), 0, 0);
            if (worldIn instanceof ServerLevel) {
                worldIn.addFreshEntity(maid);
            }
            maid.spawnExplosionParticle();
            maid.playSound(SoundEvents.PLAYER_SPLASH, 1.0F, worldIn.random.nextFloat() * 0.1F + 0.9F);
            runnable.run();
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        } else {
            if (worldIn.isClientSide) {
                player.sendSystemMessage(Component.translatable("message.touhou_little_maid.photo.have_no_nbt_data"));
            }
        }
        return super.useOn(context);
    }
}
