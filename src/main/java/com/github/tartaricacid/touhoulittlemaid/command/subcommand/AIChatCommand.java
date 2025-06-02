package com.github.tartaricacid.touhoulittlemaid.command.subcommand;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class AIChatCommand {
    private static final String ROOT_NAME = "ai_chat";
    private static final String RELOAD_NAME = "reload";

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        LiteralArgumentBuilder<CommandSourceStack> root = LiteralArgumentBuilder.literal(ROOT_NAME);
        LiteralArgumentBuilder<CommandSourceStack> reload = LiteralArgumentBuilder.literal(RELOAD_NAME);
        LiteralArgumentBuilder<CommandSourceStack> tokens = ChatTokensCommand.get();
        root.then(reload.executes(AIChatCommand::reload));
        root.then(tokens);
        return root;
    }

    private static int reload(CommandContext<CommandSourceStack> context) {
        AvailableSites.init();
        context.getSource().sendSuccess(() -> Component.translatable("message.touhou_little_maid.ai_chat.reload_success"), true);
        return Command.SINGLE_SUCCESS;
    }
}
