package com.github.tartaricacid.touhoulittlemaid.ai.manager.setting;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.bean.MetaData;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.bean.Setting;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.PapiReplacer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class CharacterSetting {
    private static final Yaml YAML = new Yaml(new Constructor(Setting.class, new LoaderOptions()));
    private static final String COMMENTS = "#";

    private final MetaData data;
    private final String rawSetting;

    public CharacterSetting(MetaData data, String rawSetting) {
        this.data = data;
        this.rawSetting = rawSetting;
    }

    public CharacterSetting(File settingFile) throws Exception {
        try (FileReader reader = new FileReader(settingFile, StandardCharsets.UTF_8)) {
            Setting setting = YAML.load(reader);
            if (setting == null) {
                throw new IOException(settingFile.getAbsolutePath() + " is not a valid setting");
            }
            this.data = setting.getMeta();
            this.rawSetting = processText(setting.getSetting());
        }
    }

    public CharacterSetting(InputStream stream) throws Exception {
        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            Setting setting = YAML.load(reader);
            if (setting == null) {
                throw new IOException("InputStream is not a valid setting");
            }
            this.data = setting.getMeta();
            this.rawSetting = processText(setting.getSetting());
        }
    }

    public void save(File settingFile) throws IOException {
        try (FileWriter writer = new FileWriter(settingFile, StandardCharsets.UTF_8)) {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setExplicitStart(false);
            options.setExplicitEnd(false);
            options.setIndent(2);
            Yaml yaml = new Yaml(options);
            Setting setting = new Setting(data, rawSetting);
            writer.write(yaml.dumpAs(setting, Tag.MAP, null));
        }
    }

    private String processText(String text) {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(StringUtils.split(text, "\n"))
                .map(StringUtils::trim)
                .filter(s -> !s.startsWith(COMMENTS))
                .filter(StringUtils::isNotEmpty).toList()
                .forEach(value -> builder.append(value).append("\n"));
        return builder.toString();
    }

    public String getSetting(EntityMaid maid, String language) {
        return PapiReplacer.replace(rawSetting, maid, language);
    }

    public String getAuthor() {
        return this.data.getAuthor();
    }

    public List<String> getModelId() {
        return this.data.getModelId();
    }
}