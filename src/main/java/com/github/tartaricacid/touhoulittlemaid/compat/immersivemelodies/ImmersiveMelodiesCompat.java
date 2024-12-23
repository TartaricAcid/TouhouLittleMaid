package com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoBone;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public class ImmersiveMelodiesCompat {
    private static final String IMMERSIVE_MELODIES = "immersive_melodies";
    private static boolean IS_LOADED = false;

    public static void init() {
        IS_LOADED = ModList.get().isLoaded(IMMERSIVE_MELODIES);
    }

    public static void setAngles(IMaid maid, BedrockPart head, BedrockPart hat, BedrockPart leftArm, BedrockPart rightArm) {
        if (IS_LOADED) {
            MaidArmsAndHeadAccessor.setAngles(maid.asEntity(), head, hat, leftArm, rightArm);
        }
    }

    public static void setGeckoAngles(IMaid maid, @Nullable AnimatedGeoBone head, @Nullable AnimatedGeoBone hat, @Nullable AnimatedGeoBone leftArm, @Nullable AnimatedGeoBone rightArm) {
        if (IS_LOADED) {
            GeckoMaidArmsAndHeadAccessor.setAngles(maid.asEntity(), head, hat, leftArm, rightArm);
        }
    }
}
