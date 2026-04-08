package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.record;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class WikiRecord {
    private static final int MAX_TEXT_LENGTH = 4096;

    public record QueryTitleResult(@SerializedName("query") QueryTitle query) {
        public String getFirstTitle() {
            if (query == null || query.search == null || query.search.length == 0) {
                return StringUtils.EMPTY;
            }
            return query.search[0].title;
        }
    }

    public record PageExtractResult(@SerializedName("query") QueryPages query) {
        public String getFirstExtract() {
            if (query == null || query.pages == null || query.pages.isEmpty()) {
                return StringUtils.EMPTY;
            }
            return query.pages.values().stream()
                    .findFirst()
                    .map(PageEntry::extract)
                    .filter(StringUtils::isNotBlank)
                    .map(extract -> StringUtils.abbreviate(extract, MAX_TEXT_LENGTH))
                    .orElse(StringUtils.EMPTY);
        }
    }

    private record QueryTitle(@SerializedName("search") SearchEntry[] search) {
    }

    private record SearchEntry(@SerializedName("title") String title) {
    }

    private record QueryPages(@SerializedName("pages") Map<String, PageEntry> pages) {
    }

    private record PageEntry(@SerializedName("extract") String extract) {
    }
}
