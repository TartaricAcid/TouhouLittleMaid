package com.github.tartaricacid.simplebedrockmodel.client.manager;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.AbstractBedrockEntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;

import java.io.InputStream;
import java.util.function.Function;

public class BedrockEntityModelRegisterEvent<T extends AbstractBedrockEntityModel<? extends Entity>> extends Event implements IModBusEvent, ICancellableEvent {
    private final BedrockEntityModelSet<T> modelSet;

    public BedrockEntityModelRegisterEvent(BedrockEntityModelSet<T> modelSet) {
        this.modelSet = modelSet;
    }

    public void register(ResourceLocation location, Function<InputStream, T> function) {
        this.modelSet.addModel(location, function);
    }
}
