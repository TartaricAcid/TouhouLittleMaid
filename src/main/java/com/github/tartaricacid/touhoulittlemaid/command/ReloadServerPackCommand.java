package com.github.tartaricacid.touhoulittlemaid.command;

import com.github.tartaricacid.touhoulittlemaid.network.serverpack.ServerPackManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/8/17 15:03
 **/
public class ReloadServerPackCommand extends CommandBase {
    private static final String SUB_COMMAND = "reload";

    @Nonnull
    @Override
    public String getName() {
        return "tlm_server_pack";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("tlmsp");
        return aliases;
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "commands.touhou_little_maid.tlm_server_pack.usage";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if (args.length < 1 || !SUB_COMMAND.equals(args[0])) {
            sender.sendMessage(new TextComponentTranslation("commands.touhou_little_maid.tlm_server_pack.usage"));
            return;
        }
        ServerPackManager.initCrc32Info();
        sender.sendMessage(new TextComponentTranslation("commands.touhou_little_maid.tlm_server_pack.reload"));
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length != 1 ? Collections.EMPTY_LIST : getListOfStringsMatchingLastWord(args, SUB_COMMAND);
    }
}
