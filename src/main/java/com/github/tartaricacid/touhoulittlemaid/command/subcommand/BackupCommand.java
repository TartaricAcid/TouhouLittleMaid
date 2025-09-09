package com.github.tartaricacid.touhoulittlemaid.command.subcommand;

import com.github.tartaricacid.touhoulittlemaid.item.ItemCamera;
import com.github.tartaricacid.touhoulittlemaid.world.backups.MaidBackupsManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class BackupCommand {
    private static final String BACKUP_NAME = "backup";
    private static final String GET_NAME = "get";
    private static final String PLAYER_NAME = "player";
    private static final String MAID_UUID = "maid_uuid";
    private static final String FILE_NAME = "file_name";

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(BACKUP_NAME);
        LiteralArgumentBuilder<CommandSourceStack> get = Commands.literal(GET_NAME);

        RequiredArgumentBuilder<CommandSourceStack, EntitySelector> player = Commands.argument(PLAYER_NAME, EntityArgument.player());
        RequiredArgumentBuilder<CommandSourceStack, UUID> maidUuid = Commands.argument(MAID_UUID, UuidArgument.uuid());
        RequiredArgumentBuilder<CommandSourceStack, String> fileName = Commands.argument(FILE_NAME, StringArgumentType.string());

        root.then(get.then(player.executes(BackupCommand::handlePlayerMaidIndex)));
        root.then(get.then(player.then(maidUuid.executes(BackupCommand::handlePlayerMaid))));
        root.then(get.then(player.then(maidUuid.then(fileName.executes(BackupCommand::handlePlayerMaidFile)))));

        return root;
    }

    private static String getFormattedTime(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.UTC);
        return dateTime.format(DateTimeFormatter.ofPattern("(MM/dd HH:mm)"));
    }

    private static int handlePlayerMaidIndex(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, PLAYER_NAME);
        var maidIndexMap = MaidBackupsManager.getMaidIndexMap(player);

        // 如果没找到
        if (maidIndexMap.isEmpty()) {
            MutableComponent error = Component.translatable("message.touhou_little_maid.maid_backup.player.no_data", player.getScoreboardName());
            player.sendSystemMessage(error.withStyle(ChatFormatting.RED));
            return Command.SINGLE_SUCCESS;
        }

        // 将 maidIndexMap 按照时间戳，从新到旧排序
        maidIndexMap = maidIndexMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(
                        Comparator.comparingLong(MaidBackupsManager.IndexData::timestamp).reversed()
                ))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        MutableComponent separator = Component.translatable("message.touhou_little_maid.maid_backup.player.separator", player.getScoreboardName());
        player.sendSystemMessage(separator.withStyle(ChatFormatting.DARK_GREEN));

        // 把女仆的信息按行打印出来，显示名称、坐标和维度
        int index = 1;
        for (var entry : maidIndexMap.entrySet()) {
            UUID id = entry.getKey();
            MaidBackupsManager.IndexData data = entry.getValue();

            String time = "§7" + getFormattedTime(data.timestamp());
            MutableComponent msg = Component.literal("§7%d.".formatted(index))
                    .append(CommonComponents.SPACE)
                    .append(data.name())
                    .withStyle(ChatFormatting.GOLD)
                    .append(CommonComponents.SPACE)
                    .append(time);

            MutableComponent dimension = Component.translatable("tooltips.touhou_little_maid.fox_scroll.dimension", data.dimension());
            MutableComponent pos = Component.translatable("tooltips.touhou_little_maid.fox_scroll.position", data.pos().toShortString());
            String command = "/tlm backup get @s %s".formatted(id);

            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, CommonComponents.joinLines(dimension, pos));
            ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);

            msg.withStyle(style -> style.withHoverEvent(hoverEvent))
                    .withStyle(style -> style.withClickEvent(clickEvent));

            player.sendSystemMessage(msg);
            index++;
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int handlePlayerMaid(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, PLAYER_NAME);
        UUID uuid = UuidArgument.getUuid(context, MAID_UUID);

        var maidIndexMap = MaidBackupsManager.getMaidIndexMap(player);
        var indexData = maidIndexMap.get(uuid);
        if (indexData == null) {
            MutableComponent error = Component.translatable("message.touhou_little_maid.maid_backup.maid.not_found", uuid);
            player.sendSystemMessage(error.withStyle(ChatFormatting.RED));
            return Command.SINGLE_SUCCESS;
        }

        MutableComponent separator = Component.translatable("message.touhou_little_maid.maid_backup.maid.separator", indexData.name());
        player.sendSystemMessage(separator.withStyle(ChatFormatting.DARK_GREEN));

        var backupFiles = MaidBackupsManager.getMaidBackupFiles(player, uuid);
        for (String backupFile : backupFiles) {
            MutableComponent msg = Component.literal(backupFile).withStyle(ChatFormatting.DARK_PURPLE);

            String command = "/tlm backup get @s %s \"%s\"".formatted(uuid, backupFile);
            ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
            msg.withStyle(style -> style.withClickEvent(clickEvent));

            player.sendSystemMessage(msg);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int handlePlayerMaidFile(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, PLAYER_NAME);
        UUID uuid = UuidArgument.getUuid(context, MAID_UUID);
        String fileName = StringArgumentType.getString(context, FILE_NAME);

        CompoundTag backupData = MaidBackupsManager.getMaidBackFile(player, uuid, fileName);
        if (backupData.isEmpty()) {
            MutableComponent error = Component.translatable("message.touhou_little_maid.maid_backup.file.not_found", fileName);
            player.sendSystemMessage(error.withStyle(ChatFormatting.RED));
            return Command.SINGLE_SUCCESS;
        }

        // 生成包含此女仆信息的照片
        ItemCamera.spawnMaidPhoto(player.level, backupData, player);
        // 发送成功信息
        MutableComponent success = Component.translatable("message.touhou_little_maid.maid_backup.file.success", fileName);
        player.sendSystemMessage(success.withStyle(ChatFormatting.GREEN));
        return Command.SINGLE_SUCCESS;
    }
}
