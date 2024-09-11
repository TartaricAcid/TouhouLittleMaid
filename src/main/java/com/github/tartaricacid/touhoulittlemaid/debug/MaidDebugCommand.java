package com.github.tartaricacid.touhoulittlemaid.debug;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public final class MaidDebugCommand {
    private static final String MAID_DEBUG_NAME = "debug";

    private static final String SPAWN_MAID = "spawn_maid";
    private static final String COUNT_NAME = "count";
    private static final String MODEL_ID = "model_id";

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        LiteralArgumentBuilder<CommandSourceStack> debug = Commands.literal(MAID_DEBUG_NAME);
        LiteralArgumentBuilder<CommandSourceStack> spawnMaid = Commands.literal(SPAWN_MAID);
        RequiredArgumentBuilder<CommandSourceStack, String> modelId = Commands.argument(MODEL_ID, StringArgumentType.string());
        RequiredArgumentBuilder<CommandSourceStack, Integer> count = Commands.argument(COUNT_NAME, IntegerArgumentType.integer(0));
        debug.then(spawnMaid.then(modelId.then(count.executes(MaidDebugCommand::spawnMaid))));
        return debug;
    }

    private static int spawnMaid(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String modelId = StringArgumentType.getString(context, MODEL_ID);
        int count = IntegerArgumentType.getInteger(context, COUNT_NAME);
        ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                BlockPos blockPos = serverPlayer.blockPosition();
                int x = blockPos.getX() + i % 10 + 1;
                int z = blockPos.getZ() + i / 10 + 1;
                EntityMaid entityMaid = new EntityMaid(serverPlayer.level);
                entityMaid.setPos(x, blockPos.getY(), z);
                entityMaid.tame(serverPlayer);
                entityMaid.setModelId(modelId);
                entityMaid.setInSittingPose(true);
                serverPlayer.level.addFreshEntity(entityMaid);
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
