package com.JSONsWorld.main;

import com.google.gson.JsonParser;

import java.util.regex.Pattern;


public class OutputProcessor {

    public static String processResponse(String response) {
        // Yes, this is a lot, but it basically just navigates the JSON response and gets the part we actually care about.
        response = JsonParser.parseString(response).getAsJsonObject().get("output")
                .getAsJsonArray().get(0).getAsJsonObject().get("content").getAsJsonArray().get(0)
                .getAsJsonObject().get("text").toString();

        if(response.toLowerCase().contains("as an ai language model")) throw new RuntimeException("Denial of service.");
        if(Pattern.compile("((\\\\n){1,2}\\d\\.)").matcher(response).results().count() > 2) {
            response = response.replaceAll("\\\\n\\\\n", "\n").replaceAll("\\\\n", "\n");
            response = response.replaceAll("\\\\t", "\t");

        }
        return response;
    }
}
