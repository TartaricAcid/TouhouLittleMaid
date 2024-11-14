package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.binding.ContextBinding;

public class TLMBinding extends ContextBinding {
    public static final TLMBinding INSTANCE = new TLMBinding();

    private TLMBinding() {
        maidEntityVar("is_begging", ctx -> ctx.entity().isBegging());
        maidEntityVar("is_sitting", ctx -> ctx.entity().isMaidInSittingPose());
        maidEntityVar("has_backpack", ctx -> ctx.entity().hasBackpack());
    }
}