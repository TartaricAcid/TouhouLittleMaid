package com.github.tartaricacid.touhoulittlemaid.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class AdditionalLootModifier extends LootModifier {
    public static final MapCodec<AdditionalLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            codecStart(inst).and(inst.group(
                    LootContextParamSets.CODEC.fieldOf("param_set").forGetter(o -> o.paramSet),
                    ResourceLocation.CODEC.fieldOf("additional_loot_table").forGetter(o -> o.additionalLootTable.location())
            )).apply(inst, AdditionalLootModifier::new));

    private final LootContextParamSet paramSet;
    private final ResourceKey<LootTable> additionalLootTable;

    public AdditionalLootModifier(LootItemCondition[] conditions, LootContextParamSet paramSet, ResourceLocation lootTable) {
        super(conditions);
        this.paramSet = paramSet;
        this.additionalLootTable = ResourceKey.create(Registries.LOOT_TABLE, lootTable);
    }

    @Override
    public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ResourceLocation currentTable = context.getQueriedLootTableId();
        if (!currentTable.equals(additionalLootTable.location())) {
            LootTable additionalTable = context.getLevel().getServer().reloadableRegistries().getLootTable(additionalLootTable);
            if (additionalTable.getParamSet().equals(paramSet)) {
                additionalTable.getRandomItems(context, LootTable.createStackSplitter(context.getLevel(), generatedLoot::add));
            }
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}