package com.github.tartaricacid.touhoulittlemaid.command;

import com.github.tartaricacid.touhoulittlemaid.danmaku.CustomSpellCardManger;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/8/17 15:03
 **/
public class ReloadSpellCardCommand extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "spell_card";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("sc");
        aliases.add("spell");
        aliases.add("card");
        return aliases;
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "commands.touhou_little_maid.spell_card.usage";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if (sender instanceof EntityPlayerMP) {
            if (args.length < 1 || !SUB.RELOAD.name().toLowerCase().equals(args[0])) {
                sender.sendMessage(new TextComponentTranslation("commands.touhou_little_maid.spell_card.usage"));
                return;
            }
            CustomSpellCardManger.onCustomSpellCardReload();
            sender.sendMessage(new TextComponentTranslation("commands.touhou_little_maid.spell_card.reload"));
        }
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
