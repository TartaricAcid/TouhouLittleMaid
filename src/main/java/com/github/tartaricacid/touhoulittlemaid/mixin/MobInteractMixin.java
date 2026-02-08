package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 修复与 Immersive Melodies 模组的兼容性问题
 *
 * IM 的 MobEntityMixin 在 Mob.interact 的 HEAD 位置注入
 * 当 Mob 手上有乐器时会将其掉落并取消后续交互逻辑
 *
 * 此处额外注入一个优先级更高的 Mixin，在其之前执行女仆的交互逻辑，
 * 当交互对象是女仆、交互者是主人时，先执行女仆的 mobInteract 逻辑打开 GUI
 * IM MobEntityMixin 使用默认优先级 1000，此处使用 999 以在其之前执行
 */
@Mixin(value = Mob.class, priority = 999)
public class MobInteractMixin {
    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void touhouLittleMaid$RightClick(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!((Object) this instanceof EntityMaid maid)) return;
        if (hand != InteractionHand.MAIN_HAND) return;
        if (!maid.isOwnedBy(player)) return;
        
        InteractionResult result = maid.mobInteract(player, hand);
        if (result.consumesAction()) cir.setReturnValue(result);
    }
}
