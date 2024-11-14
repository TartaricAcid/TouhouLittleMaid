package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.functions;

import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.struct.Vec3fStruct;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBone;
import org.jetbrains.annotations.NotNull;

public final class BonePosition extends BoneParamFunction {
    @Override
    protected Vec3fStruct getParam(@NotNull IBone bone) {
        return new BonePositionStruct(bone);
    }

    private static final class BonePositionStruct extends Vec3fStruct {
        private final IBone bone;

        public BonePositionStruct(IBone bone) {
            this.bone = bone;
        }

        @Override
        protected float getX() {
            return bone.getPositionX();
        }

        @Override
        protected float getY() {
            return bone.getPositionY();
        }

        @Override
        protected float getZ() {
            return bone.getPositionZ();
        }
    }
}
