package com.github.tartaricacid.touhoulittlemaid.danmaku;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import javax.script.Bindings;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Objects;

/**
 * @author TartaricAcid
 * @date 2019/11/16 9:55
 **/
public final class CustomSpellCardManger {
    /**
     * 用于客户端显示的符卡条目，会从所处服务端获取数据
     */
    public static final Map<String, CustomSpellCardEntry> CUSTOM_SPELL_CARD_CLIENT = Maps.newHashMap();
    /**
     * 用于服务端的符卡条目，会同步客户端数据
     */
    public static final Map<String, CustomSpellCardEntry> CUSTOM_SPELL_CARD_SERVER = Maps.newHashMap();

    private static final Path CONFIG_SPELL_CARD_FOLDER = Paths.get("config", TouhouLittleMaid.MOD_ID, "custom_spell_card");
    private static final String JAR_SPELL_CARD_FOLDER = "/assets/touhou_little_maid/custom_spell_card";
    private static final String ACCEPTED_SPELL_CARD_SUFFIX = ".js";
    private static final ResourceLocation DEFAULT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/items/spell_card/spellcard_default.png");

    public static void onCustomSpellCardReload() {
        CUSTOM_SPELL_CARD_SERVER.clear();
        checkCustomSpellCardFolder();
        loadInternalSpellCard();
        loadConfigSpellCard();
        TouhouLittleMaid.LOGGER.info("Loaded {} Custom Spell Cards", CUSTOM_SPELL_CARD_SERVER.size());
    }

    /**
     * 内部符卡通过硬编码直接进行加载，目的是保持其不变，
     * 方便后续女仆相关设定添加，同时不影响其自定义性。
     */
    private static void loadInternalSpellCard() {
        loadInternalSpellCard("border_sign.boundary_between_wave_and_particle");
        loadInternalSpellCard("border_sign.boundary_between_wave_and_particle_3d");
        loadInternalSpellCard("magic_sign.milky_way");
        loadInternalSpellCard("metal_sign.metal_fatigue");
        loadInternalSpellCard("night_sign.night_bird");
    }

    private static void loadConfigSpellCard() {
        File[] files = CONFIG_SPELL_CARD_FOLDER.toFile().listFiles((dir, name) -> name.endsWith(ACCEPTED_SPELL_CARD_SUFFIX));
        if (files == null || files.length < 1) {
            return;
        }
        for (File file : files) {
            try {
                CustomSpellCardEntry entry = loadCustomSpellCard(file);
                if (CUSTOM_SPELL_CARD_SERVER.containsKey(entry.getId())) {
                    TouhouLittleMaid.LOGGER.warn("Have a spell card of the same id: {}", entry.getId());
                } else {
                    CUSTOM_SPELL_CARD_SERVER.put(entry.getId(), entry);
                }
            } catch (NullPointerException | IOException | ScriptException e) {
                TouhouLittleMaid.LOGGER.error("Exception while loading spell in {}:", file);
                TouhouLittleMaid.LOGGER.catching(e);
            }
        }
    }

    private static void loadInternalSpellCard(String spellcardName) {
        InputStream stream = TouhouLittleMaid.class.getResourceAsStream(String.format("%s/%s.js",
                JAR_SPELL_CARD_FOLDER, spellcardName));
        try {
            CustomSpellCardEntry entry = loadCustomSpellCard(stream);
            CUSTOM_SPELL_CARD_SERVER.put(entry.getId(), entry);
        } catch (NullPointerException | IOException | ScriptException e) {
            TouhouLittleMaid.LOGGER.error("Exception while loading spell in {}:", spellcardName);
            TouhouLittleMaid.LOGGER.catching(e);
        }
    }

    private static void checkCustomSpellCardFolder() {
        if (!Files.isDirectory(CONFIG_SPELL_CARD_FOLDER)) {
            try {
                Files.createDirectories(CONFIG_SPELL_CARD_FOLDER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static CustomSpellCardEntry loadCustomSpellCard(File file) throws IOException, ScriptException {
        return loadCustomSpellCard(Files.newInputStream(file.toPath(), StandardOpenOption.READ));
    }

    private static CustomSpellCardEntry loadCustomSpellCard(InputStream stream) throws IOException, ScriptException {
        Bindings bindings = CommonProxy.NASHORN.createBindings();
        Object scriptObject = CommonProxy.NASHORN.eval(IOUtils.toString(stream, StandardCharsets.UTF_8), bindings);
        return transObjectToEntry(scriptObject);
    }

    @SuppressWarnings("unchecked")
    private static CustomSpellCardEntry transObjectToEntry(Object scriptObject) {
        Map<String, Object> scriptMaps = (Map<String, Object>) scriptObject;
        String id = (String) scriptMaps.get(Args.ID.getName());
        Objects.requireNonNull(id);
        String nameKey;
        String descriptionKey = "";
        String author = "";
        String version = "";
        int cooldown = 60;
        ResourceLocation icon = DEFAULT;
        ResourceLocation snapshot = DEFAULT;

        if (scriptMaps.containsKey(Args.NAME_KEY.getName())) {
            nameKey = (String) scriptMaps.get(Args.NAME_KEY.getName());
        } else {
            nameKey = String.format("spell_card.%s.name", id);
        }
        if (scriptMaps.containsKey(Args.DESC_KEY.getName())) {
            descriptionKey = (String) scriptMaps.get(Args.DESC_KEY.getName());
        }
        if (scriptMaps.containsKey(Args.AUTHOR.getName())) {
            author = (String) scriptMaps.get(Args.AUTHOR.getName());
        }
        if (scriptMaps.containsKey(Args.VERSION.getName())) {
            version = (String) scriptMaps.get(Args.VERSION.getName());
        }
        if (scriptMaps.containsKey(Args.COOLDOWN.getName())) {
            cooldown = (int) scriptMaps.get(Args.COOLDOWN.getName());
        }
        if (scriptMaps.containsKey(Args.ICON.getName())) {
            icon = new ResourceLocation((String) scriptMaps.get(Args.ICON.getName()));
        }
        if (scriptMaps.containsKey(Args.SNAPSHOT.getName())) {
            snapshot = new ResourceLocation((String) scriptMaps.get(Args.SNAPSHOT.getName()));
        }
        return new CustomSpellCardEntry(id, nameKey, descriptionKey, author, version, scriptObject, cooldown, icon, snapshot);
    }

    enum Args {
        // 必要的脚本参数
        ID("id"),
        NAME_KEY("nameKey"),
        DESC_KEY("descKey"),
        AUTHOR("author"),
        VERSION("version"),
        COOLDOWN("cooldown"),
        ICON("icon"),
        SNAPSHOT("snapshot");

        private String name;

        Args(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

