package com.JSONsWorld.main;

public class ContextManager {

    public static String buildInput(ConfigurationFile config) {
        String context = config.getProperty("context");
        String prompt = config.getProperty("prompt");

        StringBuilder input = new StringBuilder();
        if(context != null) {
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
        }
        else input.append("\"content\": \"").append(prompt).append("\"");

        return input.toString();
    }
}
