package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.api.block.IMultiBlock;
import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBeacon;
import com.github.tartaricacid.touhoulittlemaid.block.multiblock.MultiBlockManager;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.google.common.base.Predicates;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

import static com.github.tartaricacid.touhoulittlemaid.block.BlockAltar.getCoreAltar;
import static com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBeacon.POSITION;
import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.BINDING_POS;

public class ItemHakureiGohei extends ProjectileWeaponItem {
    public ItemHakureiGohei() {
        super((new Properties())
                .durability(300)
                .setNoRepair()
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .build()));
    }

    public static boolean isGohei(ItemStack stack) {
        return stack.getItem() instanceof ItemHakureiGohei;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return Predicates.alwaysTrue();
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    protected void shootProjectile(LivingEntity pShooter, Projectile pProjectile, int pIndex, float pVelocity, float pInaccuracy, float pAngle, @Nullable LivingEntity pTarget) {
        // 御币不能发射弹幕的，所以该方法为空体
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity pEntity) {
        return 500;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getHand() == InteractionHand.MAIN_HAND) {
            BlockPos clickedPos = context.getClickedPos();
            Level level = context.getLevel();
            BlockState state = level.getBlockState(clickedPos);
            Block block = state.getBlock();
            ItemStack itemInHand = context.getItemInHand();
            // 判定是否为神社庭灯
            if (block instanceof BlockMaidBeacon) {
                BlockMaidBeacon.Position value = state.getValue(POSITION);
                if (value == BlockMaidBeacon.Position.DOWN) {
                    itemInHand.set(BINDING_POS, clickedPos.above());
                } else {
                    itemInHand.set(BINDING_POS, clickedPos);
                }
                if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
                    serverPlayer.sendSystemMessage(Component.translatable("tooltips.touhou_little_maid.hakurei_gohei.bind_beacon"));
                }
                return InteractionResult.SUCCESS;
            }
            // 如果是神社祭坛，则绑定御币的祭坛位置
            getCoreAltar(level, clickedPos).ifPresent(coreAltar -> {
                BlockPos blockPos = itemInHand.get(BINDING_POS);
                if (blockPos != null) {
                    coreAltar.setMaidBeaconPos(blockPos);
                    itemInHand.set(BINDING_POS, null);
                    if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
                        serverPlayer.sendSystemMessage(Component.translatable("tooltips.touhou_little_maid.hakurei_gohei.bind_altar"));
                    }
                }
            });

            List<IMultiBlock> multiBlockList = MultiBlockManager.getMultiBlockList();
            BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            Direction direction = context.getClickedFace();

            for (IMultiBlock multiBlock : multiBlockList) {
                if (multiBlock.isCoreBlock(blockState) && multiBlock.directionIsSuitable(direction)) {
                    if (world instanceof ServerLevel) {
                        BlockPos posStart = pos.offset(multiBlock.getCenterPos(direction));
                        StructureTemplate template = multiBlock.getTemplate((ServerLevel) world, direction);
                        if (multiBlock.isMatch(world, posStart, direction, template)) {
                            multiBlock.build(world, posStart, direction, template);
                            if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
                                InitTrigger.MAID_EVENT.get().trigger(serverPlayer, TriggerType.BUILD_ALTAR);
                            }
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useOn(context);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(2, attacker, attacker.getEquipmentSlotForItem(stack));
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return super.isEnchantable(pStack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        BlockPos pos = stack.get(BINDING_POS);
        if (pos != null) {
            String posString = I18n.get("tooltips.touhou_little_maid.wireless_io.binding_pos.has",
                    pos.getX(), pos.getY(), pos.getZ());
            tooltip.add(Component.literal(posString).withStyle(ChatFormatting.GRAY));
        }
    }
}
