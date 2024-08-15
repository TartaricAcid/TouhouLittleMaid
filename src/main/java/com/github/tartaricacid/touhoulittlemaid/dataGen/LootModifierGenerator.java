//package com.github.tartaricacid.touhoulittlemaid.dataGen;
//
//import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
//import com.github.tartaricacid.touhoulittlemaid.loot.AdditionLootModifier;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.data.PackOutput;
//import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
//import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//public class LootModifierGenerator extends GlobalLootModifierProvider {
//    public LootModifierGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modid) {
//        super(output, registries, modid);
//    }
//
//    @Override
//    protected void start() {
//        add("touhou_little_maid", new AdditionLootModifier(new LootItemCondition[]{}, "touhou_little_maid", List.of(InitItems.POWER_POINT.get(), InitItems.SHRINE.get())));
//    }
//}
