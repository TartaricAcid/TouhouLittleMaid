package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.command.RootCommand;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class CommandRegistry {
    public static void onServerStaring(RegisterCommandsEvent event) {
        RootCommand.register(event.getDispatcher());
    }
}
