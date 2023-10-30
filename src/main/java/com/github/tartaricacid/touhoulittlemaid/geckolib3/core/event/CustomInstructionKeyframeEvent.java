/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationController;

public class CustomInstructionKeyframeEvent<T> extends KeyframeEvent<T> {
    public final String instructions;

    /**
     * KeyframeEvent 所需的所有数据
     *
     * @param instructions 指令列表，在 blockbench 中，自定义指令框中的每一行都是单独的指令
     */
    @SuppressWarnings("rawtypes")
    public CustomInstructionKeyframeEvent(T entity, double animationTick, String instructions, AnimationController controller) {
        super(entity, animationTick, controller);
        this.instructions = instructions;
    }
}