package com.github.tartaricacid.touhoulittlemaid.command.subcommand;

import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.command.arguments.HandleTypeArgument;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
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

public final class PowerCommand {
    private static final String POWER_NAME = "power";
    private static final String GET_NAME = "get";
    private static final String HANDLE_NAME = "handle";
    private static final String TARGETS_NAME = "targets";
    private static final String COUNT_NAME = "count";

    public static LiteralArgumentBuilder<CommandSource> get() {
        LiteralArgumentBuilder<CommandSource> pack = Commands.literal(POWER_NAME);
        RequiredArgumentBuilder<CommandSource, EntitySelector> targets = Commands.argument(TARGETS_NAME, EntityArgument.players());
        RequiredArgumentBuilder<CommandSource, Float> count = Commands.argument(COUNT_NAME, FloatArgumentType.floatArg(0, PowerCapability.MAX_POWER));
        RequiredArgumentBuilder<CommandSource, String> handleType = Commands.argument(HANDLE_NAME, HandleTypeArgument.type());

        pack.then(Commands.literal(GET_NAME).then(targets.executes(PowerCommand::getPower)));
        pack.then(handleType.then(targets.then(count.executes(PowerCommand::handlePower))));
        return pack;
    }

    private static int handlePower(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgument.getPlayers(context, TARGETS_NAME);
        float count = FloatArgumentType.getFloat(context, COUNT_NAME);
        String type = HandleTypeArgument.getType(context, HANDLE_NAME);
        for (PlayerEntity player : players) {
            switch (type) {
                case "set":
                    player.getCapability(PowerCapabilityProvider.POWER_CAP, null).ifPresent(power -> power.set(count));
                    break;
                case "add":
                    player.getCapability(PowerCapabilityProvider.POWER_CAP, null).ifPresent(power -> power.add(count));
                    break;
                case "min":
                    player.getCapability(PowerCapabilityProvider.POWER_CAP, null).ifPresent(power -> power.min(count));
                    break;
                default:
            }
        }
        context.getSource().sendSuccess(new TranslationTextComponent("commands.touhou_little_maid.power.handle.info", players.size()), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int getPower(CommandContext<CommandSource> context) throws CommandSyntaxException {
        for (PlayerEntity player : EntityArgument.getPlayers(context, TARGETS_NAME)) {
            player.getCapability(PowerCapabilityProvider.POWER_CAP, null).ifPresent(power ->
                    context.getSource().sendSuccess(new TranslationTextComponent("commands.touhou_little_maid.power.get.info",
                            player.getScoreboardName(), power.get()), false));
        }
        return Command.SINGLE_SUCCESS;
    }
}
