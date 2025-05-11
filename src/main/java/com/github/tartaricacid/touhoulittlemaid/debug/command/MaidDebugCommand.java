package com.github.tartaricacid.touhoulittlemaid.debug.command;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;

@VisibleForDebug
public final class MaidDebugCommand {
    private static final String MAID_DEBUG_NAME = "debug";

    private static final String ENABLE = "enable";
    private static final String VALUE = "value";

    private static final String SPAWN_MAID = "spawn_maid";
    private static final String COUNT_NAME = "count";
    private static final String MODEL_ID = "model_id";

    private static final String SET_FARM = "set_farm";
    private static final String SIZE = "size";

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        LiteralArgumentBuilder<CommandSourceStack> debug = Commands.literal(MAID_DEBUG_NAME);

        // 开启调试渲染功能
        LiteralArgumentBuilder<CommandSourceStack> enable = Commands.literal(ENABLE);
        RequiredArgumentBuilder<CommandSourceStack, Boolean> value = Commands.argument(VALUE, BoolArgumentType.bool());
        debug.then(enable.then(value.executes(MaidDebugCommand::enableDebug)));

        // 批量生成女仆
        LiteralArgumentBuilder<CommandSourceStack> spawnMaid = Commands.literal(SPAWN_MAID);
        RequiredArgumentBuilder<CommandSourceStack, String> modelId = Commands.argument(MODEL_ID, StringArgumentType.string());
        RequiredArgumentBuilder<CommandSourceStack, Integer> count = Commands.argument(COUNT_NAME, IntegerArgumentType.integer(0));
        debug.then(spawnMaid.then(modelId.then(count.executes(MaidDebugCommand::spawnMaid))));

        // 生成大量农田
        LiteralArgumentBuilder<CommandSourceStack> setFarm = Commands.literal(SET_FARM);
        RequiredArgumentBuilder<CommandSourceStack, Integer> size = Commands.argument(SIZE, IntegerArgumentType.integer());
        debug.then(setFarm.then(size.executes(MaidDebugCommand::setFarm)));

        return debug;
    }

    private static int enableDebug(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        boolean value = BoolArgumentType.getBool(context, VALUE);
        ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
        if (value) {
            TouhouLittleMaid.DEBUG = true;
            serverPlayer.sendSystemMessage(Component.translatable("debug.touhou_little_maid.enable.true"));
        } else {
            TouhouLittleMaid.DEBUG = false;
            serverPlayer.sendSystemMessage(Component.translatable("debug.touhou_little_maid.enable.false"));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int spawnMaid(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!TouhouLittleMaid.DEBUG) {
            return Command.SINGLE_SUCCESS;
        }

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

    private static int setFarm(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!TouhouLittleMaid.DEBUG) {
            return Command.SINGLE_SUCCESS;
        }

        int size = IntegerArgumentType.getInteger(context, SIZE);
        ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
        if (size > 0) {
            BlockPos blockPos = serverPlayer.blockPosition();
            Level level = serverPlayer.level;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            for (int x = -size; x < size; x++) {
                for (int z = -size; z < size; z++) {
                    BlockPos.MutableBlockPos pos = mutableBlockPos.setWithOffset(blockPos, x, -1, z);
                    if (x % 5 == 0 && z % 5 == 0) {
                        // 放置水和荷叶
                        level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
                        level.setBlockAndUpdate(pos.move(Direction.UP), Blocks.LILY_PAD.defaultBlockState());
                    } else {
                        // 放置农田和作物
                        level.setBlockAndUpdate(pos, Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, 7));
                        level.setBlockAndUpdate(pos.move(Direction.UP), Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 7));
                    }
                }
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}
