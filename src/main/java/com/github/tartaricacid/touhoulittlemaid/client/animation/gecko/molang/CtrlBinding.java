package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang;

import com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies.client.ImmersiveMelodiesCompat;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.binding.ContextBinding;

public class CtrlBinding extends ContextBinding {
    public static final CtrlBinding INSTANCE = new CtrlBinding();

    private CtrlBinding() {
        ImmersiveMelodiesCompat.addBinding(this);
    }
}
