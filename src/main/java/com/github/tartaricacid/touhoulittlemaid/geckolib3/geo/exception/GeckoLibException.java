package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.exception;

import net.minecraft.util.ResourceLocation;

public class GeckoLibException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GeckoLibException(ResourceLocation fileLocation, String message) {
        super(fileLocation + ": " + message);
    }

    public GeckoLibException(ResourceLocation fileLocation, String message, Throwable cause) {
        super(fileLocation + ": " + message, cause);
    }
}
