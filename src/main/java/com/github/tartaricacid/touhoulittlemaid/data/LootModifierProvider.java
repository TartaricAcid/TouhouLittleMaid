package com.github.tartaricacid.touhoulittlemaid.data;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitLootModifier;
import com.github.tartaricacid.touhoulittlemaid.loot.AdditionLootModifier;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class LootModifierProvider extends GlobalLootModifierProvider {
    public LootModifierProvider(DataGenerator gen) {
        super(gen, TouhouLittleMaid.MOD_ID);
    }

    @Override
    protected void start() {
        LootItemCondition[] randomChance = {LootItemRandomChanceCondition.randomChance(0.5f).build()};
        ResourceLocation chest = LootContextParamSets.getKey(LootContextParamSets.CHEST);
        ResourceLocation table = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/power_point");
        if (chest != null) {
            AdditionLootModifier modifier = new AdditionLootModifier(randomChance, chest, table);
            this.add("power_point", InitLootModifier.ADDITION.get(), modifier);
        }
    }
}
