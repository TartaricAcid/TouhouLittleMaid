package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.data.TrackInfo;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitDataComponent {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(TouhouLittleMaid.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> RECIPES_ID_TAG = DATA_COMPONENTS
            .register("recipe_id", () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> MAID_INFO = DATA_COMPONENTS
            .register("maid_info", () -> DataComponentType.<CustomData>builder().persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TrackInfo>> TRACK_INFO = DATA_COMPONENTS
            .register("track_info", () -> DataComponentType.<TrackInfo>builder().persistent(TrackInfo.CODEC).networkSynchronized(TrackInfo.STREAM_CODEC).build());

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

    public static final String KAPPA_COMPASS_ACTIVITY_POS_NAME = "kappa_compass_activity_pos";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Map<String, BlockPos>>> KAPPA_COMPASS_ACTIVITY_POS = DATA_COMPONENTS
            .register(KAPPA_COMPASS_ACTIVITY_POS_NAME, () -> DataComponentType.<Map<String, BlockPos>>builder().persistent(Codec.unboundedMap(Codec.STRING, BlockPos.CODEC))
                    .networkSynchronized(ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, BlockPos.STREAM_CODEC)).build());

    public static final String KAPPA_COMPASS_DIMENSION_NAME = "kappa_compass_dimension";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> KAPPA_COMPASS_DIMENSION = DATA_COMPONENTS
            .register(KAPPA_COMPASS_DIMENSION_NAME, () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());

    public static final String FILTER_MODE_NAME = "item_filter_mode";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> FILTER_MODE = DATA_COMPONENTS
            .register(FILTER_MODE_NAME, () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());

    public static final String IO_MODE_NAME = "item_io_mode";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> IO_MODE = DATA_COMPONENTS
            .register(IO_MODE_NAME, () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());

    public static final String FILTER_LIST_TAG_NAME = "item_filter_list";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> FILTER_LIST_TAG = DATA_COMPONENTS
            .register(FILTER_LIST_TAG_NAME, () -> DataComponentType.<CompoundTag>builder().persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.COMPOUND_TAG).build());

    public static final String BINDING_POS_NAME = "binding_pos";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> BINDING_POS = DATA_COMPONENTS
            .register(BINDING_POS_NAME, () -> DataComponentType.<BlockPos>builder().persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC).build());

    public static final String SLOT_CONFIG_TAG_NAME = "slot_config_data";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<Boolean>>> SLOT_CONFIG_TAG = DATA_COMPONENTS
            .register(SLOT_CONFIG_TAG_NAME, () -> DataComponentType.<List<Boolean>>builder().persistent(Codec.BOOL.listOf())
                    .networkSynchronized(ByteBufCodecs.BOOL.apply(ByteBufCodecs.list())).build());

    public static final String STORAGE_DATA_TAG_NAME = "storage_data";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> STORAGE_DATA_TAG = DATA_COMPONENTS
            .register(STORAGE_DATA_TAG_NAME, () -> DataComponentType.<CompoundTag>builder().persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.COMPOUND_TAG).build());

    public static final String TANK_BACKPACK_TAG_NAME = "tanks";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> TANK_BACKPACK_TAG = DATA_COMPONENTS
            .register(TANK_BACKPACK_TAG_NAME, () -> DataComponentType.<CompoundTag>builder().persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.COMPOUND_TAG).build());

    public static final String INGREDIENT_TAG_NAME = "ingredient";
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Ingredient>> INGREDIENT_TAG = DATA_COMPONENTS
            .register(INGREDIENT_TAG_NAME, () -> DataComponentType.<Ingredient>builder().persistent(Ingredient.CODEC).networkSynchronized(Ingredient.CONTENTS_STREAM_CODEC).build());
}
