package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder;


import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Locale;

public interface ILoopType {
    /**
     * 从动画文件读取播放类型
     *
     * @param json json 文件
     * @return 播放类型
     */
    static ILoopType fromJson(JsonElement json) {
        if (json == null || !json.isJsonPrimitive()) {
            return EDefaultLoopTypes.PLAY_ONCE;
        }
        JsonPrimitive primitive = json.getAsJsonPrimitive();
        if (primitive.isBoolean()) {
            return primitive.getAsBoolean() ? EDefaultLoopTypes.LOOP : EDefaultLoopTypes.PLAY_ONCE;
        }
        if (primitive.isString()) {
            String string = primitive.getAsString();
            if ("false".equalsIgnoreCase(string)) {
                return EDefaultLoopTypes.PLAY_ONCE;
            }
            if ("true".equalsIgnoreCase(string)) {
                return EDefaultLoopTypes.LOOP;
            }
            try {
                return EDefaultLoopTypes.valueOf(string.toUpperCase(Locale.ROOT));
            } catch (Exception ignore) {
            }
        }
        return EDefaultLoopTypes.PLAY_ONCE;
    }

    /**
     * 是否在动画结束后重复
     *
     * @return 是否在动画结束后重复
     */
    boolean isRepeatingAfterEnd();

    enum EDefaultLoopTypes implements ILoopType {
        /**
         * 动画播放类型
         */
        LOOP(true),
        PLAY_ONCE,
        HOLD_ON_LAST_FRAME;

        private final boolean looping;

        EDefaultLoopTypes(boolean looping) {
            this.looping = looping;
        }

        EDefaultLoopTypes() {
            this(false);
        }

        @Override
        public boolean isRepeatingAfterEnd() {
            return this.looping;
        }
    }
}
