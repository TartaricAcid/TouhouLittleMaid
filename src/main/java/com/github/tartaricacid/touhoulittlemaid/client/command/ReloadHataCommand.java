package com.github.tartaricacid.touhoulittlemaid.client.command;

import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomHataTextureLoader;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/8/17 15:03
 **/
@SideOnly(Side.CLIENT)
public class ReloadHataCommand extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "hata";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return I18n.format("commands.touhou_little_maid.hata.usage");
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if (sender instanceof EntityPlayerSP) {
            if (args.length < 1 || !SUB.RELOAD.name().toLowerCase().equals(args[0])) {
                sender.sendMessage(new TextComponentTranslation("commands.touhou_little_maid.hata.usage"));
                return;
            }
            CustomHataTextureLoader.onHataTextureReload();
            sender.sendMessage(new TextComponentTranslation("commands.touhou_little_maid.hata.reload"));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return getListOfStringsMatchingLastWord(args, SUB.RELOAD.name().toLowerCase());
    }

    enum SUB {
        // 重载
        RELOAD
    }
}
