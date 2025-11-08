package com.github.tartaricacid.touhoulittlemaid.loot;

import com.github.tartaricacid.touhoulittlemaid.init.InitLootModifier;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public record LootTableTypeCondition(ResourceLocation lootTableType,
                                     @Nullable ResourceKey<LootTable> lootTableId,
                                     ResourceKey<LootTable> lootTableAdd) implements LootItemCondition {
    public static final MapCodec<LootTableTypeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("loot_table_type").forGetter(m -> m.lootTableType),
            ResourceKey.codec(Registries.LOOT_TABLE).optionalFieldOf("loot_table_id").forGetter(m -> Optional.ofNullable(m.lootTableId)),
            ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table_add").forGetter(m -> m.lootTableAdd)
    ).apply(instance, (type, id, add)
            -> new LootTableTypeCondition(type, id.orElse(null), add)));

    @Override
    public boolean test(LootContext context) {
        ResourceLocation currentLootTable = context.getQueriedLootTableId();
        return !currentLootTable.equals(lootTableAdd.location()) && typeAreEquals(context) && idAreEquals(context);
    }

    private boolean typeAreEquals(LootContext context) {
        ResourceKey<LootTable> currentLootTable = ResourceKey.create(Registries.LOOT_TABLE, context.getQueriedLootTableId());
        return context.getResolver().get(Registries.LOOT_TABLE, currentLootTable).map(lootTable ->
                        Objects.equals(lootTable.value().getParamSet(), LootContextParamSets.REGISTRY.get(lootTableType)))
                .orElse(false);
    }

    private boolean idAreEquals(LootContext context) {
        if (this.lootTableId == null) {
            return true;
        }
        return context.getQueriedLootTableId().equals(this.lootTableId.location());
    }

    @Override
    public LootItemConditionType getType() {
        return InitLootModifier.LOOT_TABLE_TYPE.get();
    }
}