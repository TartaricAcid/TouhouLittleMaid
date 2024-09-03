package com.github.tartaricacid.touhoulittlemaid.command;

import com.github.tartaricacid.touhoulittlemaid.command.subcommand.MaidNumCommand;
import com.github.tartaricacid.touhoulittlemaid.command.subcommand.PackCommand;
import com.github.tartaricacid.touhoulittlemaid.command.subcommand.PowerCommand;
import com.github.tartaricacid.touhoulittlemaid.debug.MaidDebugCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class RootCommand {
    private static final String ROOT_NAME = "tlm";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(ROOT_NAME)
                .requires((source -> source.hasPermission(2)));
        root.then(PackCommand.get());
        root.then(PowerCommand.get());
        root.then(MaidNumCommand.get());

        root.then(MaidDebugCommand.get());

        dispatcher.register(root);
    }
}
