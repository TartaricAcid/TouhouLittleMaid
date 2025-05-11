package com.github.tartaricacid.touhoulittlemaid.client.resource;

import com.github.tartaricacid.simplebedrockmodel.client.manager.BedrockEntityModelRegister;
import com.github.tartaricacid.simplebedrockmodel.client.manager.BedrockEntityModelRegisterEvent;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.BroomModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityBoxModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityFairyModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.NewEntityFairyModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;

/**
 * 把所有硬编码的模型全部资源包化，方便资源包替换模型
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class BedrockModelLoader {
    // 内部数据
    private static final Map<ResourceLocation, Function<InputStream, ? extends SimpleBedrockModel<? extends Entity>>> ALL_MODELS = Maps.newHashMap();

    // 注册数据
    public static final ResourceLocation ALTAR = registerSimpleBlockModel("altar");
    public static final ResourceLocation BOOKSHELF = registerSimpleBlockModel("bookshelf");
    public static final ResourceLocation COMPUTER = registerSimpleBlockModel("computer");
    public static final ResourceLocation KEYBOARD = registerSimpleBlockModel("keyboard");
    public static final ResourceLocation PICNIC_MAT = registerSimpleBlockModel("picnic_mat");
    public static final ResourceLocation PICNIC_BASKET = registerSimpleBlockModel("picnic_basket");
    public static final ResourceLocation STATUE_BASE = registerSimpleBlockModel("statue_base");
    public static final ResourceLocation SHRINE = registerSimpleBlockModel("shrine");
    public static final ResourceLocation GOMOKU = registerSimpleBlockModel("gomoku");
    public static final ResourceLocation GOMOKU_PIECE = registerSimpleBlockModel("gomoku_piece");
    public static final ResourceLocation CCHESS = registerSimpleBlockModel("cchess");
    public static final ResourceLocation CCHESS_PIECES = registerSimpleBlockModel("cchess_pieces");
    public static final ResourceLocation WCHESS = registerSimpleBlockModel("wchess");
    public static final ResourceLocation WCHESS_PIECES = registerSimpleBlockModel("wchess_pieces");

    public static final ResourceLocation CAKE_BOX = registerEntityModel("cake_box", EntityBoxModel::new);
    public static final ResourceLocation MAID_FAIRY = registerEntityModel("maid_fairy", EntityFairyModel::new);
    public static final ResourceLocation NEW_MAID_FAIRY = registerEntityModel("new_maid_fairy", NewEntityFairyModel::new);
    public static final ResourceLocation BROOM = registerEntityModel("broom", BroomModel::new);

    public static final ResourceLocation REIMU_YUKKURI = registerSimpleEntityModel("reimu_yukkuri");
    public static final ResourceLocation MARISA_YUKKURI = registerSimpleEntityModel("marisa_yukkuri");
    public static final ResourceLocation TOMBSTONE = registerSimpleEntityModel("tombstone");
    public static final ResourceLocation MAID_BANNER = registerSimpleEntityModel("maid_banner");

    public static final ResourceLocation BIG_BACKPACK = registerSimpleEntityModel("backpack/big_backpack");
    public static final ResourceLocation CRAFTING_TABLE_BACKPACK = registerSimpleEntityModel("backpack/crafting_table_backpack");
    public static final ResourceLocation END_CHEST_BACKPACK = registerSimpleEntityModel("backpack/end_chest_backpack");
    public static final ResourceLocation FURNACE_BACKPACK = registerSimpleEntityModel("backpack/furnace_backpack");
    public static final ResourceLocation MIDDLE_BACKPACK = registerSimpleEntityModel("backpack/middle_backpack");
    public static final ResourceLocation SMALL_BACKPACK = registerSimpleEntityModel("backpack/small_backpack");
    public static final ResourceLocation TANK_BACKPACK = registerSimpleEntityModel("backpack/tank_backpack");

    public static ResourceLocation registerSimpleBlockModel(String name) {
        ResourceLocation location = new ResourceLocation(TouhouLittleMaid.MOD_ID, "bedrock/block/" + name);
        return registerSimpleModel(location);
    }

    public static ResourceLocation registerSimpleEntityModel(String name) {
        ResourceLocation location = new ResourceLocation(TouhouLittleMaid.MOD_ID, "bedrock/entity/" + name);
        return registerSimpleModel(location);
    }

    public static ResourceLocation registerSimpleModel(ResourceLocation location) {
        return registerModel(location, SimpleBedrockModel::new);
    }

    public static ResourceLocation registerBlockModel(String name, Function<InputStream, ? extends SimpleBedrockModel<? extends Entity>> function) {
        ResourceLocation location = new ResourceLocation(TouhouLittleMaid.MOD_ID, "bedrock/block/" + name);
        return registerModel(location, function);
    }

    public static ResourceLocation registerEntityModel(String name, Function<InputStream, ? extends SimpleBedrockModel<? extends Entity>> function) {
        ResourceLocation location = new ResourceLocation(TouhouLittleMaid.MOD_ID, "bedrock/entity/" + name);
        return registerModel(location, function);
    }

    public static ResourceLocation registerModel(ResourceLocation location, Function<InputStream, ? extends SimpleBedrockModel<? extends Entity>> function) {
        ALL_MODELS.put(location, function);
        return location;
    }

    @SubscribeEvent
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void onRegisterBedrockModelRenderers(BedrockEntityModelRegisterEvent event) {
        ALL_MODELS.forEach(event::register);
        ALL_MODELS.clear();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends Entity> SimpleBedrockModel<T> getModel(ResourceLocation location) {
        return (SimpleBedrockModel<T>) BedrockEntityModelRegister.INSTANCE.getModel(location);
    }
}
