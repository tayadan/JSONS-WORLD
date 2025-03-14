package com.JSONsWorld.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class InputProcessor {

    public static String processResponse(String response) {
        if(response.toLowerCase().startsWith("as an ai language model")) return "Error";
        else if(response.contains("\n2.") || (response.contains("1.") && response.contains("2."))) {
            System.out.println("Numbered list!");
            return "";
        }
        return "";
    }

    public static void runTestDataset() throws IOException {
        File file = new File("C:\\Users\\sebas\\Documents\\Project 3\\SampleResponses.txt");

        StringBuilder currentResponse = new StringBuilder();
        for(String line : Files.readString(file.toPath()).split("\n")) {
            if(line.startsWith("\"")) currentResponse = new StringBuilder(line);
            else {
                currentResponse.append(line.trim());
                if(!line.trim().endsWith("\"")) currentResponse.append("\n");
            }


            if(line.trim().endsWith("\"")) {
                System.out.println(currentResponse);
                currentResponse = new StringBuilder();
            }
        }
    }
}
