package com.github.tartaricacid.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.multiblock.MultiBlockManager;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManger;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.meal.MaidMealManager;
import com.github.tartaricacid.touhoulittlemaid.init.*;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import com.github.tartaricacid.touhoulittlemaid.util.AnnotatedInstanceUtil;
import com.google.common.collect.Lists;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(TouhouLittleMaid.MOD_ID)
public final class TouhouLittleMaid {
    public static final String MOD_ID = "touhou_little_maid";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static List<ILittleMaid> EXTENSIONS = Lists.newArrayList();

    public TouhouLittleMaid() {
        initRegister(FMLJavaModLoadingContext.get().getModEventBus());
        InitTrigger.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GeneralConfig.init());
        ChatBubbleManger.initDefaultChat();
        modApiInit();
    }

    private static void initRegister(IEventBus eventBus) {
        InitEntities.ENTITY_TYPES.register(eventBus);
        InitEntities.ATTRIBUTES.register(eventBus);
        InitEntities.MEMORY_MODULE_TYPES.register(eventBus);
        InitEntities.SENSOR_TYPES.register(eventBus);
        InitEntities.SCHEDULES.register(eventBus);
        InitEntities.DATA_SERIALIZERS.register(eventBus);
        InitBlocks.BLOCKS.register(eventBus);
        InitBlocks.TILE_ENTITIES.register(eventBus);
        InitItems.ITEMS.register(eventBus);
        InitEnchantments.ENCHANTMENTS.register(eventBus);
        InitCreativeTabs.TABS.register(eventBus);
        InitContainer.CONTAINER_TYPE.register(eventBus);
        InitSounds.SOUNDS.register(eventBus);
        InitRecipes.RECIPE_SERIALIZERS.register(eventBus);
        InitLootModifier.GLOBAL_LOOT_MODIFIER_SERIALIZER.register(eventBus);
        InitCommand.ARGUMENT_TYPE.register(eventBus);
        InitPoi.POI_TYPES.register(eventBus);
    }

    private static void modApiInit() {
        EXTENSIONS = AnnotatedInstanceUtil.getModExtensions();
        TaskManager.init();
        BackpackManager.init();
        BaubleManager.init();
        MultiBlockManager.init();
        ChestManager.init();
        MaidMealManager.init();
    }
}
