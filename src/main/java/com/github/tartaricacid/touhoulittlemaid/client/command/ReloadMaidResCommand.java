package com.github.tartaricacid.touhoulittlemaid.client.command;

import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.lang3.time.StopWatch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReloadMaidResCommand extends CommandBase {
    private String RELOAD = "reload";

    @Nonnull
    @Override
    public String getName() {
        return "maid_res";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return I18n.format("commands.touhou_little_maid.maid_res.usage");
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if (sender instanceof EntityPlayerSP) {
            if (args.length < 1 || !RELOAD.equals(args[0])) {
                sender.sendMessage(new TextComponentTranslation("commands.touhou_little_maid.maid_res.usage"));
                sender.sendMessage(new TextComponentTranslation("commands.touhou_little_maid.maid_res.warning"));
                return;
            }
            StopWatch stopWatch = StopWatch.createStarted();
            CustomResourcesLoader.reloadResources();
            stopWatch.stop();
            sender.sendMessage(new TextComponentTranslation("commands.touhou_little_maid.maid_res.time",
                    stopWatch.getTime(TimeUnit.MICROSECONDS) / 1000.0));
            sender.sendMessage(new TextComponentTranslation("commands.touhou_little_maid.maid_res.warning"));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return getListOfStringsMatchingLastWord(args, RELOAD);
    }
}
