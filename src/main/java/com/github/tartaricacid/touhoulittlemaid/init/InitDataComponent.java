package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.data.CompoundData;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitDataComponent {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(TouhouLittleMaid.MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> RECIPES_ID_TAG = DATA_COMPONENTS
            .register("recipe_id", () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundData>> MAID_INFO = DATA_COMPONENTS
            .register("maid_info", () -> DataComponentType.<CompoundData>builder().persistent(CompoundData.CODEC).networkSynchronized(CompoundData.STREAM_CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> ENTITY_INFO_TAG = DATA_COMPONENTS
            .register("entity_info", () -> DataComponentType.<CustomData>builder().persistent(CustomData.CODEC_WITH_ID).networkSynchronized(CustomData.STREAM_CODEC).build());
}
