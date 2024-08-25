package com.github.tartaricacid.touhoulittlemaid.loot;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitLootCondition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record LootTableTypeCondition(String prefix) implements LootItemCondition {
    public static final MapCodec<LootTableTypeCondition> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(Codec.STRING.fieldOf("loot_table_type").forGetter(LootTableTypeCondition::prefix))
                    .apply(inst, LootTableTypeCondition::new));

    @Override
    public boolean test(LootContext context) {
        ResourceLocation lootTableId = context.getQueriedLootTableId();
        return lootTableId.getPath().startsWith(prefix) && !lootTableId.getNamespace().equals(TouhouLittleMaid.MOD_ID);
    }

    @Override
    public LootItemConditionType getType() {
        return InitLootCondition.LOOT_TABLE_TYPE.get();
    }
}