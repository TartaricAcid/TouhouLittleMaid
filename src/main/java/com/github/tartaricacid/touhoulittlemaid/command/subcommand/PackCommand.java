package com.github.tartaricacid.touhoulittlemaid.command.subcommand;

import com.github.tartaricacid.touhoulittlemaid.client.event.ReloadResourceEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.info.ServerCustomPackLoader;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public final class PackCommand {
    private static final String PACK_NAME = "pack";
    private static final String RELOAD_NAME = "reload";

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        LiteralArgumentBuilder<CommandSourceStack> pack = Commands.literal(PACK_NAME);
        LiteralArgumentBuilder<CommandSourceStack> reload = Commands.literal(RELOAD_NAME);
        pack.then(reload.executes(PackCommand::reloadAllPack));
        return pack;
    }

    private static int reloadAllPack(CommandContext<CommandSourceStack> context) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ReloadResourceEvent::reloadAllPack);
        // DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> PackCommand::sendPackToClient);
        ServerCustomPackLoader.reloadPacks();
        // TODO：打印加载的时间到客户端聊天栏
        context.getSource().sendSuccess(new TranslatableComponent("commands.touhou_little_maid.pack.reload.info"), true);
        return Command.SINGLE_SUCCESS;
    }

    private static void sendPackToClient() {
        // TODO: 把服务端模型包发送到每个客户端
    }
}
