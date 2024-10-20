package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class LanguageGenerator extends LanguageProvider {
    private static final List<MutableComponent> DATA = Lists.newArrayList();

    public LanguageGenerator(PackOutput output) {
        super(output, TouhouLittleMaid.MOD_ID, "en_us");
    }

    public static void addLanguage(MutableComponent component) {
        DATA.add(component);
    }

    @Override
    protected void addTranslations() {
        DATA.forEach(component -> {
            if (component.getContents() instanceof TranslatableContents translatableContents) {
                String key = translatableContents.getKey();
                add(key, "");
            }
        });
    }
}
