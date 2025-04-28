package com.JSONsWorld.main.api;

import com.JSONsWorld.main.ConfigurationFile;

public class ContextManager {

    public static String translateRequest(String language, String translateText) {
        translateText = translateText.replaceAll("\n", "\\\\n").replaceAll("\t", "\\\\t").replaceAll("\r", "");

        String prompt = "Hello! Please translate the text at the end of the message to " + language +
                ". Do not include any other text in your reply except for the translated text. Replace all occurrences of \\\"\\\\n\\\" with the line break character. " +
                "If a word is followed by something in parenthesis then please don't translate the word in the parenthesis, translate the preceding word in that form. " +
                "For example \\\"you speak (plural)\\\" should be translated as if it was \\\"we speak\\\", without the \\\"(plural)\\\". Make sure to include proper pronouns and to preserve formatting. " +
                "The text you should translate is: " +
                translateText;

        StringBuilder input = new StringBuilder();
        input.append("\"messages\": [");
        input.append("{\"role\": \"user\",")
                .append("\"content\": \"").append(prompt).append("\"")
                .append("}]");

        return input.toString();
    }
}
