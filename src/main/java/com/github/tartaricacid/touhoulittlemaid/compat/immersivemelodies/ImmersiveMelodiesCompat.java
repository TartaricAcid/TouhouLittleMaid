package com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoBone;
import net.minecraftforge.fml.ModList;

public class ImmersiveMelodiesCompat {
    private static final String IMMERSIVE_MELODIES = "immersive_melodies";
    private static boolean isLoaded = false;

    public static void init() {
        isLoaded = ModList.get().isLoaded(IMMERSIVE_MELODIES);
    }

    public static void setAngles(EntityMaid maid, BedrockPart head, BedrockPart hat, BedrockPart leftArm, BedrockPart rightArm) {
        if (isLoaded) {
            MaidArmsAndHeadAccessor.setAngles(maid, head, hat, leftArm, rightArm);
        }
    }

    public static void setGeckoAngles(EntityMaid maid, AnimatedGeoBone head, AnimatedGeoBone hat, AnimatedGeoBone leftArm, AnimatedGeoBone rightArm) {
        if (isLoaded) {
            GeckoMaidArmsAndHeadAccessor.setAngles(maid, head, hat, leftArm, rightArm);
        }
    }

}
