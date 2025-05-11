package com.github.tartaricacid.touhoulittlemaid.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class AdditionLootModifier extends LootModifier {
    public static final Supplier<Codec<AdditionLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(instance -> codecStart(instance).and(instance.group(
                    ResourceLocation.CODEC.fieldOf("loot_table_type").forGetter(m -> m.lootTableType),
                    ResourceLocation.CODEC.optionalFieldOf("loot_table_id").forGetter(m -> Optional.ofNullable(m.lootTableId)),
                    ResourceLocation.CODEC.fieldOf("loot_table_add").forGetter(m -> m.lootTableAdd)
            )).apply(instance, AdditionLootModifier::new)));

    private final ResourceLocation lootTableType;
    private final @Nullable ResourceLocation lootTableId;
    private final ResourceLocation lootTableAdd;

    public AdditionLootModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTableType,
                                Optional<ResourceLocation> lootTableId, ResourceLocation lootTableAdd) {
        super(conditionsIn);
        this.lootTableType = lootTableType;
        this.lootTableId = lootTableId.orElse(null);
        this.lootTableAdd = lootTableAdd;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ResourceLocation currentLootTable = context.getQueriedLootTableId();
        if (!currentLootTable.equals(lootTableAdd) && typeAreEquals(context) && idAreEquals(context)) {
            LootTable additionTable = context.getResolver().getLootTable(lootTableAdd);
            additionTable.getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), generatedLoot::add));
        }
        return generatedLoot;
    }

    private boolean typeAreEquals(LootContext context) {
        ResourceLocation currentLootTable = context.getQueriedLootTableId();
        LootTable lootTable = context.getResolver().getLootTable(currentLootTable);
        return Objects.equals(lootTable.getParamSet(), LootContextParamSets.get(lootTableType));
    }

    private boolean idAreEquals(LootContext context) {
        if (this.lootTableId == null) {
            return true;
        }
        return context.getQueriedLootTableId().equals(this.lootTableId);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}