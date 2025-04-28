package com.JSONsWorld.main;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConfigurationFile {
    private Properties properties;
    private ArrayList<String> context = new ArrayList<>();

    public ConfigurationFile(String fileName) {
        this.properties = new Properties();

        properties.put("schedule", "conjugation, left, whole,  story, left, whole, conjugation, left, conjugation, whole, story");

        properties.put("conjugation.prompt", "Please create a comic revolving around conjugation in {language}. Make sure each line of dialogue has a corresponding translation.");
        properties.put("left.prompt", "Please create a comic with the first character in each scene talking in {language} and the other one translating what they say into English.");
        properties.put("whole.prompt", "Please create a comic with both characters peaking in {language} and specify translations for each character.");
        properties.put("story.prompt", "Please create a comic involving characters speaking in both {language} and English. Translations do NOT need to be specified, but make sure it includes both languages.");

        properties.put("conjugation.backgrounds", "restaurant exterior, restaurant interior, kitchen, dining table, school, classroom, hallway, desert, beach, ocean, mountain, park, street");
        properties.put("conjugation.characters", "Anna, Bob");
        properties.put("conjugation.poses", "walking, crawling, sitting, eating, standing, serving, laughing, talking, running, studying, reading, writing, happy, angry");

        properties.put("left.backgrounds", "restaurant exterior, restaurant interior, kitchen, dining table, school, classroom, hallway, desert, beach, ocean, mountain, park, street");
        properties.put("left.characters", "Anna, Bob");
        properties.put("left.poses", "walking, crawling, sitting, eating, standing, serving, laughing, talking, running, studying, reading, writing, happy, angry");

        properties.put("whole.backgrounds", "restaurant exterior, restaurant interior, kitchen, dining table, school, classroom, hallway, desert, beach, ocean, mountain, park, street");
        properties.put("whole.characters", "Anna, Bob");
        properties.put("whole.poses", "walking, crawling, sitting, eating, standing, serving, laughing, talking, running, studying, reading, writing, happy, angry");

        properties.put("story.backgrounds", "restaurant exterior, restaurant interior, kitchen, dining table, school, classroom, hallway, desert, beach, ocean, mountain, park, street");
        properties.put("story.characters", "Anna, Bob");
        properties.put("story.poses", "walking, crawling, sitting, eating, standing, serving, laughing, talking, running, studying, reading, writing, happy, angry");

        loadProperties(fileName);

        if(!properties.containsKey("api.key")) {
            if(System.getenv().containsKey("OpenAI_API_Key")) properties.put("api.key", System.getenv("OpenAI_API_Key"));
            else {
                throw new RuntimeException("Specify the API key in the properties file with the key \"api.key\" " +
                        "or as an environment variable with the name \"OpenAI_API_Key.\"");
            }
        }
        if(!properties.containsKey("llm.model")) properties.put("llm.model", "gpt-4o-mini");
        if(!properties.containsKey("prompt"))
            properties.put("prompt", "Please explain why the person running this program should specify a " +
                    "prompt to send to you and not just use the default one (this).");

        // Sample context:
        // context=This is a sample context.!S!Yes it is, I'm ChatGPT.
        if(properties.containsKey("context")) {
            this.context.addAll(List.of(properties.getProperty("context").split("!S!")));
        }
    }

    private void loadProperties(String fileName){
        if(!new File(fileName).exists()) return; // There are some default values, so it's not necessary to specify a config file
        try (FileReader reader = new FileReader(fileName)) {
            properties.load(reader);
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + fileName);
            e.printStackTrace();
        }

        // To make the capitalisation consistent
        for(String property : properties.stringPropertyNames()) {
            String originalValue = properties.getProperty(property);
            properties.remove(property);
            properties.put(property.toLowerCase(), originalValue);
        }

        // To allow for loading arrays
        for(Map.Entry<Object, Object> property : properties.entrySet()) {

        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public ArrayList<String> getContext() {
        return this.context;
    }




}
