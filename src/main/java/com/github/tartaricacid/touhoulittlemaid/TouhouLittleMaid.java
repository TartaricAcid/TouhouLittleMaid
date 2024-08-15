package com.github.tartaricacid.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.multiblock.MultiBlockManager;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManger;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.meal.MaidMealManager;
import com.github.tartaricacid.touhoulittlemaid.init.*;
import com.github.tartaricacid.touhoulittlemaid.init.registry.CommandRegistry;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import com.github.tartaricacid.touhoulittlemaid.network.NewNetwork;
import com.github.tartaricacid.touhoulittlemaid.util.AnnotatedInstanceUtil;
import com.google.common.collect.Lists;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(TouhouLittleMaid.MOD_ID)
public final class TouhouLittleMaid {
    public static final String MOD_ID = "touhou_little_maid";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static List<ILittleMaid> EXTENSIONS = Lists.newArrayList();

    public TouhouLittleMaid(IEventBus modEventBus, ModContainer modContainer) {
        initRegister(modEventBus);
        ChatBubbleManger.initDefaultChat();
        modApiInit();
        modContainer.registerConfig(ModConfig.Type.COMMON, GeneralConfig.getConfigSpec());
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
        InitCreativeTabs.TABS.register(eventBus);
        InitContainer.CONTAINER_TYPE.register(eventBus);
        InitSounds.SOUNDS.register(eventBus);
        InitRecipes.RECIPE_SERIALIZERS.register(eventBus);
        InitRecipes.RECIPE_TYPES.register(eventBus);
        InitCommand.ARGUMENT_TYPE.register(eventBus);
        InitPoi.POI_TYPES.register(eventBus);
        InitTrigger.TRIGGERS.register(eventBus);
        InitDataAttachment.ATTACHMENT_TYPES.register(eventBus);
        InitDataComponent.DATA_COMPONENTS.register(eventBus);
        InitLootModifier.GLOBAL_LOOT_MODIFIER_SERIALIZER.register(eventBus);

        eventBus.addListener(NewNetwork::registerPacket);
        eventBus.addListener(InitCapabilities::registerGenericItemHandlers);
        NeoForge.EVENT_BUS.addListener(CommandRegistry::onServerStaring);
        ShapedRecipePattern.setCraftingSize(6,1);
        /*TODO : 曾经兼容，但是现在还没迁移到1.21neoforge的列表
        驯养革新 Domestication Innovation
        更多箱子 Iron Chests
        拔刀剑2 SlashBlade 2
        帕秋莉手册 Patchouli
        [TaCZ]永恒枪械工坊：零 Timeless and Classics Zero
        [IPN]一键背包整理NextInventory Profiles Next
         */
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
