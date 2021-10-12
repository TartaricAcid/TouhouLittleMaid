package com.github.tartaricacid.touhoulittlemaid.data;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitLootModifier;
import com.github.tartaricacid.touhoulittlemaid.loot.AdditionLootModifier;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class LootModifierProvider extends GlobalLootModifierProvider {
    public LootModifierProvider(DataGenerator gen) {
        super(gen, TouhouLittleMaid.MOD_ID);
    }

    @Override
    protected void start() {
        ILootCondition[] randomChance = {RandomChance.randomChance(0.5f).build()};
        ResourceLocation chest = LootParameterSets.getKey(LootParameterSets.CHEST);
        ResourceLocation table = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/power_point");
        if (chest != null) {
            AdditionLootModifier modifier = new AdditionLootModifier(randomChance, chest, table);
            this.add("power_point", InitLootModifier.ADDITION.get(), modifier);
        }
    }
}
