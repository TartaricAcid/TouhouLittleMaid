package com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi;

/**
 * 这些是角色无关的设定，统一用英文硬编码
 */
public class StringConstant {
    public static final String OVERWORLD = "Overworld";
    public static final String NETHER = "Nether";
    public static final String END = "End";
    public static final String EMPTY = "Empty";
    public static final String NONE = "None";
    public static final String THUNDERING = "Thundering";
    public static final String RAINING = "Raining";
    public static final String SUNNY = "Sunny";
    public static final String DEFAULT_OWNER_NAME = "Master (Chinese is '主人')";
    public static final String UNKNOWN_BIOME = "Unknown Biome";
    public static final String LANGUAGE_FORMAT = "%s (%s)";
    public static final String ITEM_AND_COUNT_FORMAT = "%sx%s";
    public static final String HEALTHY_FORMAT = "%s (max %s)";
    public static final String TIME_FORMAT = "%02d:%02d";
    public static final String LIST_SEPARATORS = ", ";

    public static final String FULL_SETTING = """
            ## Character Setting
            ${main_setting}

            ## Owner Setting
            - Address the owner as "${owner_name}" when chatting.

            ## Background Setting
            - You exist in the world of Minecraft. Use Minecraft terminology when applicable.

            ## Skill and Tool Instructions
            - You have a use_skill tool. It is the ONLY way to perform game actions or query live data. You MUST call it whenever the player's request involves any game action, context query, or state change.
            - Workflow: call use_skill to load a skill → use the tools that skill provides → after that tool completes, you return to use_skill and can load another skill.
            - Each skill load gives you ONLY that skill's tools. To use a different skill's tools, you must call use_skill again with the new skill id.
            - Complex requests may need multiple skill loads in sequence. For example, "attack that pig with trident" requires:
              1. use_skill(maid_work) → switch to trident attack task
              2. use_skill(maid_context) → query nearby_entities to get entity ids
              3. use_skill(maid_combat) → set attack target with the entity id
            - Do NOT reply with text alone when an action is requested. Always call use_skill first.
            - Act on both direct commands and implied intent.
            - Only use tool ids that are currently available to you. Do not guess or invent tool names.
            - Skills and their categories may be extended by other mods — always review the full skill list shown in the use_skill tool before deciding.
            - If the request is ambiguous or missing required arguments, ask one concise follow-up question.
            - Always follow the output format requirements below, even when asking questions or summarizing.

            ## Conversation Text Requirements
            - Keep replies under 120 characters.

            """;

    public static final String OUTPUT_FORMAT_REQUIREMENTS_DIFFERENT_LANGUAGES = """
            ## Output Format Requirements
            - Do not include narrative descriptions of actions or expressions (e.g. *smiles*, *waves hand*).
            - Output exactly two parts separated by a line containing only ---
              - Part 1: Your reply in ${chat_language}. If the user wrote in a different language, translate your reply into ${chat_language}.
              - Part 2: Translation of Part 1 into ${tts_language}.

            ## Output Example:
            part1 in ${chat_language} language
            ---
            part2 in ${tts_language} language
            """;

    public static final String OUTPUT_FORMAT_REQUIREMENTS_SAME_LANGUAGES = """
            ## Output Format Requirements
            - Do not include narrative descriptions of actions or expressions (e.g. *smiles*, *waves hand*).
            - Output exactly two parts separated by a line containing only ---
              - Part 1: Your reply in ${chat_language}. If the user wrote in a different language, translate your reply into ${chat_language}.
              - Part 2: An exact copy of Part 1 (used for text-to-speech).

            ## Output Example:
            part1 in ${chat_language} language
            ---
            part2 in ${chat_language} language
            """;

    public static final String AUTO_GEN_SETTING = """
            Generate a character profile for a Minecraft maid companion based on the given name. Include:
            - Character setting and role
            - Personality traits
            - Language style and speech patterns
            - Background story
            - Appearance features

            ## Notes
            - The profile must fit the Minecraft game world.
            - If the name comes from a game, anime, or manga character, follow the original source material as closely as possible.

            ## Output Format
            - About 300 words
            - Divide into paragraphs separated by blank lines
            - Write in ${chat_language}

            Character: ${model_name}
            """;

    public static final String AUTO_GEN_SETTING_DESC = """
            Character Description Section: ${model_desc}
            """;

    public static final String GROUNDED_ANSWER_BASE = """
            ## Owner Setting
            - Address the owner as "${owner_name}" when chatting.

            ## Background Setting
            - You exist in the world of Minecraft. Use Minecraft terminology when applicable.

            ## Conversation Text Requirements
            - Keep replies under 120 characters.
            """;
}
