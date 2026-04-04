package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout;

import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor.TTSSiteEditorScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.MODELS_NAME;

/**
 * TTS 站点编辑器的布局策略基类
 */
public abstract class TTSSiteFormLayout {
    protected final TTSSite sourceSite;

    protected TTSSiteFormLayout(TTSSite sourceSite) {
        this.sourceSite = sourceSite;
    }

    public abstract List<FieldDescriptor> getFieldDescriptors();

    @Nullable
    public abstract TTSSite buildSite(Function<String, String> fieldValues, Map<String, String> models, Consumer<Component> showStatus);

    /**
     * 额外添加按钮等组件
     *
     * @param x      按钮起始 x 坐标
     * @param y      按钮起始 y 坐标
     * @param width  可用宽度（不包含左右边距）
     * @param screen 当前编辑界面实例
     * @return 该组件整体占用的高度，用于后续其他组件调整自身 Y 值
     */
    public int extraInit(int x, int y, int width, TTSSiteEditorScreen screen) {
        return 0;
    }

    public Map<String, String> getInitialModels() {
        return Map.of();
    }

    public boolean supportsModelRows() {
        return false;
    }

    public MutableComponent modelsTitle() {
        return MODELS_NAME;
    }
}
