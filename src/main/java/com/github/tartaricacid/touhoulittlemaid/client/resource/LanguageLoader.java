package com.github.tartaricacid.touhoulittlemaid.client.resource;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LanguageLoader {
    private static final String COMMENT_SYMBOL = "#";
    private static final Pattern LANG_PATTERN = Pattern.compile("^assets/[\\w.]+/lang/(\\w{2}_\\w{2})\\.lang$");
    private static final Map<String, Map<String, String>> LANG_CACHE = Maps.newHashMap();
    private static final String REMOVE_KEY = "subtitle.touhou_little_maid.other.credit";

    public static void clear() {
        LANG_CACHE.clear();
    }

    public static Map<String, String> getLanguages(String code) {
        return LANG_CACHE.get(code);
    }

    public static void loadDownloadInfoLanguages() {
        InfoGetManager.DOWNLOAD_INFO_LIST_ALL.forEach(info -> LanguageLoader.getLanguageMap(info.getLanguages()));
    }

    public static void readLanguageFile(ZipFile zipFile, String filePath) throws IOException {
        Matcher matcher = LANG_PATTERN.matcher(filePath);
        if (matcher.find()) {
            String languageCode = matcher.group(1);
            LANG_CACHE.putIfAbsent(languageCode, Maps.newHashMap());
            getLanguageMap(zipFile, LANG_CACHE.get(languageCode), filePath);
        }
    }

    public static void readLanguageFile(Path rootPath, String namespace) throws IOException {
        File[] languageFiles = rootPath.resolve("assets").resolve(namespace).resolve("lang").toFile().listFiles((dir, name) -> true);
        if (languageFiles == null) {
            return;
        }
        for (File file : languageFiles) {
            String languageCode = FilenameUtils.getBaseName(file.getName());
            LANG_CACHE.putIfAbsent(languageCode, Maps.newHashMap());
            getLanguageMap(file, LANG_CACHE.get(languageCode));
        }
    }

    public static void getLanguageMap(HashMap<String, HashMap<String, String>> langMap) {
        for (String languageCode : langMap.keySet()) {
            LANG_CACHE.putIfAbsent(languageCode, Maps.newHashMap());
            Map<String, String> languages = LANG_CACHE.get(languageCode);
            languages.putAll(langMap.get(languageCode));
        }
    }

    private static void getLanguageMap(ZipFile zipFile, Map<String, String> langData, String filePath) throws IOException {
        ZipEntry entry = zipFile.getEntry(filePath);
        if (entry == null) {
            return;
        }
        try (InputStream stream = zipFile.getInputStream(entry)) {
            readLanguages(langData, stream);
        } catch (IOException ioe) {
            TouhouLittleMaid.LOGGER.warn("Failed to load language file: {}", filePath);
            ioe.printStackTrace();
        }
    }

    private static void getLanguageMap(File languageFile, Map<String, String> langData) throws IOException {
        if (!languageFile.isFile()) {
            return;
        }
        try (InputStream stream = Files.newInputStream(languageFile.toPath())) {
            readLanguages(langData, stream);
        }
    }

    private static void readLanguages(Map<String, String> langData, InputStream stream) throws IOException {
        List<String> lines = IOUtils.readLines(stream, StandardCharsets.UTF_8);
        for (String str : lines) {
            if (str.startsWith(COMMENT_SYMBOL)) {
                continue;
            }
            String[] map = str.split("=", 2);
            if (map.length != 2) {
                continue;
            }
            String key = map[0];
            if (REMOVE_KEY.equals(key)) {
                continue;
            }
            langData.put(key, map[1]);
        }
    }
}
