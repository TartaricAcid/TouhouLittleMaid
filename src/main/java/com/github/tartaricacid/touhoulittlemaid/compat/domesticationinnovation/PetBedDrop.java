package com.github.tartaricacid.touhoulittlemaid.compat.domesticationinnovation;

import com.github.alexthe668.domesticationinnovation.server.entity.TameableUtils;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.fml.ModList;

public class PetBedDrop {
    private static final String DOMESTICATION_INNOVATION = "domesticationinnovation";

    public static boolean hasPetBedPos(EntityMaid maid) {
        if (ModList.get().isLoaded(DOMESTICATION_INNOVATION)) {
            return TameableUtils.isTamed(maid) && TameableUtils.getPetBedPos(maid) != null;
        }
        return false;
    }
}
