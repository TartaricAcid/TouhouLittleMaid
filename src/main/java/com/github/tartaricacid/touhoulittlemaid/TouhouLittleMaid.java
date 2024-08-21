package com.github.tartaricacid.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.multiblock.MultiBlockManager;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.compat.cloth.MenuIntegration;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManger;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.meal.MaidMealManager;
import com.github.tartaricacid.touhoulittlemaid.init.*;
import com.github.tartaricacid.touhoulittlemaid.init.registry.CommandRegistry;
import com.github.tartaricacid.touhoulittlemaid.init.registry.CompatRegistry;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import com.github.tartaricacid.touhoulittlemaid.network.NewNetwork;
import com.github.tartaricacid.touhoulittlemaid.util.AnnotatedInstanceUtil;
import com.google.common.collect.Lists;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
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
        registerConfiguration(modContainer);
        modApiInit();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            registerClientOnly();
            registerConfigMenu(modContainer);
        }
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
        InitLootCondition.LOOT_CONDITION_TYPES.register(eventBus);

        eventBus.addListener(NewNetwork::registerPacket);
        eventBus.addListener(InitCapabilities::registerGenericItemHandlers);
        NeoForge.EVENT_BUS.addListener(CommandRegistry::onServerStaring);
    }

    private static void registerConfiguration(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, GeneralConfig.getConfigSpec());
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

    @OnlyIn(Dist.CLIENT)
    private void registerClientOnly() {
        // 这个仅用于客户端，所以不需要在服务端注册
        InitDataAttachment.ATTACHMENT_TYPES.register("gecko_maid", () -> GeckoMaidEntity.TYPE);
    }

    @OnlyIn(Dist.CLIENT)
    private void registerConfigMenu(ModContainer modContainer) {
        ModFileInfo clothConfigInfo = LoadingModList.get().getModFileById(CompatRegistry.CLOTH_CONFIG);
        if (clothConfigInfo != null) {
            MenuIntegration.registerModsPage(modContainer);
        } else {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
    }
}
