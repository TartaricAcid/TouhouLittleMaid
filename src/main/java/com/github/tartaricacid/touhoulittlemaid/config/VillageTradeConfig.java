package com.github.tartaricacid.touhoulittlemaid.config;

import com.github.tartaricacid.touhoulittlemaid.config.pojo.VillageTradePOJO;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/8/7 18:05
 **/
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class VillageTradeConfig {
    /**
     * 预生成对应的配置文件和文件夹，同时进行文件内容读取
     */
    public static List<VillageTradePOJO> getPOJO(InputStream configStream) throws IOException {
        File root = new File(".");
        File configDir = new File(root.toString() + File.separatorChar + "config" + File.separatorChar + "touhou_little_maid");
        File villageTradeFile = new File(configDir.getPath() + File.separatorChar + "village_trade.json");
        // 检查配置文件夹是否存在
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        // 创建配置文件
        if (!villageTradeFile.exists()) {
            FileOutputStream fileOutputStream = new FileOutputStream(villageTradeFile);
            IOUtils.write(IOUtils.toString(configStream, StandardCharsets.UTF_8), fileOutputStream, StandardCharsets.UTF_8);
            IOUtils.closeQuietly(fileOutputStream);
        }
        // 读取配置文件，进行解析
        return CommonProxy.GSON.fromJson(new InputStreamReader(new FileInputStream(villageTradeFile), StandardCharsets.UTF_8),
                new TypeToken<List<VillageTradePOJO>>() {
                }.getType());
    }
}
