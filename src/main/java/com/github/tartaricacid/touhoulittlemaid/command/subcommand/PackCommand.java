package com.github.tartaricacid.touhoulittlemaid.command.subcommand;

import com.github.tartaricacid.touhoulittlemaid.client.event.ReloadResourceEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.info.ServerCustomPackLoader;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public final class PackCommand {
    private static final String PACK_NAME = "pack";
    private static final String RELOAD_NAME = "reload";

    public static LiteralArgumentBuilder<CommandSource> get() {
        LiteralArgumentBuilder<CommandSource> pack = Commands.literal(PACK_NAME);
        LiteralArgumentBuilder<CommandSource> reload = Commands.literal(RELOAD_NAME);
        pack.then(reload.executes(PackCommand::reloadAllPack));
        return pack;
    }

    private static int reloadAllPack(CommandContext<CommandSource> context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ReloadResourceEvent::reloadAllPack);
        DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> PackCommand::sendPackToClient);
        ServerCustomPackLoader.reloadPacks();
        context.getSource().sendSuccess(new TranslationTextComponent("commands.touhou_little_maid.pack.reload.info"), true);
        return Command.SINGLE_SUCCESS;
    }

    private static void sendPackToClient() {
        // TODO: 把服务端模型包发送到每个客户端
    }
}
