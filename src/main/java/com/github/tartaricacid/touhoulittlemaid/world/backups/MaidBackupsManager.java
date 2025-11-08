package com.github.tartaricacid.touhoulittlemaid.world.backups;

import com.github.tartaricacid.touhoulittlemaid.config.ServerConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid.LOGGER;

/**
 * 女仆数据备份管理器
 * <p>
 * 存储结构：
 * <pre>
 * maid_backups/
 * ├── owner_uuid/
 * │    ├── index.dat                    # 索引文件，存储所有女仆的基本信息
 * │    ├── maid_uuid/
 * │    │    ├── 2025-09-08-20-25-30.dat # 女仆数据备份文件
 * │    │    ├── 2025-09-08-20-30-30.dat
 * │    │    └── ...
 * │    ├── maid_uuid/
 * │    └── ...
 * ├── owner_uuid/
 * └── ...
 * </pre>
 *
 * @author TartaricAcid
 */
public final class MaidBackupsManager {
    private static final String BACKUPS_FOLDER_NAME = "maid_backups";
    private static final String INDEX_FILE_NAME = "index.dat";
    private static final String BACKUP_FILE_EXTENSION = ".dat";

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "Maid-Backups-Thread");
        thread.setDaemon(true);
        thread.setUncaughtExceptionHandler((t, e) ->
                LOGGER.error("Uncaught exception in maid backup thread: {}", t.getName(), e));
        return thread;
    });

    private static final DateTimeFormatter BACKUP_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

    // 私有构造函数，防止实例化
    private MaidBackupsManager() {
    }

    /**
     * 保存女仆数据备份
     *
     * @param server 服务器实例
     * @param maid   要备份的女仆实体
     */
    public static void save(@NotNull MinecraftServer server, @NotNull EntityMaid maid) {
        UUID ownerId = maid.getOwnerUUID();
        if (ownerId == null) {
            return;
        }

        ServerLevel overWorld = server.getLevel(Level.OVERWORLD);
        if (overWorld == null) {
            LOGGER.error("Cannot access overworld for maid backup: {}", maid.getStringUUID());
            return;
        }

        try {
            BackupData backupData = createBackupData(maid, overWorld, ownerId);
            if (backupData != null) {
                executeBackupAsync(backupData);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to initiate backup for maid: {}", maid.getStringUUID(), e);
        }
    }

    /**
     * 获取玩家的女仆索引数据
     */
    public static CompoundTag getMaidIndex(ServerPlayer player) {
        UUID ownerId = player.getUUID();
        MinecraftServer server = player.getServer();
        if (server == null) {
            return new CompoundTag();
        }
        ServerLevel overWorld = server.getLevel(Level.OVERWORLD);
        if (overWorld == null) {
            LOGGER.error("Cannot access overworld for maid index retrieval: {}", ownerId);
            return new CompoundTag();
        }
        Path ownerFolder = overWorld.getDataStorage().dataFolder.toPath()
                .resolve(BACKUPS_FOLDER_NAME)
                .resolve(ownerId.toString());
        File indexFile = ownerFolder.resolve(INDEX_FILE_NAME).toFile();
        return loadExistingIndexData(indexFile);
    }

    /**
     * 创建备份数据
     */
    @Nullable
    private static BackupData createBackupData(@NotNull EntityMaid maid, @NotNull ServerLevel overWorld, @NotNull UUID ownerId) {
        try {
            Path saveFolder = buildBackupFolderPath(maid, overWorld, ownerId);
            String saveFileName = generateBackupFileName();

            CompoundTag entityData = new CompoundTag();
            boolean saveResult = maid.saveAsPassenger(entityData);

            if (!saveResult) {
                LOGGER.warn("Failed to serialize maid data for backup: {}", maid.getStringUUID());
                return null;
            }

            CompoundTag indexData = createIndexData(maid);
            return new BackupData(saveFolder, saveFileName, entityData, indexData, maid.getStringUUID());

        } catch (Exception e) {
            LOGGER.error("Error creating backup data for maid: {}", maid.getStringUUID(), e);
            return null;
        }
    }

    /**
     * 创建索引数据
     */
    @NotNull
    private static CompoundTag createIndexData(@NotNull EntityMaid maid) {
        CompoundTag indexData = new CompoundTag();
        indexData.putString("Name", Component.Serializer.toJson(maid.getName(), maid.level.registryAccess()));
        indexData.putString("Dimension", maid.level.dimension().location().toString());
        indexData.put("Pos", NbtUtils.writeBlockPos(maid.blockPosition()));
        indexData.putLong("Timestamp", System.currentTimeMillis());
        return indexData;
    }

    public static Map<UUID, IndexData> getMaidIndexMap(ServerPlayer player) {
        CompoundTag indexTag = getMaidIndex(player);
        Map<UUID, IndexData> map = Maps.newHashMap();
        for (String s : indexTag.getAllKeys()) {
            CompoundTag maidTag = indexTag.getCompound(s);
            UUID maidUuid = UUID.fromString(s);
            Component name = Component.Serializer.fromJson(maidTag.getString("Name"), player.level.registryAccess());
            BlockPos pos = NbtUtils.readBlockPos(maidTag, "Pos").orElse(BlockPos.ZERO);
            String dimension = maidTag.getString("Dimension");
            long timestamp = maidTag.getLong("Timestamp");
            map.put(maidUuid, new IndexData(name, pos, dimension, timestamp));
        }
        return map;
    }

    public static List<String> getMaidBackupFiles(ServerPlayer player, UUID maidUuid) {
        List<String> backupFiles = Lists.newArrayList();
        Path folderPath = player.serverLevel().getDataStorage().dataFolder.toPath()
                .resolve(BACKUPS_FOLDER_NAME)
                .resolve(player.getUUID().toString())
                .resolve(maidUuid.toString());
        if (!Files.isDirectory(folderPath)) {
            return backupFiles;
        }
        File[] files = folderPath.toFile().listFiles((dir, name) -> name.endsWith(BACKUP_FILE_EXTENSION));
        if (files == null) {
            return backupFiles;
        }
        for (File file : files) {
            backupFiles.add(file.getName());
        }
        backupFiles.sort(Comparator.reverseOrder());
        return backupFiles;
    }

    public static CompoundTag getMaidBackFile(ServerPlayer player, UUID maidUuid, String fileName) {
        Path filePath = player.serverLevel().getDataStorage().dataFolder.toPath()
                .resolve(BACKUPS_FOLDER_NAME)
                .resolve(player.getUUID().toString())
                .resolve(maidUuid.toString())
                .resolve(fileName);
        File file = filePath.toFile();
        if (!file.exists() || !file.isFile()) {
            return new CompoundTag();
        }
        try {
            return NbtIo.readCompressed(filePath, NbtAccounter.create(0x6_400_000L));
        } catch (IOException e) {
            LOGGER.error("Failed to read maid backup file: {}", filePath, e);
            return new CompoundTag();
        }
    }


    /**
     * 异步执行备份
     */
    private static void executeBackupAsync(@NotNull BackupData backupData) {
        CompletableFuture
                .runAsync(() -> performBackup(backupData), EXECUTOR_SERVICE)
                .exceptionally(error -> {
                    LOGGER.error("Async backup failed for maid: {}", backupData.maidUuid, error);
                    return null;
                });
    }

    /**
     * 执行实际的备份操作
     */
    private static void performBackup(@NotNull BackupData backupData) {
        try {
            // 1. 确保目录存在
            if (!ensureDirectoryExists(backupData.saveFolder)) {
                return;
            }

            // 2. 更新索引文件
            if (!updateIndexFile(backupData)) {
                return;
            }

            // 3. 保存实体数据
            saveEntityData(backupData);

        } catch (Exception e) {
            LOGGER.error("Backup operation failed for maid: {}", backupData.maidUuid, e);
        }
    }

    /**
     * 确保目录存在
     */
    private static boolean ensureDirectoryExists(@NotNull Path directory) {
        if (Files.isDirectory(directory)) {
            return true;
        }

        try {
            Files.createDirectories(directory);
            return true;
        } catch (IOException e) {
            LOGGER.error("Failed to create backup directory: {}", directory, e);
            return false;
        }
    }

    /**
     * 更新索引文件
     */
    private static boolean updateIndexFile(@NotNull BackupData backupData) {
        File indexFile = backupData.saveFolder.getParent().resolve(INDEX_FILE_NAME).toFile();

        try {
            CompoundTag allIndexData = loadExistingIndexData(indexFile);
            allIndexData.put(backupData.saveFolder.getFileName().toString(), backupData.indexData);

            NbtIo.writeCompressed(allIndexData, indexFile.toPath());
            return true;

        } catch (IOException e) {
            LOGGER.error("Failed to update index file: {}", indexFile, e);
            return false;
        }
    }

    /**
     * 加载现有的索引数据
     */
    @NotNull
    private static CompoundTag loadExistingIndexData(@NotNull File indexFile) {
        if (!indexFile.exists()) {
            return new CompoundTag();
        }

        try {
            return NbtIo.readCompressed(indexFile.toPath(), NbtAccounter.create(0x6_400_000L));
        } catch (IOException e) {
            LOGGER.warn("Failed to read existing index file, creating new one: {}", indexFile, e);
            return new CompoundTag();
        }
    }

    /**
     * 保存实体数据
     */
    private static void saveEntityData(@NotNull BackupData backupData) {
        File backupFile = backupData.saveFolder.resolve(backupData.saveFileName).toFile();

        try {
            NbtUtils.addCurrentDataVersion(backupData.entityData);
            NbtIo.writeCompressed(backupData.entityData, backupFile.toPath());

            // 删除旧备份
            removeOldBackups(backupData.saveFolder, ServerConfig.MAID_BACKUP_MAX_COUNT.get());

        } catch (IOException e) {
            LOGGER.error("Failed to save entity data to: {}", backupFile, e);
        }
    }

    private static void removeOldBackups(@NotNull Path backupFolder, int maxBackups) {
        try {
            File[] backupFiles = backupFolder.toFile().listFiles((dir, name) -> name.endsWith(BACKUP_FILE_EXTENSION));
            if (backupFiles == null || backupFiles.length <= maxBackups) {
                return;
            }

            // 按文件名排序，文件名包含时间戳
            Arrays.sort(backupFiles, Comparator.comparing(File::getName));

            // 删除多余的备份文件
            for (int i = 0; i < backupFiles.length - maxBackups; i++) {
                if (!backupFiles[i].delete()) {
                    LOGGER.warn("Failed to delete old backup file: {}", backupFiles[i].getAbsolutePath());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error while removing old backups in folder: {}", backupFolder, e);
        }
    }

    /**
     * 构建备份文件夹路径
     */
    @NotNull
    private static Path buildBackupFolderPath(@NotNull EntityMaid maid, @NotNull ServerLevel level, @NotNull UUID ownerId) {
        return level.getDataStorage().dataFolder.toPath()
                .resolve(BACKUPS_FOLDER_NAME)
                .resolve(ownerId.toString())
                .resolve(maid.getStringUUID());
    }

    /**
     * 生成备份文件名
     */
    @NotNull
    public static String generateBackupFileName() {
        return LocalDateTime.now().format(BACKUP_TIME_FORMATTER) + BACKUP_FILE_EXTENSION;
    }

    public record IndexData(
            Component name,
            BlockPos pos,
            String dimension,
            long timestamp) {
    }

    /**
     * 备份数据容器类
     */
    private record BackupData(
            Path saveFolder,
            String saveFileName,
            CompoundTag entityData,
            CompoundTag indexData,
            String maidUuid) {
    }
}
