package com.github.tartaricacid.touhoulittlemaid.danmaku;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.danmaku.pojo.CustomSpellCard;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.github.tartaricacid.touhoulittlemaid.util.GetJarResources;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;

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
    private static final Gson GSON = new Gson();
    private static final Map<String, CustomSpellCard> CUSTOM_SPELL_CARD_MAP = CommonProxy.CUSTOM_SPELL_CARD_MAP;
    private static final Path CONFIG_SPELL_CARD_FOLDER = Paths.get(Minecraft.getMinecraft().gameDir.getPath(), "config", TouhouLittleMaid.MOD_ID, "custom_spell_card");
    private static final String JAR_SPELL_CARD_FOLDER = "/assets/touhou_little_maid/custom_spell_card";
    private static final String ACCEPTED_SPELL_CARD_SUFFIX = ".json";

    public static void onCustomSpellCardReload() {
        CUSTOM_SPELL_CARD_MAP.clear();
        unzipCustomSpellCardFolder();
        File[] files = CONFIG_SPELL_CARD_FOLDER.toFile().listFiles((dir, name) -> name.endsWith(ACCEPTED_SPELL_CARD_SUFFIX));
        if (files == null || files.length < 1) {
            throw new NullPointerException();
        }
        for (File file : files) {
            try {
                int endIndex = file.getName().length() - ACCEPTED_SPELL_CARD_SUFFIX.length();
                String id = file.getName().substring(0, endIndex);
                CUSTOM_SPELL_CARD_MAP.put(id, loadCustomSpellCard(file));
            } catch (IOException e) {
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

    private static CustomSpellCard loadCustomSpellCard(File file) throws IOException {
        return GSON.fromJson(FileUtils.readFileToString(file, StandardCharsets.UTF_8), CustomSpellCard.class);
    }
}
