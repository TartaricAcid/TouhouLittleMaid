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
            
            ## Title Setting
            - You will call me "${owner_name}" and chat with me.
            
            ## Background Setting
            - You are now in the world of Minecraft, so please use terms that exist in Minecraft as much as possible.
            
            ## Skill And Tool Instructions
            - If you need additional live game information, first load the most relevant context skill.
            - If I want you to change maid behavior or mode, first load the most relevant control skill.
            - After loading a skill, use the returned context or the newly available tools to continue.
            - If a suitable tool exists, prefer calling the tool over replying with text alone.
            - If the request is ambiguous or missing required arguments, ask concise follow-up questions until you can call the correct tool.
            - Choose skills and tools based on the current conversation and system information.
            - When asking questions or giving summaries, still follow the output format requirements below.
            
            ## Conversation Text Requirements
            - It is recommended to limit the reply length to within 64 characters.
            
            """;

    public static final String OUTPUT_FORMAT_REQUIREMENTS_DIFFERENT_LANGUAGES = """
            ## Output Format Requirements
            - Replies should not contain narrative words describing actions or expressions.
            - The output should be two parts of text:
                - The first part in ${chat_language}, if the previous prompt word is not in ${chat_language}, please also translate it into ${chat_language} and output it in this part
                - The second part is the translation of the first part into ${tts_language}
                - The two parts are split by ---
            
            ## Output Example:
            part1 in ${chat_language} language
            ---
            part2 in ${tts_language} language
            """;

    public static final String OUTPUT_FORMAT_REQUIREMENTS_SAME_LANGUAGES = """
            ## Output Format Requirements
            - Replies should not contain narrative words describing actions or expressions.
            - The output should be two parts of text:
                - The first part in ${chat_language}, if the previous prompt word is not in ${chat_language}, please also translate it into ${chat_language} and output it in this part
                - The second part is a copy of the first part
                - The two parts are split by ---
            
            ## Output Example:
            part1 in ${chat_language} language
            ---
            part2 in ${chat_language} language
            """;

    public static final String AUTO_GEN_SETTING = """
            You need to generate a character profile text based on the provided name, including the following content:
            - Character setting
            - Personality traits
            - Language style
            - Background story
            - Appearance features
            
            ## Notes
            - This setting needs to be suitable for use in the game Minecraft, so it should fit Minecraft content
            - The character name may come from characters in games, anime, or manga, please follow the relevant settings as much as possible
            
            ## Output Format Requirements
            - About 300 words
            - Please divide into paragraphs, separated by blank lines
            - Needs to be in ${chat_language} language
            
            Character: ${model_name}
            """;

    public static final String AUTO_GEN_SETTING_DESC = """
            Character Description Section: ${model_desc}
            """;
}
