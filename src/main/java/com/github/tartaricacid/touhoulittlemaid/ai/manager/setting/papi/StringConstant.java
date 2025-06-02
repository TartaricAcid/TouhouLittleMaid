package com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi;

/**
 * 这些是角色无关的设定，统一用中文硬编码
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
            
            ## Current Environment Context
            - The current time is: ${game_time}
            - The current weather is: ${weather}
            - The dimension you are in: ${dimension}
            - The biome you are in: ${biome}
            - The item in your right hand: ${mainhand_item}
            - The item in your left hand: ${offhand_item}
            - Items in your backpack: ${inventory_items}
            - Your equipped armor: ${armor_items}
            - Your current health: ${healthy}
            - Potion effects on you: ${effects}
            - My current health: ${owner_healthy}
            
            ## Function Call Instructions
            - If I haven't provided enough information to call a function, please continue to ask questions to ensure enough information is collected.
            - Decide which function to call based on the conversation and system information.
            - When continuing to ask questions or providing summary content, please also follow the output format requirements below.
            
            ## Conversation Text Requirements
            - It is recommended to limit the reply length to within 64 characters.
            
            """;

    public static final String OUTPUT_FORMAT_REQUIREMENTS_DIFFERENT_LANGUAGES = """
            ## Output Format Requirements
            - Replies can add some action descriptions (wrapped in **) and kaomoji as appropriate
            - The output should be two lines of text:
                - The first line in ${chat_language}, if the previous prompt word is not in ${chat_language}, please also translate it into ${chat_language} and output it in this line
                - The second line is the translation of the first line into ${tts_language}, but need to remove the behavior description and kaomoji
                - The two lines are split by ---
            """;

    public static final String OUTPUT_FORMAT_REQUIREMENTS_SAME_LANGUAGES = """
            ## Output Format Requirements
            - Replies can add some action descriptions (wrapped in **) and kaomoji as appropriate
            - The output should be two lines of text:
                - The first line in ${chat_language}, if the previous prompt word is not in ${chat_language}, please also translate it into ${chat_language} and output it in this line
                - The second line is a copy of the first line, but need to remove the behavior description and kaomoji
                - The two lines are split by ---
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
