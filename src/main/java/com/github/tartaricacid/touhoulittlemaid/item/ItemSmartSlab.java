package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabWorkScreen;
import com.github.tartaricacid.touhoulittlemaid.data.MaidNumAttachment;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.util.PlaceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment.MAID_NUM;

public class ItemSmartSlab extends AbstractStoreMaidItem {
    private final Type type;

    public ItemSmartSlab(Type type) {
        super((new Properties()).stacksTo(1).rarity(Rarity.RARE));
        this.type = type;
    }

    public static UUID getInitMaidOwner(ItemStack stack) {
        if (stack.has(InitDataComponent.INIT_MAID_OWNER_TAG)) {
            return stack.getOrDefault(InitDataComponent.INIT_MAID_OWNER_TAG, Util.NIL_UUID);
        }
        return Util.NIL_UUID;
    }

    public static boolean setInitMaidOwner(ItemStack stack, UUID ownerUid) {
        if (stack.getItem() instanceof ItemSmartSlab smartSlab && smartSlab.type == Type.INIT) {
            stack.set(InitDataComponent.INIT_MAID_OWNER_TAG, ownerUid);
            return true;
        }
        return false;
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
        if (this.type == Type.EMPTY) {
            // 空魂符右键任意方块（任意面）打开女仆工作设置面板。
            // 只在逻辑客户端开屏；服务端 / 集成服的服务器线程什么都不做。
            if (worldIn.isClientSide && FMLEnvironment.dist == Dist.CLIENT) {
                openWabWorkScreen();
            }
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }
        if (clickedFace == Direction.UP && !PlaceHelper.notSuitableForPlaceMaid(worldIn, clickedPos)) {
            EntityMaid maid = InitEntities.MAID.get().create(worldIn);
            if (maid == null) {
                return super.useOn(context);
            }
            if (this.type == Type.INIT) {
                // 检查是否有初始主人锁定
                UUID initOwnerUid = getInitMaidOwner(context.getItemInHand());
                // 有锁定则进行 UUID 判断
                if (!initOwnerUid.equals(Util.NIL_UUID) && !player.getUUID().equals(initOwnerUid)) {
                    MutableComponent tip = Component.translatable("tooltips.touhou_little_maid.smart_slab.not_your_maid").withStyle(ChatFormatting.DARK_RED);
                    if (!worldIn.isClientSide) {
                        player.sendSystemMessage(tip);
                    }
                    return InteractionResult.FAIL;
                }
                return spawnNewMaid(context, player, worldIn, maid);
            }
            if (this.type == Type.HAS_MAID) {
                return spawnFromStore(context, player, worldIn, maid, () -> {
                    player.setItemInHand(context.getHand(), InitItems.SMART_SLAB_EMPTY.get().getDefaultInstance());
                    player.getCooldowns().addCooldown(InitItems.SMART_SLAB_EMPTY.get(), 20);
                });
            }
        } else {
            if (worldIn.isClientSide) {
                player.sendSystemMessage(Component.translatable("message.touhou_little_maid.photo.not_suitable_for_place_maid"));
            }
        }
        return super.useOn(context);
    }

    /** 右键空气：空魂符也打开工作设置面板（右键方块面走 useOn）。INIT/HAS_MAID 保持原样。 */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (this.type == Type.EMPTY) {
            if (level.isClientSide && FMLEnvironment.dist == Dist.CLIENT) {
                openWabWorkScreen();
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(level, player, hand);
    }

    @OnlyIn(Dist.CLIENT)
    private void openWabWorkScreen() {
        Minecraft.getInstance().setScreen(new WabWorkScreen());
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
