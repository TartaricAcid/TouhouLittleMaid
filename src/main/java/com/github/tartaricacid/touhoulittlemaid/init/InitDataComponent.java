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

    public static final String MODEL_ID_TAG_NAME = "model_id";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> MODEL_ID_TAG = DATA_COMPONENTS
            .register(MODEL_ID_TAG_NAME, () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());

    public static final String MOUNTED_HEIGHT_TAG_NAME = "mounted_height";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> MOUNTED_HEIGHT_TAG = DATA_COMPONENTS
            .register(MOUNTED_HEIGHT_TAG_NAME, () -> DataComponentType.<Float>builder().persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).build());

    public static final String TAMEABLE_CAN_RIDE_TAG_NAME = "tameable_can_ride";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> TAMEABLE_CAN_RIDE_TAG = DATA_COMPONENTS
            .register(TAMEABLE_CAN_RIDE_TAG_NAME, () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> IS_NO_GRAVITY_TAG = DATA_COMPONENTS
            .register("is_no_gravity", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());

    public static final String OWNER_UUID_TAG_NAME = "owner_uuid";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> OWNER_UUID_TAG = DATA_COMPONENTS
            .register(OWNER_UUID_TAG_NAME, () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());

}
