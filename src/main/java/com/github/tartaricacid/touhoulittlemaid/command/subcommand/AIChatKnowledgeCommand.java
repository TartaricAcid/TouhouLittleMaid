package com.github.tartaricacid.touhoulittlemaid.command.subcommand;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.knowledge.KnowledgeRegister;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public final class AIChatKnowledgeCommand {
    private static final String ROOT_NAME = "knowledge";

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(ROOT_NAME);
        root.executes(AIChatKnowledgeCommand::listKnowledge);
        return root;
    }

    private static int listKnowledge(CommandContext<CommandSourceStack> context) {
        if (KnowledgeRegister.getAllKnowledge().isEmpty()) {
            context.getSource().sendSuccess(() -> Component.translatable("commands.touhou_little_maid.ai_chat.knowledge.empty"), false);
            return Command.SINGLE_SUCCESS;
        }

        context.getSource().sendSuccess(() -> Component.translatable("commands.touhou_little_maid.ai_chat.knowledge.list.header"), false);
        KnowledgeRegister.getAllKnowledge().values().forEach(knowledge ->
                context.getSource().sendSuccess(() -> Component.literal("- %s: %s".formatted(knowledge.getId(), knowledge.getDesc())), false));
        return Command.SINGLE_SUCCESS;
    }
}
