package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.util.Optional;

public class ItemFairySpawnEgg extends ForgeSpawnEggItem {
    public ItemFairySpawnEgg() {
        super(() -> EntityFairy.TYPE, 0xffffff, 0xffffff, new Item.Properties());
    }

    @Override
    public Optional<Mob> spawnOffspringFromSpawnEgg(Player player, Mob parentMob, EntityType<? extends Mob> type, ServerLevel level, Vec3 pos, ItemStack stack) {
        if (!this.spawnsEntity(stack.getTag(), type)) {
            return Optional.empty();
        }
        if (!(parentMob instanceof EntityFairy)) {
            return Optional.empty();
        }
        EntityFairy fairy = EntityFairy.TYPE.create(level);
        if (fairy == null) {
            return Optional.empty();
        }
        fairy.finalizeSpawn(level, level.getCurrentDifficultyAt(fairy.blockPosition()), MobSpawnType.SPAWN_EGG, null, null);
        fairy.setBaby(true);
        if (!fairy.isBaby()) {
            return Optional.empty();
        }
        fairy.moveTo(pos.x(), pos.y(), pos.z(), 0.0F, 0.0F);
        level.addFreshEntityWithPassengers(fairy);
        if (stack.hasCustomHoverName()) {
            fairy.setCustomName(stack.getHoverName());
        }
        if (!player.isCreative()) {
            stack.shrink(1);
        }
        return Optional.of(fairy);
    }
}
