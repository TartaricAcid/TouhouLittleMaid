package com.github.tartaricacid.touhoulittlemaid.geckolib3.model.provider;


import net.minecraft.resources.ResourceLocation;

public interface IAnimatableModelProvider<E> {
    ResourceLocation getAnimationFileLocation(E animatable);
}