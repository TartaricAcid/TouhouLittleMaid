package com.github.tartaricacid.touhoulittlemaid.loot;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.datapack.BoardStateData;
import com.github.tartaricacid.touhoulittlemaid.datapack.pojo.BoardStateRecord;
import com.github.tartaricacid.touhoulittlemaid.init.InitLootModifier;
import com.github.tartaricacid.touhoulittlemaid.item.ItemBoardState;
import com.github.tartaricacid.touhoulittlemaid.util.WeightedPicker;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class RandomBoardStateFunction extends LootItemConditionalFunction {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "board_state_randomly");
    public static final MapCodec<RandomBoardStateFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance).and(
                    Codec.STRING.listOf().fieldOf("tags").forGetter(f -> f.tags)
            ).apply(instance, RandomBoardStateFunction::new)
    );

    /**
     * 会选择的残局所具有的 tag
     */
    private final List<String> tags;

    protected RandomBoardStateFunction(List<LootItemCondition> predicates, List<String> tags) {
        super(predicates);
        this.tags = tags;
    }

    public static RandomBoardStateFunction.Builder create() {
        return new RandomBoardStateFunction.Builder();
    }


    private boolean checkTags(BoardStateRecord record) {
        for (String tag : tags) {
            if (record.tags().contains(tag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        List<BoardStateRecord> records = BoardStateData.getRecordsByItem(stack);
        if (records.isEmpty()) {
            return stack;
        }
        if (this.tags.isEmpty()) {
            return stack;
        }
        List<BoardStateRecord> matchedRecords = records.stream().filter(this::checkTags).toList();
        if (matchedRecords.isEmpty()) {
            return stack;
        }
        BoardStateRecord selected = WeightedPicker.pickRandom(matchedRecords, BoardStateRecord::weight);
        if (selected == null) {
            return stack;
        }
        BoardStateRecord.Display display = selected.display();
        ItemBoardState.setState(stack, selected.data(), display.description(), display.author());
        return stack;
    }

    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return InitLootModifier.BOARD_STATE_RANDOMLY.get();
    }

    public static class Builder extends LootItemConditionalFunction.Builder<RandomBoardStateFunction.Builder> {
        private final List<String> tags = Lists.newArrayList();

        @Override
        protected Builder getThis() {
            return this;
        }

        public RandomBoardStateFunction.Builder addTag(String tag) {
            this.tags.add(tag);
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new RandomBoardStateFunction(this.getConditions(), this.tags);
        }
    }
}
