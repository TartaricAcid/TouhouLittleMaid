package com.github.tartaricacid.touhoulittlemaid.draw;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.github.tartaricacid.touhoulittlemaid.draw.Level.*;

public final class DrawManger {
    private static final String DEFAULT_DRAW_CSV_FILE = "assets/touhou_little_maid/draw.csv";
    private static final HashMap<String, CardPoolEntry> CARD_POOL_ENTRY_MAP = Maps.newHashMap();
    private static Random RANDOM = new Random();

    static {
        registerCardPool(N.getName(), 50);
        registerCardPool(R.getName(), 30);
        registerCardPool(SR.getName(), 20);
        registerCardPool(SSR.getName(), 10);
        registerCardPool(UR.getName(), 5);
    }

    public static void readDrawCsvFile(Object clz) {
        clearAllCard();
        try {
            InputStream input;
            // 读取指定文件
            input = clz.getClass().getClassLoader().getResourceAsStream(DEFAULT_DRAW_CSV_FILE);
            if (input != null) {
                List<String> lines = IOUtils.readLines(input, StandardCharsets.UTF_8);
                for (String line : lines) {
                    String[] data = line.split(",");
                    if (data.length < 3) {
                        throw new IOException();
                    } else {
                        String cardPool = StringUtils.deleteWhitespace(data[2]);
                        CardPoolEntry entry = CARD_POOL_ENTRY_MAP.get(cardPool);
                        if (entry != null) {
                            String modelId = StringUtils.deleteWhitespace(data[0]);
                            int weight = Integer.parseInt(StringUtils.deleteWhitespace(data[1]));
                            entry.addCard(modelId, weight);
                        }
                    }
                }
            }
            // 别忘了关闭输入流
            IOUtils.closeQuietly(input);
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.warn("Fail to read csv file");
        }
    }

    public CardPoolEntry getEntry(Level level) {
        return CARD_POOL_ENTRY_MAP.get(level.getName());
    }

    private static void clearAllCard() {
        for (CardPoolEntry entry : CARD_POOL_ENTRY_MAP.values()) {
            entry.clearCard();
        }
    }

    private static void registerCardPool(String name, int baseWeight) {
        CARD_POOL_ENTRY_MAP.put(name, CardPoolEntry.createEntry(name, baseWeight));
    }
}
