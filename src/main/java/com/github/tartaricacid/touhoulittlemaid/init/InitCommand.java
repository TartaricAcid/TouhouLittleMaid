package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.command.arguments.HandleTypeArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitCommand {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPE = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, TouhouLittleMaid.MOD_ID);

    public static final RegistryObject<ArgumentTypeInfo<?, ?>> MAID_HANDLE_TYPES = ARGUMENT_TYPE.register("handle_types", () -> ArgumentTypeInfos.registerByClass(HandleTypeArgument.class, SingletonArgumentInfo.contextFree(HandleTypeArgument::type)));
}