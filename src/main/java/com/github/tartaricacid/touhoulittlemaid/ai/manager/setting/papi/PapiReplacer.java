package com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.*;

/**
 * 因为现在的大语言模型基本都有多语言支持，故直接用中文写设定文件
 * <p>
 * 虽然看着很不舒服，但是为了方便使用，还是这样吧
 */
public class PapiReplacer {
    private static final Map<String, BiFunction<EntityMaid, String, String>> PAPI_MAP = Maps.newLinkedHashMap();

    static {
        registerContext("game_time", PapiReplacer::getTime);
        registerContext("weather", PapiReplacer::getWeather);
        registerContext("dimension", PapiReplacer::getDimension);
        registerContext("mainhand_item", (maid, lang) -> getSlotItemName(EquipmentSlot.MAINHAND, maid));
        registerContext("offhand_item", (maid, lang) -> getSlotItemName(EquipmentSlot.OFFHAND, maid));
        registerContext("inventory_items", PapiReplacer::getInventoryItems);
        registerContext("chat_language", (maid, lang) -> language(lang));
        registerContext("tts_language", ((maid, lang) -> language(maid.getAiChatManager().getTTSLanguage())));
        registerContext("healthy", PapiReplacer::getHealthyInfo);
        registerContext("owner_healthy", PapiReplacer::getOwnerHealthyInfo);
        registerContext("armor_items", PapiReplacer::getArmorItems);
        registerContext("effects", PapiReplacer::getEffects);
        registerContext("biome", PapiReplacer::getBiome);
        registerContext("owner_name", PapiReplacer::getOwnerName);
    }

    public static String replace(String input, EntityMaid maid, String language) {
        Map<String, String> valueMap = Maps.newHashMap();
        valueMap.put("main_setting", input);
        for (String key : PAPI_MAP.keySet()) {
            valueMap.put(key, PAPI_MAP.get(key).apply(maid, language));
        }
        String base = new StrSubstitutor(valueMap).replace(FULL_SETTING);
        if (language.equals(maid.getAiChatManager().getTTSLanguage())) {
            base += new StrSubstitutor(valueMap).replace(OUTPUT_FORMAT_REQUIREMENTS_SAME_LANGUAGES);
        } else {
            base += new StrSubstitutor(valueMap).replace(OUTPUT_FORMAT_REQUIREMENTS_DIFFERENT_LANGUAGES);
        }
        return base;
    }

    public static void registerContext(String key, BiFunction<EntityMaid, String, String> function) {
        PAPI_MAP.put(key, function);
    }

    /**
     * 不能调用 LanguageManager，那个是客户端方法
     */
    private static String language(String languageTag) {
        // 将语言代码转换为 Locale 所需的格式，例如 zh_cn -> zh-CN
        String[] parts = languageTag.split("_");
        if (parts.length == 2) {
            languageTag = parts[0] + "-" + parts[1].toUpperCase(Locale.ENGLISH);
        }
        Locale locale = Locale.forLanguageTag(languageTag);
        return LANGUAGE_FORMAT.formatted(locale.getDisplayLanguage(), locale.getDisplayCountry());
    }

    private static String getBiome(EntityMaid maid, String chatLanguage) {
        Biome biome = maid.level.getBiome(maid.blockPosition()).value();
        ResourceLocation key = maid.level.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome);
        return key == null ? UNKNOWN_BIOME : key.toString();
    }

    private static String getEffects(EntityMaid maid, String chatLanguage) {
        List<String> names = new ArrayList<>();
        maid.getActiveEffects().forEach(i -> {
            names.add(i.toString());
        });
        if (names.isEmpty()) {
            return NONE;
        }
        return StringUtils.join(names, LIST_SEPARATORS);
    }

    private static String getArmorItems(EntityMaid maid, String chatLanguage) {
        List<String> names = new ArrayList<>();
        maid.getArmorSlots().forEach(stack -> {
            if (!stack.isEmpty()) {
                String itemName = stack.getDisplayName().getString();
                int count = stack.getCount();
                names.add(ITEM_AND_COUNT_FORMAT.formatted(itemName, count));
            }
        });
        if (names.isEmpty()) {
            return EMPTY;
        }
        return StringUtils.join(names, LIST_SEPARATORS);
    }

    private static String getHealthyInfo(EntityMaid maid, String chatLanguage) {
        float maxHealth = maid.getMaxHealth();
        float health = maid.getHealth();
        return HEALTHY_FORMAT.formatted(health, maxHealth);
    }

    private static String getOwnerHealthyInfo(EntityMaid maid, String chatLanguage) {
        LivingEntity owner = maid.getOwner();
        if (owner != null) {
            float maxHealth = owner.getMaxHealth();
            float health = owner.getHealth();
            return HEALTHY_FORMAT.formatted(health, maxHealth);
        }
        return StringUtils.EMPTY;
    }

    private static String getOwnerName(EntityMaid maid, String chatLanguage) {
        String ownerName = maid.getAiChatManager().ownerName;
        if (StringUtils.isBlank(ownerName)) {
            return DEFAULT_OWNER_NAME;
        }
        return ownerName;
    }

    private static String getWeather(EntityMaid maid, String chatLanguage) {
        Level level = maid.level;
        if (level.isThundering()) {
            return THUNDERING;
        }
        if (level.isRaining()) {
            return RAINING;
        }
        return SUNNY;
    }

    private static String getTime(EntityMaid maid, String chatLanguage) {
        long time = maid.level.getDayTime();
        long hours = (time / 1000 + 6) % 24;
        long minutes = (time % 1000) / (50 / 3);
        return TIME_FORMAT.formatted(hours, minutes);
    }

    private static String getDimension(EntityMaid maid, String chatLanguage) {
        ResourceKey<Level> dimension = maid.level.dimension();
        if (dimension == Level.OVERWORLD) {
            return OVERWORLD;
        }
        if (dimension == Level.NETHER) {
            return NETHER;
        }
        if (dimension == Level.END) {
            return END;
        }
        return dimension.location().toString();
    }

    private static String getSlotItemName(EquipmentSlot slot, EntityMaid maid) {
        ItemStack stack = maid.getItemBySlot(slot);
        if (stack.isEmpty()) {
            return EMPTY;
        }
        String itemName = stack.getDisplayName().getString();
        int count = stack.getCount();
        return ITEM_AND_COUNT_FORMAT.formatted(itemName, count);
    }

    private static String getInventoryItems(EntityMaid maid, String chatLanguage) {
        List<String> names = Lists.newArrayList();
        RangedWrapper backpack = maid.getAvailableBackpackInv();
        for (int i = 0; i < backpack.getSlots(); i++) {
            ItemStack stack = backpack.getStackInSlot(i);
            if (!stack.isEmpty()) {
                String itemName = stack.getDisplayName().getString();
                int count = stack.getCount();
                names.add(ITEM_AND_COUNT_FORMAT.formatted(itemName, count));
            }
        }
        if (names.isEmpty()) {
            return EMPTY;
        }
        return StringUtils.join(names, LIST_SEPARATORS);
    }
}
