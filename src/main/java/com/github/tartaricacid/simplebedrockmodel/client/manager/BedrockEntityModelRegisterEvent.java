package com.github.tartaricacid.simplebedrockmodel.client.manager;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.AbstractBedrockEntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

import java.io.InputStream;
import java.util.function.Function;

public class BedrockEntityModelRegisterEvent<T extends AbstractBedrockEntityModel<? extends Entity>> extends Event implements IModBusEvent {
    private final BedrockEntityModelSet<T> modelSet;

    public BedrockEntityModelRegisterEvent(BedrockEntityModelSet<T> modelSet) {
        this.modelSet = modelSet;
    }

    public void register(ResourceLocation location, Function<InputStream, T> function) {
        this.modelSet.addModel(location, function);
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}
