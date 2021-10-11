package com.github.tartaricacid.touhoulittlemaid.command.subcommand;

import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.command.arguments.HandleTypeArgument;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public final class MaidNumCommand {
    private static final String MAID_NUM_NAME = "maid_num";
    private static final String GET_NAME = "get";
    private static final String HANDLE_NAME = "handle";
    private static final String TARGETS_NAME = "targets";
    private static final String COUNT_NAME = "count";

    public static LiteralArgumentBuilder<CommandSource> get() {
        LiteralArgumentBuilder<CommandSource> pack = Commands.literal(MAID_NUM_NAME);
        RequiredArgumentBuilder<CommandSource, EntitySelector> targets = Commands.argument(TARGETS_NAME, EntityArgument.players());
        RequiredArgumentBuilder<CommandSource, Integer> count = Commands.argument(COUNT_NAME, IntegerArgumentType.integer(0));
        RequiredArgumentBuilder<CommandSource, String> handleType = Commands.argument(HANDLE_NAME, HandleTypeArgument.type());

        pack.then(Commands.literal(GET_NAME).then(targets.executes(MaidNumCommand::getMaidNum)));
        pack.then(handleType.then(targets.then(count.executes(MaidNumCommand::handleMaidNum))));
        return pack;
    }

    private static int handleMaidNum(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgument.getPlayers(context, TARGETS_NAME);
        int count = IntegerArgumentType.getInteger(context, COUNT_NAME);
        String type = HandleTypeArgument.getType(context, HANDLE_NAME);
        for (PlayerEntity player : players) {
            switch (type) {
                case "set":
                    player.getCapability(MaidNumCapabilityProvider.MAID_NUM_CAP, null).ifPresent(power -> power.set(count));
                    break;
                case "add":
                    player.getCapability(MaidNumCapabilityProvider.MAID_NUM_CAP, null).ifPresent(power -> power.add(count));
                    break;
                case "min":
                    player.getCapability(MaidNumCapabilityProvider.MAID_NUM_CAP, null).ifPresent(power -> power.min(count));
                    break;
                default:
            }
        }
        context.getSource().sendSuccess(new TranslationTextComponent("commands.touhou_little_maid.maid_num.handle.info", players.size()), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int getMaidNum(CommandContext<CommandSource> context) throws CommandSyntaxException {
        for (PlayerEntity player : EntityArgument.getPlayers(context, TARGETS_NAME)) {
            player.getCapability(MaidNumCapabilityProvider.MAID_NUM_CAP, null).ifPresent(maidNum ->
                    context.getSource().sendSuccess(new TranslationTextComponent("commands.touhou_little_maid.maid_num.get.info",
                            player.getScoreboardName(), maidNum.get()), false));
        }
        return Command.SINGLE_SUCCESS;
    }
}
