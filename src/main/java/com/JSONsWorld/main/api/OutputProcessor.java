package com.JSONsWorld.main.api;

import com.google.gson.JsonNull;
import com.google.gson.JsonParser;

import java.util.regex.Pattern;


public class OutputProcessor {

    public static String processResponse(String response) {
        // Yes, this is a lot, but it basically just navigates the JSON response and gets the part we actually care about.
        Object refusal = JsonParser.parseString(response).getAsJsonObject().get("choices")
                .getAsJsonArray().get(0).getAsJsonObject()
                .get("message").getAsJsonObject().get("refusal");
        response = JsonParser.parseString(response).getAsJsonObject().get("choices")
                .getAsJsonArray().get(0).getAsJsonObject()
                .get("message").getAsJsonObject().get("content").toString();

        if(response.toLowerCase().contains("as an ai language model") || !(refusal instanceof JsonNull)) throw new RuntimeException("Denial of service.");
        if(Pattern.compile("((\\\\n){1,2}\\d\\.)").matcher(response).results().count() > 2) {
            response = response.replaceAll("\\\\n\\\\n", "\n").replaceAll("\\\\n", "\n");
            response = response.replaceAll("\\\\t", "\t");

        }
        return response.replaceAll("\"", "").replaceAll("\\\\n", "\n");
    }
}
