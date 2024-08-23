package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.api.block.IMultiBlock;
import com.github.tartaricacid.touhoulittlemaid.block.multiblock.MultiBlockManager;
import com.google.common.base.Predicates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

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
}
