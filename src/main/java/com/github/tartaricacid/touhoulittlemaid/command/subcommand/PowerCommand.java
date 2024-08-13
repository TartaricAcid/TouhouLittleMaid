package com.github.tartaricacid.touhoulittlemaid.command.subcommand;

import com.github.tartaricacid.touhoulittlemaid.data.MaidNumAttachment;
import com.github.tartaricacid.touhoulittlemaid.data.PowerAttachment;
import com.github.tartaricacid.touhoulittlemaid.command.arguments.HandleTypeArgument;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment;
import com.github.tartaricacid.touhoulittlemaid.network.pack.SyncDataPackage;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
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
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Collection;

public final class PowerCommand {
    private static final String POWER_NAME = "power";
    private static final String GET_NAME = "get";
    private static final String HANDLE_NAME = "handle";
    private static final String TARGETS_NAME = "targets";
    private static final String COUNT_NAME = "count";

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        LiteralArgumentBuilder<CommandSourceStack> pack = Commands.literal(POWER_NAME);
        RequiredArgumentBuilder<CommandSourceStack, EntitySelector> targets = Commands.argument(TARGETS_NAME, EntityArgument.players());
        RequiredArgumentBuilder<CommandSourceStack, Float> count = Commands.argument(COUNT_NAME, FloatArgumentType.floatArg(0, PowerAttachment.MAX_POWER));
        RequiredArgumentBuilder<CommandSourceStack, String> handleType = Commands.argument(HANDLE_NAME, HandleTypeArgument.type());

        pack.then(Commands.literal(GET_NAME).then(targets.executes(PowerCommand::getPower)));
        pack.then(handleType.then(targets.then(count.executes(PowerCommand::handlePower))));
        return pack;
    }

    private static int handlePower(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(context, TARGETS_NAME);
        float count = FloatArgumentType.getFloat(context, COUNT_NAME);
        String type = HandleTypeArgument.getType(context, HANDLE_NAME);
        for (ServerPlayer player : players) {
            PowerAttachment powerAttachment = player.getData(InitDataAttachment.POWER_NUM);
            MaidNumAttachment maidNumAttachment = player.getData(InitDataAttachment.MAID_NUM);
            switch (type) {
                case "set":
                    powerAttachment.set(count);
                    break;
                case "add":
                    powerAttachment.add(count);
                    break;
                case "min":
                    powerAttachment.min(count);
                    break;
                default:
            }
            player.setData(InitDataAttachment.POWER_NUM, new PowerAttachment(powerAttachment.get()));
            PacketDistributor.sendToPlayer(player,new SyncDataPackage(powerAttachment.get(),maidNumAttachment.get()));
        }
        context.getSource().sendSuccess(() -> Component.translatable("commands.touhou_little_maid.power.handle.info", players.size()), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int getPower(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        for (Player player : EntityArgument.getPlayers(context, TARGETS_NAME)) {
            PowerAttachment power = player.getData(InitDataAttachment.POWER_NUM);
            context.getSource().sendSuccess(() -> Component.translatable("commands.touhou_little_maid.power.get.info",
                    player.getScoreboardName(), power.get()), false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
