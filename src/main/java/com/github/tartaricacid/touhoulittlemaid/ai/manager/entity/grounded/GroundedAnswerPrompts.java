package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.grounded;

public final class GroundedAnswerPrompts {
    public static final String GROUNDED_INSTRUCTIONS = """
            ## Grounded Result Extraction Instructions
            Extract raw facts from the provided knowledge and map them to the core intent of the conversation.
            
            ### Mission:
            - **Identify the Subject**: Determine what specific entity the user is currently focused on.
            - **Extract Key-Value Facts**: Extract only the hard facts and pair them with their clear meanings.
            - **Normalize References**: Convert all relative terms (this, it, her) into their specific names based on the conversation history provided below.
            
            ### Constraints:
            1. **Output Format**: Use "Subject > Fact" or "Parameter: Value" style. Be as dry as possible.
            2. **Strictness**: If the knowledge doesn't contain a direct answer, output "Unknown: [Subject]".
            3. **No Conversational Noise**: No "Here is what I found", no "Based on the docs".
            4. **No Formatting**: Plain text only. No Markdown tables or separators.
            """;

    public static String buildUserPrompt(String question, String knowledgeText) {
        return """
                ## Prepared Knowledge Package
                %s
                
                ## User Question Or Normalization Context
                %s
                """.formatted(knowledgeText, question);
    }
}
