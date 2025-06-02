package com.github.tartaricacid.touhoulittlemaid.command.subcommand;

import com.github.tartaricacid.touhoulittlemaid.command.arguments.HandleTypeArgument;
import com.github.tartaricacid.touhoulittlemaid.data.ChatTokensAttachment;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

public final class ChatTokensCommand {
    private static final String TOKENS = "tokens";
    private static final String GET_NAME = "get";
    private static final String HANDLE_NAME = "handle";
    private static final String TARGETS_NAME = "targets";
    private static final String COUNT_NAME = "count";

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        LiteralArgumentBuilder<CommandSourceStack> pack = Commands.literal(TOKENS);
        RequiredArgumentBuilder<CommandSourceStack, EntitySelector> targets = Commands.argument(TARGETS_NAME, EntityArgument.players());
        RequiredArgumentBuilder<CommandSourceStack, Integer> count = Commands.argument(COUNT_NAME, IntegerArgumentType.integer(0));
        RequiredArgumentBuilder<CommandSourceStack, String> handleType = Commands.argument(HANDLE_NAME, HandleTypeArgument.type());

        pack.then(Commands.literal(GET_NAME).then(targets.executes(ChatTokensCommand::getTokens)));
        pack.then(handleType.then(targets.then(count.executes(ChatTokensCommand::handleTokens))));
        return pack;
    }

    private static int handleTokens(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(context, TARGETS_NAME);
        int count = IntegerArgumentType.getInteger(context, COUNT_NAME);
        String type = HandleTypeArgument.getType(context, HANDLE_NAME);
        for (Player player : players) {
            switch (type) {
                case "set":
                    player.getData(InitDataAttachment.CHAT_TOKENS).set(count);
                    break;
                case "add":
                    player.getData(InitDataAttachment.CHAT_TOKENS).add(count);
                    break;
                case "min":
                    player.getData(InitDataAttachment.CHAT_TOKENS).min(count);
                    break;
                default:
            }
        }
        context.getSource().sendSuccess(() -> Component.translatable("commands.touhou_little_maid.chat_tokens.handle.info", players.size()), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int getTokens(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        for (Player player : EntityArgument.getPlayers(context, TARGETS_NAME)) {
            ChatTokensAttachment tokens = player.getData(InitDataAttachment.CHAT_TOKENS);
            context.getSource().sendSuccess(() -> Component.translatable("commands.touhou_little_maid.chat_tokens.get.info",
                    player.getScoreboardName(), tokens.get()), false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
