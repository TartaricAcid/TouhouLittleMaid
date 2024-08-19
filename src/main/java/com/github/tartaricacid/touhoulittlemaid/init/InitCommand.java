package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.command.arguments.HandleTypeArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitCommand {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPE = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, TouhouLittleMaid.MOD_ID);

    public static final DeferredHolder<ArgumentTypeInfo<?, ?>,ArgumentTypeInfo<?, ?>> MAID_HANDLE_TYPES = ARGUMENT_TYPE.register("handle_types", () -> ArgumentTypeInfos.registerByClass(HandleTypeArgument.class, SingletonArgumentInfo.contextFree(HandleTypeArgument::type)));
}
