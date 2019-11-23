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

/**
 * @author TartaricAcid
 * @date 2019/11/16 9:55
 **/
public final class CustomSpellCardManger {
    private static final Map<String, CustomSpellCardEntry> CUSTOM_SPELL_CARD_MAP = CommonProxy.CUSTOM_SPELL_CARD_MAP;
    private static final Path CONFIG_SPELL_CARD_FOLDER = Paths.get("config", TouhouLittleMaid.MOD_ID, "custom_spell_card");
    private static final String JAR_SPELL_CARD_FOLDER = "/assets/touhou_little_maid/custom_spell_card";
    private static final String ACCEPTED_SPELL_CARD_SUFFIX = ".js";

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
            } catch (IOException | ScriptException e) {
                TouhouLittleMaid.LOGGER.error(e);
                return;
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
        String script = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        Bindings bindings = CommonProxy.NASHORN.createBindings();
        CommonProxy.NASHORN.eval(script, bindings);
        String id = (String) bindings.get(Args.ID.getName());
        String nameKey;
        String descriptionKey = "";
        String author = "";
        String version = "";
        int cooldown = 60;
        ResourceLocation icon = null;
        ResourceLocation snapshot = null;

        if (bindings.containsKey(Args.NAME_KEY.getName())) {
            nameKey = (String) bindings.get(Args.NAME_KEY.getName());
        } else {
            nameKey = String.format("spell_card.%s.name", id);
        }
        if (bindings.containsKey(Args.DESC_KEY.getName())) {
            descriptionKey = (String) bindings.get(Args.DESC_KEY.getName());
        }
        if (bindings.containsKey(Args.AUTHOR.getName())) {
            author = (String) bindings.get(Args.AUTHOR.getName());
        }
        if (bindings.containsKey(Args.VERSION.getName())) {
            version = (String) bindings.get(Args.VERSION.getName());
        }
        if (bindings.containsKey(Args.COOLDOWN.getName())) {
            cooldown = (int) bindings.get(Args.COOLDOWN.getName());
        }
        if (bindings.containsKey(Args.ICON.getName())) {
            icon = new ResourceLocation((String) bindings.get(Args.ICON.getName()));
        }
        if (bindings.containsKey(Args.SNAPSHOT.getName())) {
            snapshot = new ResourceLocation((String) bindings.get(Args.SNAPSHOT.getName()));
        }
        return new CustomSpellCardEntry(id, nameKey, descriptionKey, author, version, script, cooldown, icon, snapshot);
    }

    enum Args {
        // 必要的脚本参数
        ID("id"),
        NAME_KEY("name_key"),
        DESC_KEY("desc_Key"),
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

