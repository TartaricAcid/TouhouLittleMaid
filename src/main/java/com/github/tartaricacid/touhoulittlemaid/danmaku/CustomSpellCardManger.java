package com.github.tartaricacid.touhoulittlemaid.danmaku;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.github.tartaricacid.touhoulittlemaid.util.GetJarResources;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;

import javax.script.Bindings;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

/**
 * @author TartaricAcid
 * @date 2019/11/16 9:55
 **/
public final class CustomSpellCardManger {
    private static final Map<String, CustomSpellCardEntry> CUSTOM_SPELL_CARD_MAP = CommonProxy.CUSTOM_SPELL_CARD_MAP_SERVER;
    private static final Path CONFIG_SPELL_CARD_FOLDER = Paths.get("config", TouhouLittleMaid.MOD_ID, "custom_spell_card");
    private static final String JAR_SPELL_CARD_FOLDER = "/assets/touhou_little_maid/custom_spell_card";
    private static final String ACCEPTED_SPELL_CARD_SUFFIX = ".js";
    private static final ResourceLocation NULL = new ResourceLocation(TouhouLittleMaid.MOD_ID, "null");

    public static void onCustomSpellCardReload() {
        CUSTOM_SPELL_CARD_MAP.clear();

        unzipCustomSpellCardFolder();

        File[] files = CONFIG_SPELL_CARD_FOLDER.toFile().listFiles((dir, name) -> name.endsWith(ACCEPTED_SPELL_CARD_SUFFIX));
        if (files == null || files.length < 1) {
            throw new NullPointerException();
        }

        for (File file : files) {
            try {
                CustomSpellCardEntry entry = loadCustomSpellCard(file);
                CUSTOM_SPELL_CARD_MAP.put(entry.getId(), entry);
            } catch (NullPointerException | IOException | ScriptException e) {
                TouhouLittleMaid.LOGGER.error("Exception while loading spell in {}:", file);
                TouhouLittleMaid.LOGGER.catching(e);
            }
        }

        TouhouLittleMaid.LOGGER.info("Loaded {} Custom Spell Cards", CUSTOM_SPELL_CARD_MAP.size());
    }

    private static void unzipCustomSpellCardFolder() {
        File[] files = CONFIG_SPELL_CARD_FOLDER.toFile().listFiles((dir, name) -> name.endsWith(ACCEPTED_SPELL_CARD_SUFFIX));
        boolean dirNotExist = !Files.isDirectory(CONFIG_SPELL_CARD_FOLDER);
        boolean dirIsEmpty = files == null || files.length < 1;
        boolean shouldCopyDir = dirNotExist || dirIsEmpty;
        if (shouldCopyDir) {
            GetJarResources.copyTouhouLittleMaidFolder(JAR_SPELL_CARD_FOLDER, CONFIG_SPELL_CARD_FOLDER);
        }
    }

    private static CustomSpellCardEntry loadCustomSpellCard(File file) throws IOException, ScriptException {
        Bindings bindings = CommonProxy.NASHORN.createBindings();
        Object scriptObject = CommonProxy.NASHORN.eval(FileUtils.readFileToString(file, StandardCharsets.UTF_8), bindings);
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
        ResourceLocation icon = NULL;
        ResourceLocation snapshot = NULL;

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

