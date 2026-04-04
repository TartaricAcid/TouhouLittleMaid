package com.github.tartaricacid.touhoulittlemaid.command.subcommand;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.ContextCategory;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.GameContextRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.SkillInstance;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.SkillLoader;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ToolRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.minecraft.ChatFormatting.*;

public class AIChatCommand {
    private static final String ROOT_NAME = "ai_chat";
    private static final String RELOAD_NAME = "reload";
    private static final String SKILL_NAME = "skill";
    private static final String TOOL_NAME = "tool";
    private static final String CONTEXT_NAME = "context";

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        LiteralArgumentBuilder<CommandSourceStack> root = LiteralArgumentBuilder.literal(ROOT_NAME);
        LiteralArgumentBuilder<CommandSourceStack> reload = LiteralArgumentBuilder.literal(RELOAD_NAME);
        LiteralArgumentBuilder<CommandSourceStack> skill = LiteralArgumentBuilder.literal(SKILL_NAME);
        LiteralArgumentBuilder<CommandSourceStack> tool = LiteralArgumentBuilder.literal(TOOL_NAME);
        LiteralArgumentBuilder<CommandSourceStack> context = LiteralArgumentBuilder.literal(CONTEXT_NAME);
        LiteralArgumentBuilder<CommandSourceStack> tokens = ChatTokensCommand.get();

        root.then(reload.executes(AIChatCommand::reload));
        root.then(skill.executes(AIChatCommand::showSkills));
        root.then(tool.executes(AIChatCommand::showTools));
        root.then(context.executes(AIChatCommand::showContexts));
        root.then(tokens);

        return root;
    }

    private static int reload(CommandContext<CommandSourceStack> context) {
        AvailableSites.init();
        SkillLoader.init();

        context.getSource().sendSuccess(() -> Component.translatable("message.touhou_little_maid.ai_chat.reload_success"), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int showSkills(CommandContext<CommandSourceStack> context) {
        Map<String, SkillInstance> skills = SkillLoader.getAllSkills();
        if (skills.isEmpty()) {
            sendSuccess(context, "skill.empty");
            return Command.SINGLE_SUCCESS;
        }

        sendSuccess(context, "skill.header");
        skills.forEach((name, skill) -> context.getSource().sendSuccess(() -> {
            MutableComponent nameComp = Component.literal(name).withStyle(YELLOW);
            MutableComponent descComp = Component.literal(skill.description()).withStyle(GRAY, ITALIC);
            if (skill.isKnowledgeType()) {
                return component("skill.entry.knowledge", nameComp, descComp);
            }
            return component("skill.entry", nameComp, descComp);
        }, false));

        return Command.SINGLE_SUCCESS;
    }

    private static int showTools(CommandContext<CommandSourceStack> context) {
        Map<String, ?> tools = ToolRegister.getAllTools();
        if (tools.isEmpty()) {
            sendSuccess(context, "tool.empty");
            return Command.SINGLE_SUCCESS;
        }

        sendSuccess(context, "tool.header");
        tools.keySet().forEach(tool -> context.getSource().sendSuccess(() -> {
            MutableComponent nameComp = Component.literal(tool).withStyle(YELLOW);
            return component("tool.entry", nameComp);
        }, false));
        return Command.SINGLE_SUCCESS;
    }

    private static int showContexts(CommandContext<CommandSourceStack> context) {
        List<ContextCategory> toolCategories = GameContextRegister.allToolCategories().stream()
                .sorted(Comparator.comparing(ContextCategory::id))
                .toList();
        List<ContextCategory> promptCategories = GameContextRegister.allPromptCategories().stream()
                .sorted(Comparator.comparing(ContextCategory::id))
                .toList();
        if (toolCategories.isEmpty() && promptCategories.isEmpty()) {
            sendSuccess(context, "context.empty");
            return Command.SINGLE_SUCCESS;
        }

        sendSuccess(context, "context.tool.header");
        showContextCategoryList(context, toolCategories);

        sendSuccess(context, "context.prompt.header");
        showContextCategoryList(context, promptCategories);
        return Command.SINGLE_SUCCESS;
    }

    private static void showContextCategoryList(CommandContext<CommandSourceStack> context, List<ContextCategory> categories) {
        if (categories.isEmpty()) {
            sendSuccess(context, "context.group.empty");
            return;
        }

        categories.forEach(category -> context.getSource().sendSuccess(() -> {
            String contextIds = GameContextRegister.getContextKeys(category.id()).stream()
                    .sorted()
                    .collect(Collectors.joining(", "));
            MutableComponent categoryComp = Component.literal(category.id()).withStyle(AQUA);
            MutableComponent idsComp = Component.literal(contextIds).withStyle(YELLOW);
            return component("context.entry", categoryComp, idsComp);
        }, false));
    }

    private static void sendSuccess(CommandContext<CommandSourceStack> context, String key) {
        context.getSource().sendSuccess(() -> component(key), false);
    }

    private static Component component(String key, Object... args) {
        return Component.translatable("commands.touhou_little_maid.ai_chat." + key, args);
    }
}
