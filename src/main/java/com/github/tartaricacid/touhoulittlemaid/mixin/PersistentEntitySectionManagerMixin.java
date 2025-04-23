package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * 当马上要从世界卸载时，传送到玩家身边，用来避免女仆在卸载区块上
 * <p>
 * 参考自：<a href="https://github.com/Snownee/Companion/blob/1.20-forge/src/main/java/snownee/companion/mixin/PersistentEntitySectionManagerMixin.java">companion</a>
 */
@Mixin(PersistentEntitySectionManager.class)
public abstract class PersistentEntitySectionManagerMixin {
    @Final
    @Shadow
    private LongSet chunksToUnload;
    @Final
    @Shadow
    private Long2ObjectMap<Visibility> chunkVisibility;
    @Final
    @Shadow
    EntitySectionStorage<EntityAccess> sectionStorage;

    @Inject(
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/world/level/entity/PersistentEntitySectionManager;processUnloads()V"
            ), method = "tick"
    )
    private void companionTick(CallbackInfo ci) {
        chunksToUnload.forEach(l -> {
            if (this.chunkVisibility.get(l) != Visibility.HIDDEN || !areEntitiesLoaded(l)) {
                return;
            }
            List<EntityAccess> entities = sectionStorage.getExistingSectionsInChunk(l).flatMap(EntitySection::getEntities).toList();
            handleChunkPreUnload(entities);
        });
    }

    @Unique
    private static void handleChunkPreUnload(List<EntityAccess> entities) {
        for (var entityAccess : entities) {
            if (entityAccess instanceof EntityMaid maid && maid.getOwner() instanceof Player owner) {
                // 跨维度就不能传送了
                if (owner.level() != maid.level()) {
                    continue;
                }
                if (!shouldFollowOwner(owner, maid)) {
                    continue;
                }
                if (maid.teleportToOwner(owner)) {
                    MaidWorldData data = MaidWorldData.get(maid.level);
                    if (data != null) {
                        data.removeInfo(maid);
                    }
                }
            }
        }
    }

    @Unique
    private static boolean shouldFollowOwner(LivingEntity owner, EntityMaid maid) {
        if (owner == null || owner.isDeadOrDying() || owner.isSpectator()) {
            return false;
        }
        return !maid.isHomeModeEnable() && maid.canBrainMoving();
    }

    @Shadow
    public abstract boolean areEntitiesLoaded(long l);
}
