package com.JSONsWorld.main.api;

import com.JSONsWorld.main.ConfigurationFile;

public class ContextManager {

    public static String buildInput(ConfigurationFile config) {
        String prompt = config.getProperty("prompt");

        StringBuilder input = new StringBuilder();
        input.append("\"messages\": [");
        String role = "user";
        for(String contextLine : config.getContext()) {
            contextLine = contextLine.replaceAll("\"", "\\\\\"");
            input.append("{\"role\": \"").append(role).append("\",")
                    .append("\"content\": \"").append(contextLine).append("\"")
                    .append("},");

            role = role.equals("user") ? "assistant" : "user"; // Toggles the role
        }
        input.append("{\"role\": \"user\",")
                .append("\"content\": \"").append(prompt).append("\"")
                .append("}]");

        return input.toString();
    }

    public static String translateRequest(String language, String translateText) {
        translateText = translateText.replaceAll("\n", "\\\\n").replaceAll("\t", "\\\\t").replaceAll("\r", "");

        String prompt = "Hello! Please translate the text at the end of the message to " + language +
                ". Do not include any other text in your reply except for the translated text. Replace all occurences of \\\"\\\\t\\\" with the tab character. " +
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
