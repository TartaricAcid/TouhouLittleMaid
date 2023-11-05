package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.google.gson.annotations.SerializedName;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@OnlyIn(Dist.CLIENT)
public class InGameMaidConfig {
    private static final File CONFIG_FILE = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("touhou_little_maid").resolve("in_game_maid_config.json").toFile();
    public static InGameMaidConfig INSTANCE;

    @SerializedName("sound_frequency")
    private double soundFrequency = 1.0;

    @SerializedName("show_chat_bubble")
    private boolean showChatBubble = true;

    @SerializedName("show_backpack")
    private boolean showBackpack = true;

    public double getSoundFrequency() {
        return soundFrequency;
    }

    public void setSoundFrequency(double soundFrequency) {
        this.soundFrequency = soundFrequency;
    }

    public boolean isShowChatBubble() {
        return showChatBubble;
    }

    public void setShowChatBubble(boolean showChatBubble) {
        this.showChatBubble = showChatBubble;
    }

    public boolean isShowBackpack() {
        return showBackpack;
    }

    public void setShowBackpack(boolean showBackpack) {
        this.showBackpack = showBackpack;
    }

    public static void read() {
        if (CONFIG_FILE.isFile()) {
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(CONFIG_FILE.toPath()), StandardCharsets.UTF_8)) {
                INSTANCE = CustomPackLoader.GSON.fromJson(reader, InGameMaidConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            INSTANCE = new InGameMaidConfig();
        }
    }

    public static void save() {
        try {
            FileUtils.write(CONFIG_FILE, CustomPackLoader.GSON.toJson(INSTANCE), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
