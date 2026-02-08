package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies.server.ImmersiveMelodiesServerCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 修复与 Immersive Melodies 模组的兼容性问题
 * <p>
 * IM 的 MobEntityMixin 在 Mob.interact 的 HEAD 位置注入
 * 当 Mob 手上有乐器时会将其掉落并取消后续交互逻辑
 * <p>
 * 此处额外注入一个优先级更高的 Mixin，在其之前执行女仆的交互逻辑，
 * 当交互对象是女仆、交互者是主人时，先执行女仆的 mobInteract 逻辑打开 GUI
 * <p>
 * IM MobEntityMixin 使用默认优先级 1000，此处使用 999 以在其之前执行
 */
@Mixin(value = Mob.class, priority = 999)
public class MobInteractMixin {
    @Shadow
    @Final
    private NonNullList<ItemStack> handItems;

    @SuppressWarnings("all")
    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void touhouLittleMaid$RightClick(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!((Object) this instanceof EntityMaid maid)) {
            return;
        }
        if (hand != InteractionHand.MAIN_HAND) {
            return;
        }
        if (!maid.isOwnedBy(player)) {
            return;
        }
        // 仅在手上有乐器时，才执行 mixin 过的逻辑，尽可能减少对原版交互逻辑的影响
        for (ItemStack handItem : this.handItems) {
            if (ImmersiveMelodiesServerCompat.isInstrumentItem(handItem)) {
                InteractionResult result = maid.mobInteract(player, hand);
                if (result.consumesAction()) {
                    cir.setReturnValue(result);
                }
                return;
            }
        }
    }
}
