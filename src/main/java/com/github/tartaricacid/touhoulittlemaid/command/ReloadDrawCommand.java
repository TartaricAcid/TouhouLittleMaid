package com.github.tartaricacid.touhoulittlemaid.command;

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

import static com.github.tartaricacid.touhoulittlemaid.util.DrawCalculation.readDrawCsvFile;

/**
 * @author TartaricAcid
 * @date 2019/8/17 15:03
 **/
public class ReloadDrawCommand extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "draw_coupon";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("dc");
        aliases.add("draw");
        return aliases;
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "commands.touhou_little_maid.draw_coupon.usage";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if (args.length < 1 || !SUB.RELOAD.name().toLowerCase().equals(args[0])) {
            sender.sendMessage(new TextComponentTranslation("commands.touhou_little_maid.draw_coupon.usage"));
            return;
        }
        readDrawCsvFile(this);
        sender.sendMessage(new TextComponentTranslation("commands.touhou_little_maid.draw_coupon.reload"));
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length != 1 ? Collections.EMPTY_LIST : getListOfStringsMatchingLastWord(args, SUB.RELOAD.name().toLowerCase());
    }

    enum SUB {
        // 重载
        RELOAD
    }
}
